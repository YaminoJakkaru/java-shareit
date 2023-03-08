package ru.practicum.shareit.item.dto;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ItemDto {
    private int id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotNull
    private Boolean available;

    private int request;

    public  Item toItem() {
        return Item.builder()
                .id(this.getId())
                .name(this.getName())
                .description(this.getDescription())
                .available(this.getAvailable())
                .build();
    }
}
