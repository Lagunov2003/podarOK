package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.entities.User;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link User}.
 * Предоставляет методы для выполнения операций с пользователями в базе данных.
 * Наследуется от {@link JpaRepository}, что обеспечивает базовые CRUD-операции.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Находит пользователя по его email.
     *
     * @param email электронная почта пользователя.
     * @return {@link Optional}, содержащий объект {@link User}, если пользователь найден,
     * или пустое значение, если пользователь с указанным email отсутствует.
     */
    Optional<User> findUserByEmail(String email);
}
