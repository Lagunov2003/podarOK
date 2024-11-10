package ru.uniyar.podarok.testRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.testEntities.GiftCategory;
import ru.uniyar.podarok.testEntities.GiftOccasion;
import ru.uniyar.podarok.testEntities.GiftOccasionId;

public interface GiftOccasionRepository extends JpaRepository<GiftOccasion, GiftOccasionId> {
}
