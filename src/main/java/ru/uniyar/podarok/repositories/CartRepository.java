package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.uniyar.podarok.entities.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query(value = "SELECT DISTINCT c.* " +
            "FROM cart c " +
            "WHERE gift_id = :giftId ",
            nativeQuery = true)
    Optional<Cart> findItemByGiftId(
            @Param("giftId") Long surveyId
    );
}
