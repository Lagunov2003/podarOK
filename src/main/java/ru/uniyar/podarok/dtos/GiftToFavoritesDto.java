package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto для добавления подарка в избранные.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftToFavoritesDto {
    private Long giftId;
}
