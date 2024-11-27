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
import ru.uniyar.podarok.entities.Occasion;
import ru.uniyar.podarok.repositories.GiftRepository;
import ru.uniyar.podarok.repositories.projections.GiftProjection;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GiftServiceTest {

    @Mock
    private GiftRepository giftRepository;
    @Mock
    private GiftFilterService giftFilterService;
    @InjectMocks
    private GiftService giftService;

    private Pageable pageable;

    private GiftFilterRequest filterRequest;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        Occasion occasion = new Occasion();
        occasion.setId(1L);
        occasion.setName("Birthday");

        filterRequest = new GiftFilterRequest();
        filterRequest.setBudget(new BigDecimal("200.00"));
        filterRequest.setGender(true);
        filterRequest.setAge(30);
        filterRequest.setCategories(List.of(1L, 2L));
        filterRequest.setOccasions(List.of(1L, 3L));
    }

    private GiftProjection createMockGiftProjection(Long id, String name, BigDecimal price) {
        return mock(GiftProjection.class);
    }

    @Test
    public void GiftService_GetAllGifts_ReturnsGiftProjection() {
        GiftProjection giftProjection = createMockGiftProjection(1L, "Test Gift", new BigDecimal("50.00"));
        Page<GiftProjection> expectedPage = new PageImpl<>(List.of(giftProjection));
        when(giftRepository.findAllGifts(pageable)).thenReturn(expectedPage);

        Page<GiftProjection> result = giftService.getAllGifts(pageable);

        assertNotNull(result);
        assertTrue(result.hasContent());
        assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
        verify(giftRepository, times(1)).findAllGifts(pageable);
    }

    @Test
    public void GiftService_GetGiftsByFilter_ReturnsGiftProjection() {
        GiftProjection giftProjection = createMockGiftProjection(1L, "Filtered Gift", new BigDecimal("100.00"));
        Page<GiftProjection> expectedPage = new PageImpl<>(List.of(giftProjection));
        when(giftFilterService.processRequest(any())).thenReturn(filterRequest);
        when(giftRepository.findGiftsByFilter(
                filterRequest.getBudget(),
                filterRequest.getGender(),
                filterRequest.getAge(),
                filterRequest.getCategories(),
                filterRequest.getOccasions(),
                pageable
        )).thenReturn(expectedPage);

        Page<GiftProjection> result = giftService.getGiftsByFilter(filterRequest, pageable);

        assertNotNull(result);
        assertTrue(result.hasContent());
        assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
        verify(giftRepository, times(1)).findGiftsByFilter(
                filterRequest.getBudget(),
                filterRequest.getGender(),
                filterRequest.getAge(),
                filterRequest.getCategories(),
                filterRequest.getOccasions(),
                pageable
        );
    }

    @Test
    public void GiftService_GetGiftById_ReturnsGift() {
        Gift gift = new Gift();
        gift.setName("test");
        gift.setId(1L);
        when(giftRepository.findById(1L)).thenReturn(Optional.of(gift));

        Gift result = giftService.getGiftById(1L);

        assertEquals("test", result.getName());
        assertEquals(1L, result.getId());
    }
}
