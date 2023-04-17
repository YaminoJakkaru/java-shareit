package ru.practicum.shareit.webMvcTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.vo.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class BookingControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;
    @MockBean
    BookingService bookingService;
    @MockBean
    ItemService itemService;
    @MockBean
    ItemRequestService itemRequestService;


    User userFirst = new User()
            .setId(2)
            .setName("this.getSecondName")
            .setEmail("this.getSecond@Email.ru");
    Item itemFirst = new Item()
            .setId(1)
            .setName("axe")
            .setDescription("blunt, but can be used as a hammer")
            .setAvailable(true)
            .setOwner(userFirst);
    Booking bookingFirst = new Booking()
            .setId(1)
            .setStart(LocalDateTime.parse("2100-01-21T05:47:08.644"))
            .setEnd(LocalDateTime.parse("2111-01-21T05:47:08.644"))
            .setItem(itemFirst)
            .setBooker(userFirst)
            .setStatus(Status.WAITING);

    @SneakyThrows
    @Test
    void createTest() {
        int userId = 0;

        when(bookingService.addBooking(userId, bookingFirst.toBookingDto())).thenReturn(bookingFirst);
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(new Booking())))
                .andExpect(status().isBadRequest());
        verify(bookingService, never()).addBooking(userId, new BookingDto());

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingFirst.toBookingDto())))
                .andExpect(status().isBadRequest());
        verify(bookingService, never()).addBooking(userId, bookingFirst.toBookingDto());

        String res = mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(bookingFirst.toBookingDto())))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertEquals(res, objectMapper.writeValueAsString(bookingFirst));
        verify(bookingService).addBooking(userId, bookingFirst.toBookingDto());
    }

    @SneakyThrows
    @Test
    void updateStatus() {
        int userId = 0;
        int itemId = 0;
        int bookingId = 0;

        when(bookingService.updateStatus(userId, itemId, true)).thenReturn(bookingFirst);
        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());
        verify(bookingService, never()).updateStatus(userId, itemId, true);

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("approved", "true"))
                .andExpect(status().isBadRequest());
        verify(bookingService, never()).updateStatus(userId, itemId, true);

        String res = mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertEquals(res, objectMapper.writeValueAsString(bookingFirst));
        verify(bookingService).updateStatus(userId, itemId, true);
    }

    @SneakyThrows
    @Test
    void findItemByIdTest() {
        int userId = 0;
        int bookingId = 0;

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(bookingService, never()).findBookingById(userId, bookingId);
        when(bookingService.findBookingById(userId, bookingId)).thenReturn(bookingFirst);
        String res = mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertEquals(res, objectMapper.writeValueAsString(bookingFirst));
        verify(bookingService).findBookingById(userId, bookingId);
    }

    @SneakyThrows
    @Test
    void getBookingsByUserIdTest() {
        int userId = 0;
        int from = 0;
        int size = 20;
        String wrongValue = "-1";
        String rightValue = "1";
        String state = "ALL";

        mockMvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(bookingService, never()).getBookingsByUserId(userId, "ALL", from, size);

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("from", wrongValue))
                .andExpect(status().isBadRequest());
        verify(bookingService, never()).getBookingsByUserId(userId, state, Integer.parseInt(wrongValue), size);

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("size", wrongValue))
                .andExpect(status().isBadRequest());
        verify(bookingService, never()).getBookingsByUserId(userId, state, from, Integer.parseInt(wrongValue));

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("size", rightValue))
                .andExpect(status().isOk());
        verify(bookingService).getBookingsByUserId(userId, state, from, Integer.parseInt(rightValue));

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
        verify(bookingService).getBookingsByUserId(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void getBookingsByOwnerIdTest() {
        int userId = 0;
        int from = 0;
        int size = 20;
        String wrongValue = "-1";
        String rightValue = "1";
        String state = "ALL";

        mockMvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(bookingService, never()).getBookingsByItemsOwnerId(userId, "ALL", from, size);

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("from", wrongValue))
                .andExpect(status().isBadRequest());
        verify(bookingService, never()).getBookingsByItemsOwnerId(userId, state, Integer.parseInt(wrongValue), size);

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("size", wrongValue))
                .andExpect(status().isBadRequest());
        verify(bookingService, never()).getBookingsByItemsOwnerId(userId, state, from, Integer.parseInt(wrongValue));

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("size", rightValue))
                .andExpect(status().isOk());
        verify(bookingService).getBookingsByItemsOwnerId(userId, state, from, Integer.parseInt(rightValue));

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
        verify(bookingService).getBookingsByItemsOwnerId(userId, state, from, size);
    }
}
