package br.com.microservices.orchestrated.productvalidationservice.core.controller;

import br.com.microservices.orchestrated.productvalidationservice.core.model.Product;
import br.com.microservices.orchestrated.productvalidationservice.core.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping
    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
