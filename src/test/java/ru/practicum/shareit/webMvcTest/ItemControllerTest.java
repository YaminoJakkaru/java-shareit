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
import ru.practicum.shareit.item.dto.ItemDto;
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

    int userId = 0;
    int itemId = 0;
    int from = 0;
    int size = 20;
    String wrongValue = "-1";
    String rightValue = "1";

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
        ItemDto itemEmptyName = new ItemDto()
                .setName("")
                .setDescription("text")
                .setAvailable(true);
        ItemDto itemEmptyDescription = new ItemDto()
                .setName("text")
                .setDescription("")
                .setAvailable(true);
        ItemDto itemNullAvailable = new ItemDto()
                .setName("text")
                .setDescription("text");
        when(itemService.addItem(userId, itemFirst.toItemDto())).thenReturn(itemFirst.toItemDto());

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemEmptyName)))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).addItem(userId, itemEmptyName);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemEmptyDescription)))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).addItem(userId, itemEmptyDescription);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemNullAvailable)))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).addItem(userId, itemNullAvailable);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemFirst)))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).addItem(userId, itemFirst.toItemDto());


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

        mockMvc.perform(patch("/items/{itemId}",itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemFirst)))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).updateItem(userId,itemId, itemFirst);

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
        mockMvc.perform(get("/items/{itemId}",itemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).findItemById(userId,itemId);

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
        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).getAllUserItems(userId,from, size);

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .param("from", wrongValue))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).getAllUserItems(userId,Integer.parseInt(wrongValue), size);

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .param("size", wrongValue))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).getAllUserItems(userId, from, Integer.parseInt(wrongValue));

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .param("size", rightValue))
                .andExpect(status().isOk());
        verify(itemService).getAllUserItems(userId, from, Integer.parseInt(rightValue));

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
        verify(itemService).getAllUserItems(userId, from, size);
    }

    @SneakyThrows
    @Test
    void searchItemsTest() {
        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemFirst)))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).searchItems(rightValue,from, size);

        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("from", wrongValue))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).searchItems(rightValue,Integer.parseInt(wrongValue), size);

        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("size", wrongValue))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).searchItems(rightValue, from, Integer.parseInt(wrongValue));

        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", rightValue)
                        .param("size", rightValue))
                .andExpect(status().isOk());
        verify(itemService).searchItems(rightValue, from, Integer.parseInt(rightValue));

        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", rightValue))
                .andExpect(status().isOk());
        verify(itemService).searchItems(rightValue, from, size);
    }

    @SneakyThrows
    @Test
    void  createCommentTest() {

        CommentDto commentFirst = new CommentDto()
                .setText("Text");

        when(itemService.addComment(userId, itemId, commentFirst)).thenReturn(commentFirst);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(new CommentDto())))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).addComment(userId, itemId, new CommentDto());

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemFirst)))
                .andExpect(status().isBadRequest());
        verify(itemService, never()).addComment(userId, itemId, commentFirst);


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
