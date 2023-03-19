package ru.practicum.shareit.item.model;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
@Builder
public class Item {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest request;

    public  ItemDto toItemDto() {
        return ItemDto.builder()
                .id(this.getId())
                .name(this.getName())
                .description(this.getDescription())
                .available(this.getAvailable())
                .request(this.getRequest() != null ? this.getRequest().getId() : null)
                .build();
    }
}
