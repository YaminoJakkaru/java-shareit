package ru.practicum.shareit.item.storage.inMemory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Integer, Item> items = new HashMap<>();

    private int id = 0;


    @Override
    public Item addItem(Item item) {
        item.setId(++id);
        items.put(id, item);
        log.info("Добавлен предмет " + id);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        log.info("Обновлены данные предмета " + item.getId());
        return item;
    }

    @Override
    public Item findItemById(int itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllUserItems(int userId) {
        return items.values().stream().filter(item -> item.getOwner().getId() == userId).collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItems(String text) {
        List<Item> searchedItems = new ArrayList<>();
        if (!text.isEmpty()) {
            items.values().stream().filter(item -> (item.getName().toLowerCase().contains(text) ||
                            item.getDescription().toLowerCase().contains(text)) && item.getAvailable())
                    .forEach(searchedItems::add);
        }
        return searchedItems;
    }
}
