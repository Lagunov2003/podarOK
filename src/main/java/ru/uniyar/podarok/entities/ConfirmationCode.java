package ru.uniyar.podarok.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "confirmation_code")
public class ConfirmationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private String code;
    private String password;
    @Column(name = "own_user_id")
    private long ownUserId;
    private LocalDate expiryDate;
}
