package ru.uniyar.podarok.utils.Converters;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.uniyar.podarok.dtos.ReviewDto;
import ru.uniyar.podarok.entities.Review;

/**
 * Конвертер для преобразования объекта типа Review в DTO.
 */
@Component
@AllArgsConstructor
public class ReviewDtoConverter {
    public ReviewDto convertToReviewDto(Review review) {
        return new ReviewDto(
                review.getText(),
                review.getRating(),
                review.getUser().getFirstName()
        );
    }
}
