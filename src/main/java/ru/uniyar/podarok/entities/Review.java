package ru.uniyar.podarok.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private LocalDate creationDate;
    private Integer rating;
    @ManyToOne
    @JoinColumn(name = "gift_id")
    private Gift gift;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
