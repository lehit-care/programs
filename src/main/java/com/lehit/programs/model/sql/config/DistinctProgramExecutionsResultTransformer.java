package com.lehit.programs.model.sql.config;

import com.lehit.programs.model.Program;
import com.lehit.programs.model.ProgramExecution;
import com.lehit.programs.model.Task;
import com.lehit.programs.model.TaskExecution;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.util.Identifiable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DistinctProgramExecutionsResultTransformer implements ListResultTransformer{
    private final EntityManager entityManager;


    @Override
    public List transformList(List list) {

        Map<Serializable, Object> identifiableMap =
                new LinkedHashMap<>(list.size());

        for (Object entityArray : list) {
            if (Object[].class.isAssignableFrom(entityArray.getClass())) {
                ProgramExecution programExecution = null;
                Program program = null;
                TaskExecution taskExecution = null;
                Task task = null;

                Object[] tuples = (Object[]) entityArray;

                for (Object tuple : tuples) {
//                    if(tuple instanceof Identifiable) {
                        entityManager.detach(tuple);

                        if (tuple instanceof ProgramExecution) {
                            programExecution = (ProgramExecution) tuple;
                        }
//                        else if (tuple instanceof Program) {
//                            program = (Program) tuple;
//                        }
                        else if (tuple instanceof TaskExecution){
                            taskExecution = (TaskExecution) tuple;
                        }
//                        else if (tuple instanceof Task){
//                            task = (Task) tuple;
//                        }
//                        else {
//                            throw new UnsupportedOperationException(
//                                    "Tuple " + tuple.getClass() + " is not supported!"
//                            );
//                        }
//                    }
                }

                if (programExecution != null) {
                    if (!identifiableMap.containsKey(programExecution.getId())) {
                        identifiableMap.put(programExecution.getId(), programExecution);
                        programExecution.setTaskExecutions(new ArrayList<>());
                    }
                    if (taskExecution != null) {
//                        taskExecution.setProgramExecution(programExecution);
                        programExecution.getTaskExecutions().add(taskExecution);
                        taskExecution.setItemExecutions(new ArrayList<>());
//                        if (task != null) {
//                            taskExecution.setTask(task);
//                        }
                    }
//                    if(program != null){
//                        programExecution.setProgram(program);
//                    }

                }
            }
        }
        return new ArrayList<>(identifiableMap.values());
    }



    @Override
    public Object transformTuple(Object[] objects, String[] strings) {
        return null;
    }

//    @Bean
//    public DistinctProgramExecutionsResultTransformer transformer() {
//        return new DistinctProgramExecutionsResultTransformer(entityManager);
//    }
}
