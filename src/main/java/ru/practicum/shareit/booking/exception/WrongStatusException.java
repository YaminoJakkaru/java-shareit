package ru.practicum.shareit.booking.exception;

public class WrongStatusException extends  RuntimeException{

    public WrongStatusException(String message) {
        super(message);
    }
}
