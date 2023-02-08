package com.lehit.programs.controller;

import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.model.ProgramExecution;
import com.lehit.programs.service.ExecutionProgressService;
import com.lehit.programs.service.ProgramsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class ClientController {

    private final ProgramsService programsService;
    private final ExecutionProgressService executionService;


    @GetMapping("/client/{clientId}/programs/{programId}")
    public ProgramExecution getProgramByClient(@PathVariable UUID clientId, @PathVariable UUID programId){
        return executionService.getByClientAndProgram(clientId, programId)
                .orElseGet(() -> ProgramExecution.builder()
                        .userId(clientId)
                        .programId(programId)
                        .lifecycleStatus(ExecutionStatus.NEW)
                        .program(programsService.findById(programId).orElseThrow())
                        .build());
    }


}
