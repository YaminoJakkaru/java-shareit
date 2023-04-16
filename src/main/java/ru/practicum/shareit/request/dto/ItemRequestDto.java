package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class ItemRequestDto {
    private int id;
    @NotNull
    private String description;

    private LocalDateTime created;
    private List<ItemDto> items = new ArrayList<>();


    public ItemRequest toItemRequest() {
        return new ItemRequest().setDescription(this.getDescription());
    }

    public void addItem(Item item){
        items.add(item.toItemDto());
    }
}
