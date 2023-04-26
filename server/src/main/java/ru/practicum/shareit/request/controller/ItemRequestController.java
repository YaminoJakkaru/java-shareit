package ru.practicum.shareit.request.controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import java.util.List;



@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

   private final ItemRequestService itemRequestService;

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                     @RequestBody ItemRequestDto itemRequestDto) {
        return  itemRequestService.addRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getAllUserRequests(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemRequestService.findAllByRequestorId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id")  int userId,
                                       @RequestParam  int from,
                                       @RequestParam  int size) {

        return itemRequestService.findAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findRequestById(@RequestHeader("X-Sharer-User-Id") int userId,
                                          @PathVariable int requestId) {
        return  itemRequestService.findById(userId, requestId);
    }
}
