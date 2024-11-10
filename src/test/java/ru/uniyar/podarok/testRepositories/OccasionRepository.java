package ru.uniyar.podarok.testRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.entities.Occasion;

public interface OccasionRepository extends JpaRepository<Occasion, Long> {
}
