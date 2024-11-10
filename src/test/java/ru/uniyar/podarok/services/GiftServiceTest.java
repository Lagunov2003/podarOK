package ru.uniyar.podarok.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.entities.Survey;
import ru.uniyar.podarok.entities.Occasion;
import ru.uniyar.podarok.repositories.GiftRepository;
import ru.uniyar.podarok.repositories.projections.GiftProjection;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GiftServiceTest {

    @Mock
    private GiftRepository giftRepository;

    @InjectMocks
    private GiftService giftService;

    private Pageable pageable;
    private Survey survey;
    private GiftFilterRequest filterRequest;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        Occasion occasion = new Occasion();
        occasion.setId(1L);
        occasion.setName("Birthday");

        survey = new Survey();
        survey.setId(1L);
        survey.setBudget(new BigDecimal("100.00"));
        survey.setGender(true);
        survey.setAge(25);
        survey.setUrgency(true);
        survey.setOccasion(occasion);

        filterRequest = new GiftFilterRequest();
        filterRequest.setBudget(new BigDecimal("200.00"));
        filterRequest.setGender(true);
        filterRequest.setAge(30);
        filterRequest.setUrgency(false);
        filterRequest.setCategories(List.of(1L, 2L));
        filterRequest.setOccasions(List.of(1L, 3L));
    }

    private GiftProjection createMockGiftProjection(Long id, String name, BigDecimal price) {
        GiftProjection giftProjection = mock(GiftProjection.class);
        when(giftProjection.getId()).thenReturn(id);
        when(giftProjection.getName()).thenReturn(name);
        when(giftProjection.getPrice()).thenReturn(price);
        return giftProjection;
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
    public void GiftService_GetGiftsBySurvey_ReturnsGiftProjection() {
        GiftProjection giftProjection = createMockGiftProjection(1L, "Survey Gift", new BigDecimal("75.00"));
        Page<GiftProjection> expectedPage = new PageImpl<>(List.of(giftProjection));
        when(giftRepository.findGiftsBySurvey(
                survey.getId(),
                survey.getBudget(),
                survey.getGender(),
                survey.getAge(),
                survey.getUrgency(),
                survey.getOccasion().getId(),
                pageable
        )).thenReturn(expectedPage);

        Page<GiftProjection> result = giftService.getGiftsBySurvey(survey, pageable);

        assertNotNull(result);
        assertTrue(result.hasContent());
        assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
        verify(giftRepository, times(1)).findGiftsBySurvey(
                survey.getId(),
                survey.getBudget(),
                survey.getGender(),
                survey.getAge(),
                survey.getUrgency(),
                survey.getOccasion().getId(),
                pageable
        );
    }

    @Test
    public void GiftService_GetGiftsByFilter_ReturnsGiftProjection() {
        GiftProjection giftProjection = createMockGiftProjection(1L, "Filtered Gift", new BigDecimal("100.00"));
        Page<GiftProjection> expectedPage = new PageImpl<>(List.of(giftProjection));
        when(giftRepository.findGiftsByFilter(
                filterRequest.getBudget(),
                filterRequest.getGender(),
                filterRequest.getAge(),
                filterRequest.getUrgency(),
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
                filterRequest.getUrgency(),
                filterRequest.getCategories(),
                filterRequest.getOccasions(),
                pageable
        );
    }
}
