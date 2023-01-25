package com.lehit.programs.repository;


import com.lehit.programs.model.Program;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProgramRepository extends CrudRepository<Program, UUID>, JpaSpecificationExecutor<Program>{
    Slice<Program> findAll(Pageable pageable);

}
