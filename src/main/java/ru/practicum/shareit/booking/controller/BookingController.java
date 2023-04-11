package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public Booking create(@RequestHeader("X-Sharer-User-Id") int userId, @RequestBody @Valid BookingDto bookingDto) {
        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Booking updateStatus(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int bookingId,
                                @RequestParam boolean approved) {
        return bookingService.updateStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking findBookingById(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable @NotNull int bookingId) {
        return bookingService.findBookingById(userId, bookingId);
    }

    @GetMapping
    public List<Booking> getBookingsByUserId(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingsByUserId(userId, state);
    }

    @GetMapping("/owner")
    List<Booking> getBookingsByOwnerId(@RequestHeader("X-Sharer-User-Id") int userId,
                                       @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingsByItemsOwnerId(userId, state);
    }
}
