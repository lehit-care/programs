package com.lehit.programs.data;

import com.lehit.programs.model.*;
import com.lehit.programs.model.enums.ActionItemType;
import com.lehit.programs.model.enums.ContentVisibilityStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class TestDataGenerator {

    public ActionItem generateAI(ActionItemType type, Integer position, String mediaUrl, String question, UUID taskId){
        return ActionItem.builder()
                .title("title for " + taskId)
                .itemType(type)
                .position(position.byteValue())
                .help("helpTest")
                .informationItem(new InformationItem(mediaUrl, ""))
                .questionItem(QuestionItem.builder().question(question).build())
                .taskId(taskId)
                .build();
    }

    public Task generateTask(UUID programId, int position){
        return Task.builder()
                .avatarUrl("testAvatar")
                .position(position)
                .programId(programId)
                .title("title for "  + position)
                .build();
    }

    public Task generateDummyTask(){
        return Task.builder()
                .avatarUrl("testAvatar")
                .build();
    }



    public Program generateProgram(){
        return Program.builder()
                .description("test")
                .title("test")
                .author(UUID.randomUUID())
                .build();
    }


    public Program generateProgram(String title){
        return Program.builder()
                .description("test")
                .title(title)
                .author(UUID.randomUUID())
                .build();
    }

    public Program generateProgram(UUID authorId){
        return Program.builder()
                .description("test")
                .title("test")
                .author(authorId)
                .visibilityStatus(ContentVisibilityStatus.DRAFT)
                .build();
    }

    public int generateRandomStep(){
       return new Random().nextInt() & Integer.MAX_VALUE;
    }


}
