package com.example.oh_nat_foods_order;

public class Product {
    private String productName;
    private int quantity;

    // Constructor
    public Product(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    // Getter and setter for productName
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // Getter and setter for quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}