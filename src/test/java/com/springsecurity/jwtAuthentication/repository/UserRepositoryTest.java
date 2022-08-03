package com.springsecurity.jwtAuthentication.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.springsecurity.jwtAuthentication.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {
    @Autowired
    private IUserRepository userRepository;

    @Test
    public void testCreateUser(){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "1234";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User newUser = new User("manuel@gmail.com", encodedPassword);

        User saveUser = userRepository.save(newUser);

        assertThat(saveUser).isNotNull();
        assertThat(saveUser.getId()).isGreaterThan(0);
    }
}