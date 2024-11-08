package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.entities.Survey;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.repositories.projections.GiftProjection;

@Service
@AllArgsConstructor
public class CatalogService {
    private GiftService giftService;
    private UserService userService;

    public Page<GiftProjection> getGiftsCatalog(GiftFilterRequest giftFilterRequest, Pageable pageable) throws UserNotFoundException, UserNotAuthorizedException, IllegalArgumentException {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            Survey survey = userService.getCurrentAuthenticationUser().getSurvey();
            if (survey != null) {
                if (giftFilterRequest != null && giftFilterRequest.hasAnyFilter()) {
                    throw new IllegalArgumentException("Вы не можете использовать фильтрацию по опросу и параметрам одновременно!");
                }
                return giftService.getGiftsBySurvey(survey, pageable);
            }
        }
        return giftFilterRequest.hasAnyFilter() ?
                giftService.getGiftsByFilter(giftFilterRequest, pageable) :
                giftService.getAllGifts(pageable);
    }
}
