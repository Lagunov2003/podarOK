package ru.uniyar.podarok.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.entities.Survey;
import ru.uniyar.podarok.repositories.projections.GiftProjection;
import ru.uniyar.podarok.entities.Gift;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface GiftRepository extends JpaRepository<Gift, Long> {
    @Query("SELECT g FROM Gift g")
    Page<GiftProjection> findAllGifts(Pageable pageable);

    @Query(value = "SELECT DISTINCT g.* " +
            "FROM gift g " +
            "JOIN gift_recommendations gr ON g.recommendation_id = gr.id " +
            "JOIN gift_occasion goc ON goc.gift_id = g.id " +
            "JOIN gift_category gc ON gc.gift_id = g.id " +
            "JOIN survey_category sc ON sc.category_id = gc.category_id " +
            "WHERE sc.survey_id = :surveyId " +
            "AND (:budget IS NULL OR :budget >= g.price) " +
            "AND (:gender IS NULL OR :gender = gr.gender) " +
            "AND (:age IS NULL OR :age >= gr.min_age) " +
            "AND (:age IS NULL OR :age <= gr.max_age) " +
            "AND (:urgency IS NULL OR :urgency = gr.urgency) " +
            "AND (:occasionId IS NULL OR :occasionId = goc.occasion_id)",
            nativeQuery = true)
    Page<GiftProjection> findGiftsBySurvey(
            @Param("surveyId") Long surveyId,
            @Param("budget") BigDecimal budget,
            @Param("gender") Boolean gender,
            @Param("age") Integer age,
            @Param("urgency") Boolean urgency,
            @Param("occasionId") Long occasionId,
            Pageable pageable
    );

    @Query(value = "SELECT DISTINCT g.* " +
            "FROM gift g " +
            "JOIN gift_recommendations gr ON g.recommendation_id = gr.id " +
            "JOIN gift_occasion goc ON goc.gift_id = g.id " +
            "JOIN gift_category gc ON gc.gift_id = g.id " +
            "WHERE (:budget IS NULL OR g.price <= :budget) " +
            "AND (:gender IS NULL OR gr.gender = :gender) " +
            "AND (:age IS NULL OR gr.min_age <= :age) " +
            "AND (:age IS NULL OR gr.max_age >= :age) " +
            "AND (:urgency IS NULL OR gr.urgency = :urgency) " +
            "AND (:categories IS NULL OR gc.category_id IN :categories) " +
            "AND (:occasions IS NULL OR goc.occasion_id IN :occasions)",
            nativeQuery = true)
    Page<GiftProjection> findGiftsByFilter(
            @Param("budget") BigDecimal budget,
            @Param("gender") Boolean gender,
            @Param("age") Integer age,
            @Param("urgency") Boolean urgency,
            @Param("categories") List<Long> categories,
            @Param("occasions") List<Long> occasions,
            Pageable pageable
    );

    Optional<Gift> findById(Long id);
}
