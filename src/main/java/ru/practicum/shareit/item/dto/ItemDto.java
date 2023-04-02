package ru.practicum.shareit.item.dto;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Accessors(chain = true)
@Data
public class ItemDto {

    private int id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotNull
    private Boolean available;


    private User owner;


    private ItemRequest request;

    private BookingDto lastBooking;

    private BookingDto nextBooking;

    private final List<CommentDto> comments = new ArrayList<>();



    public  Item toItem() {
        return new Item()
                .setId(this.getId())
                .setName(this.getName())
                .setDescription(this.getDescription())
                .setAvailable(this.getAvailable());
    }

    public  void setNearBookings(List<Booking> bookings) {
        bookings.removeIf(booking -> booking.getItem().getId() != this.getId());
       Booking bookingLast = bookings.stream().filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
               .max(Comparator.comparing(Booking::getStart)).orElse(null);
        Booking bookingNext = bookings.stream().filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
               .min(Comparator.comparing(Booking::getStart)).orElse(null);
        if (bookingLast != null) {
            this.lastBooking = bookingLast.toBookingDto();
        }
        if (bookingNext != null) {
            this.nextBooking = bookingNext.toBookingDto();
        }
    }

    public void addComment(Comment comment) {
        comments.add(comment.toCommentDto());
    }


}
