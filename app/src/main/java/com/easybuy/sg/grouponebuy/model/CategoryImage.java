package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)

public class CategoryImage implements Serializable {
    @JsonProperty("_id")
    private String id;
    @JsonProperty("sequence")
    private Integer sequence;
    @JsonProperty("image_ch")
    private String imageCh;
    @JsonProperty("image_en")
    private String imageEn;


    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("sequence")
    public Integer getSequence() {
        return sequence;
    }

    @JsonProperty("sequence")
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @JsonProperty("image_ch")
    public String getImageCh() {
        return imageCh;
    }

    @JsonProperty("image_ch")
    public void setImageCh(String imageCh) {
        this.imageCh = imageCh;
    }

    @JsonProperty("image_en")
    public String getImageEn() {
        return imageEn;
    }

    @JsonProperty("image_en")
    public void setImageEn(String imageEn) {
        this.imageEn = imageEn;
    }


}
