package ru.practicum.shareit.unitTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentTest {

    User user =  new User()
                .setId(1)
                .setName("this.getName")
                .setEmail("this.get@Email.ru");

    Item item = new Item()
                .setId(1)
                .setName("this.getName")
                .setDescription("this.getDescription")
                .setAvailable(true)
                .setOwner(user);
    Comment comment = new Comment()
            .setId(1)
            .setText("this.getText")
            .setCreated(LocalDateTime.parse("2019-01-21T05:47:08.644"))
            .setAuthor(user)
            .setItem(item);

    @Test
    void toCommentDtoTest() {
        Assertions.assertEquals(comment.toCommentDto(),new CommentDto()
                .setId(1)
                .setText("this.getText")
                .setAuthorId(1)
                .setAuthorName("this.getName")
                .setItemId(1)
                .setCreated(LocalDateTime.parse("2019-01-21T05:47:08.644")));
    }

    @Test
    void toCommentTest() {
        Assertions.assertEquals(comment.toCommentDto().toComment(),new Comment()
                .setText("this.getText"));
    }
}
