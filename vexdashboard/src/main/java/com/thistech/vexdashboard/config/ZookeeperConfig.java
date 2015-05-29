package com.thistech.vexdashboard.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperConfig {
    private static final Logger log = LoggerFactory.getLogger(ZookeeperConfig.class);

    @Autowired
    private ConfigListener configListener;

    //private String zkEnsemble = isNotBlank(System.getenv(ZK_ENSEMBLE)) ? System.getenv(ZK_ENSEMBLE) : ZK_ENSEMBLE_DEFAULT;
    //private int zkTimeout = Integer.parseInt(isNotBlank(System.getenv(ZK_TIMEOUT)) ? System.getenv(ZK_TIMEOUT) : ZK_TIMEOUT_DEFAULT);
    @Value("${zookeeper.ensemble.default:127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183}")
    private String zkEnsemble;
    @Value("${zookeeper.timeout.default:1000000}")
    private int zkTimeout;

    @Bean(initMethod = "start")
    public CuratorFramework curatorClient() {
        final CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(this.zkEnsemble)
                .connectionTimeoutMs(this.zkTimeout)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();

        UnhandledErrorListener errorsListener = new UnhandledErrorListener() {
            public void unhandledError(String message, Throwable e) {
                log.error("Unrecoverable error: " + message, e);
                try {
                    //curatorFramework.close(); //DO'NT close ZooKeeper Session if session timeout or expired because zookeeper client library will automatically re-establish session
                } catch (Exception ex) {
                    log.warn( "Exception when closing.", ex);
                }
            }
        };

        curatorFramework.getCuratorListenable().addListener(configListener);
        curatorFramework.getUnhandledErrorListenable().addListener(errorsListener);
        return curatorFramework;
    }
}
