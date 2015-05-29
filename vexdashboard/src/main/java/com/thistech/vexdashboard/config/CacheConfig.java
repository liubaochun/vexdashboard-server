package com.thistech.vexdashboard.config;

import com.google.code.ssm.CacheFactory;
import com.google.code.ssm.config.AddressProvider;
import com.google.code.ssm.config.DefaultAddressProvider;
import com.google.code.ssm.providers.CacheConfiguration;
import com.google.code.ssm.providers.spymemcached.MemcacheClientFactoryImpl;
import com.google.code.ssm.providers.spymemcached.SpymemcachedConfiguration;
import com.google.code.ssm.spring.ExtendedSSMCacheManager;
import com.google.code.ssm.spring.SSMCache;
import com.google.common.collect.Sets;
import com.thistech.common.exception.ApplicationException;
import com.thistech.vexdashboard.common.model.VexBox;
import com.thistech.vinz.common.model.Identity;
import net.spy.memcached.DefaultHashAlgorithm;
import net.spy.memcached.transcoders.SerializingTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.DefaultKeyGenerator;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

import static net.spy.memcached.ConnectionFactoryBuilder.Locator.CONSISTENT;
import static net.spy.memcached.FailureMode.Redistribute;

@Configuration
@EnableCaching(proxyTargetClass = true)
public class CacheConfig implements CachingConfigurer {
    private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);

    @Value("${memcached.addresses}")
    private String addresses = null;
    @Value("${memcached.enabled:false}")
    private boolean enabled = false;
    @Value("${memcached.expiration:15}")
    private int expiration;
    @Value("${memcached.timeout.ms:1000}")
    private int timeout;
    @Value("${memcached.hash.algorithm:KETAMA_HASH}")
    private String hashAlgorithm;
    public static final int METHOD_LEVEL_EXPIRATION = 600;

    @Bean
    public CacheManager cacheManager() {
        CacheManager cacheManager = new NoOpCacheManager();
        if (this.enabled) {
            ExtendedSSMCacheManager ssmCacheManager = new ExtendedSSMCacheManager();
            try {
                Set<SSMCache> caches = Sets.newHashSet(
                        new SSMCache(vexboxCache().getObject(), expiration,true),
                        new SSMCache(identityCache().getObject(), expiration, true));
                ssmCacheManager.setCaches(caches);
            }
            catch (Exception e) {
                log.error(ApplicationException.Type.GENERAL.toString("Unable to initialize caches"), e);
            }
            cacheManager = ssmCacheManager;
        }
        return cacheManager;
    }

    @Bean
    public AddressProvider addressProvider() {
        return new DefaultAddressProvider(this.addresses);
    }

    @Bean
    public CacheConfiguration configuration() {
        SerializingTranscoder transcoder = new SerializingTranscoder();
        transcoder.setCompressionThreshold(1024);

        SpymemcachedConfiguration config = new SpymemcachedConfiguration();
        config.setUseBinaryProtocol(true);
        config.setOperationTimeout(timeout);
        config.setConsistentHashing(true);
        config.setDefaultTranscoder(transcoder);
        config.setTimeoutExceptionThreshold(1998);
        config.setLocatorType(CONSISTENT);
        config.setFailureMode(Redistribute);
        config.setUseNagleAlgorithm(false);
        config.setHashAlg(DefaultHashAlgorithm.valueOf(hashAlgorithm));
        return config;
    }

    @Override
    public KeyGenerator keyGenerator() {
        return new DefaultKeyGenerator();
    }

    @Bean(destroyMethod = "destroy")
    public CacheFactory identityCache() {
        return this.buildCacheFactory(Identity.CACHE_NAME);
    }

    @Bean(destroyMethod = "destroy")
    public CacheFactory vexboxCache(){
        return this.buildCacheFactory(VexBox.CACHE_NAME);
    }


    private CacheFactory buildCacheFactory(String cacheName) {
        CacheFactory factory = new CacheFactory();
        factory.setCacheName(cacheName);
        factory.setConfiguration(this.configuration());
        factory.setCacheClientFactory(new MemcacheClientFactoryImpl());
        factory.setAddressProvider(this.addressProvider());
        return factory;
    }
}
