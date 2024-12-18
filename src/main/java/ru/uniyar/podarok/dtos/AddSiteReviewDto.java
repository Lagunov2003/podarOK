package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto для добавления отзыва о сайте.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSiteReviewDto {
    private Integer mark;
    private String review;
}
