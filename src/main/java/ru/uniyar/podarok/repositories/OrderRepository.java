package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.entities.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);
}
