package com.thistech.vexdashboard.job;

import com.mongodb.MongoClient;
import com.thistech.common.job.isolation.AbstractSingletonJob;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Jobs extending this object will only be run on a SINGLE MetaMore instance
 * within a cluster.
 *
 * @author <a href="mailto:jeffb@thistech.com">Jeff Bailey</a>
 */
public abstract class VexdashboardSigletonJob extends AbstractSingletonJob<Boolean>{

    @Resource
    private MongoClient mongoClient;
    @Value("${mongo.database}")
    private String databaseName;

    private String organizationId;

    @Override
    @PostConstruct
    public void init() {
        super.init();
    }

    @Override
    public MongoClient getMongoClient() {
        return mongoClient;
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationId(){
        return this.organizationId;
    }
}
