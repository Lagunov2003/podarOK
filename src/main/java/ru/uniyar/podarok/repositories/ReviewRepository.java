package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.uniyar.podarok.entities.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.gift.id = :giftId")
    Double findAverageRatingByGiftId(@Param("giftId") Long giftId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.gift.id = :giftId")
    Long countReviewsAmountById(@Param("giftId") Long giftId);

    List<Review> findReviewsByGiftId(Long giftId);
}
