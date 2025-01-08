package ru.uniyar.podarok.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.uniyar.podarok.dtos.AddSiteReviewDto;
import ru.uniyar.podarok.dtos.SiteReviewsDto;
import ru.uniyar.podarok.entities.SiteReviews;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.SiteReviewAlreadyExistException;
import ru.uniyar.podarok.exceptions.SiteReviewNotFoundException;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.repositories.SiteReviewsRepository;
import ru.uniyar.podarok.utils.converters.SiteReviewsDtoConverter;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SiteReviewsServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private SiteReviewsRepository siteReviewsRepository;

    @Mock
    private SiteReviewsDtoConverter siteReviewsDtoConverter;

    @InjectMocks
    private SiteReviewsService siteReviewsService;

    @Test
    void SiteReviewsService_GetSiteReviews_ReturnsAcceptedReviewsList() {
        User user = new User();
        user.setId(1L);
        SiteReviews siteReviews1 = new SiteReviews();
        siteReviews1.setId(1L);
        siteReviews1.setReview("Review 1");
        siteReviews1.setUser(user);
        siteReviews1.setAccepted(true);
        siteReviews1.setMark(5);
        SiteReviews siteReviews2 = new SiteReviews();
        siteReviews2.setId(2L);
        siteReviews2.setReview("Review 2");
        siteReviews2.setUser(user);
        siteReviews2.setAccepted(true);
        siteReviews2.setMark(4);
        List<SiteReviews> siteReviews = List.of(
                siteReviews1, siteReviews2
        );
        List<SiteReviewsDto> siteReviewsDtos = List.of(
                new SiteReviewsDto(1L, 1L, "user", "Review 1", 5),
                new SiteReviewsDto(2L, 2L, "user", "Review 2", 4)
        );
        when(siteReviewsRepository.findTop6ByAcceptedTrue()).thenReturn(siteReviews);
        when(siteReviewsDtoConverter.convertToSiteReviewsDto(any(SiteReviews.class)))
                .thenReturn(siteReviewsDtos.get(0), siteReviewsDtos.get(1));

        List<SiteReviewsDto> result = siteReviewsService.getSiteReviews();

        assertEquals(2, result.size());
        assertEquals("Review 1", result.get(0).getReview());
        verify(siteReviewsRepository).findTop6ByAcceptedTrue();
    }

    @Test
    void SiteReviewsService_GetSiteReviewsByAcceptedStatus_ReturnsAcceptedReviews_WhenAcceptedIsTrue() {
        SiteReviews siteReviews = new SiteReviews();
        siteReviews.setId(1L);
        siteReviews.setReview("Review 1");
        siteReviews.setAccepted(true);
        siteReviews.setMark(5);
        List<SiteReviews> siteReviewsList = List.of(siteReviews);
        List<SiteReviewsDto> siteReviewsDtos = List.of(new SiteReviewsDto(
                1L,
                1L,
                "user",
                "Review 1",
                5));
        when(siteReviewsRepository.findByAcceptedTrue()).thenReturn(siteReviewsList);
        when(siteReviewsDtoConverter.convertToSiteReviewsDto(any(SiteReviews.class)))
                .thenReturn(siteReviewsDtos.get(0));

        List<SiteReviewsDto> result = siteReviewsService.getSiteReviewsByAcceptedStatus(true);

        assertEquals(1, result.size());
        assertEquals("Review 1", result.get(0).getReview());
        verify(siteReviewsRepository).findByAcceptedTrue();
    }

    @Test
    void SiteReviewsService_AddSiteReview_VerifiesReviewSaved()
            throws UserNotFoundException, UserNotAuthorizedException, SiteReviewAlreadyExistException {
        AddSiteReviewDto addSiteReviewDto = new AddSiteReviewDto(5, "Great service!");
        User user = new User();
        user.setId(1L);
        user.setEmail("User");
        when(userService.getCurrentAuthenticationUser()).thenReturn(user);

        siteReviewsService.addSiteReview(addSiteReviewDto);

        verify(siteReviewsRepository).save(any(SiteReviews.class));
    }

    @Test
    void SiteReviewsService_AddSiteReview_ThrowsSiteReviewAlreadyExistException()
            throws UserNotFoundException, UserNotAuthorizedException {
        AddSiteReviewDto addSiteReviewDto = new AddSiteReviewDto(5, "Great service!");
        User user = new User();
        user.setId(1L);
        user.setEmail("User");
        user.setSiteReviews(new SiteReviews());
        when(userService.getCurrentAuthenticationUser()).thenReturn(user);

        assertThrows(SiteReviewAlreadyExistException.class,
                () -> siteReviewsService.addSiteReview(addSiteReviewDto));

    }


    @Test
    void SiteReviewsService_ChangeAcceptedStatusSiteReviews_VerifiesReviewIsSaved()
            throws SiteReviewNotFoundException {
        SiteReviews siteReviews = new SiteReviews();
        siteReviews.setId(1L);
        siteReviews.setReview("Review 1");
        siteReviews.setAccepted(false);
        siteReviews.setMark(5);
        when(siteReviewsRepository.findById(1L)).thenReturn(Optional.of(siteReviews));

        siteReviewsService.changeAcceptedStatusSiteReviews(1L);

        assertTrue(siteReviews.getAccepted());
        verify(siteReviewsRepository).save(siteReviews);
    }

    @Test
    void SiteReviewsService_ChangeAcceptedStatusSiteReviews_ThrowsSiteReviewNotFoundException() {
        when(siteReviewsRepository.findById(1L)).thenReturn(Optional.empty());

        SiteReviewNotFoundException exception = assertThrows(SiteReviewNotFoundException.class, () ->
                siteReviewsService.changeAcceptedStatusSiteReviews(1L)
        );
        assertEquals("Отзыв с id 1 не найден!", exception.getMessage());
        verify(siteReviewsRepository, never()).save(any(SiteReviews.class));
    }

    @Test
    void SiteReviewsService_DeleteSiteReviews_VerifiesReviewDeleted()
            throws SiteReviewNotFoundException {
        when(siteReviewsRepository.existsById(any())).thenReturn(true);
        siteReviewsService.deleteSiteReviews(1L);

        verify(siteReviewsRepository).deleteById(1L);
    }

    @Test
    void SiteReviewsService_DeleteSiteReviews_ThrowsSiteReviewNotFoundException() {
        when(siteReviewsRepository.existsById(any())).thenReturn(false);

        assertThrows(SiteReviewNotFoundException.class, () -> siteReviewsService.deleteSiteReviews(1L));
    }
}
