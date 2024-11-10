package ru.uniyar.podarok.testEntities;

import java.io.Serializable;
import java.util.Objects;

public class GiftCategoryId implements Serializable {
    private Long categoryId;
    private Long giftId;

    public GiftCategoryId() {}

    public GiftCategoryId(Long categoryId, Long giftId) {
        this.categoryId = categoryId;
        this.giftId = giftId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCategoryId that = (GiftCategoryId) o;
        return Objects.equals(categoryId, that.categoryId) && Objects.equals(giftId, that.giftId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, giftId);
    }
}