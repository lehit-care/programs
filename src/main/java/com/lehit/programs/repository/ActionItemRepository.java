package com.lehit.programs.repository;


import com.lehit.programs.model.ActionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ActionItemRepository extends JpaRepository<ActionItem, UUID>, JpaSpecificationExecutor<ActionItem>{
    @Query(value = "SELECT ai.id FROM ActionItem ai WHERE task.id = ?1")
    List<UUID> selectIdsByTaskId(UUID taskId);
}
