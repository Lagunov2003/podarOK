package ru.uniyar.podarok.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.OrderDto;
import ru.uniyar.podarok.entities.GiftOrder;
import ru.uniyar.podarok.entities.Order;

import java.util.List;

/**
 * Конвертер для преобразования объектов типа Order в DTO,
 * с использованием другого конвертера для преобразования подарков.
 */
@Component
@AllArgsConstructor
public class OrderDtoConverter {
    private final GiftDtoConverter giftDtoConverter;

    public OrderDto convertToOrderDto(Order order) {
        List<GiftDto> giftDtos = order.getGiftOrders().stream()
                .map(GiftOrder::getGift)
                .map(giftDtoConverter::convertToGiftDto)
                .toList();

        return new OrderDto(
                order.getId(),
                order.getDeliveryDate(),
                order.getFromDeliveryTime(),
                order.getToDeliveryTime(),
                order.getStatus(),
                order.getInformation(),
                order.getPayMethod(),
                order.getOrderCost(),
                order.getRecipientName(),
                order.getRecipientEmail(),
                order.getRecipientPhoneNumber(),
                giftDtos
        );
    }
}
