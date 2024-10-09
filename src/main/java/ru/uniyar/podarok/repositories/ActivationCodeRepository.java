package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.entities.ActivationCodeEntity;

public interface ActivationCodeRepository extends JpaRepository<ActivationCodeEntity, Long> {
    ActivationCodeEntity findByUserId(Long userId);
}
