package com.lehit.programs.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "spring.kafka.enabled", havingValue = "true")
@EnableConfigurationProperties({KafkaTopicProperties.class})
@Profile("!test")
@RequiredArgsConstructor
public class KafkaConfig {
    private final KafkaTopicProperties topicProperties;


    @Bean
    public NewTopic createExecutedItemsTopic() {
        return TopicBuilder.name(topicProperties.name())
                .partitions(topicProperties.partitions())
                .replicas(topicProperties.replicas())
                .build();
    }

}
