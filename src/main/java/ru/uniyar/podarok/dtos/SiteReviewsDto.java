package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto для добавления отзыва о сайтеа.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteReviewsDto {
    private Long userId;
    private String userName;
    private String review;
    private Integer mark;
}
