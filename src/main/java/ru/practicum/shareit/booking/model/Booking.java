package ru.practicum.shareit.booking.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.vo.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "start_date")
    private LocalDateTime start;

    @Column(name = "end_date")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "booker_id")
    private User booker;

    @Enumerated(EnumType.STRING)
    private Status status;

    public BookingDto toBookingDto() {
        return new BookingDto()
                .setId(this.getId())
                .setStart(this.getStart())
                .setEnd(this.getEnd())
                .setItemId(this.getItem().getId())
                .setItemName(this.getItem().getName())
                .setBookerId(this.getBooker().getId())
                .setStatus(this.getStatus());
    }
}
