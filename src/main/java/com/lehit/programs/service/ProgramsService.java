package com.lehit.programs.service;

import com.lehit.programs.model.Program;
import com.lehit.programs.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
