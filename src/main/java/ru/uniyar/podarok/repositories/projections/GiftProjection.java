package ru.uniyar.podarok.repositories.projections;

import ru.uniyar.podarok.entities.GiftPhoto;

import java.math.BigDecimal;
import java.util.List;

public interface GiftProjection {
    Long getId();
    String getName();
    BigDecimal getPrice();
    default String getPhotoUrl() {
        List<GiftPhoto> photos = getPhotos();
        return (photos != null && !photos.isEmpty()) ? photos.get(0).getPhotoUrl() : null;
    }
    List<GiftPhoto> getPhotos();
}
