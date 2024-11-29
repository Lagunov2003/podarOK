package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.dtos.OrderDataDto;
import ru.uniyar.podarok.dtos.OrderDto;
import ru.uniyar.podarok.entities.Order;
import ru.uniyar.podarok.exceptions.OrderNotFoundException;
import ru.uniyar.podarok.repositories.OrderRepository;
import ru.uniyar.podarok.utils.OrderDtoConverter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;
    private OrderDtoConverter orderDtoConverter;

    public void placeNewOrder(Order order) {
        orderRepository.save(order);
    }

    public void changeOrderStatus(OrderDataDto orderDataDto) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderDataDto.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Заказ с Id " + orderDataDto.getOrderId() + "не найден в корзине!"));
        order.setStatus(orderDataDto.getNewOrderStatus());
        orderRepository.save(order);
    }

    public List<OrderDto> getOrders(String status) {
        Set<String> validStatus = new HashSet<>(Set.of("Исполняется", "Доставляется", "Выполнен"));
        List<Order> orders;

        if (!validStatus.contains(status)) {
            orders = orderRepository.findAll();
        } else {
            orders = orderRepository.findByStatus(status);
        }

        return orders.stream()
                .map(orderDtoConverter::convertToOrderDto)
                .collect(Collectors.toList());
    }
}
