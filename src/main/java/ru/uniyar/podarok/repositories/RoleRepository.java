package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.entities.Role;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link Role}.
 * Предоставляет методы для выполнения операций с ролями пользователей.
 * Наследуется от {@link JpaRepository}, что обеспечивает базовые CRUD-операции.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Находит роль по имени.
     *
     * @param name имя роли.
     * @return {@link Optional}, содержащий объект {@link Role}, если роль найдена,
     * или пустое значение, если роль с указанным именем отсутствует.
     */
    Optional<Role> findByName(String name);
}
