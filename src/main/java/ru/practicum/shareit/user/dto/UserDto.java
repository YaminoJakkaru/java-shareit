package ru.practicum.shareit.user.dto;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.*;

@Data
@Accessors(chain = true)
public class UserDto {
    private int id;

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    public User toUser() {
        return new User()
                .setId(this.getId())
                .setName(this.getName())
                .setEmail(this.getEmail());
    }
}
