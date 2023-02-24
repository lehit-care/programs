package com.lehit.programs.repository;


import com.lehit.programs.model.Task;
import com.lehit.programs.model.projection.TaskWithItemsProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task>{

    @Query(value = "SELECT t.id FROM Task t WHERE program.id = ?1 ORDER BY t.position ASC")
    List<UUID> selectIdsByProgramId(UUID programId);

    @EntityGraph(value = "task-including-items")
    @Query(value = "SELECT t.id FROM Task t WHERE program.id = ?1")
    Slice<TaskWithItemsProjection> selectTasksWithItemsByProgramId(UUID programId, Pageable pageable);

    @EntityGraph(value = "task-including-program")
    @Query(value = "SELECT t FROM Task t WHERE t.id = ?1")
    Optional<Task> selectFullTask(UUID id);

}
