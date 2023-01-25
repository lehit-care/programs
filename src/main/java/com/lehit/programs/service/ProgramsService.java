package com.lehit.programs.service;

import com.lehit.programs.model.Program;
import com.lehit.programs.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.Asserts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProgramsService {
    private final ProgramRepository programRepository;

    @Transactional
    public Program saveProgram(Program program){
        return programRepository.save(program);
    }

    @Transactional
    public Program updateProgramData(UUID authorId, UUID programId, Program programPayload){
        var program = programRepository.findById(programId).orElseThrow();
        Asserts.check(authorId.equals(program.getAuthor()), "Only Author can modify the Program.");
        Asserts.check(programPayload.getId() == null, "The id is not allowed in the payload.");

//       todo beanUtils.copy programPayload
        return program;
    }

    @Transactional
    public void deleteProgram(UUID authorId, UUID programId){
        var program = programRepository.findById(programId).orElseThrow();
        Asserts.check(authorId.equals(program.getAuthor()), "Only Author can delete the Program.");
        programRepository.delete(program);
    }

}
