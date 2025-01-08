package ru.uniyar.podarok.testEntities;

import java.io.Serializable;
import java.util.Objects;

public class GiftOccasionId implements Serializable {
    private Long occasionId;
    private Long giftId;

    public GiftOccasionId() {}

    public GiftOccasionId(Long occasionId, Long giftId) {
        this.occasionId = occasionId;
        this.giftId = giftId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftOccasionId that = (GiftOccasionId) o;
        return Objects.equals(occasionId, that.occasionId) && Objects.equals(giftId, that.giftId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(occasionId, giftId);
    }
}
