package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ShippingDate implements Serializable {
    @JsonProperty("_id")
    private String id;
    @JsonProperty("shippingDate")
   private String shippingDate;
    @JsonProperty("currentCount")
   private Integer currentCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(String shippingDate) {
        this.shippingDate = shippingDate;
    }

    public Integer getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(Integer currentCount) {
        this.currentCount = currentCount;
    }
}
