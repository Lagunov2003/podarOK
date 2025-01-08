package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Dto для вывода заказа.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private LocalDate deliveryDate;
    private LocalTime fromDeliveryTime;
    private LocalTime toDeliveryTime;
    private String status;
    private String information;
    private String payMethod;
    private BigDecimal orderCost;
    private String recipientName;
    private String recipientEmail;
    private String recipientPhoneNumber;
    private List<GiftDto> gifts;
}
