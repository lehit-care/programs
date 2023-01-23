package com.lehit.programs.config;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;

@Profile("!test")
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheExpirationConfig.class)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring", name = "cache.type", havingValue = "redis")
public class CacheConfig {

    private final CacheExpirationConfig expirationConfig;

    @Bean
    CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, org.redisson.spring.cache.CacheConfig> config = new HashMap<>();
        config.put("TasksExecutedByUser", expirationConfig.tasksExecutionsByUser());
        config.put("Task", expirationConfig.task());
        config.put("UserProgramData", expirationConfig.userProgram());

        return new RedissonSpringCacheManager(redissonClient, config);
    }

}
