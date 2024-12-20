package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.dtos.GiftResponseDto;
import ru.uniyar.podarok.dtos.GiftToFavoritesDto;
import ru.uniyar.podarok.dtos.ReviewDto;
import ru.uniyar.podarok.dtos.ReviewRequestDto;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.GiftGroup;
import ru.uniyar.podarok.exceptions.GiftNotFoundException;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.utils.converters.ReviewDtoConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления каталогом подарков.
 */
@Service
@AllArgsConstructor
public class CatalogService {
    private GiftService giftService;
    private UserService userService;
    private ReviewService reviewService;
    private ReviewDtoConverter reviewDtoConverter;
    private GiftFilterService giftFilterService;

    /**
     * Получает информацию о подарке по его идентификатору.
     *
     * @param giftId идентификатор подарка
     * @return объект {@link Gift}, представляющий подарок
     * @throws GiftNotFoundException если подарок не найден
     */
    public Gift getGift(Long giftId) throws GiftNotFoundException {
        return giftService.getGiftById(giftId);
    }

    /**
     * Добавляет подарок в избранное текущего авторизованного пользователя.
     *
     * @param giftToFavoritesDto DTO с информацией о подарке
     * @throws UserNotFoundException если пользователь не найден
     * @throws UserNotAuthorizedException если пользователь не авторизован
     * @throws GiftNotFoundException если подарок не найден
     */
    public void addGiftToFavorites(GiftToFavoritesDto giftToFavoritesDto)
            throws UserNotFoundException, UserNotAuthorizedException, GiftNotFoundException {
        Gift gift = giftService.getGiftById(giftToFavoritesDto.getGiftId());
        userService.addGiftToFavorites(gift);
    }

    /**
     * Получает список похожих подарков для указанного подарка.
     *
     * @param giftId идентификатор подарка
     * @return список {@link GiftDto}, представляющий похожие подарки
     * @throws GiftNotFoundException если подарок не найден
     */
    public List<GiftDto> getSimilarGifts(Long giftId) throws GiftNotFoundException {
        Gift gift = giftService.getGiftById(giftId);
        return giftService.getSimilarGifts(gift);
    }

    /**
     * Получает список подарков по идентификатору группы.
     *
     * @param groupId идентификатор группы
     * @return список {@link Gift}, представляющий подарки в группе
     */
    public List<Gift> getGiftsByGroupId(Long groupId) {
        return giftService.getGiftsByGroupId(groupId);
    }

    /**
     * Получает подробную информацию о подарке, включая отзывы и схожие подарки.
     *
     * @param giftId идентификатор подарка
     * @return объект {@link GiftResponseDto}, содержащий данные о подарке
     * @throws GiftNotFoundException если подарок не найден
     */
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

    /**
     * Выполняет поиск подарков по указанным фильтрам.
     *
     * @param giftFilterRequest объект фильтрации подарков
     * @param name имя подарка (поиск по названию)
     * @param sort тип сортировки
     * @param pageable параметры пагинации
     * @return страница {@link GiftDto}, содержащая результаты поиска
     */
    public Page<GiftDto> getGiftsCatalog(GiftFilterRequest giftFilterRequest,
                                         String name,
                                         String sort,
                                         Pageable pageable) {
        GiftFilterRequest effectiveRequest = giftFilterService.processRequest(giftFilterRequest);
        return giftFilterService.hasSurveyData(effectiveRequest) || giftFilterService.hasFilters(effectiveRequest)
                ? giftService.searchGiftsByFilters(effectiveRequest, name, sort, pageable)
                : giftService.getAllGifts(pageable);
    }

    /**
     * Добавляет отзыв к подарку.
     *
     * @param reviewRequestDto объект запроса с данными отзыва
     * @throws UserNotFoundException если пользователь не найден
     * @throws GiftNotFoundException если подарок не найден
     * @throws UserNotAuthorizedException если пользователь не авторизован
     */
    public void addGiftReview(ReviewRequestDto reviewRequestDto)
            throws UserNotFoundException, GiftNotFoundException, UserNotAuthorizedException {
        reviewService.addGiftReview(reviewRequestDto);
    }
}
