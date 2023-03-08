package ru.practicum.shareit.user.service.impl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;
import javax.validation.ValidationException;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    private int id = 0;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    @Override
    public User addUser(User user) {
        if (userStorage.findUserByEmail(user.getEmail()).isPresent()) {
            log.warn("Попытка создать аккаунт с уже сществующей почтой");
            throw new ValidationException();
        }
        user.setId(++id);
        log.info("Создан аккаунт " + id);
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(int useId, User newUser) {
        User user = userStorage.findUserById(useId);
        if (user == null) {
            log.warn("Попытка изменить несуществующий аккаунт:" + useId);
            throw new UserNotFoundException();
        }
        if (newUser.getName() != null && !newUser.getName().isBlank() && !newUser.getName().equals(user.getName())) {
            user.setName(newUser.getName());
        }
        if (newUser.getEmail() != null && !newUser.getEmail().equals(user.getEmail())) {
            if (userStorage.findUserByEmail(newUser.getEmail()).isPresent()) {
                log.warn("Попытка привязать аккаунт к чужой почте");
                throw new ValidationException();
            }
            user.setEmail(newUser.getEmail());
        }
        log.info("Обновлен аккаунт " + useId);
        return userStorage.updateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User findUserById(int useId) {

        User user = userStorage.findUserById(useId);
        if (user == null) {
            log.warn("Попытка получить несучествующий аккаунт:" + useId);
            throw new UserNotFoundException();
        }
        return user;
    }

    @Override
    public void deleteUser(int userId) {
        log.info("Удален аккаунт " + userId);
        userStorage.deleteUser(userId);
    }
}
