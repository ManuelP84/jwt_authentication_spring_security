package com.springsecurity.jwtAuthentication.repository;

import com.springsecurity.jwtAuthentication.entity.user.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTest {

    @Autowired
    private IRoleRepository roleRepository;

    @Test
    public void testCreateRole(){
        Role admin = new Role("ROLE_ADMIN");
        Role customer = new Role("ROLE_CUSTOMER");
        Role editor = new Role("ROLE_EDITOR");

        roleRepository.saveAll(List.of(admin, customer, editor));

        Long numberOfRoles = roleRepository.count();
        assertEquals(3, numberOfRoles);
    }

    @Test
    public void testListRoles(){
        List<Role> roles = roleRepository.findAll();
        assertThat(roles.size()).isGreaterThan(0);

        roles.forEach(System.out::println);
    }
}
