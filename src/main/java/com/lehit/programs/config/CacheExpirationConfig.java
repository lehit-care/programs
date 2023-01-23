package com.lehit.programs.config;

import org.redisson.spring.cache.CacheConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

@ConfigurationProperties(prefix = "spring.cache.expiration")
@Profile("!test")
public record CacheExpirationConfig(CacheConfig tasksExecutionsByUser, CacheConfig task,
                                    CacheConfig userProgram) {
}
