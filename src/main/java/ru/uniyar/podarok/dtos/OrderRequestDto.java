package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Dto для добавления заказа.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    private List<OrderItemDto> items;
    private LocalDate deliveryDate;
    private LocalTime fromDeliveryTime;
    private LocalTime toDeliveryTime;
    private BigDecimal orderCost;
    private String information;
    private String payMethod;
    private String recipientName;
    private String recipientEmail;
    private String recipientPhoneNumber;
}
