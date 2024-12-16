package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.entities.Review;
import ru.uniyar.podarok.repositories.ReviewRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {
    private ReviewRepository reviewRepository;

    public Double getAverageRating(Long giftId) {
        return Math.round(reviewRepository.findAverageRatingByGiftId(giftId) * 10.0) / 10.0;
    }

    public Long getReviewsAmountByGiftId(Long giftId) {
        return reviewRepository.countReviewsAmountById(giftId);
    }

    public List<Review> getReviewsByGiftId(Long giftId) {
        return reviewRepository.findReviewsByGiftId(giftId);
    }
}
