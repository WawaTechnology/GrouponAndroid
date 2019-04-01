package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductOrderList implements Serializable {

    @JsonProperty("productInfo")
    private ProductInfo productInfo;
    @JsonProperty("quantityActual")
    private Double quantityActual;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("productInfo")
    public ProductInfo getProductInfo() {
        return productInfo;
    }

    @JsonProperty("productInfo")
    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    @JsonProperty("quantityActual")
    public Double getQuantityActual() {
        return quantityActual;
    }

    @JsonProperty("quantityActual")
    public void setQuantityActual(Double quantityActual) {
        this.quantityActual = quantityActual;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("quantity")
    public Integer getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
