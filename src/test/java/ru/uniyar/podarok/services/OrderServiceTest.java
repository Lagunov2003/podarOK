package ru.uniyar.podarok.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.uniyar.podarok.entities.Order;
import ru.uniyar.podarok.repositories.OrderRepository;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;

    @Test
    public void OrderService_PlaceNewOrder_VerifiesOrderIsSaved() {
        Order order = new Order();
        order.setId(1L);

        orderService.placeNewOrder(order);

        verify(orderRepository, times(1)).save(order);
    }
}
