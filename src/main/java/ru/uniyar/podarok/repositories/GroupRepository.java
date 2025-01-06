package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.uniyar.podarok.entities.GiftGroup;

/**
 * Репозиторий для работы с сущностью {@link GiftGroup}.
 * Предоставляет методы для выполнения операций с группами подарков.
 * Наследуется от {@link JpaRepository}, что обеспечивает базовые CRUD-операции.
 */
public interface GroupRepository extends JpaRepository<GiftGroup, Long> {
    /**
     * Добавляет группу подарков в базу данных.
     *
     * @param id идентификатор группы подарков.
     */
    @Modifying
    @Query(value = "INSERT INTO gift_group(id) VALUES(:id) ", nativeQuery = true)
    void addGroup(@Param("id") Long id);
}
