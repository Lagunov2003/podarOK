package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto для изменения пароля пользователя.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserPasswordDto {
    private String password;
    private String confirmPassword;
}
