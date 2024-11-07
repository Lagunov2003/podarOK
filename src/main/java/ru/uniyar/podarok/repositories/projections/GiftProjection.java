package ru.uniyar.podarok.repositories.projections;

import java.math.BigDecimal;

public interface GiftProjection {
    Long getId();
    String getName();
    BigDecimal getPrice();
    String getPhotoFilePath();
}
