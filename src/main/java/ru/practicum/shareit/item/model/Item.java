package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import javax.persistence.*;

@Accessors(chain = true)
@Entity
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 252)
    private String name;

    @Column(nullable = false, length = 512)
    private String description;

    @Column(name = "is_available", nullable = false)
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ItemRequest request;

    public ItemDto toItemDto() {
        return new ItemDto()
                .setId(this.getId())
                .setName(this.getName())
                .setDescription(this.getDescription())
                .setOwner(this.getOwner())
                .setAvailable(this.getAvailable())
                .setRequestId(this.getRequest() == null ? null : this.getRequest().getId());
    }
}
