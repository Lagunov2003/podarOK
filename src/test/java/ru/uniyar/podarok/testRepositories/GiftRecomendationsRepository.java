package ru.uniyar.podarok.testRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.entities.GiftRecommendation;

public interface GiftRecomendationsRepository extends JpaRepository<GiftRecommendation, Long> {
}
