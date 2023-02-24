package com.lehit.programs.model.sql.config;

import com.lehit.programs.model.ProgramExecution;
import com.lehit.programs.model.TaskExecution;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.ResultListTransformer;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DistinctProgramExecutionsResultTransformer implements ResultListTransformer {
    private final EntityManager entityManager;


    @Override
    public List transformList(List list) {

        Map<Serializable, Object> identifiableMap =
                new LinkedHashMap<>(list.size());

        for (Object entityArray : list) {
            if (Object[].class.isAssignableFrom(entityArray.getClass())) {
                ProgramExecution programExecution = null;
                TaskExecution taskExecution = null;

                Object[] tuples = (Object[]) entityArray;

                for (Object tuple : tuples) {
//                    if(tuple instanceof Identifiable) {
                        entityManager.detach(tuple);

                        if (tuple instanceof ProgramExecution) {
                            programExecution = (ProgramExecution) tuple;
                        }

                        else if (tuple instanceof TaskExecution){
                            taskExecution = (TaskExecution) tuple;
                        }
                }

                if (programExecution != null) {
                    if (!identifiableMap.containsKey(programExecution.getId())) {
                        identifiableMap.put(programExecution.getId(), programExecution);
                        programExecution.setTaskExecutions(new ArrayList<>());
                    }
                    if (taskExecution != null) {
                        programExecution.getTaskExecutions().add(taskExecution);
                        taskExecution.setItemExecutions(new ArrayList<>());
                    }
                }
            }
        }
        return new ArrayList<>(identifiableMap.values());
    }



}
