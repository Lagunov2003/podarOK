package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.uniyar.podarok.dtos.AddGiftDto;
import ru.uniyar.podarok.dtos.ChangeGiftDto;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.entities.Category;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.Occasion;
import ru.uniyar.podarok.exceptions.GiftNotFoundException;
import ru.uniyar.podarok.repositories.GiftRepository;
import ru.uniyar.podarok.utils.converters.GiftDtoConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервис для управления подарками в системе.
 */
@Service
@AllArgsConstructor
public class GiftService {
    private GiftRepository giftRepository;
    private GiftDtoConverter giftDtoConverter;

    /**
     * Получает список всех подарков.
     *
     * @param pageable параметры пагинации.
     * @return список всех подарков.
     */
    public Page<GiftDto> getAllGifts(Pageable pageable) {
        return giftDtoConverter.convertToGiftDtoPage(giftRepository.findAllGifts(pageable));
    }

    /**
     * Получает подарок по идентификатору.
     *
     * @param id идентификатор подарка.
     * @return подарок, найденный по id.
     * @throws GiftNotFoundException если подарок не найден.
     */
    public Gift getGiftById(Long id) throws GiftNotFoundException {
        return giftRepository.findById(id).orElseThrow(
                () -> new GiftNotFoundException("Подарок с id " + id + " не найден!")
        );
    }

    /**
     * Получает подарки, похожие на заданный.
     * Похожие подарки ищутся по категориям и поводам.
     *
     * @param gift подарок, для которого ищутся похожие.
     * @return список похожих подарков.
     */
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

    /**
     * Получает подарки по идентификатору группы.
     *
     * @param groupId идентификатор группы.
     * @return список подарков, относящихся к заданной группе.
     */
    public List<Gift> getGiftsByGroupId(Long groupId) {
        return giftRepository.findGiftsByGroupId(groupId);
    }

    /**
     * Удаляет подарок по идентификатору.
     *
     * @param id идентификатор подарка.
     * @throws GiftNotFoundException если подарок с указанным id не найден.
     */
    @Transactional
    public void deleteGift(Long id) throws GiftNotFoundException {
        if (!giftRepository.existsById(id)) {
            throw new GiftNotFoundException("Подарок с id " + id + " не найден!");
        }
        giftRepository.deleteById(id);
    }

    /**
     * Обновляет информацию о подарке.
     *
     * @param changeGiftDto объект с обновленными данными о подарке.
     * @throws GiftNotFoundException если подарок с указанным id не найден.
     */
    @Transactional
    public void updateGift(ChangeGiftDto changeGiftDto) throws GiftNotFoundException {
        Long recommendationId = getGiftById(changeGiftDto.getId()).getRecommendation().getId();

        giftRepository.updateGift(
                changeGiftDto.getId(),
                changeGiftDto.getPrice(),
                recommendationId,
                changeGiftDto.getDescription(),
                changeGiftDto.getName(),
                changeGiftDto.getGroupId()
        );
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
        giftRepository.updateGiftRecommendation(
                recommendationId,
                changeGiftDto.getGender(),
                changeGiftDto.getMinAge(),
                changeGiftDto.getMaxAge()
        );
    }

    /**
     * Добавляет новый подарок.
     *
     * @param addGiftDto объект с данными для добавления нового подарка.
     */
    @Transactional
    public void addGift(AddGiftDto addGiftDto) {
        Long recommendationId = Long.valueOf(
                giftRepository.addGiftRecommendation(
                        addGiftDto.getGender(), addGiftDto.getMinAge(), addGiftDto.getMaxAge()
                )
        );

        Long giftId = Long.valueOf(
                giftRepository.addGift(
                        addGiftDto.getPrice(),
                        recommendationId,
                        addGiftDto.getDescription(),
                        addGiftDto.getName(),
                        addGiftDto.getGroupId()
                )
        );

        for (String photoUrl : addGiftDto.getPhotos()) {
            giftRepository.addGiftPhoto(giftId, photoUrl);
        }
        for (Long categoryId : addGiftDto.getCategories()) {
            giftRepository.addGiftCategory(giftId, categoryId);
        }
        giftRepository.addGiftOccasion(giftId, addGiftDto.getOccasion());
        for (Map.Entry<String, String> feature : addGiftDto.getFeatures().entrySet()) {
            giftRepository.addGiftFeature(giftId, feature.getKey(), feature.getValue());
        }
    }
    
    /**
     * Поиск подарков по фильтрам, имени и параметрам сортировки с постраничной навигацией.
     *
     * @param giftFilterRequest запрос с параметрами фильтрации.
     * @param name имя подарка.
     * @param sort параметр сортировки.
     * @param pageable параметры пагинации.
     * @return страница с отфильтрованными и отсортированными подарками.
     */
    public Page<GiftDto> searchGiftsByFilters(
            GiftFilterRequest giftFilterRequest,
            String name,
            String sort,
            Pageable pageable
    ) {
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
