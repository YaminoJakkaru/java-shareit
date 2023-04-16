package ru.practicum.shareit.unitTest.serviseTest;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.EmailException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;


import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void updateUserTest_whenUserIsNull() {
        int userId = 0;
        User user = new User();
        when(userRepository.findUserById(userId)).thenReturn(null);
        Assertions.assertThrows( UserNotFoundException.class, () -> userServiceImpl.updateUser(userId, user));
    }

    @Test
    void updateUserTest_DuplicateEmail() {
        int userId = 0;
        User user = new User().setEmail("e@ma.il");
        when(userRepository.findUserById(userId)).thenReturn(new User().setEmail("other.e@ma.il"));
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(new User()));
        Assertions.assertThrows( EmailException.class, () -> userServiceImpl.updateUser(userId, user));
    }

    @Test
    void updateUserTest_OnlyName() {
        int userId = 0;
        User newUser = new User().setName("nameNew").setEmail("e@ma.il");
        when(userRepository.findUserById(userId)).thenReturn(new User().setName("name").setName("e@ma.il"));
        User user = userRepository.findUserById(userId);
        when(userRepository.save(user)).thenReturn(user);
        Assertions.assertEquals(userServiceImpl.updateUser(userId, newUser), user);

    }

    @Test
    void updateUserTest_OnlyEmail() {
        int userId = 0;
        User newUser = new User().setName("name").setEmail("e@maNew.il");
        when(userRepository.findUserById(userId)).thenReturn(new User().setName("name").setName("e@ma.il"));
        User user = userRepository.findUserById(userId);
        when(userRepository.save(user)).thenReturn(user);
        Assertions.assertEquals(userServiceImpl.updateUser(userId, newUser), user);
    }

    @Test
    void getUserByIdTest_whetUserNotFound() {
        int userId = 0;
        when(userRepository.findUserById(userId)).thenReturn(null);
        Assertions.assertThrows( UserNotFoundException.class, () -> userServiceImpl.findUserById(userId));
    }

    @Test
    void getUserByIdTest_whetAllRight() {

        int userId = 1;
        User user = new User();
        when(userRepository.findUserById(userId)).thenReturn(user);

        Assertions.assertEquals(userServiceImpl.findUserById(userId),user);
    }


}
