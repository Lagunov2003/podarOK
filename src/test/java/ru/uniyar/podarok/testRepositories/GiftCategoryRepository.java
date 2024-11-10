package ru.uniyar.podarok.testRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.testEntities.GiftCategory;
import ru.uniyar.podarok.testEntities.GiftCategoryId;

public interface GiftCategoryRepository extends JpaRepository<GiftCategory, GiftCategoryId> {
}
