package com.thistech.vexdashboard.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Service
@Path("/test")
public class TestService {

    public static final String PRIMARY_HLS_URL = "http://localhost:8080/origin/playlists/espn/index.m3u8";
    public static final String ALT_HLS_URL = "http://localhost:8080/origin/playlists/cnn/index.m3u8";

    @Resource
    private MongoTemplate mongoTemplate;

    @GET
    @Path("/cleanup")
    public Response truncateCollections() {
        /*
        DBObject query = new BasicDBObject("_id", new BasicDBObject("$exists", true));
        EmpIntegrationTestable.clearTestMaps();
        synchronized(this) {
            for (String collectionName : mongoTemplate.getCollectionNames()) {
                if (!StringUtils.contains(collectionName, "system")) {
                    mongoTemplate.getCollection(collectionName).remove(query);
                }
            }

        }*/
        return Response.ok().build();
    }

    @GET
    @Path("/dropCollections")
    public Response dropCollections() {
        /*
        EmpIntegrationTestable.clearTestMaps();
        synchronized(this) {
            for (String collectionName : mongoTemplate.getCollectionNames()) {
                if (!StringUtils.contains(collectionName, "system")) {
                    try {
                        mongoTemplate.dropCollection(collectionName);
                    }
                    catch (Exception e) {}
                }
            }
        }*/
        return Response.ok().build();
    }
}

