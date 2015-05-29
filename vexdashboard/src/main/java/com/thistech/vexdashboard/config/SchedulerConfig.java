package com.thistech.vexdashboard.config;

import com.thistech.common.exception.ApplicationException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * SchedulerConfig
 * @author <a href="mailto:jeffb@thistech.com">Jeff Bailey</a>
 */
@Configuration
public class SchedulerConfig {

    @Value("${scheduler.threadCount:10}")
    private String threadCount;
    @Value("${scheduler.idleWaitTime:30000}")
    private String idleWaitTime;
    @Value("${scheduler.instanceId:AUTO}")
    private String instanceId;

    @Resource
    ApplicationContext applicationContext;

    @Bean(destroyMethod = "shutdown")
    public Scheduler scheduler() {
        // create Properties list
        // This is done here because by default, we do not have access to properties except
        // through the annotations. Quartz needs a Properties object.
        Properties properties = new Properties();
        properties.put("org.quartz.threadPool.threadCount", threadCount);
        properties.put("org.quartz.scheduler.idleWaitTime", idleWaitTime);
        properties.put("org.quartz.scheduler.instanceId", instanceId);
        try {
            AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
            jobFactory.setApplicationContext(applicationContext);

            StdSchedulerFactory factory = new StdSchedulerFactory();
            factory.initialize(properties);

            Scheduler scheduler = factory.getScheduler();
            scheduler.setJobFactory(jobFactory);
            scheduler.start();
            return scheduler;

        }
        catch (SchedulerException se) {
            throw new ApplicationException(ApplicationException.Type.GENERAL, se);
        }
    }
}
