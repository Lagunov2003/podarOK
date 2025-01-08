package ru.uniyar.podarok.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.uniyar.podarok.entities.Role;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(3L);
        role.setName("ROLE_EXAMPLE");
    }
    @Test
    void RoleRepository_FindByName_ReturnFoundRole(){
        roleRepository.save(role);
        Role role2 = new Role();
        role2.setId(4L);
        role2.setName("ROLE_TEST");
        roleRepository.save(role2);

        Role foundRole = roleRepository.findByName("ROLE_EXAMPLE").get();

        assertThat(foundRole).isNotNull();
        assertEquals("ROLE_EXAMPLE", foundRole.getName());

    }
}
