package ru.uniyar.podarok.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.uniyar.podarok.entities.Role;
import ru.uniyar.podarok.repositories.RoleRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    public void getUserRole_Success_whenRoleExist() {
        Role role = new Role();
        role.setName("ROLE_USER");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));

        Role result = roleService.getUserRole();
        assertEquals("ROLE_USER", result.getName(), "Должна возвращаться роль с именем ROLE_USER");
    }

    @Test
    public void getUserRole_roleNotFound_whenRoleDoesNotExist() {
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> roleService.getUserRole(),
                "Если роль не найдена, должно выбрасываться исключение NoSuchElementException");
    }
}
