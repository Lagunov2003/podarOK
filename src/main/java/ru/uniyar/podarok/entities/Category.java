package ru.uniyar.podarok.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Feature> features = new ArrayList<>();
    @ManyToMany(mappedBy = "categories")
    private List<Gift> gifts = new ArrayList<>();
}
