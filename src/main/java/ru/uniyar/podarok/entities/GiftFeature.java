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

/**
 * Сущность "Атрибут подарка".
 */
@Entity
@Data
@Table(name = "gift_feature")
public class GiftFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String itemValue;
    @Column(nullable = false)
    private String itemName;
    @ManyToOne
    @JoinColumn(name = "gift_id", nullable = false)
    @JsonBackReference
    private Gift gift;
}
