package ru.uniyar.podarok.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "gift")
public class Gift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private BigDecimal price;
    private String photoFilePath;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "gift_category",
            joinColumns = @JoinColumn(name = "gift_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "recommendation_id")
    private GiftRecommendation recommendation;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "gift_ocassion",
            joinColumns = @JoinColumn(name = "gift_id"),
            inverseJoinColumns = @JoinColumn(name = "ocassion_id")
    )
    private List<Ocassion> ocassions = new ArrayList<>();
    @OneToMany(mappedBy = "gift", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GiftFeature> features = new ArrayList<>();
}
