package ru.uniyar.podarok.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.entities.Gift;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс-конвертер, преобразующий объекты типа Gift в DTO
 * и обеспечивающий работу с коллекциями этих объектов.
 */
@Component
public class GiftDtoConverter {
    public GiftDto convertToGiftDto(Gift gift) {
        String photoUrl = (gift.getPhotos() != null && !gift.getPhotos().isEmpty())
                ? gift.getPhotos().get(0).getPhotoUrl()
                : null;

        return new GiftDto(
                gift.getId(),
                gift.getName(),
                gift.getPrice(),
                photoUrl
        );
    }

    public List<GiftDto> convertToGiftDtoList(List<Gift> gifts) {
        return gifts.stream()
                .map(this::convertToGiftDto)
                .collect(Collectors.toList());
    }

    public Page<GiftDto> convertToGiftDtoPage(Page<Gift> giftsPage) {
        List<GiftDto> giftDtos = giftsPage.getContent().stream()
                .map(this::convertToGiftDto)
                .collect(Collectors.toList());
        return new PageImpl<>(giftDtos, giftsPage.getPageable(), giftsPage.getTotalElements());
    }
}