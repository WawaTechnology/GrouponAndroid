package com.easybuy.sg.grouponebuy.adapter;

import com.easybuy.sg.grouponebuy.model.CategoryProduct;
import com.easybuy.sg.grouponebuy.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

@JsonInclude(JsonInclude.Include.NON_NULL)

public class SpecialCategory implements Serializable {

    @JsonProperty("productList")
    private List<CategoryProduct> productList = null;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("name_ch")
    private String nameCh;

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @JsonProperty("sequence")
    private Integer sequence;



    @JsonProperty("name_en")
    private String nameEn;


    public SpecialCategory()
    {

    }



    @JsonProperty("productList")
    public List<CategoryProduct> getProductList() {
        return productList;
    }

    @JsonProperty("productList")
    public void setProductList(List<CategoryProduct> productList) {
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
