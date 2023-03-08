package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User addUser(User user);

    User updateUser(int userId, User user);

    List<User> getAllUsers();

    User findUserById(int userId);

    void deleteUser(int userId);
}
