package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)


public class ProductInfo implements Serializable {

    @JsonProperty("productID")
    private String productID;
    @JsonProperty("name_ch")
    private String nameCh;
    @JsonProperty("name_en")
    private String nameEn;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("unit_ch")
    private String unitCh;
    @JsonProperty("unit_en")
    private String unitEn;
    @JsonProperty("category")
    private String category;
    @JsonProperty("imageCover")
    private String imageCover;

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @JsonProperty("specification_ch")

    private String specificationCh;
    @JsonProperty("specification_en")
    private String specificationEn;
    @JsonProperty("stock")

    private Integer stock;

    @JsonProperty("productID")
    public String getProductID() {
        return productID;
    }

    @JsonProperty("productID")
    public void setProductID(String productID) {
        this.productID = productID;
    }

    @JsonProperty("name_ch")
    public String getNameCh() {
        return nameCh;
    }

    @JsonProperty("name_ch")
    public void setNameCh(String nameCh) {
        this.nameCh = nameCh;
    }

    @JsonProperty("name_en")
    public String getNameEn() {
        return nameEn;
    }

    @JsonProperty("name_en")
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @JsonProperty("price")
    public Double getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Double price) {
        this.price = price;
    }

    @JsonProperty("unit_ch")
    public String getUnitCh() {
        return unitCh;
    }

    @JsonProperty("unit_ch")
    public void setUnitCh(String unitCh) {
        this.unitCh = unitCh;
    }

    @JsonProperty("unit_en")
    public String getUnitEn() {
        return unitEn;
    }

    @JsonProperty("unit_en")
    public void setUnitEn(String unitEn) {
        this.unitEn = unitEn;
    }

    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("imageCover")
    public String getImageCover() {
        return imageCover;
    }

    @JsonProperty("imageCover")
    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }

    @JsonProperty("specification_ch")
    public String getSpecificationCh() {
        return specificationCh;
    }

    @JsonProperty("specification_ch")
    public void setSpecificationCh(String specificationCh) {
        this.specificationCh = specificationCh;
    }

    @JsonProperty("specification_en")
    public String getSpecificationEn() {
        return specificationEn;
    }

    @JsonProperty("specification_en")
    public void setSpecificationEn(String specificationEn) {
        this.specificationEn = specificationEn;
    }

}
