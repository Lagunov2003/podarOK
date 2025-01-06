package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.uniyar.podarok.entities.GiftOrder;

import java.util.Set;

/**
 * Репозиторий для работы с сущностью {@link GiftOrder}.
 * Предоставляет методы для выполнения операций с заказами подарков.
 * Наследуется от {@link JpaRepository}, что обеспечивает базовые CRUD-операции.
 */
public interface GiftOrderRepository extends JpaRepository<GiftOrder, Long> {
    /**
     * Находит все заказы подарков по идентификатору заказа.
     *
     * @param orderId идентификатор заказа.
     * @return множество заказов подарков {@link GiftOrder} для указанного заказа.
     */
    @Query("SELECT go FROM GiftOrder go JOIN FETCH go.gift WHERE go.order.id = :orderId")
    Set<GiftOrder> findGiftOrdersByOrderId(@Param("orderId") Long orderId);
}
