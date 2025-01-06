package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto для регистрации нового пользователя.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationUserDto {
    private String firstName;
    private String email;
    private String password;
}
