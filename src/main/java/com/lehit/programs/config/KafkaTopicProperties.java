package com.lehit.programs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.kafka.topic")
public record KafkaTopicProperties(String adminClient, String name, int replicas, int partitions){
}
