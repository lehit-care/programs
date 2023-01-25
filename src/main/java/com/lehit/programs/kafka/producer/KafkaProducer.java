package com.lehit.programs.kafka.producer;

import com.lehit.programs.model.ItemExecution;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {
    @Value("${spring.kafka.topic.name}")
    private String topicName;

    private final KafkaTemplate<String, ItemExecution> kafkaTemplate;

    public void send(ItemExecution message){
        var res = kafkaTemplate.send(topicName, message.getUserId().toString(), message);

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
    }

}
