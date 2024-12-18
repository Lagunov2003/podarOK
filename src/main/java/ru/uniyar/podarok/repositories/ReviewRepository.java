package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.uniyar.podarok.entities.Review;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Review}.
 * Предоставляет методы для выполнения операций с отзывами о подарках.
 * Наследуется от {@link JpaRepository}, что обеспечивает базовые CRUD-операции.
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {
    /**
     * Находит средний рейтинг для подарка по его идентификатору.
     *
     * @param giftId идентификатор подарка.
     * @return средний рейтинг для подарка.
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.gift.id = :giftId")
    Double findAverageRatingByGiftId(@Param("giftId") Long giftId);

    /**
     * Находит количество отзывов для подарка по его идентификатору.
     *
     * @param giftId идентификатор подарка.
     * @return количество отзывов для подарка.
     */
    @Query("SELECT COUNT(r) FROM Review r WHERE r.gift.id = :giftId")
    Long countReviewsAmountById(@Param("giftId") Long giftId);


    /**
     * Находит все отзывы о подарке по его идентификатору.
     *
     * @param giftId идентификатор подарка.
     * @return список отзывов о подарке {@link Review}.
     */
    List<Review> findReviewsByGiftId(Long giftId);
}
