package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private boolean gender;
    private String email;
    private String phoneNumber;

    public void validate() {
        if (firstName == null && lastName == null && email == null && phoneNumber == null && dateOfBirth == null) {
            throw new IllegalArgumentException("Поля не должны быть пустыми!");
        }
        if (email != null && email.isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым!");
        }
    }
}
