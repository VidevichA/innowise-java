package com.task.entity;

public class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.price + " $)\n";
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

}
