package com.lehit.programs.kafka.producer;

import com.lehit.programs.model.ItemExecution;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {
    @Value("${spring.kafka.topic.name}")
    private String topicName;

    private final KafkaTemplate<String, ItemExecution> kafkaTemplate;

//    todo callback
    public void send(ItemExecution message){
        kafkaTemplate.send(topicName, message.getUserId().toString(), message);
    }

}
