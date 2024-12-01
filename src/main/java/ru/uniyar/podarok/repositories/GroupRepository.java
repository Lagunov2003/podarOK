package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.uniyar.podarok.entities.GiftGroup;

public interface GroupRepository extends JpaRepository<GiftGroup, Long> {
    @Modifying
    @Query(value = "INSERT INTO gift_group(id) VALUES(:id) ", nativeQuery = true)
    void addGroup(@Param("id") Long id);
}
