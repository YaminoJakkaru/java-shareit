package ru.practicum.shareit.item.comment.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.practicum.shareit.item.comment.model.Comment;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;


@Accessors(chain = true)
@Data
public class CommentDto {

    private int id;

    @NotEmpty
    private String text;

    private int authorId;

    private String authorName;

    private int itemId;

    private LocalDateTime created;

    public Comment toComment (){
        return new Comment()
                .setText(this.getText());
    }


}
