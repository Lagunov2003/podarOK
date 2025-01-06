package ru.uniyar.podarok.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Dto для изменения данных пользователя сайта.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    @NotBlank(message = "Имя не может быть пустым или null!")
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Boolean gender;
    @NotBlank(message = "Email не может быть пустым или null!")
    @Email(message = "Некорректный формат email!")
    private String email;
    private String phoneNumber;
}
