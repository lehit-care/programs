package com.lehit.programs.service;

import com.lehit.common.dto.UserProfile;
import com.lehit.common.dto.UserProgramData;
import com.lehit.programs.model.UserData;
import com.lehit.programs.model.projection.UserDataWithoutProgramProjection;
import com.lehit.programs.repository.UserDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    private final UserDataRepository userDataRepository;

    @Caching(evict = {
            @CacheEvict(value="UserData", key = "#userId"),
            @CacheEvict(value="UserProgramData", key = "#userId")
    })
    @Transactional
    public UserData assignProgram(UUID userId, UUID programId){
        log.debug("assign program {} for user {}", programId, userId);
        var userData = userDataRepository.findById(userId)
                .orElseGet( () -> userDataRepository.save(UserData.builder().id(userId).build()));
        userData.setProgramId(programId);
        return userData;
    }

    @Cacheable(value="UserData", key = "#userId")
    public UserProgramData getUserData(UUID userId){
        return userDataRepository.findById(userId, UserProgramData.class).orElseThrow();
    }


    @Caching(evict = {
            @CacheEvict(value="UserData", key = "#userId"),
            @CacheEvict(value="UserProgramData", key = "#userId")
    })
    @Transactional
    public void deleteUserData(UUID userId){
        userDataRepository.deleteById(userId);
    }



    @Caching(evict = {
            @CacheEvict(value="UserData", key = "#profile.id"),
            @CacheEvict(value="UserProgramData", key = "#profile.id")
    })
    @Transactional
    public UserData saveUserData(UserProfile profile){
        return userDataRepository.save(
                UserData.builder()
                        .id(profile.getId())
                        .build()
        );
    }


    public Page<UserDataWithoutProgramProjection> getUserDataByProgram(UUID programId, Pageable pageable){
        return userDataRepository.findByProgramId(programId, UserDataWithoutProgramProjection.class, pageable);
    }

}
