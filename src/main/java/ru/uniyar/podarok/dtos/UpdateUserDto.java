package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserDto {
    private String firstName;
    private String lastName;
    private String password;
}
