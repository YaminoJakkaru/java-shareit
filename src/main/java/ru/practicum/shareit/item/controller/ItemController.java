package ru.practicum.shareit.item.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public Item createItem(@RequestHeader("X-Sharer-User-Id") int userId, @RequestBody @Valid ItemDto itemDto) {
        return itemService.addItem(userId, itemDto.toItem());
    }

    @PatchMapping("/{itemId}")
    public Item changeItem(@RequestHeader("X-Sharer-User-Id") @NotNull int userId, @PathVariable int itemId,
                       @RequestBody Item item) {
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemById(@RequestHeader("X-Sharer-User-Id") @NotNull int userId, @PathVariable int itemId) {
        return itemService.findItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") @NotNull int userId) {
        return itemService.getAllUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems( @RequestParam String text) {
        return itemService.searchItems( text);
    }

    @PostMapping("/{itemId}/comment")
            public CommentDto CreateComment(@RequestHeader("X-Sharer-User-Id") @NotNull int userId,
                                         @PathVariable @NotNull int itemId, @RequestBody @Valid CommentDto commentDto){
        return itemService.addComment(userId,itemId,commentDto);
    }
}
