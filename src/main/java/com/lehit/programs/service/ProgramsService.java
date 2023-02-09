package com.lehit.programs.service;

import com.lehit.programs.model.Program;
import com.lehit.programs.model.projection.ProgramWithTasksProjection;
import com.lehit.programs.repository.ProgramRepository;
import com.lehit.programs.service.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.Asserts;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProgramsService {
    private final ProgramRepository programRepository;
    private final BeanUtils beanUtils;

    @Transactional
    public Program saveProgram(Program program){
        return programRepository.save(program);
    }

    @Transactional
    public Program updateProgramData(UUID authorId, UUID programId, Map<String, Object> fields) {
        var program = programRepository.findById(programId).orElseThrow();
        Asserts.check(authorId.equals(program.getAuthor()), "Only Author can modify the Program.");

        beanUtils.updateFields(fields, program);
        program.setId(programId);
        return program;
    }

    public Optional<Program> findById(UUID programId){
        return programRepository.findById(programId);
    }

    public Optional<ProgramWithTasksProjection> findProgramWithTasks(UUID programId){
        return programRepository.selectProgramWithTasks(programId);
    }


    @Transactional
    public void deleteProgram(UUID authorId, UUID programId){
        var program = programRepository.findById(programId).orElseThrow();
        Asserts.check(authorId.equals(program.getAuthor()), "Only Author can delete the Program.");
        programRepository.delete(program);
    }

    public Slice<Program> findAll(Pageable pageable){
        return programRepository.findAll(pageable);
    }


    public Slice<Program> findByAuthor(UUID authorId, Pageable pageable){
        return programRepository.findByAuthor(authorId, pageable);
    }

    public Slice<Program> searchByTitle(String title, Pageable pageable){
        return programRepository.findByTitleContainingIgnoreCase(title, pageable);
    }


}
