package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    // TODO
    // Убрать данный метод
    User findByEmail(String email);
}
