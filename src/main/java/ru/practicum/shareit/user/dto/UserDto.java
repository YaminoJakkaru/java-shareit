package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

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

    public  User toUser() {
        return User.builder()
                .id(this.getId())
                .name(this.getName())
                .email(this.getEmail())
                .build();
    }
}
