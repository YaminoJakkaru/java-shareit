package ru.practicum.shareit.item.storage.inMemory;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Integer, Item> items = new HashMap<>();


    @Override
    public Item addItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
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
