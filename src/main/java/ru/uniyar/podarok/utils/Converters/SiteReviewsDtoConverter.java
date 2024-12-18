package ru.uniyar.podarok.utils.Converters;

import org.springframework.stereotype.Component;
import ru.uniyar.podarok.dtos.SiteReviewsDto;
import ru.uniyar.podarok.entities.SiteReviews;

/**
 * Конвертер для преобразования объекта типа SiteReviews в DTO.
 */
@Component
public class SiteReviewsDtoConverter {
    /**
     * Преобразует объект SiteReviews в объект SiteReviewsDto.
     *
     * @param siteReviews объект SiteReviews, который необходимо преобразовать.
     * @return объект SiteReviewsDto, представляющий отзыв о сайте в виде DTO.
     */
    public SiteReviewsDto convertToSiteReviewsDto(SiteReviews siteReviews) {
        return new SiteReviewsDto(
                siteReviews.getId(),
                siteReviews.getUser().getFirstName(),
                siteReviews.getReview(),
                siteReviews.getMark()
        );
    }
}
