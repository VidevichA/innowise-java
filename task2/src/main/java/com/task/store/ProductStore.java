package com.task.store;

import java.util.Arrays;
import java.util.List;

import com.task.entity.Product;

public class ProductStore {
    private ProductStore(){};

    public static List<Product> getProducts() {
        return Arrays.asList(
            new Product("Jeans", 59.99),
            new Product("Running Shoes", 79.99),
            new Product("Backpack", 39.99),
            new Product("T-Shirt", 19.99),
            new Product("Smartphone", 699.99),
            new Product("Headphones", 99.99),
            new Product("Watch", 199.99),
            new Product("Laptop", 999.99),
            new Product("Camera", 499.99),
            new Product("Gaming Console", 399.99),
            new Product("Fitness Tracker", 89.99),
            new Product("Sunglasses", 29.99),
            new Product("Skincare Set", 49.99),
            new Product("Coffee Maker", 79.99),
            new Product("Bluetooth Speaker", 69.99));
    }
}
