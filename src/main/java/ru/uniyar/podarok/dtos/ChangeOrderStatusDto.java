package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto для изменения статуса заказа.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeOrderStatusDto {
    private Long orderId;
    private String newOrderStatus;
}
