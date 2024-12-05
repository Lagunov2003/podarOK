package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.uniyar.podarok.entities.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.giftOrders go LEFT JOIN FETCH go.gift")
    List<Order> findAll();
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.giftOrders go LEFT JOIN FETCH go.gift WHERE o.status = :status")
    List<Order> findByStatus(String status);
}
