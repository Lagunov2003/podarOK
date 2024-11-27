package ru.uniyar.podarok.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.uniyar.podarok.dtos.GiftFilterRequest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GiftFilterServiceTest {
    private GiftFilterService giftFilterService;

    @BeforeEach
    void setUp() {
        giftFilterService = new GiftFilterService();
    }

    @Test
    void GiftFilterService_HasFilters_ReturnsTrue() {
        GiftFilterRequest request = new GiftFilterRequest(
                List.of(1L),
                null,
                true,
                BigDecimal.valueOf(100),
                25,
                null, null, null, null, null
        );

        boolean result = giftFilterService.hasFilters(request);

        assertTrue(result);
    }

    @Test
    void GiftFilterService_HasFilters_ReturnsFalse() {
        GiftFilterRequest request = new GiftFilterRequest(
                null, null, null, null, null,
                null, null, null, null, null
        );

        boolean result = giftFilterService.hasFilters(request);

        assertFalse(result);
    }

    @Test
    void GiftFilterService_HasSurveyData_ReturnsTrue() {
        GiftFilterRequest request = new GiftFilterRequest(
                null, null, null, null, null,
                List.of(1L),
                null,
                false,
                BigDecimal.valueOf(200),
                30
        );

        boolean result = giftFilterService.hasSurveyData(request);

        assertTrue(result);
    }

    @Test
    void GiftFilterService_HasSurveyData_ReturnsFalse() {
        GiftFilterRequest request = new GiftFilterRequest(
                null, null, null, null, null,
                null, null, null, null, null
        );

        boolean result = giftFilterService.hasSurveyData(request);

        assertFalse(result);
    }

    @Test
    void GiftFilterService_ProcessRequest_ReturnsRequest_whenOnlySurveyDataIsSet() {
        GiftFilterRequest request = new GiftFilterRequest(
                null, null, null, null, null,
                List.of(1L),
                null,
                false,
                BigDecimal.valueOf(150),
                28
        );

        GiftFilterRequest result = giftFilterService.processRequest(request);

        assertNotNull(result);
        assertEquals(List.of(1L), result.getSurveyCategories());
        assertEquals(false, result.getSurveyGender());
        assertEquals(BigDecimal.valueOf(150), result.getSurveyBudget());
        assertEquals(28, result.getSurveyAge());
    }

    @Test
    void GiftFilterService_ProcessRequest_ReturnsRequest_whenOnlyFilterDataIsSet() {
        GiftFilterRequest request = new GiftFilterRequest(
                List.of(1L),
                List.of(1L),
                true,
                BigDecimal.valueOf(100),
                25,
                null, null, null, null, null
        );

        GiftFilterRequest result = giftFilterService.processRequest(request);

        assertNotNull(result);
        assertEquals(List.of(1L), result.getCategories());
        assertEquals(true, result.getGender());
        assertEquals(BigDecimal.valueOf(100), result.getBudget());
        assertEquals(25, result.getAge());
        assertNull(result.getSurveyCategories());
    }

    @Test
    void GiftFilterService_ProcessRequest_ReturnsRequest_whenBothFiltersAndSurveyDataAreSet() {
        GiftFilterRequest request = new GiftFilterRequest(
                List.of(1L),
                List.of(1L),
                true,
                BigDecimal.valueOf(100),
                25,
                List.of(1L),
                null,
                false,
                BigDecimal.valueOf(200),
                30
        );

        GiftFilterRequest result = giftFilterService.processRequest(request);

        assertNotNull(result);
        assertEquals(List.of(1L), result.getCategories());
        assertEquals(false, result.getGender());
        assertEquals(BigDecimal.valueOf(200), result.getBudget());
        assertEquals(30, result.getAge());
    }
}
