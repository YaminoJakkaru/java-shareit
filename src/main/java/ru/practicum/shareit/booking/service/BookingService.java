package ru.practicum.shareit.booking.service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    Booking addBooking(int bookerId, BookingDto bookingDto);

    Booking updateStatus(int bookerId, int bookingId, boolean approved);

    Booking findBookingById(int bookerId,int bookingId);

    List<Booking> getBookingsByUserId(int userId, String state, int from, int size);

    List<Booking> getBookingsByItemsOwnerId(int ownerId, String state, int from, int size);


}
