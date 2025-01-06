package ru.uniyar.podarok.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

/**
 * Сущность "Отзыв о подарке".
 */
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
