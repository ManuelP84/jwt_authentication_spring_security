package com.springsecurity.jwtAuthentication.service;

import com.springsecurity.jwtAuthentication.entity.product.Product;
import com.springsecurity.jwtAuthentication.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final IProductRepository productRepository;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product saveProduct(Product product){
        return productRepository.save(product);
    }
}
