package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)

@JsonInclude(JsonInclude.Include.NON_NULL)

public class CategorySpecial implements Serializable {

    @JsonProperty("productList")
    private List<Product> productList = null;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("name_ch")
    private String nameCh;
    @JsonProperty("imageCorner")
    private String imageCorner;

    public String getImageCorner() {
        return imageCorner;
    }

    public void setImageCorner(String imageCorner) {
        this.imageCorner = imageCorner;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @JsonProperty("sequence")
    private Integer sequence;

    public List<String> getColorList() {
        return colorList;
    }

    public void setColorList(List<String> colorList) {
        this.colorList = colorList;
    }

    @JsonProperty("name_en")
    private String nameEn;
    @JsonProperty("image")
    private String image;
    @JsonProperty("colors")
    private List<String> colorList;
    public CategorySpecial()
    {

    }



    @JsonProperty("productList")
    public List<Product> getProductList() {
        return productList;
    }

    @JsonProperty("productList")
    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
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

    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }





}