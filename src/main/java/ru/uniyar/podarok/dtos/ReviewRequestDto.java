package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto для добавления отзыва о подарке.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDto {
    private String text;
    private Integer rating;
    private Long giftId;
}
