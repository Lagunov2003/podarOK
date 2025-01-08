package ru.uniyar.podarok.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Сущность "Фотографии подарка".
 */
@Entity
@Data
@Table(name = "gift_photo")
public class GiftPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String photoUrl;
    @ManyToOne
    @JoinColumn(name = "gift_id", nullable = false)
    @JsonBackReference
    private Gift gift;
}
