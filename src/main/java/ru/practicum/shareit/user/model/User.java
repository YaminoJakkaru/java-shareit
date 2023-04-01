package ru.practicum.shareit.user.model;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.practicum.shareit.user.dto.UserDto;
import javax.persistence.*;
import javax.validation.constraints.Email;


@Data
@Accessors(chain = true)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    public UserDto toUserDto() {
        return new UserDto()
                .setId(this.getId())
                .setName(this.getName())
                .setEmail(this.getEmail());
    }

}
