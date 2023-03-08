package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item addItem(int userId, Item item);

    Item updateItem(int userId, int itemId, Item item);

    Item findItemById(int itemId);

    List<Item> getAllUserItems(int userId);

    List<Item> searchItems(String text);
}
