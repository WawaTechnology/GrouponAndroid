package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrevOrder implements Serializable {

    @JsonProperty("orderID")
    private String orderID;
    @JsonProperty("shippingDate")
    private String shippingDate;


    @JsonProperty("orderID")
    public String getOrderID() {
        return orderID;
    }

    @JsonProperty("orderID")
    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    @JsonProperty("shippingDate")
    public String getShippingDate() {
        return shippingDate;
    }

    @JsonProperty("shippingDate")
    public void setShippingDate(String shippingDate) {
        this.shippingDate = shippingDate;
    }



}