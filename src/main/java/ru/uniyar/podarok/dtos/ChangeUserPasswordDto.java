package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeUserPasswordDto {
    private Long id;
    private String email;
    private String password;
    private String confirmPassword;
    private String code;
}
