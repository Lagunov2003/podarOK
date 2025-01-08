package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.uniyar.podarok.entities.Cart;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью Cart.
 * Предназначен для работы с корзиной покупок пользователя, включая поиск товаров в корзине, удаление и т.д.
 */
public interface CartRepository extends JpaRepository<Cart, Long> {
    /**
     * Метод для поиска элемента в корзине по идентификатору подарка.
     *
     * @param giftId идентификатор подарка.
     * @return Optional с найденным элементом корзины или пустой Optional, если элемент не найден.
     */
    @Query(value = "SELECT DISTINCT c.* "
            + "FROM cart c "
            + "WHERE gift_id = :giftId ",
            nativeQuery = true)
    Optional<Cart> findItemByGiftId(@Param("giftId") Long giftId);

    /**
     * Метод для поиска элемента в корзине по идентификатору подарка и пользователя.
     *
     * @param giftId идентификатор подарка.
     * @param userId идентификатор пользователя.
     * @return Optional с найденным элементом корзины или пустой Optional, если элемент не найден.
     */
    @Query(value = "SELECT c.* "
            + "FROM cart c "
            + "WHERE gift_id = :giftId "
            + "AND user_id = :userId ",
            nativeQuery = true)
    Optional<Cart> findItemByGiftIdAndUserId(@Param("giftId") Long giftId, @Param("userId") Long userId);

    /**
     * Метод для удаления всех элементов в корзине для данного пользователя.
     *
     * @param userId идентификатор пользователя, чьи элементы нужно удалить из корзины.
     */
    @Modifying
    @Query(value = "DELETE FROM cart c WHERE c.user_id = :userId", nativeQuery = true)
    void deleteAllByUserId(@Param("userId") Long userId);
}
