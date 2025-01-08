package ru.uniyar.podarok.testRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import ru.uniyar.podarok.entities.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    //@NonNull
    Optional<Category> findById(Long id);
}
