package com.lehit.programs.service;

import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.kafka.producer.KafkaProducer;
import com.lehit.programs.model.ExecutedItem;
import com.lehit.programs.model.payload.ExecutedItemRequest;
import com.lehit.programs.model.payload.ProgramSequence;
import com.lehit.programs.repository.ActionItemRepository;
import com.lehit.programs.repository.ExecutedItemRepository;
import com.lehit.programs.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActionItemsService {
    private final ExecutedItemRepository executedItemRepository;
    private final ActionItemRepository itemRepository;
    private final TaskRepository taskRepository;
    private final KafkaProducer kafkaProducer;
//    private final MultimediaClient multimediaClient;

    @Value("${spring.kafka.enabled}")
    private boolean kafkaEnabled;

    @Transactional
    public ExecutedItem executeItem(ExecutedItemRequest itemUserRelation, UUID userId, UUID itemId){
        return executedItemRepository.save(new ExecutedItem(userId, itemId, itemUserRelation.taskExecutionId(), itemUserRelation.itemType()));
    }


    public ExecutedItem emitEvent(ExecutedItem executedItem){
        if (kafkaEnabled){
            kafkaProducer.send(executedItem);
        }
        return executedItem;
    }


    @Transactional
    public void removeActionItem(UUID itemId){
        var item = itemRepository.findById(itemId).orElseThrow();
        var task = taskRepository.findById(item.getTaskId()).orElseThrow();
        var informationItem = item.getInformationItem();

        itemRepository.delete(item);

//        if(informationItem != null && informationItem.getMediaFileUrl() != null && !informationItem.getMediaFileUrl().isBlank())
//            multimediaClient.deleteSingleMultimedia(item.getInformationItem().getMediaFileUrl());
    }


    @Transactional
    public void archiveItem(UUID itemId){
        var item = itemRepository.findById(itemId).orElseThrow();
        var task = taskRepository.findById(item.getTaskId()).orElseThrow();

        item.setTaskId(null);
    }

    @Transactional
    public List<ProgramSequence.ExecutableEntitySequence> shuffleItems(UUID taskId, List<ProgramSequence.ExecutableEntitySequence> sequenceList){
        var task = taskRepository.findById(taskId).orElseThrow();

        Asserts.check(!executedItemRepository.existsByTaskExecutionLifecycleStatusAndTaskExecutionTaskPosition(ExecutionStatus.STARTED, task.getPosition()),
                "Current task is being executed by an User.");

            Map<UUID, Integer> sequenceMap =  sequenceList.stream()
                .collect(Collectors.toMap(ProgramSequence.ExecutableEntitySequence::id, ProgramSequence.ExecutableEntitySequence::position));

        var items = itemRepository.findAllById(sequenceMap.keySet());
        items.forEach(item -> item.setPosition(sequenceMap.get(item.getId())));

        return sequenceList;
    }


}
