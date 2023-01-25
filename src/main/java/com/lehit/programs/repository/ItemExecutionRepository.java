package com.lehit.programs.repository;


import com.lehit.programs.model.ItemExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ItemExecutionRepository extends JpaRepository<ItemExecution, UUID>, JpaSpecificationExecutor<ItemExecution>{

    Optional<ItemExecution> findByTaskExecutionIdAndItemId(UUID exeId, UUID itemId);
}
