package ru.practicum.shareit.booking.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.vo.State;
import ru.practicum.shareit.booking.vo.Status;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    @Override
    public Booking addBooking(int bookerId, BookingDto bookingDto) {
        if (!bookingDto.getStart().isBefore(bookingDto.getEnd())) {
            log.warn("Валидация не пройдена");
            throw new ValidationException();
        }
        Item item = itemRepository.findItemById(bookingDto.getItemId());
        if (item == null) {
            log.warn("Попытка получить несучествующий придмет");
            throw new ItemNotFoundException();
        }
        User booker = userRepository.findUserById(bookerId);
        if (booker == null || bookerId == item.getOwner().getId()) {
            log.warn("попутка забронировать ридмет с неправелоного аккаута");
            throw new UserNotFoundException();
        }

        if (!item.getAvailable()) {
            log.warn("Валидация не пройдена");
            throw new ValidationException();
        }
        Booking booking = bookingDto.toBooking().setBooker(booker).setItem(item).setStatus(Status.WAITING);
        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public Booking updateStatus(int userId, int bookingId, boolean approved) {
        Booking booking = bookingRepository.findBookingById(bookingId);
        if (booking == null) {
            log.warn("Попытка получить несучествующую бронь");
            throw new BookingNotFoundException();
        }
        if (approved && booking.getStatus().equals(Status.APPROVED)) {
            log.warn("Попытка повторно подтведить бронирование");
            throw new ValidationException();
        }
        if (userId != booking.getItem().getOwner().getId()) {
            log.warn("Попытка изметь статус брони не обладая правами");
            throw new UserNotFoundException();
        }
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking findBookingById(int bookerId, int bookingId) {
        Booking booking = bookingRepository.findBookingById(bookingId);
        if (booking == null) {
            log.warn("Попытка получить данные несуществующего бронирования");
            throw new BookingNotFoundException();
        }
        if (booking.getBooker().getId() != bookerId && booking.getItem().getOwner().getId() != bookerId) {
            log.warn("Попытка получить данные бронирования не обладая правами");
            throw new UserNotFoundException();
        }
        return booking;
    }

    @Override
    public List<Booking> getBookingsByUserId(int bookerId, String state, int from, int size) {
        User user = userRepository.findUserById(bookerId);
        if (user == null) {
            log.warn("Попытка получить несучествующий аккаунт");
            throw new UserNotFoundException();
        }
        switch (State.valueOf(state)) {
            case ALL:
                return bookingRepository.findBookingsByBookerIdOrderByIdDesc(bookerId,
                        PageRequest.of(from > 0 ? from / size : 0, size)).stream().collect(Collectors.toList());
            case CURRENT:
                return bookingRepository
                        .findBookingsByBookerIdAndStartIsBeforeAndEndIsAfterOrderByIdAsc(bookerId,
                                LocalDateTime.now(), LocalDateTime.now(),
                                PageRequest.of(from > 0 ? from / size : 0, size)).stream().collect(Collectors.toList());
            case PAST:
                return bookingRepository
                        .findBookingsByBookerIdAndEndIsBeforeOrderByIdDesc(bookerId, LocalDateTime.now(),
                                PageRequest.of(from > 0 ? from / size : 0, size)).stream().collect(Collectors.toList());
            case FUTURE:
                return bookingRepository
                        .findBookingsByBookerIdAndStartIsAfterOrderByIdDesc(bookerId, LocalDateTime.now(),
                                PageRequest.of(from > 0 ? from / size : 0, size)).stream().collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findBookingsByBookerIdAndStatusIsOrderByIdDesc(bookerId, Status.WAITING,
                        PageRequest.of(from > 0 ? from / size : 0, size)).stream().collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findBookingsByBookerIdAndStatusIsOrderByIdDesc(bookerId,
                        Status.REJECTED, PageRequest.of(from > 0 ? from / size : 0, size)).stream()
                        .collect(Collectors.toList());
            default:
                return null;
        }
    }

    @Override
    public List<Booking> getBookingsByItemsOwnerId(int ownerId, String state, int from, int size) {
        User user = userRepository.findUserById(ownerId);
        if (user == null) {
            log.warn("Попытка получить несучествующий аккаунт");
            throw new UserNotFoundException();
        }
        switch (State.valueOf(state)) {
            case ALL:
                return bookingRepository.findBookingsByItemOwnerIdOrderByIdDesc(ownerId,
                        PageRequest.of(from > 0 ? from / size : 0, size)).stream().collect(Collectors.toList());
            case CURRENT:
                return bookingRepository
                        .findBookingsByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByIdAsc(ownerId,
                                LocalDateTime.now(),
                                 LocalDateTime.now(), PageRequest.of(from > 0 ? from / size : 0, size)).stream()
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findBookingsByItemOwnerIdAndEndIsBeforeOrderByIdDesc(ownerId,
                        LocalDateTime.now(), PageRequest.of(from > 0 ? from / size : 0, size)).stream()
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findBookingsByItemOwnerIdAndStartIsAfterOrderByIdDesc(ownerId,
                        LocalDateTime.now(), PageRequest.of(from > 0 ? from / size : 0, size)).stream()
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findBookingsByItemOwnerIdAndStatusIsOrderByIdDesc(ownerId, Status.WAITING,
                        PageRequest.of(from > 0 ? from / size : 0, size)).stream()
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findBookingsByItemOwnerIdAndStatusIsOrderByIdDesc(ownerId, Status.REJECTED,
                        PageRequest.of(from > 0 ? from / size : 0, size)).stream()
                        .collect(Collectors.toList());
            default:
                return null;
        }
    }
}
