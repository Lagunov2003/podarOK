package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto для вывода подарка в корзине.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Integer itemCount;
    private GiftDto gift;
}
