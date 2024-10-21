package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.entities.Role;
import ru.uniyar.podarok.repositories.RoleRepository;

@Service
@AllArgsConstructor
public class RoleService {
    private RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }
}
