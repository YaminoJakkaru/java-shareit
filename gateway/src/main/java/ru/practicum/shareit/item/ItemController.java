package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;


@Controller
@RequestMapping(path = "/items")
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @Autowired
    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @RequestBody @Valid ItemDto itemDto) {
        log.info("Creating item " + itemDto);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> changeItem(@RequestHeader("X-Sharer-User-Id") @NotNull int userId,
                                             @PathVariable int itemId,
                                             @RequestBody ItemDto itemDto) {
        log.info("Changing item " + itemId);
        return itemClient.changeItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItemById(@RequestHeader("X-Sharer-User-Id") @NotNull int userId,
                                               @PathVariable int itemId) {
        log.info("Finding item " + itemId);
        return itemClient.findItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserItems(@RequestHeader("X-Sharer-User-Id") @NotNull int userId,
                                                  @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                  @PositiveOrZero@RequestParam(defaultValue = "20") int size) {
        log.info("Getting all user item");
        return itemClient.getItemsByUser(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") @NotNull int userId,
                                              @RequestParam String text,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                              @PositiveOrZero @RequestParam(defaultValue = "20") int size) {
        log.info("Searching " + text);
        return itemClient.searchItem(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") @NotNull int userId,
                                                @PathVariable @NotNull int itemId,
                                                @RequestBody @Valid CommentDto commentDto) {

        return itemClient.createComment(userId, itemId, commentDto);
    }
}


