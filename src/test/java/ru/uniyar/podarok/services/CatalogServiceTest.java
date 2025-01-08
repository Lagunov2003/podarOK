package ru.uniyar.podarok.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.uniyar.podarok.dtos.*;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.GiftGroup;
import ru.uniyar.podarok.entities.Review;
import ru.uniyar.podarok.exceptions.*;
import ru.uniyar.podarok.utils.converters.ReviewDtoConverter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatalogServiceTest {
    @Mock
    private GiftService giftService;
    @Mock
    private GiftFilterService giftFilterService;
    @InjectMocks
    @Spy
    private CatalogService catalogService;
    private Pageable pageable;
    private ReviewRequestDto reviewRequestDto;
    private GiftFavoritesDto giftFavoritesDto;
    private Gift gift;
    @Mock
    private GiftFilterRequest giftFilterRequest;
    @Mock
    private UserService userService;
    @Mock
    private ReviewService reviewService;
    @Mock
    private ReviewDtoConverter reviewDtoConverter;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        reviewRequestDto = new ReviewRequestDto("text", 5, 1L);

        giftFavoritesDto = new GiftFavoritesDto(1L);
        gift = new Gift();
        gift.setId(1L);
    }

    @Test
    public void CatalogService_GetGift_ReturnsGift()
            throws GiftNotFoundException {
        Gift gift = new Gift();
        gift.setName("test");
        gift.setId(1L);
        when(giftService.getGiftById(gift.getId())).thenReturn(gift);

        Gift result = catalogService.getGift(1L);

        verify(giftService, times(1)).getGiftById(gift.getId());
        assertEquals("test", result.getName());
        assertEquals(1L, result.getId());
    }

    @Test
    public void CatalogService_GetGift_ThrowsGiftNotFoundException()
            throws GiftNotFoundException {
        Gift gift = new Gift();
        gift.setName("test");
        gift.setId(1L);
        when(giftService.getGiftById(gift.getId())).thenThrow(
                new GiftNotFoundException("Подарок не найден!"));

        assertThrows(GiftNotFoundException.class, () -> giftService.getGiftById(gift.getId()));
    }

    @Test
    void CatalogService_AddGiftToFavorites_VerifiesGiftAdded()
            throws Exception {
        GiftFavoritesDto dto = new GiftFavoritesDto(1L);
        Gift gift = new Gift();
        when(giftService.getGiftById(dto.getGiftId())).thenReturn(gift);

        catalogService.addGiftToFavorites(dto);

        verify(giftService, times(1)).getGiftById(dto.getGiftId());
        verify(userService, times(1)).addGiftToFavorites(gift);
    }

    @Test
    void CatalogService_AddGiftToFavorites_ThrowsGiftNotFoundException()
            throws GiftNotFoundException {
        GiftFavoritesDto dto = new GiftFavoritesDto(1L);
        when(giftService.getGiftById(dto.getGiftId())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> catalogService.addGiftToFavorites(dto));
        verify(giftService, times(1)).getGiftById(dto.getGiftId());
        verifyNoInteractions(userService);
    }

    @Test
    void CatalogService_AddGiftToFavorites_ThrowsUserNotFoundException()
            throws UserNotFoundException,
            GiftNotFoundException,
            UserNotAuthorizedException,
            FavoritesGiftAlreadyExistException {
        GiftFavoritesDto dto = new GiftFavoritesDto(1L);
        Gift gift = new Gift();
        when(giftService.getGiftById(dto.getGiftId())).thenReturn(new Gift());
        doThrow(new UserNotFoundException("Пользователь не найден!"))
                .when(userService).addGiftToFavorites(gift);

        assertThrows(UserNotFoundException.class, () -> catalogService.addGiftToFavorites(dto));
        verify(giftService, times(1)).getGiftById(dto.getGiftId());
    }

    @Test
    void CatalogService_AddGiftToFavorites_ThrowsUserNotAuthorizedException()
            throws UserNotFoundException,
            UserNotAuthorizedException,
            FavoritesGiftAlreadyExistException,
            GiftNotFoundException {
        GiftFavoritesDto dto = new GiftFavoritesDto(1L);
        Gift gift = new Gift();
        when(giftService.getGiftById(dto.getGiftId())).thenReturn(new Gift());
        doThrow(new UserNotAuthorizedException("Пользователь не авторизован!"))
                .when(userService).addGiftToFavorites(gift);

        assertThrows(UserNotAuthorizedException.class, () -> catalogService.addGiftToFavorites(dto));
        verify(giftService, times(1)).getGiftById(dto.getGiftId());
    }

    @Test
    void CatalogService_AddGiftToFavorites_ThrowsFavoriteGiftAlreadyExistException()
            throws UserNotFoundException,
            GiftNotFoundException,
            UserNotAuthorizedException,
            FavoritesGiftAlreadyExistException {
        GiftFavoritesDto dto = new GiftFavoritesDto(1L);
        Gift gift = new Gift();
        when(giftService.getGiftById(dto.getGiftId())).thenReturn(new Gift());
        doThrow(new FavoritesGiftAlreadyExistException("Подарок уже в избранном!"))
                .when(userService).addGiftToFavorites(gift);

        assertThrows(FavoritesGiftAlreadyExistException.class, () -> catalogService.addGiftToFavorites(dto));
        verify(giftService, times(1)).getGiftById(dto.getGiftId());
    }

    @Test
    void CatalogService_GetSimilarGifts_ReturnsListOfGiftDto()
            throws Exception {
        Long giftId = 1L;
        Gift gift = new Gift();
        List<GiftDto> expectedGifts = List.of(new GiftDto(
                1L,
                "gift",
                new BigDecimal("100.00"),
                "photo",
                true));
        when(giftService.getGiftById(giftId)).thenReturn(gift);
        when(giftService.getSimilarGifts(gift)).thenReturn(expectedGifts);

        List<GiftDto> result = catalogService.getSimilarGifts(giftId);

        assertEquals(expectedGifts, result);
        verify(giftService, times(1)).getGiftById(giftId);
        verify(giftService, times(1)).getSimilarGifts(gift);
    }

    @Test
    void CatalogService_GetSimilarGifts_ThrowsGiftNotFoundException()
            throws GiftNotFoundException {
        Long giftId = 1L;
        when(giftService.getGiftById(giftId)).thenThrow(
                new GiftNotFoundException("Подарок не найден!"));

        assertThrows(GiftNotFoundException.class, () -> catalogService.getSimilarGifts(giftId));
        verify(giftService, times(1)).getGiftById(giftId);
    }

    @Test
    void CatalogService_GetGiftsByGroupId_ReturnsListOfGifts() {
        Long groupId = 1L;
        List<Gift> expectedGifts = List.of(new Gift(), new Gift());
        when(giftService.getGiftsByGroupId(groupId)).thenReturn(expectedGifts);

        List<Gift> result = catalogService.getGiftsByGroupId(groupId);

        assertEquals(expectedGifts, result);
        verify(giftService, times(1)).getGiftsByGroupId(groupId);
    }

    @Test
    void CatalogService_GetGiftResponse_ReturnsGiftResponseDto()
            throws Exception {
        Long giftId = 1L;
        Gift gift = new Gift();
        GiftGroup giftGroup = new GiftGroup();
        gift.setGiftGroup(giftGroup);
        gift.setId(giftId);
        List<Gift> groupGifts = List.of(gift);
        List<GiftDto> similarGifts = List.of(new GiftDto(
                2L,
                "gift2",
                new BigDecimal("200.00"),
                "photo2",
                true));
        when(giftService.getGiftById(giftId)).thenReturn(gift);
        when(giftService.getGiftsByGroupId(giftGroup.getId())).thenReturn(groupGifts);
        when(giftService.getSimilarGifts(gift)).thenReturn(similarGifts);
        when(reviewService.getAverageRating(anyLong())).thenReturn(4.5);
        when(reviewService.getReviewsAmountByGiftId(anyLong())).thenReturn(10L);
        when(reviewService.getReviewsByGiftId(anyLong())).thenReturn(List.of(new Review()));
        when(reviewDtoConverter.convertToReviewDto(any())).thenReturn(new ReviewDto());

        GiftResponseDto result = catalogService.getGiftResponse(giftId);

        assertNotNull(result);
        assertEquals(groupGifts.size(), result.getGroupGifts().size());
        assertEquals(similarGifts.size(), result.getSimilarGifts().size());
        verify(giftService, times(2)).getGiftById(giftId);
        verify(giftService, times(1)).getGiftsByGroupId(giftGroup.getId());
        verify(giftService, times(1)).getSimilarGifts(gift);
    }

    @Test
    void CatalogService_GetGiftResponse_ThrowsGiftNotFoundException() throws Exception {
        Long giftId = 1L;
        when(catalogService.getGift(giftId)).thenThrow(new GiftNotFoundException("Подарок не найден!"));

        assertThrows(GiftNotFoundException.class, () -> catalogService.getGiftResponse(giftId));
    }

    @Test
    void CatalogService_GetGiftsCatalog_ReturnsCatalog_WithFilters() {
        GiftDto giftDto = new GiftDto(
                1L,
                "giftname",
                BigDecimal.valueOf(10),
                "photo",
                false);
        when(giftFilterService.processRequest(giftFilterRequest)).thenReturn(giftFilterRequest);
        when(giftFilterService.hasSurveyData(giftFilterRequest)).thenReturn(true);
        when(giftService.searchGiftsByFilters(giftFilterRequest, "giftName", "sort", pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(giftDto)));

        Page<GiftDto> result = catalogService.getGiftsCatalog(
                giftFilterRequest,
                "giftName",
                "sort",
                pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(giftDto, result.getContent().get(0));
        verify(giftFilterService, times(1)).processRequest(giftFilterRequest);
        verify(giftFilterService, times(1)).hasSurveyData(giftFilterRequest);
        verify(giftService, times(1)).searchGiftsByFilters(
                giftFilterRequest,
                "giftName",
                "sort",
                pageable);
    }

    @Test
    void CatalogService_GetGiftsCatalog_ReturnsCatalog_WithoutFilters() {
        GiftDto giftDto = new GiftDto(
                1L,
                "giftname",
                BigDecimal.valueOf(10),
                "photo",
                false);
        when(giftFilterService.processRequest(giftFilterRequest)).thenReturn(giftFilterRequest);
        when(giftFilterService.hasSurveyData(giftFilterRequest)).thenReturn(false);
        when(giftFilterService.hasFilters(giftFilterRequest)).thenReturn(false);
        when(giftService.getAllGifts(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(giftDto)));

        Page<GiftDto> result = catalogService.getGiftsCatalog(giftFilterRequest, "", "", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(giftDto, result.getContent().get(0));
        verify(giftFilterService, times(1)).processRequest(giftFilterRequest);
        verify(giftFilterService, times(1)).hasSurveyData(giftFilterRequest);
        verify(giftFilterService, times(1)).hasFilters(giftFilterRequest);
        verify(giftService, times(1)).getAllGifts(pageable);
    }

    @Test
    void CatalogService_AddGiftReview_VerifiesReviewAdded()
            throws UserNotFoundException,
            GiftNotFoundException,
            UserNotAuthorizedException,
            GiftReviewAlreadyExistException {
        doNothing().when(reviewService).addGiftReview(reviewRequestDto);

        catalogService.addGiftReview(reviewRequestDto);

        verify(reviewService, times(1)).addGiftReview(reviewRequestDto);
    }

    @Test
    void CatalogService_AddGiftReview_ThrowsUserNotFoundException()
            throws UserNotFoundException,
            GiftNotFoundException,
            UserNotAuthorizedException,
            GiftReviewAlreadyExistException {
        doThrow(new UserNotFoundException("Пользователь не найден!"))
                .when(reviewService).addGiftReview(reviewRequestDto);

        assertThrows(UserNotFoundException.class, () -> {
            catalogService.addGiftReview(reviewRequestDto);
        });
    }

    @Test
    void CatalogService_AddGiftReview_ThrowsGiftNotFoundException()
            throws UserNotFoundException,
            GiftNotFoundException,
            UserNotAuthorizedException,
            GiftReviewAlreadyExistException {
        doThrow(new GiftNotFoundException("Подарок не найден!"))
                .when(reviewService).addGiftReview(reviewRequestDto);

        assertThrows(GiftNotFoundException.class, () -> {
            catalogService.addGiftReview(reviewRequestDto);
        });
    }

    @Test
    void CatalogService_AddGiftReview_ThrowsUserNotAuthorizedException()
            throws UserNotFoundException,
            GiftNotFoundException,
            UserNotAuthorizedException,
            GiftReviewAlreadyExistException {
        doThrow(new UserNotAuthorizedException("Пользователь не авторизован!"))
                .when(reviewService).addGiftReview(reviewRequestDto);

        assertThrows(UserNotAuthorizedException.class, () -> {
            catalogService.addGiftReview(reviewRequestDto);
        });
    }

    @Test
    void CatalogService_AddGiftReview_ThrowsGiftReviewAlreadyExistException()
            throws UserNotFoundException,
            GiftNotFoundException,
            UserNotAuthorizedException,
            GiftReviewAlreadyExistException {
        doThrow(new GiftReviewAlreadyExistException("Отзыв на подарок уже существует!"))
                .when(reviewService).addGiftReview(reviewRequestDto);

        assertThrows(GiftReviewAlreadyExistException.class, () -> {
            catalogService.addGiftReview(reviewRequestDto);
        });
    }

    @Test
    void CatalogService_DeleteFromFavorites_VerifiesFavoritesDeleted()
            throws GiftNotFoundException,
            UserNotFoundException,
            UserNotAuthorizedException,
            FavoritesGiftNotFoundException {
        when(giftService.getGiftById(giftFavoritesDto.getGiftId())).thenReturn(gift);
        doNothing().when(userService).deleteGiftFromFavorites(gift);

        catalogService.deleteFromFavorites(giftFavoritesDto);

        verify(giftService, times(1)).getGiftById(giftFavoritesDto.getGiftId());
        verify(userService, times(1)).deleteGiftFromFavorites(gift);
    }

    @Test
    void CatalogService_DeleteFromFavorites_ThrowsGiftNotFoundException()
            throws GiftNotFoundException {
        when(giftService.getGiftById(giftFavoritesDto.getGiftId())).thenThrow(
                new GiftNotFoundException("Подарок не найден!"));

        assertThrows(GiftNotFoundException.class, () -> {
            catalogService.deleteFromFavorites(giftFavoritesDto);
        });
    }

    @Test
    void CatalogService_DeleteFromFavorites_ThrowsUserNotFoundException()
            throws GiftNotFoundException,
            UserNotFoundException,
            UserNotAuthorizedException,
            FavoritesGiftNotFoundException {
        when(giftService.getGiftById(giftFavoritesDto.getGiftId())).thenReturn(gift);
        doThrow(new UserNotFoundException("Пользователь не найден!"))
                .when(userService).deleteGiftFromFavorites(gift);

        assertThrows(UserNotFoundException.class, () -> {
            catalogService.deleteFromFavorites(giftFavoritesDto);
        });
    }

    @Test
    void CatalogService_DeleteFromFavorites_ThrowsUserNotAuthorizedException()
            throws GiftNotFoundException,
            UserNotFoundException,
            UserNotAuthorizedException,
            FavoritesGiftNotFoundException {
        when(giftService.getGiftById(giftFavoritesDto.getGiftId())).thenReturn(gift);
        doThrow(new UserNotAuthorizedException("Пользователь не авторизован!"))
                .when(userService).deleteGiftFromFavorites(gift);

        assertThrows(UserNotAuthorizedException.class, () -> {
            catalogService.deleteFromFavorites(giftFavoritesDto);
        });
    }

    @Test
    void CatalogService_DeleteFromFavorites_ThrowsFavoritesGiftNotFoundException()
            throws GiftNotFoundException,
            UserNotFoundException,
            UserNotAuthorizedException,
            FavoritesGiftNotFoundException {
        when(giftService.getGiftById(giftFavoritesDto.getGiftId())).thenReturn(gift);
        doThrow(new FavoritesGiftNotFoundException("Подарок не найден в избранных!"))
                .when(userService).deleteGiftFromFavorites(gift);

        assertThrows(FavoritesGiftNotFoundException.class, () -> {
            catalogService.deleteFromFavorites(giftFavoritesDto);
        });
    }

}

