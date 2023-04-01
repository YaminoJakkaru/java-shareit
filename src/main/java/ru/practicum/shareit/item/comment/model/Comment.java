package ru.practicum.shareit.item.comment.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Accessors(chain = true)
@Entity
@Data
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "created_date")
    private LocalDateTime created;

    public CommentDto toCommentDto(){
        return new CommentDto()
                .setId(this.getId())
                .setText(this.getText())
                .setAuthorId(this.getAuthor().getId())
                .setAuthorName(this.getAuthor().getName())
                .setItemId(this.getItem().getId())
                .setCreated(this.getCreated());
    }
}