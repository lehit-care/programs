package com.lehit.programs.repository;


import com.lehit.programs.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task>{

    @EntityGraph("including-cards")
    Optional<Task> findByProgramIdAndPosition(UUID programId, int position);

    @EntityGraph("including-cards")
    @Query(value = "SELECT t FROM Task t WHERE t.id = ?1")
    Optional<Task> selectFullTask(UUID id);

    <T> List<T> findByProgramIdOrderByPositionAsc(UUID programId, Class<T> type);


//   todo @RestResource(path = "by-program")
    Page<Task> findByProgramId(UUID programId, Pageable pageable);

    long countByProgramId(UUID programId);
}
