package ru.practicum.shareit.user.dto;

import lombok.Data;

import lombok.experimental.Accessors;

import javax.validation.constraints.Email;

import javax.validation.constraints.NotNull;


@Data
@Accessors(chain = true)
public class CreatedUserDto {

	@NotNull
	private String name;

	@NotNull
	@Email
	private String email;
}
