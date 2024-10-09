package ru.uniyar.podarok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
}
