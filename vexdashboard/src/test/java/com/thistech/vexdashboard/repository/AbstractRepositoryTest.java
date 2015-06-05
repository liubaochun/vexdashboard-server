package com.thistech.vexdashboard.repository;

import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * AbstractRepositoryTest
 * @author <a href="mailto:jeffb@thistech.com">Jeff Bailey</a>
 */
@ContextConfiguration("classpath:/context-test.xml")
public class AbstractRepositoryTest extends AbstractTestNGSpringContextTests {

    @Resource
    protected MongoTemplate mongoTemplate;

    @BeforeMethod
    @AfterMethod
    protected void clear() throws Exception {
        for (String collection : mongoTemplate.getCollectionNames()) {
            if (!StringUtils.startsWith(collection, "system.")) {
                mongoTemplate.remove(query(where("_id").exists(true)), collection);
            }
        }
    }

    @AfterSuite
    protected void drop() throws Exception {
        for (String collection : mongoTemplate.getCollectionNames()) {
            if (!StringUtils.startsWith(collection, "system.")) {
                mongoTemplate.dropCollection(collection);
            }
        }
    }


}
