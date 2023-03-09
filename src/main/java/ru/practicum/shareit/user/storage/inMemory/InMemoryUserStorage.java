package ru.practicum.shareit.user.storage.inMemory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    private int id = 0;

    @Override
    public User addUser(User user) {
        user.setId(++id);
        users.put(id, user);
        log.info("Создан аккаунт " + id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        log.info("Обновлены данные аккаунта " + id);
        return user;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return users.values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    @Override
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    @Override
    public User findUserById(int userId) {
        return users.get(userId);
    }

    @Override
    public void deleteUser(int userId) {
        users.remove(userId);
    }
}
