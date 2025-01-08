package ru.uniyar.podarok.testEntities;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "gift_occasion")
@IdClass(GiftOccasionId.class)
public class GiftOccasion {
    @Id
    @Column(name = "occasion_id")
    private Long occasionId;
    @Id
    @Column(name = "gift_id")
    private Long giftId;
}