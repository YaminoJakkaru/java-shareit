package ru.practicum.shareit.item.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") int userId, @RequestBody @Valid ItemDto itemDto) {
        return itemService.addItem(userId, itemDto);
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
    public List<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") @NotNull int userId,
                                         @RequestParam(defaultValue = "0")  int from,
                                         @RequestParam(defaultValue = "20")  int size) {
        if(from < 0 || size < 0){
            throw  new ValidationException();
        }
        return itemService.getAllUserItems(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text,
                                     @RequestParam(defaultValue = "0")  int from,
                                     @RequestParam(defaultValue = "20")  int size) {
        if(from < 0 || size < 0){
            throw  new ValidationException();
        }
        return itemService.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
            public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") @NotNull int userId,
                                         @PathVariable @NotNull int itemId, @RequestBody @Valid CommentDto commentDto) {
        return itemService.addComment(userId,itemId,commentDto);
    }
}
