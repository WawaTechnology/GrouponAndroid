package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Address implements Serializable {
    @JsonProperty("district")
    DistrictAddress district;
    @JsonProperty("unit")
    String unit;

    public DistrictAddress getDistrict() {
        return district;
    }

    public void setDistrict(DistrictAddress district) {
        this.district = district;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
