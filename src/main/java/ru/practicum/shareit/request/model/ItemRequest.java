package ru.practicum.shareit.request.model;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;
    @ManyToOne
    @JoinColumn(name = "requestor_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User requestor;

    @Column(name = "created_date")
    private LocalDateTime created;

    public ItemRequestDto toItemRequestDto(){
        return new ItemRequestDto()
                .setId(this.getId())
                .setDescription(this.getDescription())
                .setCreated(this.getCreated());
    }
}
