package ru.practicum.shareit.request.controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
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
                                     @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return  itemRequestService.addRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getAllUserRequests(@RequestHeader("X-Sharer-User-Id") @NotNull int userId) {
        return itemRequestService.findAllByRequestorId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") @NotNull int userId,
                                       @RequestParam(defaultValue = "0")  int from,
                                       @RequestParam(defaultValue = "20")  int size) {
        if (from < 0 || size < 0) {
            throw  new ValidationException();
        }
        return itemRequestService.findAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findRequestById(@RequestHeader("X-Sharer-User-Id") @NotNull int userId,
                                          @PathVariable int requestId) {
        return  itemRequestService.findById(userId, requestId);
    }
}
