package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto addItem(int userId, ItemDto itemDto);

    Item updateItem(int userId, int itemId, Item item);

    ItemDto findItemById(int userId, int itemId);

    List<ItemDto> getAllUserItems(int userId, int from, int size);

    List<ItemDto> searchItems(String text, int from, int size);

    CommentDto addComment(int userId, int itemId, CommentDto commentDto);
}
