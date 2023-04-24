package ru.practicum.shareit.unitTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

 class UserTest {

    User user =  new User()
                .setId(1)
                .setName("this.getName")
                .setEmail("this.get@Email.ru");

    @Test
    void toUserDtoTest() {
        Assertions.assertEquals(user.toUserDto(), new UserDto()
                .setId(1)
                .setName("this.getName")
                .setEmail("this.get@Email.ru"));
    }

    @Test void toUserTest() {
        Assertions.assertEquals(user.toUserDto().toUser(), user);
    }
}
