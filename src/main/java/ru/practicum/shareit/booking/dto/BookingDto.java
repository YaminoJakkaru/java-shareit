package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.vo.Status;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class BookingDto {
    int id;

    @Future
    @NotNull
    LocalDateTime start;

    @Future
    @NotNull
    LocalDateTime end;

    @NotNull
    int itemId;
    String itemName;
    int bookerId;
    Status status;

    public Booking toBooking() {
        return new Booking()
                .setStart(this.getStart())
                .setEnd(this.getEnd());

    }
}
