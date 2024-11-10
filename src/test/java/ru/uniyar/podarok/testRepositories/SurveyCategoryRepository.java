package ru.uniyar.podarok.testRepositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.testEntities.SurveyCategory;
import ru.uniyar.podarok.testEntities.SurveyCategoryId;

public interface SurveyCategoryRepository extends JpaRepository<SurveyCategory, SurveyCategoryId> {
}
