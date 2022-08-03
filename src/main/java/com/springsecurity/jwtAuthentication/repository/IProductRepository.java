package com.springsecurity.jwtAuthentication.repository;

import com.springsecurity.jwtAuthentication.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {
}
