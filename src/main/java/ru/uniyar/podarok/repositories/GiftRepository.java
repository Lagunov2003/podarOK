package ru.uniyar.podarok.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.uniyar.podarok.entities.Survey;
import ru.uniyar.podarok.repositories.projections.GiftProjection;
import ru.uniyar.podarok.entities.Gift;

public interface GiftRepository extends JpaRepository<Gift, Long> {
    @Query("SELECT g FROM Gift g")
    Page<GiftProjection> findAllGifts(Pageable pageable);

    @Query(value = "SELECT DISTINCT g.* " +
            "FROM gift g " +
            "JOIN gift_recommendations gr ON g.recommendation_id = gr.id " +
            "JOIN gift_ocassion goc ON goc.gift_id = g.id " +
            "JOIN gift_category gc ON gc.gift_id = g.id " +
            "JOIN survey_category sc ON sc.category_id = gc.category_id " +
            "WHERE sc.survey_id = :#{#survey.id} " +
            "AND (:#{#survey.budget} IS NULL OR :#{#survey.budget} >= g.price) " +
            "AND (:#{#survey.gender} IS NULL OR :#{#survey.gender} = gr.gender) " +
            "AND (:#{#survey.age} IS NULL OR :#{#survey.age} >= gr.min_age) " +
            "AND (:#{#survey.age} IS NULL OR :#{#survey.age} <= gr.max_age) " +
            "AND (:#{#survey.urgency} IS NULL OR :#{#survey.urgency} = gr.urgency) " +
            "AND (:#{#survey.ocassion.id} IS NULL OR :#{#survey.ocassion.id} = goc.ocassion_id)", nativeQuery = true)
    Page<GiftProjection> findGiftsBySurvey(@Param("survey") Survey survey, Pageable pageable);
}
