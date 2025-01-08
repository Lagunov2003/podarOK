package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto для вывода отзыва о подарке.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long userId;
    private String text;
    private Integer rating;
    private String userName;
}
