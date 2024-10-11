package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.entities.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
