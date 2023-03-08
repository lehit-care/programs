package com.lehit.programs.controller;

import com.lehit.common.enums.Language;
import com.lehit.programs.model.Category;
import com.lehit.programs.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class AdminContentController {
    private final CategoryRepository categoryRepository;


    //    Categories
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/categories")
    public Category saveCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable UUID id) {
        categoryRepository.deleteById(id);
    }

    @GetMapping("/categories/search")
    public Slice<Category> searchCategories(@RequestParam(required = false) Optional<String> name, @ParameterObject Pageable pageable){
        return name.map(t -> categoryRepository.findByNameContainingIgnoreCase(t, pageable))
                .orElseGet(() -> categoryRepository.findAll(pageable));
    }

    @GetMapping("/categories")
    public Slice<Category> getCategoriesByLang(@RequestParam Language language, @ParameterObject Pageable pageable){
        return categoryRepository.findByLanguage(language, pageable);
    }
}
