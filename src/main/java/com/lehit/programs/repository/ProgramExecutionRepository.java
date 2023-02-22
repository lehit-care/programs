package com.lehit.programs.repository;


import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.model.ProgramExecution;
import com.lehit.programs.model.projection.ProgramExecutionBasicProjection;
import com.lehit.programs.model.projection.ProgramExecutionWithTaskExecutions;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProgramExecutionRepository extends JpaRepository<ProgramExecution, UUID>, JpaSpecificationExecutor<ProgramExecution>{


    @EntityGraph(value = "base-exe-including-programs-tasks-executions")
    Optional<ProgramExecution> findByUserIdAndProgramId(UUID userId, UUID programId);

    List<ProgramExecution> findProgramExecutionsWithTaskExecutionsWithTasks(UUID userId, int count);

    @EntityGraph(value = "base-exe-including-programs-tasks-executions")
    Slice<ProgramExecution> findByUserIdAndLifecycleStatus(UUID userId, ExecutionStatus lifecycleStatus, Pageable pageable);


    @EntityGraph(value = "base-exe-including-program")
    Slice<ProgramExecutionBasicProjection> findByUserIdAndLifecycleStatus(UUID userId, ExecutionStatus lifecycleStatus);

    @EntityGraph(value = "base-exe-including-program")
    Slice<ProgramExecutionBasicProjection> findByUserId(UUID userId);


}
