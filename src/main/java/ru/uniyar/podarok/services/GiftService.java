package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.repositories.projections.GiftProjection;
import ru.uniyar.podarok.entities.Survey;
import ru.uniyar.podarok.repositories.GiftRepository;

@Service
@AllArgsConstructor
public class GiftService {
    private GiftRepository giftRepository;

    public Page<GiftProjection> getAllGifts(Pageable pageable) {
        return giftRepository.findAllGifts(pageable);
    }

    public Page<GiftProjection> getGiftsBySurvey(Survey survey, Pageable pageable) {
        return giftRepository.findGiftsBySurvey(survey, pageable);
    }
}
