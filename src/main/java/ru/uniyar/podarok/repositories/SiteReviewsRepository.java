package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.entities.SiteReviews;

import java.util.List;

public interface SiteReviewsRepository extends JpaRepository<SiteReviews, Long> {
    List<SiteReviews> findByAcceptedTrue();
    List<SiteReviews> findByAcceptedFalse();
    List<SiteReviews> findTop6ByAcceptedTrue();
}
