package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.repositories.projections.GiftProjection;

@Service
@AllArgsConstructor
public class CatalogService {
    private GiftService giftService;

    public Page<GiftProjection> getGiftsCatalog(GiftFilterRequest giftFilterRequest, Pageable pageable) {
        GiftFilterRequest effectiveRequest = giftFilterRequest.filterRequest();

        return effectiveRequest.hasSurveyData() || effectiveRequest.hasFilters() ?
                giftService.getGiftsByFilter(effectiveRequest, pageable) :
                giftService.getAllGifts(pageable);
    }
}