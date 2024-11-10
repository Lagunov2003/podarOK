package ru.uniyar.podarok.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.uniyar.podarok.entities.Cart;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.repositories.projections.GiftProjection;

import java.math.BigDecimal;
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
