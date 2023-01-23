package com.lehit.programs.repository;


import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.model.TaskExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.Optional;
import java.util.UUID;

public interface TaskExecutionRepository extends JpaRepository<TaskExecution, UUID>, JpaSpecificationExecutor<TaskExecution>{

    @EntityGraph("include-cards")
    Page<TaskExecution> findByUserId(UUID userId, Pageable pageable);

    @EntityGraph("include-cards")
    Optional<TaskExecution> findByUserIdAndLifecycleStatus(UUID userId, ExecutionStatus lifecycleStatus);

    void deleteByUserId(UUID userId);

    boolean existsByUserIdAndLifecycleStatus(UUID userId, ExecutionStatus lifecycleStatus);

    @Query(value = "UPDATE TaskExecution SET lifecycleStatus = :lifecycleStatus WHERE userId = :userId")
    @Modifying
    int updateUserExecutionsLCS(UUID userId, ExecutionStatus lifecycleStatus);

    @Query(value = "UPDATE TaskExecution SET lifecycleStatus = :lifecycleStatus WHERE taskPosition = :taskPosition")
    @Modifying
    int updateTaskExecutionsLCS(int taskPosition, ExecutionStatus lifecycleStatus);

}
