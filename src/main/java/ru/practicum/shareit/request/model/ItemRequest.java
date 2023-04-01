package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String description;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    User requestor;



}
