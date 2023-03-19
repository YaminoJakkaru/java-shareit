package ru.practicum.shareit.hendler;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.ValidationException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ValidationException handle(final ValidationException e) {
        return new ValidationException();
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
}
