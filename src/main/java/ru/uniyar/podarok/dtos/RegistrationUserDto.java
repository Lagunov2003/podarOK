package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationUserDto {
    private long id;
    private String firstName;
    private String email;
    private String password;
}
