package ru.uniyar.podarok.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.dtos.GiftResponseDto;
import ru.uniyar.podarok.dtos.GiftToFavoritesDto;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.GiftGroup;
import ru.uniyar.podarok.entities.User;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatalogServiceTest {

    @Mock
    private GiftService giftService;

    @Mock
    private GiftFilterService filterService;

    @InjectMocks
    private CatalogService catalogService;

    private Pageable pageable;
    @Mock
    private GiftFilterRequest giftFilterRequest;

    @Mock
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
    }


    @Test
    public void CatalogService_GetGiftsCatalog_ShouldReturnGifts_ByFilter() throws Exception {
        GiftFilterRequest giftFilterRequest = new GiftFilterRequest();
        giftFilterRequest.setBudget(BigDecimal.valueOf(50.00));
        Page<GiftDto> mockPage = new PageImpl<>(List.of(mock(GiftDto.class)));
        when(giftService.getGiftsByFilter(giftFilterRequest, pageable)).thenReturn(mockPage);
        when(filterService.processRequest(giftFilterRequest)).thenReturn(giftFilterRequest);
        when(filterService.hasFilters(giftFilterRequest)).thenReturn(true);

        Page<GiftDto> result = catalogService.getGiftsCatalog(giftFilterRequest, pageable);

        assertNotNull(result);
        assertTrue(result.hasContent());
        verify(giftService, times(1)).getGiftsByFilter(giftFilterRequest, pageable);
    }

    @Test
    public void CatalogService_GetGiftsCatalog_ShouldReturnAllGifts() throws Exception {
        Page<GiftDto> mockPage = new PageImpl<>(List.of(mock(GiftDto.class)));
        when(giftService.getAllGifts(pageable)).thenReturn(mockPage);

        Page<GiftDto> result = catalogService.getGiftsCatalog(giftFilterRequest, pageable);

        assertNotNull(result);
        assertTrue(result.hasContent());
        verify(giftService, times(1)).getAllGifts(pageable);
    }

    @Test
    public void CatalogService_GetGift_ReturnsGift() {
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
    void CatalogService_SearchGiftsByName_ReturnsFoundedPageOfGiftDto() {
        String query = "gift";
        Pageable pageable = PageRequest.of(0, 10);
        Page<GiftDto> expectedPage = new PageImpl<>(List.of(new GiftDto(1L, "gift", new BigDecimal("100.00"), "photo")));
        when(giftService.searchGiftsByName(query, pageable)).thenReturn(expectedPage);

        Page<GiftDto> result = catalogService.searchGiftsByName(query, pageable);

        assertEquals(expectedPage, result);
        verify(giftService, times(1)).searchGiftsByName(query, pageable);
    }

    @Test
    void CatalogService_AddGiftToFavorites_VerifiesGiftAdded() throws Exception {
        GiftToFavoritesDto dto = new GiftToFavoritesDto(1L);
        Gift gift = new Gift();
        when(giftService.getGiftById(dto.getGiftId())).thenReturn(gift);

        catalogService.addGiftToFavorites(dto);

        verify(giftService, times(1)).getGiftById(dto.getGiftId());
        verify(userService, times(1)).addGiftToFavorites(gift);
    }

    @Test
    void CatalogService_AddGiftToFavorites_ThrowsEntityNotFoundException() {
        GiftToFavoritesDto dto = new GiftToFavoritesDto(1L);
        when(giftService.getGiftById(dto.getGiftId())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> catalogService.addGiftToFavorites(dto));
        verify(giftService, times(1)).getGiftById(dto.getGiftId());
        verifyNoInteractions(userService);
    }

    @Test
    void CatalogService_GetSimilarGifts_ReturnsListOfGiftDto() throws Exception {
        Long giftId = 1L;
        Gift gift = new Gift();
        List<GiftDto> expectedGifts = List.of(new GiftDto(1L, "gift", new BigDecimal("100.00"), "photo"));
        when(giftService.getGiftById(giftId)).thenReturn(gift);
        when(giftService.getSimilarGifts(gift)).thenReturn(expectedGifts);

        List<GiftDto> result = catalogService.getSimilarGifts(giftId);

        assertEquals(expectedGifts, result);
        verify(giftService, times(1)).getGiftById(giftId);
        verify(giftService, times(1)).getSimilarGifts(gift);
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
    void CatalogService_GetGiftResponse_ReturnsGiftResponseDto() throws Exception {
        Long giftId = 1L;
        Gift gift = new Gift();
        GiftGroup giftGroup = new GiftGroup();
        gift.setGiftGroup(giftGroup);
        gift.setId(giftId);
        List<Gift> groupGifts = List.of(gift);
        List<GiftDto> similarGifts = List.of(new GiftDto(2L, "gift2", new BigDecimal("200.00"), "photo2"));

        when(giftService.getGiftById(giftId)).thenReturn(gift);
        when(giftService.getGiftsByGroupId(giftGroup.getId())).thenReturn(groupGifts);
        when(giftService.getSimilarGifts(gift)).thenReturn(similarGifts);

        GiftResponseDto result = catalogService.getGiftResponse(giftId);

        assertNotNull(result);
        assertEquals(groupGifts.size(), result.getGroupGifts().size());
        assertEquals(similarGifts.size(), result.getSimilarGifts().size());
        verify(giftService, times(2)).getGiftById(giftId);
        verify(giftService, times(1)).getGiftsByGroupId(giftGroup.getId());
        verify(giftService, times(1)).getSimilarGifts(gift);
    }
}

