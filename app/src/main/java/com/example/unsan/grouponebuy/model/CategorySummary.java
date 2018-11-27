package com.example.unsan.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CategorySummary implements Serializable {
    private String id;

    private String nameCh;

    private String nameEn;

    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameCh() {
        return nameCh;
    }

    public void setNameCh(String nameCh) {
        this.nameCh = nameCh;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public CategorySummary()
    {

    }

    public CategorySummary(String id, String nameCh, String nameEn, String image) {
        this.id = id;
        this.nameCh = nameCh;
        this.nameEn = nameEn;
        this.image = image;
    }
}
