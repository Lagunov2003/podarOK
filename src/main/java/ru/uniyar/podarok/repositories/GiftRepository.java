package ru.uniyar.podarok.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.uniyar.podarok.entities.Gift;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface GiftRepository extends JpaRepository<Gift, Long> {
    @Query("SELECT DISTINCT g FROM Gift g " +
            "LEFT JOIN FETCH g.photos photos")
    Page<Gift> findAllGifts(Pageable pageable);

    @Query("SELECT DISTINCT g FROM Gift g " +
            "LEFT JOIN FETCH g.photos photos " +
            "LEFT JOIN FETCH g.recommendation rec " +
            "LEFT JOIN FETCH g.categories categories " +
            "LEFT JOIN FETCH g.occasions occasions " +
            "WHERE (:budget IS NULL OR g.price <= :budget) " +
            "AND (:gender IS NULL OR rec.gender = :gender) " +
            "AND (:age IS NULL OR (rec.minAge <= :age AND rec.maxAge >= :age)) " +
            "AND (:categories IS NULL OR categories.id IN :categories) " +
            "AND (:occasions IS NULL OR occasions.id IN :occasions)")
    Page<Gift> findGiftsByFilter(
            @Param("budget") BigDecimal budget,
            @Param("gender") Boolean gender,
            @Param("age") Integer age,
            @Param("categories") List<Long> categories,
            @Param("occasions") List<Long> occasions,
            Pageable pageable
    );

    Optional<Gift> findById(Long id);

    @Query("SELECT g FROM Gift g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Gift> findGiftsByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT DISTINCT g FROM Gift g " +
            "LEFT JOIN FETCH g.photos photos " +
            "LEFT JOIN FETCH g.categories categories " +
            "LEFT JOIN FETCH g.occasions occasions " +
            "WHERE (:categories IS NULL OR categories.id IN :categories) " +
            "OR (:occasions IS NULL OR occasions.id IN :occasions)")
    List<Gift> findGiftsByCategoriesOrOccasions(
            @Param("categories") List<Long> categories,
            @Param("occasions") List<Long> occasions
    );

    @Query("SELECT g FROM Gift g WHERE g.giftGroup.id = :groupId")
    List<Gift> findGiftsByGroupId(@Param("groupId") Long groupId);
}
