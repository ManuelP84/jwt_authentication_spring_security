package com.springsecurity.jwtAuthentication.repository;

import com.springsecurity.jwtAuthentication.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Integer> {
}
