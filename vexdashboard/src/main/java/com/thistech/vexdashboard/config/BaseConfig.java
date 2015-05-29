package com.thistech.vexdashboard.config;

import org.apache.bval.jsr303.ApacheValidationProvider;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * <p>BdsConfiguration class.</p>
 * @author <a href="mailto:matt@thistech.com">Matt Narrell</a>
 */
@Configuration
//@EnableMBeanExport
public class BaseConfig {

    /**
     * <p>validator.</p>
     * @return a {@link org.springframework.validation.beanvalidation.LocalValidatorFactoryBean} object.
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setProviderClass(ApacheValidationProvider.class);
        return validator;
    }

    /**
     * <p>propertyPlaceholderConfigurer.</p>
     * @return a {@link org.springframework.beans.factory.config.PropertyPlaceholderConfigurer} object.
     */
    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer placeholderConfigurer = new PropertyPlaceholderConfigurer();
        placeholderConfigurer.setIgnoreResourceNotFound(true);
        placeholderConfigurer.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        placeholderConfigurer.setLocations(new Resource[]{
                new ClassPathResource("/vexdashboard-defaults.properties"),
                new ClassPathResource("/vexdashboard.properties"),
                new ClassPathResource("/vexdashboard-test-defaults.properties"),
                new ClassPathResource("/vexdashboard-test.properties"),
        });
        placeholderConfigurer.setOrder(1);
        return placeholderConfigurer;
    }
}
