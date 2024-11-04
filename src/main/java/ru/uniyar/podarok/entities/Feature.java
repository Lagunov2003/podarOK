package ru.uniyar.podarok.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "feature")
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
