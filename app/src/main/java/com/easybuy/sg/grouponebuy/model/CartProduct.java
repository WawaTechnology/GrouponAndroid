package com.easybuy.sg.grouponebuy.model;

public class CartProduct {
    boolean check;
    Product product;


    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    public CartProduct(Product product,boolean check)
    {
        this.product=product;
        this.check=check;
    }
}
