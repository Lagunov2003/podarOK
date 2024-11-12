package ru.uniyar.podarok.services;

import jakarta.persistence.EntityNotFoundException;
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
    private GiftFilterService giftFilterService;

    public Page<GiftProjection> getAllGifts(Pageable pageable) {
        return giftRepository.findAllGifts(pageable);
    }

    public Page<GiftProjection> getGiftsByFilter(GiftFilterRequest filterRequest, Pageable pageable) {
        GiftFilterRequest processedRequest = giftFilterService.processRequest(filterRequest);

        return giftRepository.findGiftsByFilter(
                processedRequest.getBudget(),
                processedRequest.getGender(),
                processedRequest.getAge(),
                processedRequest.getCategories() != null ? processedRequest.getCategories() : Collections.emptyList(),
                processedRequest.getOccasions() != null ? processedRequest.getOccasions() : Collections.emptyList(),
                pageable
        );
    }

    public Gift getGiftById(Long id){
        return  giftRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Подарок не найден!"));
    }
}
