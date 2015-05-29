package com.thistech.vexdashboard.config;

import com.mongodb.*;
import com.thistech.common.exception.ApplicationException;
import com.thistech.common.mongo.MongoUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteResultChecking;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>MongoConfiguration class.</p>
 * @author <a href="mailto:matt@thistech.com">Matt Narrell</a>
 * @version $Id: $Id
 */
@Configuration
public class MongoConfig extends AbstractMongoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(MongoConfig.class);

    @Value("${mongo.host}")
    private String replicaSet = null;
    @Value("${mongo.database}")
    private String databaseName = null;
    @Value("${mongo.auth.username}")
    private String username = null;
    @Value("${mongo.auth.password}")
    private String password = null;
    @Value("${mongo.connectionsPerHost:40}")
    private int connectionsPerHost = 40;
    @Resource
    ApplicationContext applicationContext;


    @Override
    public String getDatabaseName() {
        return this.databaseName;
    }

    @Override
    public UserCredentials getUserCredentials() {
        UserCredentials credentials = null;
        if (StringUtils.isNotBlank(this.username) && StringUtils.isNotBlank(this.password)) {
            credentials = new UserCredentials(this.username, this.password);
        }
        return credentials;
    }

    @Override
    public MongoDbFactory mongoDbFactory() throws Exception {
        try {
            MongoDbFactory mongoDbFactory = super.mongoDbFactory();
            DB db = mongoDbFactory.getDb(this.getDatabaseName());
            db.getCollectionNames();
            return mongoDbFactory;
        }
        catch (Exception e) {
            log.error(ApplicationException.Type.MONGO.toString(), e);
            throw new ApplicationException(ApplicationException.Type.MONGO, e);
        }

    }

    @Override
    public Mongo mongo() throws Exception {
        MongoClient mClient = (MongoClient)applicationContext.getBean("mongoClient");
        if(mClient != null){
            return mClient;
        }
        return mongoClient();
    }

    @Bean(destroyMethod = "close" )
    public MongoClient mongoClient() throws Exception {
        MongoClientOptions options = new MongoClientOptions.Builder()
                .autoConnectRetry(true)
                .connectionsPerHost(connectionsPerHost)
                .description("ACS OpEvents MongoClient")
                .readPreference(ReadPreference.secondaryPreferred())
                .threadsAllowedToBlockForConnectionMultiplier(1500)
                .build();

        List<ServerAddress> serverAddresses = MongoUtils.getServerAddresses(replicaSet);

        MongoClient mongoClient = new MongoClient(serverAddresses, options);
        DB db = mongoClient.getDB(databaseName);

        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            if (!db.authenticate(username, password.toCharArray())) {
                String errMsg = String.format("Authentication to database [%s] failed.  Tried %s/%s", databaseName, username, password);
                throw new ApplicationException(ApplicationException.Type.MONGO, errMsg);
            }
        }
        return mongoClient;
    }

    @Override
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate template = super.mongoTemplate();
        template.setWriteConcern(WriteConcern.SAFE);
        template.setWriteResultChecking(WriteResultChecking.EXCEPTION);
        return template;
    }
}
