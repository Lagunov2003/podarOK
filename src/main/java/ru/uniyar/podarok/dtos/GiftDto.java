package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Dto для вывода подарка.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String photoUrl;
    private Boolean isFavorite;
}
