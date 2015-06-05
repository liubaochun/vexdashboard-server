package com.thistech.vexdashboard.ui.config;

import java.io.IOException;
import java.util.Properties;
import com.thistech.common.exception.ApplicationException;
import com.thistech.common.util.ProxyServlet;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class BaseConfig {

    private Properties properties;

    @Bean public ProxyServlet vexdashboardProxy() {
        return new ProxyServlet(getProperty("vexdashboard.endpoint"));
    }

    @Bean
    public ProxyServlet vinzProxy() {
        return new ProxyServlet(getProperty("vinz.endpoint"));
    }

    private Properties getProperties() {
        if (properties == null) {
            PropertiesFactoryBean bean = new PropertiesFactoryBean();
            bean.setIgnoreResourceNotFound(true);
            bean.setLocations(new Resource[] {
                    new ClassPathResource("/vexdashboard-ui-defaults.properties"),
                    new ClassPathResource("/vexdashboard-ui.properties"),
            });
            try {
                bean.afterPropertiesSet();
                properties = bean.getObject();
            }
            catch (IOException ioe) {
                throw new ApplicationException(ApplicationException.Type.CONFIGURATION_ERROR, ioe);
            }
        }
        return properties;
    }

    private String getProperty(String key) {
        return getProperties().getProperty(key);
    }
}

