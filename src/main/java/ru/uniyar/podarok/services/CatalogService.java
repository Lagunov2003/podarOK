package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.entities.Survey;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.repositories.projections.GiftProjection;

@Service
@AllArgsConstructor
public class CatalogService {
    private GiftService giftService;
    private UserService userService;

    public Page<GiftProjection> getGiftsCatalog(Pageable pageable) throws UserNotFoundException, UserNotAuthorizedException {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            Survey survey = userService.getCurrentAuthenticationUser().getSurvey();
            if (survey != null) {
                return giftService.getGiftsBySurvey(survey, pageable);
            }
        }
        return giftService.getAllGifts(pageable);
    }
}
