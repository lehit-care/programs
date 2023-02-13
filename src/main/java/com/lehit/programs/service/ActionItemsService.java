package com.lehit.programs.service;

import com.lehit.programs.kafka.producer.KafkaProducer;
import com.lehit.programs.model.ActionItem;
import com.lehit.programs.model.ItemExecution;
import com.lehit.programs.model.payload.ProgramSequence;
import com.lehit.programs.repository.ActionItemRepository;
import com.lehit.programs.repository.TaskRepository;
import com.lehit.programs.service.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    private final ActionItemRepository itemRepository;
    private final TaskRepository taskRepository;
    private final KafkaProducer kafkaProducer;
    private final BeanUtils beanUtils;

//    private final MultimediaClient multimediaClient;

    @Value("${spring.kafka.enabled}")
    private boolean kafkaEnabled;


    List<UUID> getIdsByTaskId(UUID taskId){
        return itemRepository.selectIdsByTaskId(taskId);
    }

    public Slice<ActionItem> getByTaskId(UUID taskId, Pageable pageable){
        return itemRepository.findByTaskId(taskId, pageable);
    }


    @Transactional
    public ActionItem save(UUID authorId, ActionItem ai){
        var task = taskRepository.selectFullTask(ai.getTaskId()).orElseThrow();
        Asserts.check(authorId.equals(task.getProgram().getAuthorId()), "Not allowed.");

        return itemRepository.save(ai);
    }

    @Transactional
    public ActionItem updateItem(UUID authorId, UUID aiId, Map<String, Object> fields) {
        var item = itemRepository.findById(aiId).orElseThrow();
        var task = taskRepository.selectFullTask(item.getTaskId()).orElseThrow();

        Asserts.check(authorId.equals(task.getProgram().getAuthorId()), "Not allowed.");
        beanUtils.updateFields(fields, item);

        item.setId(aiId);
        return item;
    }


    public ItemExecution emitEvent(ItemExecution executedItem){
        if (kafkaEnabled){
            kafkaProducer.send(executedItem);
        }
        return executedItem;
    }


    @Transactional
    public void removeActionItem(UUID authorId, UUID itemId){
        var item = itemRepository.findById(itemId).orElseThrow();
        var task = taskRepository.selectFullTask(item.getTaskId()).orElseThrow();

        Asserts.check(authorId.equals(task.getProgram().getAuthorId()), "Not allowed.");

//        var informationItem = item.getInformationItem();

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

    //    todo LCS check or not if planned -- maybe can work without the check
    @Transactional
    public List<ProgramSequence.ExecutableEntitySequence> shuffleItems(UUID taskId, List<ProgramSequence.ExecutableEntitySequence> sequenceList){
        var task = taskRepository.findById(taskId).orElseThrow();


        Map<UUID, Integer> sequenceMap = sequenceList.stream()
                .collect(Collectors.toMap(ProgramSequence.ExecutableEntitySequence::id, ProgramSequence.ExecutableEntitySequence::position));

        var items = itemRepository.findAllById(sequenceMap.keySet());
        items.forEach(item -> item.setPosition(sequenceMap.get(item.getId())));

        return sequenceList;
    }


}
