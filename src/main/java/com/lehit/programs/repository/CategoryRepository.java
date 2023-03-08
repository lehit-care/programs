package com.lehit.programs.repository;


import com.lehit.common.enums.Language;
import com.lehit.programs.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID>, JpaSpecificationExecutor<Category>{

    Slice<Category>findByNameContainingIgnoreCase(String name, Pageable pageable);

    Slice<Category>findByLanguage(Language language, Pageable pageable);
}
