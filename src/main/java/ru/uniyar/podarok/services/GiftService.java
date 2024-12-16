package ru.uniyar.podarok.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.uniyar.podarok.dtos.AddGiftDto;
import ru.uniyar.podarok.dtos.ChangeGiftDto;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.entities.*;
import ru.uniyar.podarok.exceptions.GiftNotFoundException;
import ru.uniyar.podarok.repositories.GiftRepository;
import ru.uniyar.podarok.utils.GiftDtoConverter;

import java.util.*;
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

    public Gift getGiftById(Long id) throws GiftNotFoundException {
        return giftRepository.findById(id).orElseThrow(() -> new GiftNotFoundException("Подарок с id " + id + " не найден!"));
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
    public void deleteGift(Long id) throws GiftNotFoundException {
        if (!giftRepository.existsById(id)) {
            throw new GiftNotFoundException("Подарок с id " + id + " не найден!");
        }
        giftRepository.deleteById(id);
    }

    @Transactional
    public void updateGift(ChangeGiftDto changeGiftDto) throws GiftNotFoundException {
        Long recommendation_id = getGiftById(changeGiftDto.getId()).getRecommendation().getId();
        giftRepository.updateGift(changeGiftDto.getId(), changeGiftDto.getPrice(), recommendation_id, changeGiftDto.getDescription(), changeGiftDto.getName(), changeGiftDto.getGroupId());
        giftRepository.deleteGiftPhotos(changeGiftDto.getId());
        for (String photoUrl : changeGiftDto.getPhotos()) {
            giftRepository.addGiftPhoto(changeGiftDto.getId(), photoUrl);
        }
        giftRepository.deleteGiftCategories(changeGiftDto.getId());
        for (Long categoryId : changeGiftDto.getCategories()) {
            giftRepository.addGiftCategory(changeGiftDto.getId(), categoryId);
        }
        giftRepository.updateGiftOccasion(changeGiftDto.getId(), changeGiftDto.getOccasion());
        giftRepository.deleteGiftFeatures(changeGiftDto.getId());
        for (Map.Entry<String, String> feature : changeGiftDto.getFeatures().entrySet()) {
            giftRepository.addGiftFeature(changeGiftDto.getId(), feature.getKey(), feature.getValue());
        }
        giftRepository.updateGiftRecommendation(recommendation_id, changeGiftDto.getGender(), changeGiftDto.getMinAge(), changeGiftDto.getMaxAge());
    }

    @Transactional
    public void addGift(AddGiftDto addGiftDto) {
        Long recommendation_id = Long.valueOf(giftRepository.addGiftRecommendation(addGiftDto.getGender(), addGiftDto.getMinAge(), addGiftDto.getMaxAge()));
        Long gift_id = Long.valueOf(giftRepository.addGift(addGiftDto.getPrice(), recommendation_id, addGiftDto.getDescription(), addGiftDto.getName(), addGiftDto.getGroupId()));
        for (String photoUrl : addGiftDto.getPhotos()) {
            giftRepository.addGiftPhoto(gift_id, photoUrl);
        }
        for (Long categoryId : addGiftDto.getCategories()) {
            giftRepository.addGiftCategory(gift_id, categoryId);
        }
        giftRepository.addGiftOccasion(gift_id, addGiftDto.getOccasion());

        for (Map.Entry<String, String> feature : addGiftDto.getFeatures().entrySet()) {
            giftRepository.addGiftFeature(gift_id, feature.getKey(), feature.getValue());
        }
    }

    public Page<GiftDto> searchGiftsBySortParam(String sortParam, Pageable pageable) {
        Page<Gift> sortGifts = switch (sortParam) {
            case "по возрастанию цены" -> giftRepository.findAllByOrderByPriceAsc(pageable);
            case "по убыванию цены" -> giftRepository.findAllByOrderByPriceDesc(pageable);
            case "по рейтингу" -> giftRepository.findAllOrderByAverageRatingDesc(pageable);
            default -> giftRepository.findAllGifts(pageable);
        };
        return giftDtoConverter.convertToGiftDtoPage(sortGifts);
    }

    public Page<GiftDto> searchGiftsByFilters(GiftFilterRequest giftFilterRequest, String name, String sort, Pageable pageable) {
        if (giftFilterRequest.getCategories() == null) {
            giftFilterRequest.setCategories(Collections.emptyList());
        }
        if (giftFilterRequest.getOccasions() == null) {
            giftFilterRequest.setOccasions(Collections.emptyList());
        }
        if (name == null) {
            name = "";
        }
        Page<Gift> filteredGifts = switch (sort) {
            case "по возрастанию цены" -> giftRepository.findAllByFiltersByNameAndByPriceAsc(
                    giftFilterRequest.getBudget(),
                    giftFilterRequest.getGender(),
                    giftFilterRequest.getAge(),
                    giftFilterRequest.getCategories(),
                    giftFilterRequest.getOccasions(),
                    name,
                    pageable
            );
            case "по убыванию цены" -> giftRepository.findAllByFiltersByNameAndByPriceDesc(
                    giftFilterRequest.getBudget(),
                    giftFilterRequest.getGender(),
                    giftFilterRequest.getAge(),
                    giftFilterRequest.getCategories(),
                    giftFilterRequest.getOccasions(),
                    name,
                    pageable
            );
            case "по рейтингу" -> giftRepository.findAllByFiltersByNameAndByAverageRatingDesc(
                    giftFilterRequest.getBudget(),
                    giftFilterRequest.getGender(),
                    giftFilterRequest.getAge(),
                    giftFilterRequest.getCategories(),
                    giftFilterRequest.getOccasions(),
                    name,
                    pageable
            );
            default -> giftRepository.findAllByFiltersByName(
                    giftFilterRequest.getBudget(),
                    giftFilterRequest.getGender(),
                    giftFilterRequest.getAge(),
                    giftFilterRequest.getCategories(),
                    giftFilterRequest.getOccasions(),
                    name,
                    pageable
            );
        };
        return giftDtoConverter.convertToGiftDtoPage(filteredGifts);
    }

}
