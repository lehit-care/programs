package com.lehit.programs.kafka.consumer;

import com.lehit.common.dto.UserProfile;
import com.lehit.common.enums.KafkaAction;
import com.lehit.programs.service.ActionItemsService;
import com.lehit.programs.service.TasksService;
import com.lehit.programs.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@ConditionalOnProperty(value = "spring.kafka.enabled", havingValue = "true")
@RequiredArgsConstructor
public class UserProfileConsumer {
    private final TasksService tasksService;
    private final ActionItemsService itemsService;
    private final UserService userService;


    @KafkaListener(topics = "${spring.kafka.profiles-topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<UUID, UserProfile> payload, @Header("__Action__") String action){
        log.debug("Kafka payload received Profile: {}, action: {}", payload.value(), action);

        var userId = payload.value().getId();

        switch (KafkaAction.valueOf(action)){
            case CREATE -> userService.saveUserData(payload.value());

            case DELETE -> {
                itemsService.removeUserData(userId);
                tasksService.removeUserData(userId);
                userService.deleteUserData(userId);
            }
        }
    }
}
