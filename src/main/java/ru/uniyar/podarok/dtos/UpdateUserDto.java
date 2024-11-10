package ru.uniyar.podarok.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    @NotBlank(message = "Имя не может быть пустым или null!")
    private String firstName;

    @NotBlank(message = "Фамилия не может быть пустой или null!")
    private String lastName;

    @NotNull(message = "Дата рождения не может быть null!")
    private LocalDate dateOfBirth;

    @NotNull(message = "Пол не может быть null!")
    private Boolean gender;

    @NotBlank(message = "Email не может быть пустым или null!")
    @Email(message = "Некорректный формат email!")
    private String email;

    @NotBlank(message = "Номер телефона не может быть пустым или null!")
    private String phoneNumber;
}
