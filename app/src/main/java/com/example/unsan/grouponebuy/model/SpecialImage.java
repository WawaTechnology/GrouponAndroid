package com.example.unsan.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class SpecialImage implements Serializable {
    @JsonProperty("imageEntry_en")
    private String imageEntryEn;
    @JsonProperty("name_ch")
    private String nameCh;
    @JsonProperty("name_en")
    private String nameEn;

    public Double getImageHeaderSize() {
        return imageHeaderSize;
    }

    public void setImageHeaderSize(Double imageHeaderSize) {
        this.imageHeaderSize = imageHeaderSize;
    }

    @JsonProperty("imageHeaderSize")

    private Double imageHeaderSize;

    public String getImageHeaderCh() {
        return imageHeaderCh;
    }

    public void setImageHeaderCh(String imageHeaderCh) {
        this.imageHeaderCh = imageHeaderCh;
    }

    public String getImageHeaderEn() {
        return imageHeaderEn;
    }

    public void setImageHeaderEn(String imageHeaderEn) {
        this.imageHeaderEn = imageHeaderEn;
    }

    @JsonProperty("imageHeader_ch")

    private String imageHeaderCh;
    @JsonProperty("imageHeader_en")
    private String imageHeaderEn;

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

    public List<Layer> getLayers() {
        return layers;
    }

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    @JsonProperty("layers")
    private List<Layer> layers = null;

    public String getImageEntryEn() {
        return imageEntryEn;
    }

    public void setImageEntryEn(String imageEntryEn) {
        this.imageEntryEn = imageEntryEn;
    }

    public String getImageEntryCh() {
        return imageEntryCh;
    }

    public void setImageEntryCh(String imageEntryCh) {
        this.imageEntryCh = imageEntryCh;
    }

    @JsonProperty("imageEntry_ch")

    private String imageEntryCh;
}
