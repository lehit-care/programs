package com.lehit.programs.repository;


import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.model.ExecutedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ExecutedItemRepository extends JpaRepository<ExecutedItem, UUID>, JpaSpecificationExecutor<ExecutedItem>{

    List<ExecutedItem> findByTaskExecutionId(UUID taskExecutionId);

    long deleteByTaskExecutionIdIn(List<UUID> executionIds);

    boolean existsByTaskExecutionLifecycleStatusAndTaskExecutionTaskPosition(ExecutionStatus lifecycleStatus, int taskPosition);

    void deleteByUserId(UUID userId);
}
