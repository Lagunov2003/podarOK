package ru.uniyar.podarok.repositories.projections;

import ru.uniyar.podarok.entities.GiftPhoto;

import java.math.BigDecimal;
import java.util.List;

public interface GiftProjection {
    Long getId();
    String getName();
    BigDecimal getPrice();
    default String getPhotoUrl() {
        return getPhotos().stream()
                .findFirst()
                .map(GiftPhoto::getPhotoUrl)
                .orElse(null);
    }
    List<GiftPhoto> getPhotos();
}
