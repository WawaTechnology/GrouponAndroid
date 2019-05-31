package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Payload2 implements Serializable {
    @JsonProperty("maxCount")
    Integer maxCount;
    @JsonProperty("_id")
    String id;
    @JsonProperty("shippingDateList")
    List<ShippingDate> shippingDateList;

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ShippingDate> getShippingDateList() {
        return shippingDateList;
    }

    public void setShippingDateList(List<ShippingDate> shippingDateList) {
        this.shippingDateList = shippingDateList;
    }
}
