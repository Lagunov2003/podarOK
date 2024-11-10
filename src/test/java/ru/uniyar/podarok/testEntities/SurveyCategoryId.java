package ru.uniyar.podarok.testEntities;

import java.io.Serializable;
import java.util.Objects;

public class SurveyCategoryId implements Serializable {
    private Long categoryId;
    private Long surveyId;

    public SurveyCategoryId() {}

    public SurveyCategoryId(Long categoryId, Long giftId) {
        this.categoryId = categoryId;
        this.surveyId = giftId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyCategoryId that = (SurveyCategoryId) o;
        return Objects.equals(categoryId, that.categoryId) && Objects.equals(surveyId, that.surveyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, surveyId);
    }
}
