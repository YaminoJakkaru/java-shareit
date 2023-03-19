package ru.practicum.shareit.user.model;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;
import javax.validation.constraints.Email;

@Data
@Builder
public class User {
    private int id;

    private String name;

    @Email
    private String email;

    public  UserDto toUserDto() {
        return UserDto.builder()
                .id(this.getId())
                .name(this.getName())
                .email(this.getEmail())
                .build();
    }

}
