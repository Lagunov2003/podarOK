package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.entities.GiftOrder;
import ru.uniyar.podarok.repositories.GiftOrderRepository;

import java.util.Set;

@Service
@AllArgsConstructor
public class GiftOrderService {
    private GiftOrderRepository giftOrderRepository;

    public Set<GiftOrder> getGiftsByOrderId(Long orderId) {
        return giftOrderRepository.findGiftOrdersByOrderId(orderId);
    }
}
