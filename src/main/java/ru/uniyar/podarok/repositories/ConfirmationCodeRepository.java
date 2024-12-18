package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.entities.ConfirmationCode;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью ConfirmationCode.
 * Используется для работы с кодами подтверждения, например, для регистрации пользователя или восстановления пароля.
 */
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {
    /**
     * Метод для поиска кода подтверждения по строковому значению кода.
     *
     * @param code строка кода подтверждения, который необходимо найти.
     * @return объект ConfirmationCode, если код найден, иначе пустой Optional.
     */
    Optional<ConfirmationCode> findByCode(String code);
}
