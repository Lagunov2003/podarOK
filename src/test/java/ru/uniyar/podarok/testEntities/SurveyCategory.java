package ru.uniyar.podarok.testEntities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "survey_category")
@IdClass(SurveyCategoryId.class)
public class SurveyCategory {
    @Id
    @Column(name = "category_id")
    private Long categoryId;
    @Id
    @Column(name = "survey_id")
    private Long surveyId;
}
