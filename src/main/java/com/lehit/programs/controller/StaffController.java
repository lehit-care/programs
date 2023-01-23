package com.lehit.programs.controller;

import com.lehit.common.dto.UserProfile;
import com.lehit.common.dto.UserProgramData;
import com.lehit.programs.kafka.producer.KafkaExecutionProducer;
import com.lehit.programs.model.UserData;
import com.lehit.programs.model.payload.UserProgramDto;
import com.lehit.programs.model.projection.UserDataWithoutProgramProjection;
import com.lehit.programs.service.ExecutionProgressService;
import com.lehit.programs.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
//todo base path
@Slf4j
@RequiredArgsConstructor
public class StaffController {
    private final UserService userService;
    private final KafkaExecutionProducer kafkaExecutionProducer;
    private final ExecutionProgressService executionProgressService;

    @Value("${spring.kafka.enabled}")
    private boolean kafkaEnabled;

// todo move to user
    @PostMapping("/admin/assign-program")
    public UserData assignProgram(@RequestBody UserProgramDto programDto) {
        var data = userService.assignProgram(programDto.userId(), programDto.programId());
        if(kafkaEnabled){
            kafkaExecutionProducer.sendPogramAssignEvent(getProfileDto(programDto.userId(), programDto.programId()));
        }
        return data;
    }


    private UserProfile getProfileDto(UUID clientId, UUID programId){
        return UserProfile.builder()
                .id(clientId)
                .programId(programId)
                .build();
    }

    @GetMapping("/user-program-data/{clientId}")
    public UserProgramData getUserData(@PathVariable UUID clientId){
        return userService.getUserData(clientId);
    }

    @GetMapping("/user-program-data/by-program")
    public Page<UserDataWithoutProgramProjection> getUserDataByProgram(@RequestParam UUID programId, @ParameterObject Pageable pageable){
        return userService.getUserDataByProgram(programId, pageable);
    }
}
