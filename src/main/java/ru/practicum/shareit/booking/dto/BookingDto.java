package ru.practicum.shareit.booking.dto;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.vo.Status;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    int id;
    LocalDateTime start;
    LocalDateTime end;
    int item;
    int booker;
    Status status;
}
