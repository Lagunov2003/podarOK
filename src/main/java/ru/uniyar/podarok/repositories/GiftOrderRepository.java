package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.uniyar.podarok.entities.GiftOrder;

import java.util.Set;

public interface GiftOrderRepository extends JpaRepository<GiftOrder, Long> {
    @Query("SELECT go FROM GiftOrder go JOIN FETCH go.gift WHERE go.order.id = :orderId")
    Set<GiftOrder> findGiftOrdersByOrderId(@Param("orderId") Long orderId);
}
