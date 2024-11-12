//package ru.uniyar.podarok.utils;
//
//import org.springframework.stereotype.Component;
//import ru.uniyar.podarok.dtos.GiftFilterRequest;
//
//@Component
//public class GiftFilterUtils {
//    public static boolean hasFilters(GiftFilterRequest request) {
//        return (request.getBudget() != null || request.getGender() != null || request.getAge() != null ||
//                (request.getCategories() != null && !request.getCategories().isEmpty()) ||
//                (request.getOccasions() != null && !request.getOccasions().isEmpty()));
//    }
//
//    public static boolean hasSurveyData(GiftFilterRequest request) {
//        return (request.getSurveyBudget() != null || request.getSurveyGender() != null || request.getSurveyAge() != null ||
//                (request.getSurveyCategories() != null && !request.getSurveyCategories().isEmpty()) ||
//                (request.getSurveyOccasions() != null && !request.getSurveyOccasions().isEmpty()));
//    }
//
//    public static GiftFilterRequest filterRequest(GiftFilterRequest request) {
//        if (hasSurveyData(request) && !hasFilters(request)) {
//            return createSurveyRequest(request);
//        } else if (hasFilters(request) && !hasSurveyData(request)) {
//            return createFilterRequest(request);
//        }
//        return mergeWithFilterData(request);
//    }
//
//    private static GiftFilterRequest createSurveyRequest(GiftFilterRequest request) {
//        GiftFilterRequest surveyRequest = new GiftFilterRequest();
//        surveyRequest.setBudget(request.getSurveyBudget());
//        surveyRequest.setGender(request.getSurveyGender());
//        surveyRequest.setAge(request.getSurveyAge());
//        surveyRequest.setCategories(request.getSurveyCategories());
//        surveyRequest.setOccasions(request.getSurveyOccasions());
//        return surveyRequest;
//    }
//
//    private static GiftFilterRequest createFilterRequest(GiftFilterRequest request) {
//        GiftFilterRequest filterRequest = new GiftFilterRequest();
//        filterRequest.setBudget(request.getBudget());
//        filterRequest.setGender(request.getGender());
//        filterRequest.setAge(request.getAge());
//        filterRequest.setCategories(request.getCategories());
//        filterRequest.setOccasions(request.getOccasions());
//        return filterRequest;
//    }
//
//    private static GiftFilterRequest mergeWithFilterData(GiftFilterRequest request) {
//        GiftFilterRequest combinedRequest = new GiftFilterRequest();
//        combinedRequest.setBudget(request.getSurveyBudget() != null ? request.getSurveyBudget() : request.getBudget());
//        combinedRequest.setGender(request.getSurveyGender() != null ? request.getSurveyGender() : request.getGender());
//        combinedRequest.setAge(request.getSurveyAge() != null ? request.getSurveyAge() : request.getAge());
//        combinedRequest.setCategories((request.getSurveyCategories() != null && !request.getSurveyCategories().isEmpty()) ? request.getSurveyCategories() : request.getCategories());
//        combinedRequest.setOccasions((request.getSurveyOccasions() != null && !request.getSurveyOccasions().isEmpty()) ? request.getSurveyOccasions() : request.getOccasions());
//        return combinedRequest;
//    }
//}
