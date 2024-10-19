package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.entities.ConfirmationCode;

import java.util.Optional;

public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {
    Optional<ConfirmationCode> findByCode(String code);
}
