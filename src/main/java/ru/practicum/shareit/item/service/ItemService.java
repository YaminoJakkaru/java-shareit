package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item addItem(int userId, Item item);

    Item updateItem(int userId, int itemId, Item item);

    ItemDto findItemById(int userId, int itemId);

    List<ItemDto> getAllUserItems(int userId);

    List<ItemDto> searchItems( String text);

    CommentDto addComment(int userId,int itemId, CommentDto commentDto);
}
