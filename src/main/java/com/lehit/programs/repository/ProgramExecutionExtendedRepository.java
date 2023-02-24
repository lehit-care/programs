package com.lehit.programs.repository;

import com.lehit.programs.model.ProgramExecution;
import com.lehit.programs.model.sql.config.DistinctProgramExecutionsResultTransformer;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.HibernateHints;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProgramExecutionExtendedRepository {

    private final EntityManager entityManager;
    private final DistinctProgramExecutionsResultTransformer programExecutionsResultTransformer;


    public List<ProgramExecution> getFullExecutions(UUID userId, int count){
        return  (List<ProgramExecution>) entityManager
                .createNamedQuery("ProgramExecution.findProgramExecutionsWithTaskExecutionsWithTasks")
                .setParameter("user", userId)
                .setParameter("rank", count)
                .setHint(HibernateHints.HINT_READ_ONLY, true)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultListTransformer(programExecutionsResultTransformer)
                .getResultList();

    }
}
