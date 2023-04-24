package ru.practicum.shareit.webMvcTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private ItemService itemService;
    @MockBean
    ItemRequestService itemRequestService;

    @InjectMocks
    private ItemController itemController;



    @SneakyThrows
    @Test
    void createTest() {

        int userId = 0;
        ItemRequestDto itemRequestDto = new ItemRequestDto().setDescription("text");

        when(itemRequestService.addRequest(userId, itemRequestDto))
                .thenReturn(itemRequestDto);


        String res = mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertEquals(res, objectMapper.writeValueAsString(itemRequestDto));
        verify(itemRequestService).addRequest(userId, itemRequestDto);

    }

    @SneakyThrows
    @Test
    void getAllUserRequestsTest() {
        int userId = 0;

        mockMvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
        verify(itemRequestService).findAllByRequestorId(userId);
    }


    @SneakyThrows
    @Test
    void getAllTest() {
        int userId = 0;
        int from = 0;
        int size = 20;

        mockMvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());
        verify(itemRequestService).findAll(userId, from, size);
    }

    @SneakyThrows
    @Test
    void findRequestByIdTest() {
        int userId = 0;
        int requestId = 0;

        ItemRequestDto itemRequestDto = new ItemRequestDto().setDescription("text");
        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(itemRequestService, never()).findById(anyInt(), anyInt());

        when(itemRequestService.findById(userId, requestId)).thenReturn(itemRequestDto);
        String res = mockMvc.perform(get("/requests/{requestId}", requestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertEquals(res, objectMapper.writeValueAsString(itemRequestDto));
        verify(itemRequestService).findById(userId, requestId);
    }
}
