package com.thistech.vexdashboard.config;

import com.thistech.common.util.HttpClientFactory;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

/**
 * HttpConfig
 * @author <a href="mailto:jeffb@thistech.com">Jeff Bailey</a>
 */
@Configuration
public class HttpConfig {

    @Value("${httpClient.defaultConnPerRoute}")
    private int defaultConnectionsPerRoute;
    @Value("${httpClient.maxRetries}")
    private int maxRetries;
    @Value("${httpClient.keepAliveDuration}")
    private int keepAliveDuration;
    @Value("${httpClient.obeyKeepAliveHeader}")
    private boolean obeyKeepAliveHeader;
    @Value("${httpClient.totalConnections}")
    private int totalConnections;
    @Value("${httpClient.timeout}")
    private int timeout;
    @Value("${httpClient.userAgent}")
    private String userAgent;

    @Bean
    public HttpClient httpClient() throws Exception {
        HttpClientFactory clientFactory = new HttpClientFactory();
        clientFactory.setDefaultConnPerRoute(defaultConnectionsPerRoute);
        clientFactory.setKeepAliveDuration(keepAliveDuration);
        clientFactory.setMaxRetries(maxRetries);
        clientFactory.setObeyKeepAliveHeader(obeyKeepAliveHeader);
        clientFactory.setTimeout(timeout);
        clientFactory.setTotalConnections(totalConnections);
        clientFactory.setUserAgent(userAgent);
        return clientFactory.getObject();
    }
}