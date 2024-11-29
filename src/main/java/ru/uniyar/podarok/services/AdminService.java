package ru.uniyar.podarok.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.dtos.OrderDataDto;
import ru.uniyar.podarok.dtos.OrderDto;
import ru.uniyar.podarok.exceptions.OrderNotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {
    private OrderService orderService;
    private GiftService giftService;

    public void changeOrderStatus(OrderDataDto orderDataDto) throws OrderNotFoundException {
        orderService.changeOrderStatus(orderDataDto);
    }

    public List<OrderDto> getOrders(String status) {
        return orderService.getOrders(status);
    }

    public void deleteGift(Long id) throws EntityNotFoundException {
        giftService.deleteGift(id);
    }
}
