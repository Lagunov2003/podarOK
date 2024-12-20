package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.uniyar.podarok.dtos.ChangeOrderStatusDto;
import ru.uniyar.podarok.dtos.OrderDto;
import ru.uniyar.podarok.entities.GiftOrder;
import ru.uniyar.podarok.entities.Order;
import ru.uniyar.podarok.exceptions.OrderNotFoundException;
import ru.uniyar.podarok.repositories.OrderRepository;
import ru.uniyar.podarok.utils.converters.OrderDtoConverter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервис для управления заказами.
 */
@Service
@AllArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;
    private OrderDtoConverter orderDtoConverter;
    private GiftOrderService giftOrderService;
    private NotificationService notificationService;

    /**
     * Размещает новый заказ и создаёт уведомление о размещении заказа.
     *
     * @param order объект, представляющий заказ
     */
    public void placeOrder(Order order) {
        orderRepository.save(order);
        notificationService.createPlaceOrderNotification(order);
    }

    /**
     * Изменяет статус существующего заказа.
     *
     * @param changeOrderStatusDto объект, содержащий информацию о заказе и новый статус
     * @throws OrderNotFoundException если заказ с указанным id не найден
     */
    @Transactional
    public void changeOrderStatus(ChangeOrderStatusDto changeOrderStatusDto) throws OrderNotFoundException {
        Order order = orderRepository.findById(changeOrderStatusDto.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(
                        "Заказ с Id " + changeOrderStatusDto.getOrderId() + " не найден в корзине!")
                );
        order.setStatus(changeOrderStatusDto.getNewOrderStatus());
        notificationService.createOrderStatusChangeNotification(order, changeOrderStatusDto);
        orderRepository.save(order);
    }

    /**
     * Получает список заказов по статусу.
     * Если статус не является одним из валидных значений, возвращает все заказы.
     *
     * @param status статус заказов, которые нужно получить
     * @return список заказов в виде DTO
     */
    @Transactional
    public List<OrderDto> getOrders(String status) {
        Set<String> validStatus = new HashSet<>(Set.of("Оформлен", "Собран", "В пути", "Доставлен", "Отменен"));
        List<Order> orders;

        if (!validStatus.contains(status)) {
            orders = orderRepository.findAll();
        } else {
            orders = orderRepository.findByStatus(status);
        }

        for (Order order : orders) {
            for (GiftOrder giftOrder : giftOrderService.getGiftsByOrderId(order.getId())) {
                giftOrder.setOrder(order);
                order.getGiftOrders().add(giftOrder);
            }
        }

        return orders.stream()
                .map(orderDtoConverter::convertToOrderDto)
                .collect(Collectors.toList());
    }
}
