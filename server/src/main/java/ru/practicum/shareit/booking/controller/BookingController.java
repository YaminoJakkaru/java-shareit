package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
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
    public Booking create(@RequestHeader("X-Sharer-User-Id") int userId, @RequestBody  BookingDto bookingDto) {
        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Booking updateStatus(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int bookingId,
                                @RequestParam boolean approved) {
        return bookingService.updateStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking findBookingById(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable  int bookingId) {
        return bookingService.findBookingById(userId, bookingId);
    }

    @GetMapping
    public List<Booking> getBookingsByUserId(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @RequestParam String state,
                                             @RequestParam  int from,
                                             @RequestParam  int size) {

        return bookingService.getBookingsByUserId(userId, state, from, size);
    }

    @GetMapping("/owner")
    List<Booking> getBookingsByOwnerId(@RequestHeader("X-Sharer-User-Id") int userId,
                                       @RequestParam String state,
                                       @RequestParam  int from,
                                       @RequestParam  int size) {
        return bookingService.getBookingsByItemsOwnerId(userId, state, from, size);
    }
}
