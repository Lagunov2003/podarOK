package ru.uniyar.podarok.services;

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
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.repositories.projections.GiftProjection;

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
    private User user;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
    }


    @Test
    public void CatalogService_GetGiftsCatalog_ShouldReturnGifts_ByFilter() throws Exception {
        GiftFilterRequest giftFilterRequest = new GiftFilterRequest();
        giftFilterRequest.setBudget(BigDecimal.valueOf(50.00));
        Page<GiftProjection> mockPage = new PageImpl<>(List.of(mock(GiftProjection.class)));
        when(giftService.getGiftsByFilter(giftFilterRequest, pageable)).thenReturn(mockPage);
        when(filterService.processRequest(giftFilterRequest)).thenReturn(giftFilterRequest);
        when(filterService.hasFilters(giftFilterRequest)).thenReturn(true);

        Page<GiftProjection> result = catalogService.getGiftsCatalog(giftFilterRequest, pageable);

        assertNotNull(result);
        assertTrue(result.hasContent());
        verify(giftService, times(1)).getGiftsByFilter(giftFilterRequest, pageable);
    }

    @Test
    public void CatalogService_GetGiftsCatalog_ShouldReturnAllGifts() throws Exception {
        Page<GiftProjection> mockPage = new PageImpl<>(List.of(mock(GiftProjection.class)));
        when(giftService.getAllGifts(pageable)).thenReturn(mockPage);

        Page<GiftProjection> result = catalogService.getGiftsCatalog(giftFilterRequest, pageable);

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
}

