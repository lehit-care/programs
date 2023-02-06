package com.lehit.programs.repository;


import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.model.ProgramExecution;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ProgramExecutionRepository extends JpaRepository<ProgramExecution, UUID>, JpaSpecificationExecutor<ProgramExecution>{

//    @EntityGraph(attributePaths = {"taskExecutions", "program"})
    @EntityGraph(value = "including-programs-tasks-executions")
    ProgramExecution findByUserIdAndLifecycleStatus(UUID userId, ExecutionStatus lifecycleStatus);

    @EntityGraph(value = "including-programs-tasks-executions")
    Optional<ProgramExecution> findTop1ByUserIdAndLifecycleStatusOrderByStartedAtDesc(UUID userId, ExecutionStatus lifecycleStatus);

    UUID findByUserIdAndProgramId(UUID userId, UUID programId);
}
