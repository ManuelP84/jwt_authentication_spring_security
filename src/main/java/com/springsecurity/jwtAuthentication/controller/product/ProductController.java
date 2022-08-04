package com.springsecurity.jwtAuthentication.controller.product;

import com.springsecurity.jwtAuthentication.entity.product.Product;
import com.springsecurity.jwtAuthentication.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    @RolesAllowed({"ROLE_ADMIN","ROLE_EDITOR","ROLE_CUSTOMER"})     //Also possible @Secured({"ROLE_ADMIN","ROLE_EDITOR","ROLE_CUSTOMER"})
    public List<Product> listProducts(){
        return productService.getAllProducts();
    }

    @PostMapping
    @RolesAllowed({"ROLE_ADMIN"})       //Also possible @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Product> createProduct(@RequestBody @Valid Product product){
        Product newProduct = productService.saveProduct(product);
        return ResponseEntity
                .created(URI.create("/products/"+newProduct.getId()))
                .body(newProduct);
    }
}
