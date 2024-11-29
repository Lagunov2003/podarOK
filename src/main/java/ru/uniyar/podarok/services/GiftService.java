package ru.uniyar.podarok.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.entities.Category;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.Occasion;
import ru.uniyar.podarok.repositories.GiftRepository;
import ru.uniyar.podarok.utils.GiftDtoConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GiftService {
    private GiftRepository giftRepository;
    private GiftFilterService giftFilterService;
    private GiftDtoConverter giftDtoConverter;

    public Page<GiftDto> getAllGifts(Pageable pageable) {
        return giftDtoConverter.convertToGiftDtoPage(giftRepository.findAllGifts(pageable));
    }

    public Page<GiftDto> getGiftsByFilter(GiftFilterRequest filterRequest, Pageable pageable) {
        GiftFilterRequest processedRequest = giftFilterService.processRequest(filterRequest);
        Page<Gift> giftsPage = giftRepository.findGiftsByFilter(
                processedRequest.getBudget(),
                processedRequest.getGender(),
                processedRequest.getAge(),
                processedRequest.getCategories(),
                processedRequest.getOccasions(),
                pageable
        );
        return giftDtoConverter.convertToGiftDtoPage(giftsPage);
    }

    public Gift getGiftById(Long id) throws EntityNotFoundException {
        return  giftRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Подарок не найден!"));
    }

    public Page<GiftDto> searchGiftsByName(String name, Pageable pageable) {
        return giftDtoConverter.convertToGiftDtoPage(giftRepository.findGiftsByName(name, pageable));
    }

    public List<GiftDto> getSimilarGifts(Gift gift) {
        Set<Long> categories = gift.getCategories().stream().map(Category::getId).collect(Collectors.toSet());
        Set<Long> occasions = gift.getOccasions().stream().map(Occasion::getId).collect(Collectors.toSet());

        List<Gift> similarGifts = giftRepository.findGiftsByCategoriesOrOccasions(
                new ArrayList<>(categories),
                new ArrayList<>(occasions)
        );

        similarGifts = similarGifts.stream()
                .filter(g -> !g.getId().equals(gift.getId()))
                .collect(Collectors.toList());

        return giftDtoConverter.convertToGiftDtoList(similarGifts);
    }

    public List<Gift> getGiftsByGroupId(Long groupId) {
        return giftRepository.findGiftsByGroupId(groupId);
    }

    @Transactional
    public void deleteGift(Long id) {
        if (!giftRepository.existsById(id)) {
            throw new EntityNotFoundException("Подарок с Id" + id + "не найден!");
        }
        giftRepository.deleteById(id);
    }
}
