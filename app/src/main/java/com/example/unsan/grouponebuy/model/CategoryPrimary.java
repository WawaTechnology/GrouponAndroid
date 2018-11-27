package com.example.unsan.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class CategoryPrimary implements Serializable {
    @JsonProperty("categories")
    private List<Category> categories = null;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("name_ch")
    private String nameCh;
    @JsonProperty("name_en")
    private String nameEn;
    @JsonProperty("image")
    private String image;


    @JsonProperty("categories")
    public List<Category> getCategories() {
        return categories;
    }

    @JsonProperty("categories")
    public void setCategories(List<Category> categories) {
        this.categories = categories;
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
