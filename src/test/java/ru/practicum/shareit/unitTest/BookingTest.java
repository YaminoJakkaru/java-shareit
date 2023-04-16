package ru.practicum.shareit.unitTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.vo.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class BookingTest {

    User user =  new User()
                .setId(1)
                .setName("this.getName")
                .setEmail("this.get@Email.ru");

    Item item = new Item()
                .setId(1)
                .setName("this.getName")
                .setDescription("this.getDescription")
                .setAvailable(true)
                .setOwner(user);

    Booking booking = new Booking()
                .setId(1)
                .setStart(LocalDateTime.parse("2019-01-21T05:47:08.644"))
            .setEnd(LocalDateTime.parse("2019-02-21T05:47:08.644"))
            .setItem(item)
                .setBooker(user)
                .setStatus(Status.APPROVED);

    @Test
    void toBookingDtoTest() {
        Assertions.assertEquals(booking.toBookingDto(), new BookingDto()
                .setId(1)
                .setStart(LocalDateTime.parse("2019-01-21T05:47:08.644"))
                .setEnd(LocalDateTime.parse("2019-02-21T05:47:08.644"))
                .setItemId(1)
                .setItemName("this.getName")
                .setBookerId(1)
                .setStatus(Status.APPROVED));
    }

    @Test
    void toBooking() {
        Assertions.assertEquals(booking.toBookingDto().toBooking(), new Booking()
                .setStart(LocalDateTime.parse("2019-01-21T05:47:08.644"))
                .setEnd(LocalDateTime.parse("2019-02-21T05:47:08.644")));
    }
}
