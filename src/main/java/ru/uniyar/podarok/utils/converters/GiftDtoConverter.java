package ru.uniyar.podarok.utils.converters;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.entities.Gift;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс-конвертер, преобразующий объекты типа Gift в DTO и обеспечивающий работу с коллекциями этих объектов.
 */
@Component
public class GiftDtoConverter {
    /**
     * Преобразует объект типа Gift в объект GiftDto.
     *
     * @param gift объект Gift, который необходимо преобразовать.
     * @return преобразованный объект GiftDto.
     */
    public GiftDto convertToGiftDto(Gift gift) {
        String photoUrl = (gift.getPhotos() != null && !gift.getPhotos().isEmpty())
                ? gift.getPhotos().get(0).getPhotoUrl()
                : null;

        return new GiftDto(
                gift.getId(),
                gift.getName(),
                gift.getPrice(),
                photoUrl,
                false
        );
    }

    /**
     * Преобразует список объектов Gift в список объектов GiftDto.
     * Для каждого элемента из исходного списка вызывается метод convertToGiftDto.
     *
     * @param gifts список объектов Gift, который необходимо преобразовать.
     * @return список преобразованных объектов GiftDto.
     */
    public List<GiftDto> convertToGiftDtoList(List<Gift> gifts) {
        return gifts.stream()
                .map(this::convertToGiftDto)
                .collect(Collectors.toList());
    }

    /**
     * Преобразует страницу объектов Gift в страницу объектов GiftDto.
     * Для каждого элемента из страницы вызывается метод convertToGiftDto.
     *
     * @param giftsPage страница объектов Gift, которую необходимо преобразовать.
     * @return страница преобразованных объектов GiftDto.
     */
    public Page<GiftDto> convertToGiftDtoPage(Page<Gift> giftsPage) {
        List<GiftDto> giftDtos = giftsPage.getContent().stream()
                .map(this::convertToGiftDto)
                .collect(Collectors.toList());
        return new PageImpl<>(giftDtos, giftsPage.getPageable(), giftsPage.getTotalElements());
    }
}
