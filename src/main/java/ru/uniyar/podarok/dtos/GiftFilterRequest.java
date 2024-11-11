package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

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

    public boolean hasFilters() {
        return (budget != null || gender != null || age != null ||
                (categories != null && !categories.isEmpty()) || (occasions != null && !occasions.isEmpty()));
    }

    public boolean hasSurveyData() {
        return (surveyBudget != null || surveyGender != null || surveyAge != null ||
                (surveyCategories != null && !surveyCategories.isEmpty()) || (surveyOccasions != null && !surveyOccasions.isEmpty()));
    }

    public GiftFilterRequest filterRequest() {
        if (hasSurveyData() && !hasFilters()) {
            return createSurveyRequest();
        } else if (hasFilters() && !hasSurveyData()) {
            return createFilterRequest();
        }
        return mergeWithFilterData();
    }

    private GiftFilterRequest createSurveyRequest() {
        GiftFilterRequest surveyRequest = new GiftFilterRequest();
        surveyRequest.budget = surveyBudget;
        surveyRequest.gender = surveyGender;
        surveyRequest.age = surveyAge;
        surveyRequest.categories = surveyCategories;
        surveyRequest.occasions = surveyOccasions;
        return surveyRequest;
    }

    private GiftFilterRequest createFilterRequest() {
        GiftFilterRequest filterRequest = new GiftFilterRequest();
        filterRequest.budget = budget;
        filterRequest.gender = gender;
        filterRequest.age = age;
        filterRequest.categories = categories;
        filterRequest.occasions = occasions;
        return filterRequest;
    }

    private GiftFilterRequest mergeWithFilterData() {
        GiftFilterRequest combinedRequest = new GiftFilterRequest();
        combinedRequest.budget = (surveyBudget != null) ? surveyBudget : budget;
        combinedRequest.gender = (surveyGender != null) ? surveyGender : gender;
        combinedRequest.age = (surveyAge != null) ? surveyAge : age;
        combinedRequest.categories = (surveyCategories != null && !surveyCategories.isEmpty()) ? surveyCategories : categories;
        combinedRequest.occasions = (surveyOccasions != null && !surveyOccasions.isEmpty()) ? surveyOccasions : occasions;
        return combinedRequest;
    }
}
