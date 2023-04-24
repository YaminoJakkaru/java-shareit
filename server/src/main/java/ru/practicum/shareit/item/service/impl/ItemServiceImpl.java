package ru.practicum.shareit.item.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    private final ItemRequestRepository itemRequestRepository;

    @Autowired
    public ItemServiceImpl(UserRepository userRepository, ItemRepository itemRepository, BookingRepository bookingRepository, CommentRepository commentRepository, ItemRequestRepository itemRequestRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.itemRequestRepository = itemRequestRepository;
    }

    @Override
    @Transactional
    public ItemDto addItem(int userId, ItemDto itemDto) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            log.warn("Попытка получить несучествующий аккаунт");
            throw new UserNotFoundException();
        }
        Item item = itemDto.toItem();
        if (itemDto.getRequestId() != null) {
            item.setRequest(itemRequestRepository.findById(itemDto.getRequestId()));
        }
        item.setOwner(user);
        return itemRepository.save(item).toItemDto();
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
        User user = userRepository.findUserById(userId);
        if (user == null) {
            log.warn("Попытка получить данные придмета без прав доступа");
            throw new UserNotFoundException();
        }
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
    public List<ItemDto> getAllUserItems(int userId, int from, int size) {

        User user = userRepository.findUserById(userId);
        if (user == null) {
            log.warn("Попытка получить данные придметов без прав доступа");
            throw new UserNotFoundException();
        }
        Map<Integer, ItemDto> items = itemRepository.findItemsByOwnerIdOrderByIdAsc(userId,
                        PageRequest.of(from > 0 ? from / size : 0, size))
                .stream()
                .collect(Collectors.toMap(Item::getId, Item::toItemDto));
        List<Booking> allBookings = bookingRepository.findBookingsByItemOwnerIdAndStatusIsNot(userId, Status.REJECTED);
        List<Comment> allComments = commentRepository.findCommentByItemOwnerId(userId);

        allComments.forEach(comment -> items.get(comment.getItem().getId()).addComment(comment));
        for (Booking booking : allBookings) {
            if (booking.getStart().isBefore(LocalDateTime.now())
                    && (items.get(booking.getItem().getId()).getLastBooking() == null
                    || items.get(booking.getItem().getId()).getLastBooking().getStart()
                    .isBefore(booking.getStart()))) {
                items.get(booking.getItem().getId()).setLastBooking(booking.toBookingDto());
            }
            if (booking.getStart().isAfter(LocalDateTime.now())
                    && (items.get(booking.getItem().getId()).getNextBooking() == null
                    || items.get(booking.getItem().getId()).getNextBooking().getStart()
                    .isAfter(booking.getStart()))) {
                items.get(booking.getItem().getId()).setNextBooking(booking.toBookingDto());
            }
        }
        return new ArrayList<>(items.values());
    }

    @Override
    public List<ItemDto> searchItems(String text, int from, int size) {

        return text.isBlank() ? List.of() : itemRepository
                .findItemByNameOrDescriptionContainsIgnoreCaseAndAvailableIsTrue(text, text,
                        PageRequest.of(from > 0 ? from / size : 0, size))
                .stream()
                .map(Item::toItemDto)
                .collect(Collectors.toList());
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
