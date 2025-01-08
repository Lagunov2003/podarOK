package ru.uniyar.podarok.repositories;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.uniyar.podarok.entities.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setup() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus("Оформлен");
        order.setOrderCost(BigDecimal.valueOf(1000));
        order.setInformation("ул. Союзная, д. 144");
        order.setDeliveryDate(LocalDate.now());
        order.setFromDeliveryTime(LocalTime.now());
        order.setPayMethod("cash");
        order.setRecipientEmail("test@example.com");
        order.setToDeliveryTime(LocalTime.now());
        orderRepository.save(order);
    }

    @BeforeEach
    void cleanDatabase() {
        entityManager.createNativeQuery("TRUNCATE TABLE orders RESTART IDENTITY CASCADE").executeUpdate();
    }

    @Test
    public void OrderRepository_FindAll_ReturnsOrderList() {
        List<Order> orderList = orderRepository.findAll();

        assertEquals(1, orderList.size());
        assertEquals(1, orderList.get(0).getId());
    }

    @Test
    public void OrderRepository_FindByStatus_ReturnsOrderList() {
        List<Order> orderList = orderRepository.findByStatus("Оформлен");

        assertEquals(1, orderList.size());
        assertEquals(1, orderList.get(0).getId());
    }
}
