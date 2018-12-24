package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class CategoryProduct implements Serializable {






        @JsonProperty("categorySpecial")
        private List<String> categorySpecial = null;





        @JsonProperty("_id")
        private String id;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("name_ch")
    private String nameCh;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getNameCh() {
        return nameCh;
    }

    public void setNameCh(String nameCh) {
        this.nameCh = nameCh;
    }

    public String getSpecificationCh() {
        return specificationCh;
    }

    public void setSpecificationCh(String specificationCh) {
        this.specificationCh = specificationCh;
    }

    public String getSpecificationEn() {
        return specificationEn;
    }

    public void setSpecificationEn(String specificationEn) {
        this.specificationEn = specificationEn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @JsonProperty("specification_ch")

    private String specificationCh;
    @JsonProperty("specification_en")
    private String specificationEn;
    @JsonProperty("name_en")
    private String nameEn;






        @JsonProperty("categorySpecial")
        public List<String> getCategorySpecial() {
            return categorySpecial;
        }

        @JsonProperty("categorySpecial")
        public void setCategorySpecial(List<String> categorySpecial) {
            this.categorySpecial = categorySpecial;
        }








        @JsonProperty("_id")
        public String getId() {
            return id;
        }

        @JsonProperty("_id")
        public void setId(String id) {
            this.id = id;
        }





}
