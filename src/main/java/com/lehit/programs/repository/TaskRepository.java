package com.lehit.programs.repository;


import com.lehit.programs.model.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task>{

    Optional<Task> findByProgramIdAndPosition(UUID programId, int position);

    List<Task> findByProgramId(UUID programId);

    @Query(value = "SELECT t.id FROM Task t WHERE program.id = ?1")
    List<UUID> selectIdsByProgramId(UUID programId);

    @EntityGraph(value = "including-program")
    @Query(value = "SELECT t FROM Task t WHERE t.id = ?1")
    Optional<Task> selectFullTask(UUID id);

}
