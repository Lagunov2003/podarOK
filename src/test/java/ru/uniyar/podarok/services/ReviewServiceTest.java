package ru.uniyar.podarok.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.uniyar.podarok.dtos.ReviewRequestDto;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.Review;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.GiftNotFoundException;
import ru.uniyar.podarok.exceptions.GiftReviewAlreadyExistException;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.repositories.ReviewRepository;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserService userService;

    @Mock
    private GiftService giftService;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    public void ReviewService_GetAverageRating_ReturnsRating() {
        Long giftId = 1L;
        Double averageRating = 4.567;
        when(reviewRepository.findAverageRatingByGiftId(giftId)).thenReturn(averageRating);

        Double result = reviewService.getAverageRating(giftId);

        assertEquals(4.6, result);
    }

    @Test
    public void ReviewService_GetAverageRating_ReturnsNull() {
        Long giftId = 1L;
        when(reviewRepository.findAverageRatingByGiftId(giftId)).thenReturn(null);

        Double result = reviewService.getAverageRating(giftId);

        assertEquals(0.0, result);
    }

    @Test
    public void ReviewService_GetReviewsAmountByGiftId_ReturnsAmount() {
        Long giftId = 1L;
        Long reviewsAmount = 5L;
        when(reviewRepository.countReviewsAmountById(giftId)).thenReturn(reviewsAmount);

        Long result = reviewService.getReviewsAmountByGiftId(giftId);

        assertEquals(reviewsAmount, result);
    }

    @Test
    public void ReviewService_GetReviewsByGiftId_ReturnsReviewList() {
        Long giftId = 1L;
        List<Review> reviews = new ArrayList<>();
        when(reviewRepository.findReviewsByGiftId(giftId)).thenReturn(reviews);

        List<Review> result = reviewService.getReviewsByGiftId(giftId);

        assertEquals(reviews, result);
    }

    @Test
    public void ReviewService_AddGiftReview_VerifiesReviewSaved() throws Exception {
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setGiftId(1L);
        reviewRequestDto.setText("Great gift!");
        reviewRequestDto.setRating(5);
        User user = new User();
        user.setId(1L);
        when(userService.getCurrentAuthenticationUser()).thenReturn(user);
        Gift gift = new Gift();
        gift.setId(1L);
        when(giftService.getGiftById(reviewRequestDto.getGiftId())).thenReturn(gift);
        List<Review> reviews = new ArrayList<>();
        when(reviewRepository.findReviewsByGiftId(gift.getId())).thenReturn(reviews);

        reviewService.addGiftReview(reviewRequestDto);

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    public void ReviewService_AddGiftReview_ThrowsGiftReviewAlreadyExistException()
            throws UserNotFoundException, UserNotAuthorizedException, GiftNotFoundException {
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setGiftId(1L);
        reviewRequestDto.setText("Great gift!");
        reviewRequestDto.setRating(5);
        User user = new User();
        user.setId(1L);
        when(userService.getCurrentAuthenticationUser()).thenReturn(user);
        Gift gift = new Gift();
        gift.setId(1L);
        when(giftService.getGiftById(reviewRequestDto.getGiftId())).thenReturn(gift);
        Review existingReview = new Review();
        existingReview.setUser(user);
        List<Review> reviews = new ArrayList<>();
        reviews.add(existingReview);
        when(reviewRepository.findReviewsByGiftId(gift.getId())).thenReturn(reviews);

        assertThrows(GiftReviewAlreadyExistException.class, () -> {
            reviewService.addGiftReview(reviewRequestDto);
        });
    }

    @Test
    public void ReviewService_AddGiftReview_ThrowsUserNotFoundException()
            throws UserNotFoundException, UserNotAuthorizedException {
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setGiftId(1L);
        reviewRequestDto.setText("Great gift!");
        reviewRequestDto.setRating(5);
        User user = new User();
        user.setId(1L);
        when(userService.getCurrentAuthenticationUser()).thenThrow(
                new UserNotFoundException("Пользователь не найден!"));

        assertThrows(UserNotFoundException.class, () -> {
            reviewService.addGiftReview(reviewRequestDto);
        });
    }

    @Test
    public void ReviewService_AddGiftReview_ThrowsUserNotAuthorizedException()
            throws UserNotFoundException, UserNotAuthorizedException {
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setGiftId(1L);
        reviewRequestDto.setText("Great gift!");
        reviewRequestDto.setRating(5);
        User user = new User();
        user.setId(1L);
        when(userService.getCurrentAuthenticationUser()).thenThrow(
                new UserNotAuthorizedException("Пользователь не авторизован!"));

        assertThrows(UserNotAuthorizedException.class, () -> {
            reviewService.addGiftReview(reviewRequestDto);
        });
    }

    @Test
    public void ReviewService_AddGiftReview_ThrowsGiftNotFoundException()
            throws UserNotFoundException, UserNotAuthorizedException, GiftNotFoundException {
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setGiftId(1L);
        reviewRequestDto.setText("Great gift!");
        reviewRequestDto.setRating(5);
        User user = new User();
        user.setId(1L);
        when(userService.getCurrentAuthenticationUser()).thenReturn(user);
        Gift gift = new Gift();
        gift.setId(1L);
        when(giftService.getGiftById(reviewRequestDto.getGiftId())).thenThrow(
                new GiftNotFoundException("Подарок не найден!"));

        assertThrows(GiftNotFoundException.class, () -> {
            reviewService.addGiftReview(reviewRequestDto);
        });
    }
}

