package ru.uniyar.podarok.utils.Converters;

import org.springframework.stereotype.Component;
import ru.uniyar.podarok.dtos.SiteReviewsDto;
import ru.uniyar.podarok.entities.SiteReviews;

/**
 * Конвертер для преобразования объекта типа SiteReviews в DTO.
 */
@Component
public class SiteReviewsDtoConverter {
    public SiteReviewsDto convertToSiteReviewsDto(SiteReviews siteReviews) {
        return new SiteReviewsDto(
                siteReviews.getId(),
                siteReviews.getUser().getFirstName(),
                siteReviews.getReview(),
                siteReviews.getMark()
        );
    }
}
