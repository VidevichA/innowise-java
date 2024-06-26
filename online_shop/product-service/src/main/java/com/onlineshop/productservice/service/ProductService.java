package com.onlineshop.productservice.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.onlineshop.productservice.dto.ProductRequest;
import com.onlineshop.productservice.dto.ProductResponse;
import com.onlineshop.productservice.model.Product;
import com.onlineshop.productservice.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;

    public Set<ProductResponse> getAllProducts() {
        List<Product> products = (List<Product>) productRepository.findAll();
        return products.stream().map(this::convertToResponse).collect(Collectors.toSet());
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        return product == null ? null : convertToResponse(product);
    }

    public void createProduct(ProductRequest productDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .imageUrl(productDto.getImageUrl())
                .build();
        productRepository.save(product);
    }

    public void updateProduct(Long id, ProductRequest productDto) {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        if (productDto.getName() != null) {
            product.setName(productDto.getName());
        }
        if (productDto.getDescription() != null) {
            product.setDescription(productDto.getDescription());
        }
        if (productDto.getPrice() != null) {
            product.setPrice(productDto.getPrice());
        }
        if (productDto.getImageUrl() != null) {
            product.setImageUrl(productDto.getImageUrl());
        }
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            productRepository.delete(product);
        }
    }

    private ProductResponse convertToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .build();
    }
}
