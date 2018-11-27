package com.example.unsan.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Layer implements Serializable {
    @JsonProperty("productList")
    private List<String> productList = null;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("name_ch")
    private String nameCh;
    @JsonProperty("name_en")
    private String nameEn;

    public String imageTitleCh;
    public String imageTitleEn;

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    @JsonProperty("bgColor")

    private String bgColor;
    @JsonProperty("color")
    private String color;
    @JsonProperty("display")
    private Integer display;
    @JsonProperty("layer")
    private Integer layer;


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




}
