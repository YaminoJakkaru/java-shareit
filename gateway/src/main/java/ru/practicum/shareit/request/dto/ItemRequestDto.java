package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class ItemRequestDto {
    @NotBlank
    private String description;
}
