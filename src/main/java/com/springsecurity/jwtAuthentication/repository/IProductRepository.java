package com.springsecurity.jwtAuthentication.repository;

import com.springsecurity.jwtAuthentication.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductRepository extends JpaRepository<Product, Integer> {
}
