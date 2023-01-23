package com.lehit.programs.repository;


import com.lehit.programs.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ProgramRepository extends JpaRepository<Program, UUID>, JpaSpecificationExecutor<Program>{

}
