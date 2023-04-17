package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.vo.Status;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findBookingById(int bookingId);

    Page<Booking> findBookingsByBookerIdOrderByIdDesc(int bookerId, Pageable pageable);

    Page<Booking> findBookingsByBookerIdAndStartIsBeforeAndEndIsAfterOrderByIdAsc(int bookerId, LocalDateTime start,
                                                                                  LocalDateTime end, Pageable pageable);

    Page<Booking> findBookingsByBookerIdAndEndIsBeforeOrderByIdDesc(int bookerId, LocalDateTime end, Pageable pageable);

    Page<Booking> findBookingsByBookerIdAndStartIsAfterOrderByIdDesc(int bookerId, LocalDateTime start,
                                                                     Pageable pageable);

    Page<Booking> findBookingsByBookerIdAndStatusIsOrderByIdDesc(int bookerId, Status status, Pageable pageable);

    Page<Booking> findBookingsByItemOwnerIdOrderByIdDesc(int ownerId, Pageable pageable);

    Page<Booking> findBookingsByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByIdAsc(int ownerId, LocalDateTime start,
                                                                                     LocalDateTime end,
                                                                                     Pageable pageable);

    Page<Booking> findBookingsByItemOwnerIdAndEndIsBeforeOrderByIdDesc(int ownerId, LocalDateTime end,
                                                                       Pageable pageable);

    Page<Booking> findBookingsByItemOwnerIdAndStartIsAfterOrderByIdDesc(int ownerId, LocalDateTime start,
                                                                        Pageable pageable);

    Page<Booking> findBookingsByItemOwnerIdAndStatusIsOrderByIdDesc(int ownerId, Status status, Pageable pageable);


    List<Booking> findBookingsByItemIdAndStatusIsNot(int itemId, Status status);

    Booking findFirstBookingsByBookerIdAndItemId(int bookerId, int itemId);

    List<Booking> findBookingsByItemOwnerIdAndStatusIsNot(int userId, Status status);
}
