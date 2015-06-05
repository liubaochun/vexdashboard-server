package com.thistech.vexdashboard.ui.config;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.common.collect.Lists;
import com.thistech.common.cxf.StringMessageBodyWriter;
import com.thistech.common.cxf.ThisObjectMapper;
import com.thistech.common.cxf.mapper.ApplicationExceptionMapper;
import com.thistech.common.cxf.mapper.ConstraintViolationExceptionMapper;
import com.thistech.common.cxf.mapper.DuplicateEntityExceptionMapper;
import com.thistech.vexdashboard.ui.service.ConfigService;
import com.thistech.vexdashboard.ui.util.VexdashboardUiUtil;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Arrays;

@Configuration
public class CxfConfig {
    @Resource
    public ConfigService configService;

    @Bean
    public Bus cxfBus() {
        Bus bus = CXFBusFactory.getDefaultBus();
        bus.getInInterceptors().add(new LoggingInInterceptor());
        bus.getInFaultInterceptors().add(new LoggingInInterceptor());
        bus.getOutInterceptors().add(new LoggingOutInterceptor());
        bus.getOutFaultInterceptors().add(new LoggingOutInterceptor());
        return bus;
    }

    @Bean
    public Server endpoints() {
        ThisObjectMapper objectMapper = new ThisObjectMapper();
        SimpleFilterProvider filters = new SimpleFilterProvider();
        filters.setFailOnUnknownId(false);
        objectMapper.setFilters(filters);
        JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
        factoryBean.setAddress("/");
        factoryBean.setDataBinding(VexdashboardUiUtil.VEXDASHBOARD_UI_JAXB_DATA_BINDING);
        factoryBean.setServiceBeans(Lists.<Object>newArrayList(configService));
        factoryBean.setProviders(Arrays.asList(
                new ApplicationExceptionMapper(),
                new ConstraintViolationExceptionMapper(),
                new DuplicateEntityExceptionMapper(),
                new StringMessageBodyWriter(),
                new JacksonJsonProvider(objectMapper)));
        return factoryBean.create();
    }
}

