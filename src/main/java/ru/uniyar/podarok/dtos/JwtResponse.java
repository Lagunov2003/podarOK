package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto для вывода токена после авторизации.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
}
