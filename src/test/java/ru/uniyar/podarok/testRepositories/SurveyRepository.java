package ru.uniyar.podarok.testRepositories;

import org.springframework.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.entities.Survey;

import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    @NonNull
    Optional<Survey> findById(Long id);
}
