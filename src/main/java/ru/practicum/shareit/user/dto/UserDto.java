package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Builder
public class UserDto {
    private int id;

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;
}
