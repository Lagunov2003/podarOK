package ru.uniyar.podarok.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @Column(columnDefinition = "text")
    private String text;
    private LocalDate creationDate;
    private Integer rating;
    @ManyToOne
    @JoinColumn(name = "gift_id")
    @JsonBackReference
    private Gift gift;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
