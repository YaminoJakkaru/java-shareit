package ru.practicum.shareit.unitTest.serviseTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingRepository;

import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;
import ru.practicum.shareit.booking.vo.Status;
import ru.practicum.shareit.item.ItemRepository;

import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    BookingRepository bookingRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;


    @InjectMocks
    BookingServiceImpl bookingService;


    User userFirst = new User()
            .setId(1)
            .setName("this.getName")
            .setEmail("this.get@Email.ru");
    User userSecond = new User()
            .setId(2)
            .setName("this.getSecondName")
            .setEmail("this.getSecond@Email.ru");
    Item itemFirst = new Item()
            .setId(1)
            .setName("hammer")
            .setDescription("heavy")
            .setAvailable(true)
            .setOwner(userFirst);
    Item itemSecond = new Item()
            .setId(2)
            .setName("axe")
            .setDescription("blunt, but can be used as a hammer")
            .setAvailable(true)
            .setOwner(userSecond);
    Booking bookingFirst = new Booking()
            .setId(1)
            .setStart(LocalDateTime.parse("2010-01-21T05:47:08.644"))
            .setEnd(LocalDateTime.parse("2019-01-21T05:47:08.644"))
            .setItem(itemSecond)
            .setBooker(userFirst)
            .setStatus(Status.WAITING);

    @Test
    void addBookingTest_wrongBooking() {
        Booking bookingWrong = new Booking()
                .setId(2)
                .setStart(LocalDateTime.parse("2019-01-21T05:47:08.644"))
                .setEnd(LocalDateTime.parse("2010-01-21T05:47:08.644"))
                .setItem(itemFirst)
                .setBooker(userSecond)
                .setStatus(Status.WAITING);
        Assertions.assertThrows(ValidationException.class, () -> bookingService.addBooking(userFirst.getId(),
                bookingWrong.toBookingDto()));
    }

    @Test
    void addBookingTest_whenUserIsNull() {
        int userBasicId = 0;

        when(itemRepository.findItemById(itemSecond.getId())).thenReturn(itemSecond);
        when(userRepository.findUserById(userBasicId)).thenReturn(null);
        Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.addBooking(userBasicId, bookingFirst.toBookingDto()));
    }

    @Test
    void addBookingTest_whenItemIsNull() {
        when(itemRepository.findItemById(bookingFirst.getItem().getId())).thenReturn(null);
        Assertions.assertThrows(ItemNotFoundException.class,
                () -> bookingService.addBooking(userFirst.getId(), bookingFirst.toBookingDto()));
    }

    @Test
    void addBookingTest_whenIsNotAvailable() {
        when(itemRepository.findItemById(anyInt())).thenReturn(new Item().setAvailable(false).setOwner(userSecond));
        when(userRepository.findUserById(anyInt())).thenReturn(userFirst);
        Assertions.assertThrows(ValidationException.class, () -> bookingService.addBooking(userFirst.getId(),
                bookingFirst.toBookingDto()));
    }

    @Test
    void addBookingTest_whenAllRight() {
        when(itemRepository.findItemById(anyInt())).thenReturn(itemSecond);
        when(userRepository.findUserById(anyInt())).thenReturn(userFirst);
        when(bookingRepository.save(any())).thenReturn(bookingFirst);
        Assertions.assertEquals(bookingService.addBooking(userFirst.getId(),
                bookingFirst.toBookingDto()), bookingFirst);
        verify(bookingRepository).save(any());
    }

    @Test
    void updateStatusTest_whenBookingIsNull() {
        int userBasicId = 0;
        int bookingBasicId = 0;
        when(bookingRepository.findBookingById(anyInt())).thenReturn(null);
        Assertions.assertThrows(BookingNotFoundException.class,
                () -> bookingService.updateStatus(userBasicId, bookingBasicId, true));
    }

    @Test
    void updateStatusTest_whenApproved() {
        int userBasicId = 0;
        int bookingBasicId = 0;
        when(bookingRepository.findBookingById(anyInt())).thenReturn(new Booking().setStatus(Status.APPROVED));
        Assertions.assertThrows(ValidationException.class,
                () -> bookingService.updateStatus(userBasicId, bookingBasicId, true));
    }

    @Test
    void updateStatusTest_whenUserIsNotOwner() {
        int userBasicId = 0;
        int bookingBasicId = 0;
        when(bookingRepository.findBookingById(anyInt())).thenReturn(bookingFirst);
        Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.updateStatus(userBasicId, bookingBasicId, true));
    }

    @Test
    void updateStatusTest_whenAllRight() {
        when(bookingRepository.findBookingById(anyInt())).thenReturn(bookingFirst);
        when(bookingRepository.save(any())).thenReturn(bookingFirst);
        Assertions.assertEquals(bookingService
                .updateStatus(userSecond.getId(), bookingFirst.getId(), true), bookingFirst);
    }

    @Test
    void findBookingByIdTest_whenBookingIsNull() {
        int userBasicId = 0;
        int bookingBasicId = 0;
        when(bookingRepository.findBookingById(anyInt())).thenReturn(null);
        Assertions.assertThrows(BookingNotFoundException.class,
                () -> bookingService.findBookingById(userBasicId, bookingBasicId));
    }

    @Test
    void findBookingByIdTest_whenUserIsNotBookerAndNotOwner() {
        int userBasicId = 0;
        int bookingBasicId = 0;
        when(bookingRepository.findBookingById(anyInt())).thenReturn(bookingFirst);
        Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.findBookingById(userBasicId, bookingBasicId));
    }

    @Test
    void findBookingByIdTest_whenIsBooker() {
        when(bookingRepository.findBookingById(anyInt())).thenReturn(bookingFirst);
        Assertions.assertEquals(bookingService.findBookingById(userFirst.getId(), bookingFirst.getId()), bookingFirst);
    }

    @Test
    void findBookingByIdTest_whenIsOwner() {
        when(bookingRepository.findBookingById(anyInt())).thenReturn(bookingFirst);
        Assertions.assertEquals(bookingService.findBookingById(userSecond.getId(), bookingFirst.getId()), bookingFirst);
    }

    @Test
    void getBookingsByUserIdTest_WhenUserIsNull() {
        int userBasicId = 0;

        when(userRepository.findUserById(userBasicId)).thenReturn(null);
        Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.getBookingsByUserId(userBasicId, "ALL", 0, 1));
    }

    @Test
    void getBookingsByUserIdTest_ALL() {
        when(userRepository.findUserById(userFirst.getId())).thenReturn(userFirst);
        when(bookingRepository.findBookingsByBookerIdOrderByIdDesc(userFirst.getId(),
                PageRequest.of(0, 10))).thenReturn(new PageImpl<>(List.of()));
        Assertions.assertEquals(bookingService.getBookingsByUserId(userFirst.getId(), "ALL", 0, 10),
                List.of());
    }

    @Test
    void getBookingsByUserIdTest_PAST() {
        when(userRepository.findUserById(userFirst.getId())).thenReturn(userFirst);
        when(bookingRepository
                .findBookingsByBookerIdAndEndIsBeforeOrderByIdDesc(eq(userFirst.getId()),
                        any(), eq(PageRequest.of(0, 10))))
                .thenReturn(new PageImpl<>(List.of(bookingFirst)));
        Assertions.assertEquals(bookingService.getBookingsByUserId(userFirst.getId(), "PAST", 0, 10),
                List.of(bookingFirst));
    }

    @Test
    void getBookingsByUserIdTest_CURRENT() {
        when(userRepository.findUserById(userFirst.getId())).thenReturn(userFirst);

        when(bookingRepository
                .findBookingsByBookerIdAndStartIsBeforeAndEndIsAfterOrderByIdAsc(eq(userFirst.getId()),
                        any(), any(), eq(PageRequest.of(0, 10))))
                .thenReturn(new PageImpl<>(List.of(bookingFirst)));
        Assertions.assertEquals(bookingService.getBookingsByUserId(userFirst.getId(), "CURRENT", 0, 10),
                List.of(bookingFirst));
    }

    @Test
    void getBookingsByUserIdTest_FUTURE() {
        when(userRepository.findUserById(userFirst.getId())).thenReturn(userFirst);
        when(bookingRepository
                .findBookingsByBookerIdAndStartIsAfterOrderByIdDesc(eq(userFirst.getId()),
                        any(), eq(PageRequest.of(0, 10))))
                .thenReturn(new PageImpl<>(List.of(bookingFirst)));
        Assertions.assertEquals(bookingService.getBookingsByUserId(userFirst.getId(), "FUTURE", 0, 10),
                List.of(bookingFirst));
    }

    @Test
    void getBookingsByUserIdTest_WAITING() {
        when(userRepository.findUserById(userFirst.getId())).thenReturn(userFirst);
        when(bookingRepository
                .findBookingsByBookerIdAndStatusIsOrderByIdDesc(eq(userFirst.getId()),
                        any(), eq(PageRequest.of(0, 10))))
                .thenReturn(new PageImpl<>(List.of(bookingFirst)));
        Assertions.assertEquals(bookingService.getBookingsByUserId(userFirst.getId(), "WAITING", 0, 10),
                List.of(bookingFirst));
    }

    @Test
    void getBookingsByUserIdTest_REJECTED() {
        when(userRepository.findUserById(userFirst.getId())).thenReturn(userFirst);
        when(bookingRepository
                .findBookingsByBookerIdAndStatusIsOrderByIdDesc(eq(userFirst.getId()),
                        any(), eq(PageRequest.of(0, 10))))
                .thenReturn(new PageImpl<>(List.of(bookingFirst)));
        Assertions.assertEquals(bookingService.getBookingsByUserId(userFirst.getId(), "REJECTED", 0, 10),
                List.of(bookingFirst));
    }

    @Test
    void getBookingsByItemsOwnerIdTest_WhenUserIsNull() {
        int userBasicId = 0;

        when(userRepository.findUserById(userBasicId)).thenReturn(null);
        Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.getBookingsByItemsOwnerId(userBasicId, "ALL", 0, 1));
    }

    @Test
    void getBookingsByItemsOwnerIdTest_ALL() {
        when(userRepository.findUserById(userFirst.getId())).thenReturn(userFirst);
        when(bookingRepository.findBookingsByItemOwnerIdOrderByIdDesc(userFirst.getId(),
                PageRequest.of(0, 10))).thenReturn(new PageImpl<>(List.of()));
        Assertions.assertEquals(bookingService.getBookingsByItemsOwnerId(userFirst.getId(), "ALL", 0, 10),
                List.of());
    }

    @Test
    void getBookingsByItemsOwnerIdTest_CURRENT() {
        when(userRepository.findUserById(userFirst.getId())).thenReturn(userFirst);

        when(bookingRepository
                .findBookingsByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByIdAsc(eq(userFirst.getId()),
                        any(), any(), eq(PageRequest.of(0, 10))))
                .thenReturn(new PageImpl<>(List.of(bookingFirst)));
        Assertions.assertEquals(bookingService.getBookingsByItemsOwnerId(userFirst.getId(), "CURRENT", 0, 10),
                List.of(bookingFirst));
    }

    @Test
    void getBookingsByItemsOwnerIdTest_PAST() {
        when(userRepository.findUserById(userFirst.getId())).thenReturn(userFirst);
        when(bookingRepository
                .findBookingsByItemOwnerIdAndEndIsBeforeOrderByIdDesc(eq(userFirst.getId()),
                        any(), eq(PageRequest.of(0, 10))))
                .thenReturn(new PageImpl<>(List.of(bookingFirst)));
        Assertions.assertEquals(bookingService.getBookingsByItemsOwnerId(userFirst.getId(), "PAST", 0, 10),
                List.of(bookingFirst));
    }

    @Test
    void getBookingsByItemsOwnerIdTest_FUTURE() {
        when(userRepository.findUserById(userFirst.getId())).thenReturn(userFirst);
        when(bookingRepository
                .findBookingsByItemOwnerIdAndStartIsAfterOrderByIdDesc(eq(userFirst.getId()),
                        any(), eq(PageRequest.of(0, 10))))
                .thenReturn(new PageImpl<>(List.of(bookingFirst)));
        Assertions.assertEquals(bookingService.getBookingsByItemsOwnerId(userFirst.getId(), "FUTURE", 0, 10),
                List.of(bookingFirst));
    }

    @Test
    void getBookingsByItemsOwnerIdTest_WAITING() {
        when(userRepository.findUserById(userFirst.getId())).thenReturn(userFirst);
        when(bookingRepository
                .findBookingsByItemOwnerIdAndStatusIsOrderByIdDesc(eq(userFirst.getId()),
                        any(), eq(PageRequest.of(0, 10))))
                .thenReturn(new PageImpl<>(List.of(bookingFirst)));
        Assertions.assertEquals(bookingService.getBookingsByItemsOwnerId(userFirst.getId(), "WAITING", 0, 10),
                List.of(bookingFirst));
    }

    @Test
    void getBookingsByItemsOwnerIdTest_REJECTED() {
        when(userRepository.findUserById(userFirst.getId())).thenReturn(userFirst);
        when(bookingRepository
                .findBookingsByItemOwnerIdAndStatusIsOrderByIdDesc(eq(userFirst.getId()),
                        any(), eq(PageRequest.of(0, 10))))
                .thenReturn(new PageImpl<>(List.of(bookingFirst)));
        Assertions.assertEquals(bookingService.getBookingsByItemsOwnerId(userFirst.getId(), "REJECTED", 0, 10),
                List.of(bookingFirst));
    }
}
