package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.repositories.projections.GiftProjection;
import ru.uniyar.podarok.repositories.GiftRepository;

import java.util.Collections;

@Service
@AllArgsConstructor
public class GiftService {
    private GiftRepository giftRepository;

    public Page<GiftProjection> getAllGifts(Pageable pageable) {
        return giftRepository.findAllGifts(pageable);
    }

    public Page<GiftProjection> getGiftsByFilter(GiftFilterRequest filterRequest, Pageable pageable) {
        return giftRepository.findGiftsByFilter(
                filterRequest.getBudget(),
                filterRequest.getGender(),
                filterRequest.getAge(),
                filterRequest.getCategories() != null ? filterRequest.getCategories() : Collections.emptyList(),
                filterRequest.getOccasions() != null ? filterRequest.getOccasions() : Collections.emptyList(),
                pageable
        );
    }

    public Gift getGiftById(Long id){
        return  giftRepository.findById(id).get();
    }
}
