package ru.uniyar.podarok.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.OrderDataDto;
import ru.uniyar.podarok.dtos.OrderDto;
import ru.uniyar.podarok.entities.GiftOrder;
import ru.uniyar.podarok.entities.Order;
import ru.uniyar.podarok.exceptions.OrderNotFoundException;
import ru.uniyar.podarok.repositories.OrderRepository;
import ru.uniyar.podarok.utils.OrderDtoConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderDtoConverter orderDtoConverter;
    @Mock
    private GiftOrderService giftOrderService;

    @Test
    public void OrderService_PlaceOrder_VerifiesOrderIsSaved() {
        Order order = new Order();
        order.setId(1L);

        orderService.placeOrder(order);

        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void OrderService_ChangeOrderStatus_VerifiesOrderStatusUpdated() throws OrderNotFoundException {
        Long orderId = 1L;
        String newStatus = "Доставляется";
        Order order = new Order();
        order.setId(orderId);
        order.setStatus("Исполняется");
        OrderDataDto orderDataDto = new OrderDataDto(orderId, newStatus);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.changeOrderStatus(orderDataDto);

        assertEquals(newStatus, order.getStatus());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void OrderService_ChangeOrderStatus_ThrowsOrderNotFoundException() {
        Long orderId = 999L;
        String newStatus = "Доставляется";
        OrderDataDto orderDataDto = new OrderDataDto(orderId, newStatus);
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () ->
                orderService.changeOrderStatus(orderDataDto)
        );

        assertEquals("Заказ с Id " + orderId + " не найден в корзине!", exception.getMessage());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void OrderService_GetOrders_ReturnsAllOrders_WhenStatusIsInvalid() {
        String invalidStatus = "Неизвестный статус";
        Order order1 = new Order();
        order1.setId(1L);
        order1.setStatus("Исполняется");
        Order order2 = new Order();
        order2.setId(2L);
        order2.setStatus("Доставляется");
        List<Order> orders = List.of(order1, order2);
        List<OrderDto> orderDtos = List.of(new OrderDto(3L, LocalDate.now(), LocalTime.now(), LocalTime.now(),
                "Исполняется", "Адрес", "card",
                BigDecimal.valueOf(100), "user", "test@example.com", "8800",
                List.of(new GiftDto())), new OrderDto(3L, LocalDate.now(), LocalTime.now(), LocalTime.now(),
                "Доставлен", "Адрес", "card",
                BigDecimal.valueOf(100), "user", "test@example.com", "8800",
                List.of(new GiftDto())));
        when(orderRepository.findAll()).thenReturn(orders);
        when(orderDtoConverter.convertToOrderDto(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            return new OrderDto(order1.getId(), LocalDate.now(), LocalTime.now(), LocalTime.now(), order1.getStatus(), "Адрес", "card",
                    BigDecimal.valueOf(100), "user", "test@example.com", "8800", List.of(new GiftDto()));
        });

        List<OrderDto> result = orderService.getOrders(invalidStatus);

        assertEquals(orderDtos.size(), result.size());
        verify(orderRepository, times(1)).findAll();
        verify(orderRepository, never()).findByStatus(anyString());
    }

    @Test
    void OrderService_GetOrders_ReturnsFilteredOrders_WhenStatusIsValid() {
        String validStatus = "Оформлен";
        Order order = new Order();
        order.setId(1L);
        order.setStatus(validStatus);
        List<Order> orders = List.of(order);
        List<OrderDto> orderDtos = List.of(new OrderDto(1L, LocalDate.now(), LocalTime.now(), LocalTime.now(), "Оформлен", "Адрес", "card",
                BigDecimal.valueOf(100), "user", "test@example.com", "8800", List.of(new GiftDto())));
        GiftOrder giftOrder = new GiftOrder();
        giftOrder.setId(1l);
        giftOrder.setOrder(order);
        when(orderRepository.findByStatus(validStatus)).thenReturn(orders);
        when(orderDtoConverter.convertToOrderDto(any(Order.class))).thenAnswer(invocation -> {
            Order order1 = invocation.getArgument(0);
            return new OrderDto(order1.getId(), LocalDate.now(), LocalTime.now(), LocalTime.now(), order1.getStatus(), "Адрес", "card",
                    BigDecimal.valueOf(100), "user", "test@example.com", "8800", List.of(new GiftDto()));
        });
        when(giftOrderService.getGiftsByOrderId(1L)).thenReturn(Set.of(giftOrder));

        List<OrderDto> result = orderService.getOrders(validStatus);

        assertEquals(orderDtos.size(), result.size());
        assertEquals(orderDtos.get(0).getStatus(), result.get(0).getStatus());
        verify(orderRepository, times(1)).findByStatus(validStatus);
        verify(orderRepository, never()).findAll();
    }
}
