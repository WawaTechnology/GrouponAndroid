package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class CategoryS implements Serializable {


    @JsonProperty("colors")
    private List<String> colors = null;
    @JsonProperty("productList")
    private List<String> productList = null;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("name_ch")
    private String nameCh;
    @JsonProperty("name_en")
    private String nameEn;
    @JsonProperty("sequence")
    private Integer sequence;
    @JsonProperty("image")
    private String image;
    @JsonProperty("imageCorner")
    private String imageCorner;
    @JsonProperty("__v")
    private Integer v;

    @JsonProperty("colors")
    public List<String> getColors() {
        return colors;
    }

    @JsonProperty("colors")
    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    @JsonProperty("productList")
    public List<String> getProductList() {
        return productList;
    }

    @JsonProperty("productList")
    public void setProductList(List<String> productList) {
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

    @JsonProperty("sequence")
    public Integer getSequence() {
        return sequence;
    }

    @JsonProperty("sequence")
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }

    @JsonProperty("imageCorner")
    public String getImageCorner() {
        return imageCorner;
    }

    @JsonProperty("imageCorner")
    public void setImageCorner(String imageCorner) {
        this.imageCorner = imageCorner;
    }

    @JsonProperty("__v")
    public Integer getV() {
        return v;
    }

    @JsonProperty("__v")
    public void setV(Integer v) {
        this.v = v;
    }



}
