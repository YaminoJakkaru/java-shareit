package ru.practicum.shareit.webMvcTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class UserControllerTest {

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


    int userId = 0;
    User user = new User()
            .setId(1)
            .setName("this.getName")
            .setEmail("this.get@Email.ru");


    @SneakyThrows
    @Test
    void createTest() {
        UserDto userEmptyName = new UserDto()
                .setEmail("this.get@Email.ru");
        UserDto userEmptyEmail = new UserDto()
                .setName("this.getName");
        UserDto userWrongEmail = new UserDto()
                .setName("this.getName")
                .setEmail("this.getEmail.ru");

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userEmptyName)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).addUser(userEmptyName.toUser());
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userEmptyEmail)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).addUser(userEmptyEmail.toUser());
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userWrongEmail)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).addUser(userWrongEmail.toUser());
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
        verify(userService).addUser(user);
    }

    @SneakyThrows
    @Test
    void changeTest() {
        User userWrongFirst = new User()
                .setId(1)
                .setName("this.getName")
                .setEmail("this.getEmail.ru");
        mockMvc.perform(patch("/users/{userId}",userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userWrongFirst)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).updateUser(userWrongFirst.getId(),userWrongFirst);
        mockMvc.perform(patch("/users/{userId}",userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
        verify(userService).updateUser(userId, user);
    }

    @SneakyThrows
    @Test
    void getAllUsersTest() {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
        verify(userService).getAllUsers();
    }

    @SneakyThrows
    @Test
    void getUserTest() {
        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk());
        verify(userService).findUserById(userId);
    }

    @SneakyThrows
    @Test
    void deleteTest() {
        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());
        verify(userService).deleteUser(userId);
    }
}
