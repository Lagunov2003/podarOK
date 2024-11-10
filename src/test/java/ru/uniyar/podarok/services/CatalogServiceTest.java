package ru.uniyar.podarok.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.entities.Survey;
import ru.uniyar.podarok.repositories.projections.GiftProjection;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CatalogServiceTest {

    @Mock
    private GiftService giftService;

    @Mock
    private UserService userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CatalogService catalogService;

    private Pageable pageable;
    private GiftFilterRequest giftFilterRequest;
    private User user;
    private Survey survey;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        survey = new Survey();
        survey.setId(1L);
        survey.setBudget(BigDecimal.valueOf(100.00));

        user = new User();
        user.setSurvey(survey);

        giftFilterRequest = new GiftFilterRequest();
    }

    @Test
    public void CatalogService_GetGiftsCatalog_ShouldThrowIllegalArgumentException() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userService.getCurrentAuthenticationUser()).thenReturn(user);
        giftFilterRequest.setBudget(BigDecimal.valueOf(50.00));

        assertThrows(IllegalArgumentException.class, () ->
                        catalogService.getGiftsCatalog(giftFilterRequest, pageable),
                "Вы не можете использовать фильтрацию по опросу и параметрам одновременно!");
    }

    @Test
    public void CatalogService_GetGiftsCatalog_ShouldReturnGifts_BySurvey() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userService.getCurrentAuthenticationUser()).thenReturn(user);
        Page<GiftProjection> mockPage = new PageImpl<>(List.of(mock(GiftProjection.class)));
        when(giftService.getGiftsBySurvey(survey, pageable)).thenReturn(mockPage);

        Page<GiftProjection> result = catalogService.getGiftsCatalog(giftFilterRequest, pageable);

        assertNotNull(result);
        assertTrue(result.hasContent());
        verify(giftService, times(1)).getGiftsBySurvey(survey, pageable);
    }

    @Test
    public void CatalogService_GetGiftsCatalog_ShouldReturnGifts_ByFilter() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        user.setSurvey(null);
        when(userService.getCurrentAuthenticationUser()).thenReturn(user);
        giftFilterRequest.setBudget(BigDecimal.valueOf(50.00));
        Page<GiftProjection> mockPage = new PageImpl<>(List.of(mock(GiftProjection.class)));
        when(giftService.getGiftsByFilter(giftFilterRequest, pageable)).thenReturn(mockPage);

        Page<GiftProjection> result = catalogService.getGiftsCatalog(giftFilterRequest, pageable);

        assertNotNull(result);
        assertTrue(result.hasContent());
        verify(giftService, times(1)).getGiftsByFilter(giftFilterRequest, pageable);
    }

    @Test
    public void CatalogService_GetGiftsCatalog_ShouldReturnAllGifts() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(new AnonymousAuthenticationToken("key", "anonymousUser", List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))));
        SecurityContextHolder.setContext(securityContext);
        Page<GiftProjection> mockPage = new PageImpl<>(List.of(mock(GiftProjection.class)));
        when(giftService.getAllGifts(pageable)).thenReturn(mockPage);

        Page<GiftProjection> result = catalogService.getGiftsCatalog(giftFilterRequest, pageable);

        assertNotNull(result);
        assertTrue(result.hasContent());
        verify(giftService, times(1)).getAllGifts(pageable);
    }
}

