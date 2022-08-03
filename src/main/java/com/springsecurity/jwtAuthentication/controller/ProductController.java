package com.springsecurity.jwtAuthentication.controller;

import com.springsecurity.jwtAuthentication.entity.Product;
import com.springsecurity.jwtAuthentication.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public List<Product> listProducts(){
        return productService.getAllProducts();
    }
}
