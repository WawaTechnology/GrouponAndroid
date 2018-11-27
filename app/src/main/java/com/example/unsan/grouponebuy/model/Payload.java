package com.example.unsan.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Payload implements Serializable {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("category")
    private String category;
    @JsonProperty("product")
    private Product product;
    @JsonProperty("image_ch")
    private String imageCh;
    @JsonProperty("image_en")
    private String imageEn;

    public String getImageCh() {
        return imageCh;
    }

    public void setImageCh(String imageCh) {
        this.imageCh = imageCh;
    }

    public String getImageEn() {
        return imageEn;
    }

    public void setImageEn(String imageEn) {
        this.imageEn = imageEn;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }



    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("product")
    public Product getProduct() {
        return product;
    }

    @JsonProperty("product")
    public void setProduct(Product product) {
        this.product = product;
    }


}
