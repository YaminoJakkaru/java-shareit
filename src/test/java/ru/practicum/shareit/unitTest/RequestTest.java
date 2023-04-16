package ru.practicum.shareit.unitTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class RequestTest {

    User user =  new User()
            .setId(1)
            .setName("this.getName")
            .setEmail("this.get@Email.ru");

    ItemRequest itemRequest= new ItemRequest()
                .setId(1)
                .setDescription("this.getDescription")
                .setRequestor(user)
                .setCreated(LocalDateTime.parse("2019-01-21T05:47:08.644"));

    @Test
    void toItemRequestDtoTest(){
        Assertions.assertEquals(itemRequest.toItemRequestDto(), new ItemRequestDto()
                .setId(1)
                .setDescription("this.getDescription")
                .setCreated(LocalDateTime.parse("2019-01-21T05:47:08.644")));
    }

    @Test
    void toItemRequestTest() {
        Assertions.assertEquals(itemRequest.toItemRequestDto().toItemRequest(), new ItemRequest()
                .setDescription("this.getDescription"));
    }
}
