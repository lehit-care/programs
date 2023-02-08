package com.lehit.programs.repository;


import com.lehit.programs.model.Program;
import com.lehit.programs.model.projection.ProgramWithTasksProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProgramRepository extends CrudRepository<Program, UUID>, JpaSpecificationExecutor<Program>{
    Slice<Program> findAll(Pageable pageable);

    Slice<Program> findByAuthor(UUID authorId, Pageable pageable);

    @EntityGraph(value = "program-including-tasks")
    @Query(value = "SELECT p FROM Program p WHERE p.id = ?1")
    Optional<ProgramWithTasksProjection> selectProgramWithTasks(UUID id);

}
