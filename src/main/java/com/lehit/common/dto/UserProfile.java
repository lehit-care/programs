package com.lehit.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lehit.common.enums.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfile {
    private UUID id;

    private String email;

    private String firstName;

    private String lastName;

    private String mobileNumber;

    private String additionalDetails;

    private String avatarUrl;

    private UUID parentId;

    private UUID programId;

    private Language language;

    private LocalDateTime createdAt;

}
