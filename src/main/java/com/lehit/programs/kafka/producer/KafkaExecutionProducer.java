package com.lehit.programs.kafka.producer;

import com.lehit.common.dto.UserProfile;
import com.lehit.common.enums.KafkaAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaExecutionProducer {
//    todo
    @Value("${spring.kafka.profiles-topic.name}")
    private String topicName;


    private final KafkaTemplate<String, UserProfile> kafkaTemplate;


    public UserProfile sendPogramAssignEvent(UserProfile profile){
        return sendWithHeader(profile, topicName, KafkaAction.PROGRAM_ASSIGN);
    }



    private UserProfile sendWithHeader(UserProfile message, String topicName, KafkaAction action){
        var producerRecord = new ProducerRecord<>(topicName, message.getId().toString(), message);
        producerRecord.headers().add("__Action__", action.toString().getBytes(StandardCharsets.UTF_8));

        var res = kafkaTemplate.send(producerRecord);

        res.addCallback(new ListenableFutureCallback() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("error during event submission: {}", ex.getMessage());
            }

            @Override
            public void onSuccess(Object result) {
                log.debug("event submitted {}", message);
            }
        });
        return message;
    }


    public UserProfile send(UserProfile message, String topicName){

        ListenableFuture res = kafkaTemplate.send(topicName, message.getId().toString(), message);

        res.addCallback(new ListenableFutureCallback() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("error during event submission: {}", ex.getMessage());
            }

            @Override
            public void onSuccess(Object result) {
                log.debug("event submitted {}", message);
            }
        });
        return message;
    }

}
