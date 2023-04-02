package ru.practicum.shareit.item.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.vo.Status;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(UserRepository userRepository, ItemRepository itemRepository, BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public Item addItem(int userId, Item item) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            log.warn("Попытка получить несучествующий аккаунт");
            throw new UserNotFoundException();
        }
        item.setOwner(user);
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item updateItem(int userId, int itemId, Item newItem) {
        Item item = itemRepository.findItemById(itemId);
        if (item == null) {
            log.warn("Попытка изменить данные несуществующего придмета");
            throw new ItemNotFoundException();
        }
        User user = userRepository.findUserById(userId);
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
        if (newItem.getAvailable() != null) {
            item.setAvailable(newItem.getAvailable());
        }
        return itemRepository.save(item);
    }

    @Override
    public ItemDto findItemById(int userId, int itemId) {
        Item item = itemRepository.findItemById(itemId);
        if (item == null) {
            log.warn("Попытка получить данные несуществующего предмета");
            throw new ItemNotFoundException();
        }
        ItemDto itemDto = item.toItemDto();
        if (userId == itemDto.getOwner().getId()) {
            itemDto.setNearBookings(bookingRepository.findBookingsByItemIdAndStatusIsNot(itemDto.getId(), Status.REJECTED));
        }
        commentRepository.findCommentsByItemId(itemId).forEach(itemDto::addComment);
        return itemDto;
    }

    @Override
    public List<ItemDto> getAllUserItems(int userId) {
        HashMap<Integer, ItemDto> items = new HashMap<>();
        itemRepository.findItemsByOwnerIdOrderByIdAsc(userId).forEach(item -> items.put(item.getId(),item.toItemDto()));
        List<Booking> allBookings = bookingRepository.findBookingsByItemOwnerIdAndStatusIsNot(userId, Status.REJECTED);
        List<Comment> allComments = commentRepository.findCommentByItemOwnerId(userId);

            allComments.forEach(comment -> items.get(comment.getItem().getId()).addComment(comment));
            for (Booking booking : allBookings) {
                if (booking.getStart().isBefore(LocalDateTime.now())) {
                    if (items.get(booking.getItem().getId()).getLastBooking() == null
                            || items.get(booking.getItem().getId()).getLastBooking().getStart()
                            .isBefore(booking.getStart())) {
                        items.get(booking.getItem().getId()).setLastBooking(booking.toBookingDto());
                    }
                }
                if (booking.getStart().isAfter(LocalDateTime.now())) {
                    if (items.get(booking.getItem().getId()).getNextBooking() == null
                            || items.get(booking.getItem().getId()).getNextBooking().getStart()
                            .isAfter(booking.getStart())) {
                        items.get(booking.getItem().getId()).setNextBooking(booking.toBookingDto());
                    }
                }
            }
        return new ArrayList<>(items.values());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        List<ItemDto> items = new ArrayList<>();
        if (!text.isBlank()) {
            itemRepository.findItemByNameOrDescriptionContainsIgnoreCaseAndAvailableIsTrue(text, text)
                    .forEach(item -> items.add(item.toItemDto()));
        }
        return items;
    }

    @Override
    @Transactional
    public CommentDto addComment(int userId, int itemId, CommentDto commentDto) {
        Item item = itemRepository.findItemById(itemId);
        if (item == null) {
            log.warn("Попытка оставить отзыв несуществующему предмету");
            throw new ValidationException();
        }
        User user = userRepository.findUserById(userId);
        if (user == null) {
            log.warn("Попытка оствить от имени несуществующего пользователя");
            throw new ValidationException();
        }
        Booking booking = bookingRepository.findFirstBookingsByBookerIdAndItemId(userId, itemId);
        if (booking == null || booking.getStart().isAfter(LocalDateTime.now())) {
            log.warn("Попытка  осавить отзыв без прав");
            throw new ValidationException();
        }
        Comment comment = commentDto.toComment().setItem(item).setAuthor(user).setCreated(LocalDateTime.now());
        return commentRepository.save(comment).toCommentDto();
    }
}
