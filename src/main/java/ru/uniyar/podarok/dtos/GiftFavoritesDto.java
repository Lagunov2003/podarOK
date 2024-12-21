package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto для добавления подарка в избранные и удаления оттуда.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftFavoritesDto {
    private Long giftId;
}
