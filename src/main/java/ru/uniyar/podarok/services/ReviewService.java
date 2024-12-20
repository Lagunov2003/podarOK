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
import ru.uniyar.podarok.utils.builders.ReviewBuilder;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервис для работы с отзывами о подарках.
 */
@Service
@AllArgsConstructor
public class ReviewService {
    private ReviewRepository reviewRepository;
    private UserService userService;
    private GiftService giftService;

    /**
     * Получает среднюю оценку для подарка по его идентификатору.
     *
     * @param giftId идентификатор подарка
     * @return средняя оценка для подарка с точностью до одного знака после запятой
     */
    public Double getAverageRating(Long giftId) {
        final double roundingScale = 10.0;
        return Math.round(reviewRepository.findAverageRatingByGiftId(giftId) * roundingScale) / roundingScale;
    }

    /**
     * Получает количество отзывов для подарка по его идентификатору.
     *
     * @param giftId идентификатор подарка
     * @return количество отзывов для подарка
     */
    public Long getReviewsAmountByGiftId(Long giftId) {
        return reviewRepository.countReviewsAmountById(giftId);
    }

    /**
     * Получает список отзывов для подарка по его идентификатору.
     *
     * @param giftId идентификатор подарка
     * @return список отзывов для подарка
     */
    public List<Review> getReviewsByGiftId(Long giftId) {
        return reviewRepository.findReviewsByGiftId(giftId);
    }

    /**
     * Добавляет новый отзыв о подарке.
     *
     * @param reviewRequestDto объект, содержащий данные отзыва
     * @throws UserNotFoundException если пользователь не найден
     * @throws UserNotAuthorizedException если пользователь не авторизован
     * @throws GiftNotFoundException если подарок с указанным идентификатором не найден
     */
    @Transactional
    public void addGiftReview(ReviewRequestDto reviewRequestDto)
            throws UserNotFoundException, UserNotAuthorizedException, GiftNotFoundException {
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
