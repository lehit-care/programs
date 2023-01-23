package com.lehit.programs.repository;


import com.lehit.programs.model.UserData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDataRepository extends JpaRepository<UserData, UUID>, JpaSpecificationExecutor<UserData>{

    @EntityGraph("including-program")
//   todo @RestResource(path = "all")
    @Query(value = "SELECT d FROM UserData d")
    Page<UserData> selectFullData(Pageable pageable);


    @Cacheable(value="UserProgramData", key = "#id")
    @EntityGraph("including-program")
//    todo @RestResource(path = "by-id")
    @Query(value = "SELECT d FROM UserData d WHERE d.id = ?1")
    Optional<UserData> selectFullDatum(UUID id);


    <T> Optional<T> findById(UUID uuid, Class<T> type);

    @EntityGraph("including-program")
//    todo @RestResource(path = "by-ids")
    List<UserData> findByIdIn(UUID... ids);

    <T> Page<T> findByProgramId(UUID programId, Class<T> type, Pageable pageable);
}
