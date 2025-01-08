package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.entities.GiftOrder;
import ru.uniyar.podarok.repositories.GiftOrderRepository;

import java.util.Set;

/**
 * Сервис для управления заказами подарков.
 */
@Service
@AllArgsConstructor
public class GiftOrderService {
    private GiftOrderRepository giftOrderRepository;

    /**
     * Получает список подарков по идентификатору заказа.
     *
     * @param orderId идентификатор заказа.
     * @return множество объектов GiftOrder, соответствующих заданному заказу.
     */
    public Set<GiftOrder> getGiftsByOrderId(Long orderId) {
        return giftOrderRepository.findGiftOrdersByOrderId(orderId);
    }
}
