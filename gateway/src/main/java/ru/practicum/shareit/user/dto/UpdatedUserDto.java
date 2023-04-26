package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;


@Data
@Accessors(chain = true)
public class UpdatedUserDto {

    private String name;

    @Email
    private String email;
}
