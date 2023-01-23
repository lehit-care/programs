package com.lehit.programs.repository;


import com.lehit.programs.model.ActionItem;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ActionItemRepository extends JpaRepository<ActionItem, UUID>, JpaSpecificationExecutor<ActionItem>{

    @Caching(evict = {
            @CacheEvict(value= "Task",  allEntries = true)
    })
    @Override
    <S extends ActionItem> S save(S entity);

    <T> List<T> findByTaskIdOrderByPositionAsc(UUID taskId, Class<T> type);
}
