package com.thistech.vexdashboard.config;

import com.thistech.vexdashboard.job.BoxMetricsMockJob;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * SchedulerConfig
 * @author <a href="mailto:jeffb@thistech.com">Jeff Bailey</a>
 */
@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer {

    @Resource
    private BoxMetricsMockJob boxMetricsMockJob;
    @Value("${mock.status.interval}")
    private int interval;


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(this.taskScheduler());

        taskRegistrar.addFixedRateTask(boxMetricsMockJob, interval);
    }

    /**
     * <p>taskScheduler.</p>
     *
     * @return a {@link java.util.concurrent.Executor} object.
     */
    @Bean(destroyMethod = "shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(10);
    }
}
