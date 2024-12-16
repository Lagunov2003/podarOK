package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.uniyar.podarok.dtos.ReviewRequestDto;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.Review;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.GiftNotFoundException;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.repositories.ReviewRepository;
import ru.uniyar.podarok.utils.Builders.ReviewBuilder;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {
    private ReviewRepository reviewRepository;
    private UserService userService;
    private GiftService giftService;

    public Double getAverageRating(Long giftId) {
        return Math.round(reviewRepository.findAverageRatingByGiftId(giftId) * 10.0) / 10.0;
    }

    public Long getReviewsAmountByGiftId(Long giftId) {
        return reviewRepository.countReviewsAmountById(giftId);
    }

    public List<Review> getReviewsByGiftId(Long giftId) {
        return reviewRepository.findReviewsByGiftId(giftId);
    }

    @Transactional
    public void addGiftReview(ReviewRequestDto reviewRequestDto) throws UserNotFoundException, UserNotAuthorizedException, GiftNotFoundException {
        User user = userService.getCurrentAuthenticationUser();
        Gift gift = giftService.getGiftById(reviewRequestDto.getGiftId());

        Review review = new ReviewBuilder()
                .setText(reviewRequestDto.getText())
                .setCreationDate(LocalDate.now())
                .setRating(reviewRequestDto.getRating())
                .setGift(gift)
                .setUser(user)
                .build();

        reviewRepository.save(review);
    }
}