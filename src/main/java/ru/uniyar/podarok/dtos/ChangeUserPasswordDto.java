package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserPasswordDto {
    private Long id;
    private String email;
    private String password;
    private String confirmPassword;
    private String code;
}
