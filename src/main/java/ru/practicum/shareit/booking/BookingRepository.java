package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.vo.Status;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findBookingById(int bookingId);

    List<Booking> findBookingsByBookerIdOrderByIdDesc(int bookerId);

    List<Booking> findBookingsByBookerIdAndStartIsBeforeAndEndIsAfterOrderByIdAsc(int bookerId, LocalDateTime start,
                                                                                  LocalDateTime end);

    List<Booking> findBookingsByBookerIdAndEndIsBeforeOrderByIdDesc(int bookerId, LocalDateTime end);

    List<Booking> findBookingsByBookerIdAndStartIsAfterOrderByIdDesc(int bookerId, LocalDateTime start);

    List<Booking> findBookingsByBookerIdAndStatusIsOrderByIdDesc(int bookerId, Status status);

    List<Booking> findBookingsByItemOwnerIdOrderByIdDesc(int ownerId);

    List<Booking> findBookingsByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByIdAsc(int ownerId, LocalDateTime start,
                                                                                     LocalDateTime end);

    List<Booking> findBookingsByItemOwnerIdAndEndIsBeforeOrderByIdDesc(int ownerId, LocalDateTime end);

    List<Booking> findBookingsByItemOwnerIdAndStartIsAfterOrderByIdDesc(int ownerId, LocalDateTime start);

    List<Booking> findBookingsByItemOwnerIdAndStatusIsOrderByIdDesc(int ownerId, Status status);


    List<Booking> findBookingsByItemIdAndStatusIsNot(int itemId, Status status);

    Booking findFirstBookingsByBookerIdAndItemId(int bookerId, int itemId);

    List<Booking> findBookingsByItemOwnerIdAndStatusIsNot(int userId, Status status);
}
