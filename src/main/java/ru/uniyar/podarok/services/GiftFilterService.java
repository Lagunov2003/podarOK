package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.dtos.GiftFilterRequest;

@Service
@AllArgsConstructor
public class GiftFilterService {
    public boolean hasFilters(GiftFilterRequest request) {
        return (request.getBudget() != null || request.getGender() != null || request.getAge() != null ||
                (request.getCategories() != null && !request.getCategories().isEmpty()) ||
                (request.getOccasions() != null && !request.getOccasions().isEmpty()));
    }

    public boolean hasSurveyData(GiftFilterRequest request) {
        return (request.getSurveyBudget() != null || request.getSurveyGender() != null || request.getSurveyAge() != null ||
                (request.getSurveyCategories() != null && !request.getSurveyCategories().isEmpty()) ||
                (request.getSurveyOccasions() != null && !request.getSurveyOccasions().isEmpty()));
    }

    public GiftFilterRequest processRequest(GiftFilterRequest request) {
        if (hasSurveyData(request) && !hasFilters(request)) {
            return createSurveyRequest(request);
        } else if (hasFilters(request) && !hasSurveyData(request)) {
            return createFilterRequest(request);
        }
        return mergeWithFilterData(request);
    }

    private GiftFilterRequest createSurveyRequest(GiftFilterRequest request) {
        return new GiftFilterRequest(
                request.getSurveyCategories(),
                request.getSurveyOccasions(),
                request.getSurveyGender(),
                request.getSurveyBudget(),
                request.getSurveyAge(),
                null,
                null,
                null,
                null,
                null
        );
    }

    private GiftFilterRequest createFilterRequest(GiftFilterRequest request) {
        return new GiftFilterRequest(
                request.getCategories(),
                request.getOccasions(),
                request.getGender(),
                request.getBudget(),
                request.getAge(),
                null,
                null,
                null,
                null,
                null
        );
    }

    private GiftFilterRequest mergeWithFilterData(GiftFilterRequest request) {
        return new GiftFilterRequest(
                (request.getSurveyCategories() != null && !request.getSurveyCategories().isEmpty()) ? request.getSurveyCategories() : request.getCategories(),
                (request.getSurveyOccasions() != null && !request.getSurveyOccasions().isEmpty()) ? request.getSurveyOccasions() : request.getOccasions(),
                request.getSurveyGender() != null ? request.getSurveyGender() : request.getGender(),
                request.getSurveyBudget() != null ? request.getSurveyBudget() : request.getBudget(),
                request.getSurveyAge() != null ? request.getSurveyAge() : request.getAge(),
                null,
                null,
                null,
                null,
                null
        );
    }
}
