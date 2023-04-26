package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.vo.Status;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class BookingDto {
    private int id;

    private LocalDateTime start;

    private LocalDateTime end;

    private int itemId;
    private String itemName;
    private int bookerId;
    private Status status;

    public Booking toBooking() {
        return new Booking()
                .setStart(this.getStart())
                .setEnd(this.getEnd());

    }
}
