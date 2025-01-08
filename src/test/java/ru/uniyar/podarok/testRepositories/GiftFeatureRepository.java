package ru.uniyar.podarok.testRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.entities.GiftFeature;

public interface GiftFeatureRepository extends JpaRepository<GiftFeature, Long> {
}
