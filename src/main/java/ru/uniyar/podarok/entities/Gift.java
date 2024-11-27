package ru.uniyar.podarok.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(columnDefinition = "text")
    private String description;
    @Column(nullable = false)
    private BigDecimal price;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "gift_category",
            joinColumns = @JoinColumn(name = "gift_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @JsonManagedReference
    private List<Category> categories = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "recommendation_id")
    private GiftRecommendation recommendation;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "gift_occasion",
            joinColumns = @JoinColumn(name = "gift_id"),
            inverseJoinColumns = @JoinColumn(name = "occasion_id")
    )
    private List<Occasion> occasions = new ArrayList<>();
    @OneToMany(mappedBy = "gift", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<GiftFeature> features = new ArrayList<>();
    @OneToMany(mappedBy = "gift", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<GiftPhoto> photos = new ArrayList<>();
}
