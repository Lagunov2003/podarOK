package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.uniyar.podarok.entities.Order;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Order}.
 * Предоставляет методы для выполнения операций с заказами.
 * Наследуется от {@link JpaRepository}, что обеспечивает базовые CRUD-операции.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Находит все заказы с их связанными объектами GiftOrder и Gift.
     *
     * @return список заказов с включенными информацией о подарках GiftOrder и Gift.
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.giftOrders go LEFT JOIN FETCH go.gift")
    List<Order> findAll();

    /**
     * Находит все заказы по статусу с их связанными объектами GiftOrder и Gift.
     *
     * @param status статус заказа.
     * @return список заказов с указанным статусом, включая информацию о подарках GiftOrder и Gift.
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.giftOrders go LEFT JOIN FETCH go.gift WHERE o.status = :status")
    List<Order> findByStatus(String status);
}
