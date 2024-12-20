package ru.uniyar.podarok.utils.converters;

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

    /**
     * Преобразует объект Order в объект OrderDto.
     * Преобразует все подарки, связанные с заказом, в DTO с использованием конвертера GiftDtoConverter.
     *
     * @param order объект Order, который необходимо преобразовать.
     * @return объект OrderDto, представляющий заказ в виде DTO.
     */
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
