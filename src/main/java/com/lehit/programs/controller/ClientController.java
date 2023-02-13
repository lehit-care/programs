package com.lehit.programs.controller;

import com.lehit.common.enums.ExecutionStatus;
import com.lehit.programs.model.ProgramExecution;
import com.lehit.programs.model.projection.ProgramWithAuthorProjection;
import com.lehit.programs.service.ExecutionProgressService;
import com.lehit.programs.service.ProgramsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
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


    @GetMapping("/client/programs/search")
    public Slice<ProgramWithAuthorProjection> searchPrograms(@RequestParam(required = false) Optional<String> title, @ParameterObject Pageable pageable){
        return title.map(t -> programsService.searchByTitle(t, pageable))
                .orElseGet(() -> programsService.findPublished(pageable));
    }


}
