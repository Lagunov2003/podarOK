package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.entities.Role;
import ru.uniyar.podarok.repositories.RoleRepository;

/**
 * Сервис для работы с ролями пользователей.
 */
@Service
@AllArgsConstructor
public class RoleService {
    private RoleRepository roleRepository;

    /**
     * Получает роль пользователя с именем "ROLE_USER".
     *
     * @return объект {@link Role} с ролью пользователя
     */
    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }
}
