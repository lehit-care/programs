package com.lehit.programs.data;

import com.lehit.programs.model.*;
import com.lehit.programs.model.enums.ActionItemType;
import com.lehit.programs.model.enums.ContentVisibilityStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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



    public Program generateProgram(UUID authorId, String title){
        return Program.builder()
                .description("test")
                .title(title)
                .authorId(authorId)
                .build();
    }

    public Program generateProgram(UUID authorId){
        return Program.builder()
                .description("test")
                .title("test")
                .authorId(authorId)
                .visibilityStatus(ContentVisibilityStatus.DRAFT)
                .build();
    }

    public Author generateAuthor(){
        return Author.builder()
                .id(UUID.randomUUID())
                .firstName("name"+UUID.randomUUID())
                .lastName("ln"+UUID.randomUUID())
                .build();
    }

}
