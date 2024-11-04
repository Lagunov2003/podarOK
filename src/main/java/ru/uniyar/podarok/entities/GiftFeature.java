package ru.uniyar.podarok.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "gift_feature")
public class GiftFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String itemValue;
    @ManyToOne
    @JoinColumn(name = "gift_id", nullable = false)
    private Gift gift;
    @ManyToOne
    @JoinColumn(name = "feature_id", nullable = false)
    private Feature feature;
}
