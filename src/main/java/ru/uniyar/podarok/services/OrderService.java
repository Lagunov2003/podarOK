package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.entities.Order;
import ru.uniyar.podarok.repositories.OrderRepository;

@Service
@AllArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;

    public void placeNewOrder(Order order) {
        orderRepository.save(order);
    }
}
