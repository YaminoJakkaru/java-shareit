package ru.practicum.shareit.integratonTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;


import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.vo.Status;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;


import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationTest {

     UserService userService;
     ItemService itemService;
     BookingService bookingService;

     ItemRequestService itemRequestService;

    User userFirst = new User()
            .setId(1)
            .setName("this.getName")
            .setEmail("this.get@Email.ru");
    User userSecond = new User()
            .setId(2)
            .setName("this.getSecondName")
            .setEmail("this.getSecond@Email.ru");
    User userThird = new User()
            .setId(3)
            .setName("this.getThirdName")
            .setEmail("this.getThird@Email.ru");
    User userFourth = new User()
            .setId(4)
            .setName("this.getFourthName")
            .setEmail("this.getFourth@Email.ru");
    User userFive = new User()
            .setId(5)
            .setName("this.getFiveName")
            .setEmail("this.getFive@Email.ru");

    ItemRequest itemRequestFirst = new ItemRequest()
            .setId(1)
            .setDescription("I am bored")
            .setRequestor(userFirst)
            .setCreated(LocalDateTime.parse("2019-01-21T05:47:08.644"));

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
    Item itemThird = new Item()
            .setId(3)
            .setName("chainsaw")
            .setDescription("do brrr")
            .setAvailable(true)
            .setOwner(userSecond);
    Item itemByFirstRequest = new Item()
            .setId(4)
            .setName("Банан")
            .setDescription("еш")
            .setOwner(userSecond)
            .setAvailable(true)
            .setRequest(itemRequestFirst);
    Booking bookingFirst = new Booking()
            .setId(1)
            .setStart(LocalDateTime.parse("2010-01-21T05:47:08.644"))
            .setEnd(LocalDateTime.parse("2019-01-21T05:47:08.644"))
            .setItem(itemSecond)
            .setBooker(userFirst)
            .setStatus(Status.WAITING);
    Booking bookingSecond = new Booking()
            .setId(2)
            .setStart(LocalDateTime.parse("2019-01-21T05:47:08.644"))
            .setEnd(LocalDateTime.parse("2100-01-21T05:47:08.644"))
            .setItem(itemSecond)
            .setBooker(userThird)
            .setStatus(Status.WAITING);
    Booking bookingThird = new Booking()
            .setId(3)
            .setStart(LocalDateTime.parse("2110-01-21T05:47:08.644"))
            .setEnd(LocalDateTime.parse("2120-01-21T05:47:08.644"))
            .setItem(itemSecond)
            .setBooker(userFirst)
            .setStatus(Status.WAITING);
    Booking bookingFourth = new Booking()
            .setId(4)
            .setStart(LocalDateTime.parse("2121-01-21T05:47:08.644"))
            .setEnd(LocalDateTime.parse("2124-01-21T05:47:08.644"))
            .setItem(itemSecond)
            .setBooker(userFive)
            .setStatus(Status.WAITING);
    Booking bookingFive = new Booking()
            .setId(5)
            .setStart(LocalDateTime.parse("2121-01-21T05:47:08.644"))
            .setEnd(LocalDateTime.parse("2124-01-21T05:47:08.644"))
            .setItem(itemSecond)
            .setBooker(userFive)
            .setStatus(Status.WAITING);


    @Test
    @Order(1)
    void addUserTest() {
        assertThat(userService.addUser(userFirst))
                .isEqualTo(userFirst);
        assertThat(userService.addUser(userSecond))
                .isEqualTo(userSecond);
        assertThat(userService.addUser(userThird))
                .isEqualTo(userThird);
        assertThat(userService.addUser(userFourth))
                .isEqualTo(userFourth);
        assertThat(userService.addUser(userFive))
                .isEqualTo(userFive);
    }

    @Test
    @Order(2)
    void updateUserTest() {

        User userNew = new User()
                .setId(1)
                .setName("this.getNewName")
                .setEmail("this.getNew@Email.ru");
        assertThat(userService.updateUser(1, userNew)).isEqualTo(userNew);
        assertThat(userService.updateUser(1, userFirst)).isEqualTo(userFirst);
    }

    @Test
    @Order(3)
    void getAllUsersTest() {
        assertThat(userService.getAllUsers()).isEqualTo(List.of(userFirst, userSecond, userThird, userFourth, userFive));
    }

    @Test
    @Order(4)
    void findUserById() {

        assertThatThrownBy(() -> userService.findUserById(99)).isInstanceOf(UserNotFoundException.class);
        assertThat(userService.findUserById(1)).isEqualTo(userFirst);
        assertThat(userService.findUserById(2)).isEqualTo(userSecond);

    }


    @Test
    @Order(5)
    void addItemTest() {
        assertThatThrownBy(() -> itemService.addItem(99, itemFirst.toItemDto()))
                .isInstanceOf(UserNotFoundException.class);
        userService.addUser(userFirst);

        assertThat(itemService.addItem(itemFirst.getOwner().getId(), itemFirst.toItemDto()))
                .isEqualTo(itemFirst.toItemDto());
        assertThat(itemService.addItem(itemSecond.getOwner().getId(), itemSecond.toItemDto()))
                .isEqualTo(itemSecond.toItemDto());
        assertThat(itemService.addItem(2, itemThird.toItemDto()))
                .isEqualTo(itemThird.toItemDto());

    }

    @Test
    @Order(6)
    void updateItemTest() {
        assertThatThrownBy(() -> itemService.updateItem(1, 99, itemFirst))
                .isInstanceOf(ItemNotFoundException.class);
        assertThatThrownBy(() -> itemService.updateItem(99, 1, itemFirst))
                .isInstanceOf(UserNotFoundException.class);
        Item itemNew = new Item()
                .setId(1)
                .setName("this.getNewName")
                .setDescription("this.getNewDescription")
                .setAvailable(false)
                .setOwner(userFirst);
        assertThat(itemService.updateItem(1, 1, itemNew))
                .isEqualTo(itemNew);
        itemService.updateItem(1, 1, itemFirst);

    }

    @Test
    @Order(7)
    void addBookingTest() {
        BookingDto bookingDtoWrongTime = new BookingDto().setStart(LocalDateTime.parse("2121-01-21T05:47:08.644"))
                .setEnd(LocalDateTime.parse("2021-01-21T05:47:08.644"))
                .setItemId(1);
        BookingDto bookingDtoWrongItem = new BookingDto().setStart(LocalDateTime.parse("2121-01-21T05:47:08.644"))
                .setEnd(LocalDateTime.parse("2221-01-21T05:47:08.644"))
                .setItemId(99);

        assertThatThrownBy(() -> bookingService.addBooking(1, bookingDtoWrongTime))
                .isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> bookingService.addBooking(1, bookingDtoWrongItem))
                .isInstanceOf(ItemNotFoundException.class);
        assertThatThrownBy(() -> bookingService.addBooking(99, bookingFirst.toBookingDto()))
                .isInstanceOf(UserNotFoundException.class);
        assertThat(bookingService.addBooking(1, bookingFirst.toBookingDto()))
                .isEqualTo(bookingFirst);
        assertThat(bookingService.addBooking(3, bookingSecond.toBookingDto()))
                .isEqualTo(bookingSecond);
        assertThat(bookingService.addBooking(1, bookingThird.toBookingDto()))
                .isEqualTo(bookingThird);
        assertThat(bookingService.addBooking(5, bookingFourth.toBookingDto()))
                .isEqualTo(bookingFourth);
        assertThat(bookingService.addBooking(5, bookingFive.toBookingDto()))
                .isEqualTo(bookingFive);
    }


    @Test
    @Order(8)
    void findBookingByIdTest() {
        assertThatThrownBy(() -> bookingService.findBookingById(1, 99))
                .isInstanceOf(BookingNotFoundException.class);
        assertThatThrownBy(() -> bookingService.findBookingById(99, 2))
                .isInstanceOf(UserNotFoundException.class);
        assertThat(bookingService.findBookingById(3, 2)).isEqualTo(bookingSecond);
    }


    @Test
    @Order(9)
    void findUItemByIdTest() {
        assertThatThrownBy(() -> itemService.findItemById(99, 4))
                .isInstanceOf(UserNotFoundException.class);
        assertThat(itemService.findItemById(1, 1)).isEqualTo(itemFirst.toItemDto());
        ItemDto itemSecondDto = itemSecond.toItemDto();
        assertThat(itemService.findItemById(1, 2)).isEqualTo(itemSecondDto);
        itemSecondDto.setLastBooking(bookingSecond.toBookingDto());
        itemSecondDto.setNextBooking(bookingThird.toBookingDto());
        assertThat(itemService.findItemById(2, 2)).isEqualTo(itemSecondDto);
    }

    @Test
    @Order(10)
    void getAllUserItemsTest() {
        assertThatThrownBy(() -> itemService.getAllUserItems(7, 0, 1))
                .isInstanceOf(UserNotFoundException.class);

        assertThat(itemService.getAllUserItems(1, 0, 2))
                .isEqualTo(List.of(itemFirst.toItemDto()));
        ItemDto itemSecondDto = itemSecond.toItemDto();
        itemSecondDto.setLastBooking(bookingSecond.toBookingDto());
        itemSecondDto.setNextBooking(bookingThird.toBookingDto());
        ItemDto itemThirdDto = itemThird.toItemDto();
        itemThirdDto.setNextBooking(bookingFive.toBookingDto());
        assertThat(itemService.getAllUserItems(2, 0, 2)).isEqualTo(List.of(itemSecondDto, itemThird.toItemDto()));
    }

    @Test
    @Order(11)
    void searchItems() {
        assertThat(itemService.searchItems("hammer", 0, 2)).isEqualTo(List.of(itemFirst.toItemDto(), itemSecond.toItemDto()));
    }

    @Test
    @Order(12)
    void addComment() {
        CommentDto commentDto = itemService.addComment(1, 2, new CommentDto()
                .setText("Text"));
        assertThatThrownBy(() -> itemService.addComment(1, 99, commentDto))
                .isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> itemService.addComment(99, 1, commentDto))
                .isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> itemService.addComment(1, 3, commentDto))
                .isInstanceOf(ValidationException.class);
        commentDto.setCreated(LocalDateTime.parse("2010-01-21T05:47:08.644"));
        assertThat(commentDto)
                .isEqualTo(new CommentDto()
                        .setId(1)
                        .setText("Text")
                        .setAuthorId(1)
                        .setAuthorName("this.getName")
                        .setItemId(2)
                        .setCreated(LocalDateTime.parse("2010-01-21T05:47:08.644")));
    }

    @Test
    @Order(13)
    void updateBookingStatusTest() {
        bookingSecond.setStatus(Status.APPROVED);
        assertThatThrownBy(() -> bookingService.updateStatus(1, 99, true))
                .isInstanceOf(BookingNotFoundException.class);
        assertThatThrownBy(() -> bookingService.updateStatus(99, 2, true))
                .isInstanceOf(UserNotFoundException.class);
        assertThat(bookingService.updateStatus(bookingSecond.getItem().getOwner().getId(),
                bookingSecond.getId(), true)).isEqualTo(bookingSecond);
        assertThatThrownBy(() -> bookingService.updateStatus(3, 2, true))
                .isInstanceOf(ValidationException.class);
        bookingFourth.setStatus(Status.REJECTED);
        assertThat(bookingService.updateStatus(bookingFourth.getItem().getOwner().getId(),
                bookingFourth.getId(), false)).isEqualTo(bookingFourth);
    }

    @Test
    @Order(14)
    void getBookingsByUserIdTest() {
        bookingSecond.setStatus(Status.APPROVED);
        bookingFourth.setStatus(Status.REJECTED);
        assertThatThrownBy(() -> bookingService.getBookingsByUserId(99, "ALL", 0, 2))
                .isInstanceOf(UserNotFoundException.class);
        assertThat(bookingService.getBookingsByUserId(1, "ALL", 0, 2))
                .isEqualTo(List.of(bookingThird, bookingFirst));
        assertThat(bookingService.getBookingsByUserId(3, "CURRENT", 0, 2))
                .isEqualTo(List.of(bookingSecond));
        assertThat(bookingService.getBookingsByUserId(1, "PAST", 0, 2))
                .isEqualTo(List.of(bookingFirst));
        assertThat(bookingService.getBookingsByUserId(5, "FUTURE", 0, 2))
                .isEqualTo(List.of(bookingFive, bookingFourth));
        assertThat(bookingService.getBookingsByUserId(5, "WAITING", 0, 2))
                .isEqualTo(List.of(bookingFive));
        assertThat(bookingService.getBookingsByUserId(5, "REJECTED", 0, 2))
                .isEqualTo(List.of(bookingFourth));
    }

    @Test
    @Order(15)
    void getBookingsByItemsOwnerIdTest() {
        bookingSecond.setStatus(Status.APPROVED);
        bookingFourth.setStatus(Status.REJECTED);
        assertThatThrownBy(() -> bookingService.getBookingsByItemsOwnerId(99, "ALL", 0, 2))
                .isInstanceOf(UserNotFoundException.class);
        assertThat(bookingService.getBookingsByItemsOwnerId(2, "ALL", 0, 2))
                .isEqualTo(List.of(bookingFive, bookingFourth));
        assertThat(bookingService.getBookingsByItemsOwnerId(2, "CURRENT", 0, 2))
                .isEqualTo(List.of(bookingSecond));
        assertThat(bookingService.getBookingsByItemsOwnerId(2, "PAST", 0, 2))
                .isEqualTo(List.of(bookingFirst));
        assertThat(bookingService.getBookingsByItemsOwnerId(2, "FUTURE", 0, 2))
                .isEqualTo(List.of(bookingFive, bookingFourth));
        assertThat(bookingService.getBookingsByItemsOwnerId(2, "WAITING", 0, 2))
                .isEqualTo(List.of(bookingFive, bookingThird));
        assertThat(bookingService.getBookingsByItemsOwnerId(2, "REJECTED", 0, 2))
                .isEqualTo(List.of(bookingFourth));
    }

    @Test
    @Order(16)
    void addRequestTest() {
        assertThatThrownBy(() -> itemRequestService.addRequest(99, itemRequestFirst.toItemRequestDto()))
                .isInstanceOf(UserNotFoundException.class);
        assertThat(itemRequestService.addRequest(itemRequestFirst.getRequestor().getId(),
                        itemRequestFirst.toItemRequestDto())
                .setCreated(itemRequestFirst.getCreated()))
                .isEqualTo(itemRequestFirst.toItemRequestDto());
    }


    @Test
    @Order(17)
    void findAllTest() {
        assertThat(itemRequestService.findAll(2, 0, 20)
                .get(0).setCreated(itemRequestFirst.getCreated()))
                .isEqualTo(itemRequestFirst.toItemRequestDto());
        assertThat(itemRequestService.findAll(itemRequestFirst.getId(), 0, 20))
                .isEqualTo(List.of());
    }

    @Test
    @Order(18)
    void findByIdTest() {
        assertThatThrownBy(() -> itemRequestService.findById(99, 1))
                .isInstanceOf(UserNotFoundException.class);
        assertThatThrownBy(() -> itemRequestService.findById(1, 99))
                .isInstanceOf(ItemRequestNotFoundException.class);
        assertThat(itemRequestService.findById(itemRequestFirst.getRequestor().getId(), itemRequestFirst.getId())
                .setCreated(itemRequestFirst.getCreated()))
                .isEqualTo(itemRequestFirst.toItemRequestDto());


    }

    @Test
    @Order(19)
    void findAllByRequestorIdTest() {
        ItemRequestDto itemRequestDto = itemRequestFirst.toItemRequestDto();
        itemRequestDto.addItem(itemByFirstRequest);
        assertThat(itemService.addItem(userSecond.getId(), itemByFirstRequest.toItemDto()))
                .isEqualTo(itemByFirstRequest.toItemDto());
        assertThatThrownBy(() -> itemRequestService.findAllByRequestorId(99))
                .isInstanceOf(UserNotFoundException.class);
        assertThat(itemRequestService.findAllByRequestorId(itemRequestFirst.getRequestor().getId())
                .get(0).setCreated(itemRequestFirst.getCreated()))
                .isEqualTo(itemRequestDto);
    }

    @Test
    @Order(20)
    void deleteUser() {
        userService.deleteUser(1);
        assertThatThrownBy(() -> userService.findUserById(1)).isInstanceOf(UserNotFoundException.class);
        assertThat(userService.getAllUsers()).isEqualTo(List.of(userSecond, userThird, userFourth, userFive));
    }
}
