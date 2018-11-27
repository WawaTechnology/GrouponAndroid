package com.example.unsan.grouponebuy.model;

public class ProductImageId {

    String category;
    String productCover;
    Product product;
    int viewType;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getCategory() {
        return category;
    }

    public ProductImageId(String category, String productCover,Product product,int viewType) {
        this.category = category;
        this.productCover = productCover;
        this.product=product;
        this.viewType=viewType;
    }

    public void setCategory(String category) {
        this.category = category;

    }

    public String getProductCover() {
        return productCover;
    }

    public void setProductCover(String productCover) {
        this.productCover = productCover;
    }
}
