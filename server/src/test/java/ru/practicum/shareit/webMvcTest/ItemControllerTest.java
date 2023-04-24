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
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.controller.ItemController;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ItemControllerTest {

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

    @InjectMocks
      ItemController itemController;



    User userFirst = new User()
            .setId(1)
            .setName("this.getName")
            .setEmail("this.get@Email.ru");
    User userSecond = new User()
            .setId(2)
            .setName("this.getSecondName")
            .setEmail("this.getSecond@Email.ru");
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
            .setOwner(userSecond)
            .setRequest(itemRequestFirst);

    @SneakyThrows
    @Test
    void createItemTest() {
        int userId = 0;


        when(itemService.addItem(userId, itemFirst.toItemDto())).thenReturn(itemFirst.toItemDto());


        String res = mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemFirst.toItemDto())))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertEquals(res, objectMapper.writeValueAsString(itemFirst.toItemDto()));
        verify(itemService).addItem(userId, itemFirst.toItemDto());
    }

    @SneakyThrows
    @Test
    void changeItemTest() {
        int userId = 0;
        int itemId = 0;



        when(itemService.updateItem(userId,itemId, itemFirst)).thenReturn(itemFirst);
        String res = mockMvc.perform(patch("/items/{itemId}",itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemFirst)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertEquals(res, objectMapper.writeValueAsString(itemFirst));
        verify(itemService).updateItem(userId,itemId, itemFirst);
    }

    @SneakyThrows
    @Test
    void findItemByIdTest() {
        int userId = 0;
        int itemId = 0;



        when(itemService.findItemById(userId,itemId)).thenReturn(itemFirst.toItemDto());
        String res = mockMvc.perform(get("/items/{itemId}",itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertEquals(res, objectMapper.writeValueAsString(itemFirst.toItemDto()));
        verify(itemService).findItemById(userId,itemId);
    }

    @SneakyThrows
    @Test
    void getAllUserItemsTest() {
        int userId = 0;
        String from = "0";
       String size = "20";

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                .param("from", from)
                .param("size", size))
                .andExpect(status().isOk());
        verify(itemService).getAllUserItems(userId, Integer.parseInt(from), Integer.parseInt(size));
    }

    @SneakyThrows
    @Test
    void searchItemsTest() {
        String from = "0";
        String size = "20";
        String rightValue = "1";


        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", rightValue)
                        .param("from", from)
                        .param("size", size))
                .andExpect(status().isOk());
        verify(itemService).searchItems(rightValue, Integer.parseInt(from), Integer.parseInt(size));
    }

    @SneakyThrows
    @Test
    void  createCommentTest() {
        int userId = 0;
        int itemId = 0;

        CommentDto commentFirst = new CommentDto()
                .setText("Text");

        when(itemService.addComment(userId, itemId, commentFirst)).thenReturn(commentFirst);

        String res = mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(commentFirst)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assertions.assertEquals(res, objectMapper.writeValueAsString(commentFirst));
        verify(itemService).addComment(userId, itemId, commentFirst);
    }



}
