package com.example.unsan.grouponebuy.model;

import java.io.Serializable;

public class ProductStock implements Serializable {

    String productName;
    int stock;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    public ProductStock(String productName,int stock)
    {
        this.productName=productName;
        this.stock=stock;
    }
}
