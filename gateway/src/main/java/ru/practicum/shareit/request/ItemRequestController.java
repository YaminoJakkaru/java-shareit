package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @Autowired
    public ItemRequestController(ItemRequestClient itemRequestClient) {
        this.itemRequestClient = itemRequestClient;
    }

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                                @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Creating request " + itemRequestDto);
        return  itemRequestClient.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public  ResponseEntity<Object> getAllUserRequests(@RequestHeader("X-Sharer-User-Id") @NotNull int userId) {
        log.info("Getting all user requests");
        return itemRequestClient.getAllUserRequests(userId);
    }

    @GetMapping("/all")
    public  ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") @NotNull int userId,
                                          @PositiveOrZero  @RequestParam(defaultValue = "0")  int from,
                                          @PositiveOrZero @RequestParam(defaultValue = "20")  int size) {
        log.info("Getting all requests");
        return itemRequestClient.getAllItemsRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public  ResponseEntity<Object> findRequestById(@RequestHeader("X-Sharer-User-Id") @NotNull int userId,
                                          @PathVariable int requestId) {
        log.info("Getting  requests " + requestId);
        return  itemRequestClient.findItemRequestsById(userId, requestId);
    }
}
