package ru.practicum.shareit.item.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import java.util.List;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final UserStorage userStorage;
    public final ItemStorage itemStorage;

    @Autowired
    public ItemServiceImpl(UserStorage userStorage, ItemStorage itemStorage) {
        this.userStorage = userStorage;
        this.itemStorage = itemStorage;
    }

    @Override
    public Item addItem(int userId, Item item) {
        User user = userStorage.findUserById(userId);
        if (user == null) {
            log.warn("Попытка получить несучествующий аккаунт");
            throw new UserNotFoundException();
        }
        item.setOwner(user);
        return itemStorage.addItem(item);
    }

    @Override
    public Item updateItem(int userId, int itemId, Item newItem) {

        Item item = itemStorage.findItemById(itemId);

        if (item == null) {
            log.warn("Попытка изменить данные несуществующего придмета");
            throw new ItemNotFoundException();
        }
        User user = userStorage.findUserById(userId);
        if (user == null || userId != item.getOwner().getId()) {
            log.warn("Попытка изменить данные придмета без прав доступа");
            throw new UserNotFoundException();
        }
        if (newItem.getName() != null && !newItem.getName().equals(item.getName())) {
            item.setName(newItem.getName());
        }
        if (newItem.getDescription() != null && !newItem.getDescription().equals(item.getDescription())) {
            item.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null && newItem.getAvailable() != item.getAvailable()) {
            item.setAvailable(newItem.getAvailable());
        }
        return itemStorage.updateItem(item);
    }

    @Override
    public Item findItemById(int itemId) {
        Item item = itemStorage.findItemById(itemId);
        if (item == null) {
            log.warn("Попытка получить данные несуществующего предмета");
            throw new ItemNotFoundException();
        }
        return item;
    }

    @Override
    public List<Item> getAllUserItems(int userId) {
        return itemStorage.getAllUserItems(userId);
    }

    @Override
    public List<Item> searchItems(String text) {
        return itemStorage.searchItems(text.toLowerCase());
    }
}
