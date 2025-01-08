package ru.uniyar.podarok.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.GiftOrder;
import ru.uniyar.podarok.repositories.GiftOrderRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GiftOrderServiceTest {
    @Mock
    private GiftOrderRepository giftOrderRepository;
    @InjectMocks
    private GiftOrderService giftOrderService;

    @Test
    public void GiftOrderService_GetGiftsByOrderId_ReturnsGifts(){
        GiftOrder giftOrder = new GiftOrder();
        giftOrder.setId(1L);
        giftOrder.setGift(new Gift());
        giftOrder.setItemCount(3);
        Set<GiftOrder> giftOrders = Set.of(giftOrder);
        when(giftOrderRepository.findGiftOrdersByOrderId(any())).thenReturn(giftOrders);

        Set<GiftOrder> result = giftOrderService.getGiftsByOrderId(1L);

        assertEquals(1, result.size());
    }
}
