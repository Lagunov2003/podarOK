package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto для вывода подарков из заказа.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Integer itemCount;
    private Long giftId;
}
