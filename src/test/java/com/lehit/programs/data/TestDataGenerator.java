package com.lehit.programs.data;

import com.lehit.programs.model.*;
import com.lehit.programs.model.enums.ActionItemType;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;

import java.util.UUID;

import static com.lehit.programs.model.enums.ContentVisibilityStatus.DRAFT;
import static org.instancio.Select.field;

@Slf4j
public class TestDataGenerator {

    public static ActionItem generateAI(ActionItemType type, int position, String mediaUrl, String question, UUID taskId){
        return ActionItem.builder()
                .title("title for " + taskId)
                .itemType(type)
                .position(position)
                .help("helpTest")
                .informationItem(new InformationItem(mediaUrl, ""))
                .questionItem(QuestionItem.builder().question(question).build())
                .taskId(taskId)
                .build();
    }

    public static ActionItem generateAI(UUID taskId, int position){
        return Instancio.of(ActionItem.class)
                .set(field(ActionItem::getTaskId), taskId)
                .set(field(ActionItem::getPosition), position)
                .create();
    }

    public static Task generateTask(UUID programId, int position){
        return Instancio.of(Task.class)
                .set(field(Task::getProgramId), programId)
                .set(field(Task::getPosition), position)
                .ignore(field(Task::getActionItems))
                .create();
    }



    public static Program generateProgram(UUID authorId, String title){
        return Instancio.of(Program.class)
                .set(field(Program::getAuthorId), authorId)
                .set(field(Program::getTitle), title)
                .set(field(Program::getVisibilityStatus), DRAFT)
                .ignore(field(Program::getTasks))
                .create();
    }

    public static Program generateProgram(UUID authorId){
        return Instancio.of(Program.class)
                .set(field(Program::getAuthorId), authorId)
                .set(field(Program::getVisibilityStatus), DRAFT)
                .ignore(field(Program::getTasks))
                .create();
    }

    public static Author generateAuthor(){
        return Instancio.create(Author.class);
    }

}
