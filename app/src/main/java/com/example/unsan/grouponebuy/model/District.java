package com.example.unsan.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)

public class District {


 /*   @JsonProperty("dateCreate")
    private String dateCreate;
    */
    @JsonProperty("cycle")
    private List<Cycle> cycle = null;

    public List<Cycle> getCycle() {
        return cycle;
    }

    public void setCycle(List<Cycle> cycle) {
        this.cycle = cycle;
    }


    @JsonProperty("state")
    private Integer state;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("namePrimary_ch")
    private String namePrimaryCh;
    @JsonProperty("namePrimary_en")
    private String namePrimaryEn;
    @JsonProperty("nameSecondary_ch")
    private String nameSecondaryCh;
    @JsonProperty("nameSecondary_en")
    private String nameSecondaryEn;
    @JsonProperty("nameTertiary_ch")
    private String nameTertiaryCh;
    @JsonProperty("nameTertiary_en")
    private String nameTertiaryEn;
   /* @JsonProperty("description_ch")
    private String descriptionCh;
    @JsonProperty("description_en")
    private String descriptionEn;
    */
    @JsonProperty("postcode")
    private String postcode;








    @JsonProperty("state")
    public Integer getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(Integer state) {
        this.state = state;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("namePrimary_ch")
    public String getNamePrimaryCh() {
        return namePrimaryCh;
    }

    @JsonProperty("namePrimary_ch")
    public void setNamePrimaryCh(String namePrimaryCh) {
        this.namePrimaryCh = namePrimaryCh;
    }

    @JsonProperty("namePrimary_en")
    public String getNamePrimaryEn() {
        return namePrimaryEn;
    }

    @JsonProperty("namePrimary_en")
    public void setNamePrimaryEn(String namePrimaryEn) {
        this.namePrimaryEn = namePrimaryEn;
    }

    @JsonProperty("nameSecondary_ch")
    public String getNameSecondaryCh() {
        return nameSecondaryCh;
    }

    @JsonProperty("nameSecondary_ch")
    public void setNameSecondaryCh(String nameSecondaryCh) {
        this.nameSecondaryCh = nameSecondaryCh;
    }

    @JsonProperty("nameSecondary_en")
    public String getNameSecondaryEn() {
        return nameSecondaryEn;
    }

    @JsonProperty("nameSecondary_en")
    public void setNameSecondaryEn(String nameSecondaryEn) {
        this.nameSecondaryEn = nameSecondaryEn;
    }

    @JsonProperty("nameTertiary_ch")
    public String getNameTertiaryCh() {
        return nameTertiaryCh;
    }

    @JsonProperty("nameTertiary_ch")
    public void setNameTertiaryCh(String nameTertiaryCh) {
        this.nameTertiaryCh = nameTertiaryCh;
    }

    @JsonProperty("nameTertiary_en")
    public String getNameTertiaryEn() {
        return nameTertiaryEn;
    }

    @JsonProperty("nameTertiary_en")
    public void setNameTertiaryEn(String nameTertiaryEn) {
        this.nameTertiaryEn = nameTertiaryEn;
    }



    @JsonProperty("postcode")
    public String getPostcode() {
        return postcode;
    }

    @JsonProperty("postcode")
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }




}