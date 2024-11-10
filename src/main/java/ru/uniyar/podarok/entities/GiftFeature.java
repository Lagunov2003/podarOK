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
    @Column(nullable = false)
    private String itemName;
    @ManyToOne
    @JoinColumn(name = "gift_id", nullable = false)
    private Gift gift;
}
