package ru.uniyar.podarok.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.OrderDto;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.Order;

@Component
@AllArgsConstructor
public class OrderDtoConverter {
    private final GiftDtoConverter giftDtoConverter;

    public OrderDto convertToOrderDto(Order order) {
        Gift gift = order.getGift();
        GiftDto giftDto = giftDtoConverter.convertToGiftDto(gift);

        return new OrderDto(
                order.getId(),
                order.getDeliveryDate(),
                order.getStatus(),
                order.getInformation(),
                giftDto
        );
    }
}
