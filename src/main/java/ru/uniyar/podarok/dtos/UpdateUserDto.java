package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UpdateUserDto {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private boolean gender;
    private String email;
    private String phoneNumber;
}
