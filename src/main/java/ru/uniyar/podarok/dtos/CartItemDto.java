package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto для добавления подарка в корзину и изменения количества товаров в корзине.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private Long giftId;
    private Integer itemCount;
}
