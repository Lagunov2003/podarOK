package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Dto для фильтрации каталога.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftFilterRequest {
    private List<Long> categories;
    private List<Long> occasions;
    private Boolean gender;
    private BigDecimal budget;
    private Integer age;
    private List<Long> surveyCategories;
    private List<Long> surveyOccasions;
    private Boolean surveyGender;
    private BigDecimal surveyBudget;
    private Integer surveyAge;
}
