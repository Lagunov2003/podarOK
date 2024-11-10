package ru.uniyar.podarok.testEntities;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "gift_category")
@IdClass(GiftCategoryId.class)
public class GiftCategory {
    @Id
    @Column(name = "category_id")
    private Long categoryId;
    @Id
    @Column(name = "gift_id")
    private Long giftId;
}
