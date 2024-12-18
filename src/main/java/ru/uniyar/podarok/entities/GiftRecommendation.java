package ru.uniyar.podarok.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Сущность "Рекомендации подарка".
 */
@Entity
@Data
@Table(name = "gift_recommendations")
public class GiftRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean gender;
    private Integer minAge;
    private Integer maxAge;
}
