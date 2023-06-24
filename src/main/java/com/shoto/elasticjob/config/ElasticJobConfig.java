package com.shoto.elasticjob.config;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author admin
 */
@Configuration
public class ElasticJobConfig {

    @Resource
    private ElasticJobProperties properties;

    /**
     * zookeeper config
     * @return ZookeeperConfiguration
     */
    @Bean
    public ZookeeperConfiguration zkConfig() {
        ZookeeperConfiguration config =
                new ZookeeperConfiguration(properties.getServerlists(), properties.getNamespace());
        config.setBaseSleepTimeMilliseconds(properties.getBaseSleepTimeMilliseconds());
        config.setMaxRetries(properties.getMaxRetries());
        config.setMaxSleepTimeMilliseconds(properties.getMaxSleepTimeMilliseconds());
        config.setConnectionTimeoutMilliseconds(properties.getConnectionTimeoutMilliseconds());
        return config;
    }

    /**
     * 初始化注册
     * @param config ZookeeperConfiguration
     * @return ZookeeperRegistryCenter
     */
    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter regCenter(ZookeeperConfiguration config) {
        return new ZookeeperRegistryCenter(config);
    }
}


