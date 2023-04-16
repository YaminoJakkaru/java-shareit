package ru.practicum.shareit.unitTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.vo.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemTest {


       User user =  new User()
                .setId(1)
                .setName("this.getName")
                .setEmail("this.get@Email.ru");

       ItemRequest itemRequest= new ItemRequest()
                .setId(1)
                .setDescription("this.getDescription")
                .setRequestor(user)
                .setCreated(LocalDateTime.parse("2019-01-21T05:47:08.644"));
       Item item = new Item()
                .setId(1)
                .setName("this.getName")
                .setDescription("this.getDescription")
                .setAvailable(true)
                .setOwner(user);
       Booking  booking = new Booking()
                .setId(1)
                .setStart(LocalDateTime.now().minusDays(3))
                .setEnd(LocalDateTime.now().minusDays(2))
                .setItem(item)
                .setBooker(user)
                .setStatus(Status.APPROVED);
       Booking bookingSecond = new Booking()
                .setId(2)
                .setStart(LocalDateTime.now().minusDays(2))
                .setEnd(LocalDateTime.now().plusDays(1))
                .setItem(item)
                .setBooker(user)
                .setStatus(Status.APPROVED);
       Booking bookingThird = new Booking()
                .setId(3)
                .setStart(LocalDateTime.now().plusDays(2))
                .setEnd(LocalDateTime.now().plusDays(3))
                .setItem(item)
                .setBooker(user)
                .setStatus(Status.APPROVED);
       Booking bookingFourth = new Booking()
                .setId(4)
                .setStart(LocalDateTime.now().plusDays(3))
                .setEnd(LocalDateTime.now().plusDays(5))
                .setItem(item)
                .setBooker(user)
                .setStatus(Status.APPROVED);
       List<Booking> bookings = List.of(booking, bookingFourth, bookingSecond, bookingThird);



    @Test
    void toItemDtoTest() {
        Assertions.assertEquals(item.toItemDto(), new ItemDto()
                .setId(1)
                .setName("this.getName")
                .setDescription("this.getDescription")
                .setAvailable(true)
                .setOwner(user));
        item.setRequest(itemRequest);
        Assertions.assertEquals(item.toItemDto(), new ItemDto()
                .setId(1)
                .setName("this.getName")
                .setDescription("this.getDescription")
                .setAvailable(true)
                .setOwner(user)
                .setRequestId(itemRequest.getId()));
    }

    @Test
    void toItemTest() {
        Assertions.assertEquals(item.toItemDto().toItem(), new Item()
                .setId(1)
                .setName("this.getName")
                .setDescription("this.getDescription")
                .setAvailable(true));
    }

    @Test
    void  setNearBookingsTest() {
        ItemDto itemDto =item.toItemDto();
        itemDto.setNearBookings(bookings);
        Assertions.assertEquals(itemDto.getLastBooking(), bookingSecond.toBookingDto());
        Assertions.assertEquals(itemDto.getNextBooking(), bookingThird.toBookingDto());
    }
}
