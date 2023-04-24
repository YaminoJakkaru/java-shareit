package ru.practicum.shareit.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.EmailException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(int userId, UserDto newUser) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            log.warn("Попытка изменить несуществующий аккаунт:" + userId);
            throw new UserNotFoundException();
        }
        if (newUser.getName() != null && !newUser.getName().isBlank() && !newUser.getName().equals(user.getName())) {
            user.setName(newUser.getName());
        }
        if (newUser.getEmail() != null && !newUser.getEmail().equals(user.getEmail())) {
            if (userRepository.findUserByEmail(newUser.getEmail()).isPresent()) {
                log.warn("Попытка привязать аккаунт к чужой почте");
                throw new EmailException();
            }
            user.setEmail(newUser.getEmail());
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(int useId) {

        User user = userRepository.findUserById(useId);
        if (user == null) {
            log.warn("Попытка получить несучествующий аккаунт:" + useId);
            throw new UserNotFoundException();
        }
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        log.info("Удален аккаунт " + userId);
        userRepository.removeUserById(userId);
    }
}
