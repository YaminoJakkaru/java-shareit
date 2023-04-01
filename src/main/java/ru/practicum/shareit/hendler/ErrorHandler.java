package ru.practicum.shareit.hendler;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.WrongStatusException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.service.impl.exception.EmailException;
import ru.practicum.shareit.user.service.impl.exception.UserNotFoundException;


import javax.validation.ValidationException;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationException handle(final ValidationException e) {
        return new ValidationException();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public EmailException handle(final EmailException e) {
        return new EmailException();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public UserNotFoundException handle(final UserNotFoundException e) {
        return new UserNotFoundException();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ItemNotFoundException handle(final ItemNotFoundException e) {
        return new ItemNotFoundException();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BookingNotFoundException handle(final BookingNotFoundException e) {
        return new BookingNotFoundException();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public  Map<String, String> handle(final WrongStatusException e) {
         return Map.of("error", e.getMessage());
    }


}
