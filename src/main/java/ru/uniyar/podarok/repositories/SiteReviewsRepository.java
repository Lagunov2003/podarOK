package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.entities.SiteReviews;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link SiteReviews}.
 * Предоставляет методы для выполнения операций с отзывами о сайте.
 * Наследуется от {@link JpaRepository}, что обеспечивает базовые CRUD-операции.
 */
public interface SiteReviewsRepository extends JpaRepository<SiteReviews, Long> {
    /**
     * Находит все отзывы о сайте с утвержденным статусом.
     *
     * @return список утвержденных отзывов {@link SiteReviews}.
     */
    List<SiteReviews> findByAcceptedTrue();

    /**
     * Находит все отзывы о сайте с неутвержденным статусом.
     *
     * @return список неутвержденных отзывов {@link SiteReviews}.
     */
    List<SiteReviews> findByAcceptedFalse();

    /**
     * Находит первые 6 отзывов о сайте с утвержденным статусом.
     *
     * @return список из 6 утвержденных отзывов {@link SiteReviews}.
     */
    List<SiteReviews> findTop6ByAcceptedTrue();
}
