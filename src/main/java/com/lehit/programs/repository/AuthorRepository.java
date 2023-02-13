package com.lehit.programs.repository;


import com.lehit.programs.model.Author;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AuthorRepository extends CrudRepository<Author, UUID>, JpaSpecificationExecutor<Author>{

}
