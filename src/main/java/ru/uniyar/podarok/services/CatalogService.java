package ru.uniyar.podarok.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.dtos.GiftResponseDto;
import ru.uniyar.podarok.dtos.GiftToFavoritesDto;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.GiftGroup;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CatalogService {
    private GiftService giftService;
    private GiftFilterService giftFilterService;
    private UserService userService;

    public Page<GiftDto> getGiftsCatalog(GiftFilterRequest giftFilterRequest, Pageable pageable) {
        GiftFilterRequest effectiveRequest = giftFilterService.processRequest(giftFilterRequest);
        return giftFilterService.hasSurveyData(effectiveRequest) || giftFilterService.hasFilters(effectiveRequest) ?
                giftService.getGiftsByFilter(effectiveRequest, pageable) :
                giftService.getAllGifts(pageable);
    }

    public Gift getGift(Long giftId) throws EntityNotFoundException {
        return giftService.getGiftById(giftId);
    }

    public Page<GiftDto> searchGiftsByName(String query, Pageable pageable) {
        return giftService.searchGiftsByName(query, pageable);
    }

    public void addGiftToFavorites(GiftToFavoritesDto giftToFavoritesDto) throws UserNotFoundException, UserNotAuthorizedException, EntityNotFoundException {
        Gift gift = giftService.getGiftById(giftToFavoritesDto.getGiftId());
        userService.addGiftToFavorites(gift);
    }

    public List<GiftDto> getSimilarGifts(Long giftId) throws EntityNotFoundException {
        Gift gift = giftService.getGiftById(giftId);
        return giftService.getSimilarGifts(gift);
    }

    public List<Gift> getGiftsByGroupId(Long groupId) {
        return giftService.getGiftsByGroupId(groupId);
    }

    public GiftResponseDto getGiftResponse(Long giftId) throws EntityNotFoundException {
        Gift gift = getGift(giftId);
        GiftGroup giftGroup = gift.getGiftGroup();
        List<Gift> groupGifts = new ArrayList<>(List.of(gift));
        if (giftGroup != null) {
            groupGifts.addAll(getGiftsByGroupId(giftGroup.getId()).stream()
                    .filter(g -> !g.getId().equals(giftId))
                    .toList());
        }
        return new GiftResponseDto(groupGifts, getSimilarGifts(giftId));
    }
}