package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.dtos.*;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.GiftGroup;
import ru.uniyar.podarok.exceptions.GiftNotFoundException;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.utils.Converters.ReviewDtoConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CatalogService {
    private GiftService giftService;
    private UserService userService;
    private ReviewService reviewService;
    private ReviewDtoConverter reviewDtoConverter;

    public Gift getGift(Long giftId) throws GiftNotFoundException {
        return giftService.getGiftById(giftId);
    }

    public void addGiftToFavorites(GiftToFavoritesDto giftToFavoritesDto) throws UserNotFoundException, UserNotAuthorizedException, GiftNotFoundException {
        Gift gift = giftService.getGiftById(giftToFavoritesDto.getGiftId());
        userService.addGiftToFavorites(gift);
    }

    public List<GiftDto> getSimilarGifts(Long giftId) throws GiftNotFoundException {
        Gift gift = giftService.getGiftById(giftId);
        return giftService.getSimilarGifts(gift);
    }

    public List<Gift> getGiftsByGroupId(Long groupId) {
        return giftService.getGiftsByGroupId(groupId);
    }

    public GiftResponseDto getGiftResponse(Long giftId) throws GiftNotFoundException {
        Gift gift = getGift(giftId);
        GiftGroup giftGroup = gift.getGiftGroup();
        List<Gift> groupGifts = new ArrayList<>(List.of(gift));
        if (giftGroup != null) {
            groupGifts.addAll(getGiftsByGroupId(giftGroup.getId()).stream()
                    .filter(g -> !g.getId().equals(giftId))
                    .toList());
        }
        Double averageRating = reviewService.getAverageRating(giftId);
        Long reviewsAmount = reviewService.getReviewsAmountByGiftId(giftId);
        List<ReviewDto> reviews = reviewService.getReviewsByGiftId(giftId).stream()
                .map(reviewDtoConverter::convertToReviewDto)
                .collect(Collectors.toList());
        return new GiftResponseDto(groupGifts, getSimilarGifts(giftId), reviewsAmount, averageRating, reviews);
    }

    public Page<GiftDto> searchGiftsByFilters(GiftFilterRequest giftFilterRequest, String name, String sort, Pageable pageable) {
        return giftService.searchGiftsByFilters(giftFilterRequest, name, sort, pageable);
    }

    public void addGiftReview(ReviewRequestDto reviewRequestDto) throws UserNotFoundException, GiftNotFoundException, UserNotAuthorizedException {
        reviewService.addGiftReview(reviewRequestDto);
    }
}