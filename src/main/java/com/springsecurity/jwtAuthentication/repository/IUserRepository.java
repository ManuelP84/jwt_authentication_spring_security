package com.springsecurity.jwtAuthentication.repository;

import com.springsecurity.jwtAuthentication.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
}
