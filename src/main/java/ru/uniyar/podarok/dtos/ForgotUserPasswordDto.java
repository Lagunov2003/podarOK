package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto для восстановления забытого пароля.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotUserPasswordDto {
    private String email;
}
