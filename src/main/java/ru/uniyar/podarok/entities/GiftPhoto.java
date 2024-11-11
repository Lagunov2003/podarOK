package ru.uniyar.podarok.entities;

import jakarta.persistence.*;
import lombok.Data;

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
    private Gift gift;
}
