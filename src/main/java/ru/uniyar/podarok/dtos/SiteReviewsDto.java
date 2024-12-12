package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteReviewsDto {
    private Long id;
    private String userName;
    private String review;
    private Integer mark;
}
