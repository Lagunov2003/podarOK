package ru.uniyar.podarok.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Сущность "Подарки в заказе".
 */
@Entity
@Data
@Table(name = "gift_order")
public class GiftOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer itemCount;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private Order order;
    @ManyToOne()
    @JoinColumn(name = "gift_id", nullable = false)
    @JsonBackReference
    private Gift gift;
}
