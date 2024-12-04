package ru.uniyar.podarok.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Set<Category> categories = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "recommendation_id")
    private GiftRecommendation recommendation;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "gift_occasion",
            joinColumns = @JoinColumn(name = "gift_id"),
            inverseJoinColumns = @JoinColumn(name = "occasion_id")
    )
    private Set<Occasion> occasions = new HashSet<>();
    @OneToMany(mappedBy = "gift", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<GiftFeature> features = new ArrayList<>();
    @OneToMany(mappedBy = "gift", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<GiftPhoto> photos = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonManagedReference
    private GiftGroup giftGroup;
    @OneToMany(mappedBy = "gift", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<GiftOrder> giftOrders;
}
