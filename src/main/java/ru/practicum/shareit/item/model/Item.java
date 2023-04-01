package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.Accessors;
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

    private String name;

    private String description;

    @Column(name = "is_available")
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    private ItemRequest request;

    public ItemDto toItemDto() {
        return new ItemDto()
                .setId(this.getId())
                .setName(this.getName())
                .setDescription(this.getDescription())
                .setOwner(this.getOwner())
                .setAvailable(this.getAvailable())
                .setRequest(this.getRequest());
    }
}
