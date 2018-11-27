package com.example.unsan.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class OrderPayload {

   // @JsonProperty("userInfo")
   // private UserInfo userInfo;


    @JsonProperty("totalPriceActual")
    private Integer totalPriceActual;

    @JsonProperty("state")
    private String state;

    @JsonProperty("_id")
    private String id;

    @JsonProperty("totalPrice")
    private Double totalPrice;



   /* @JsonProperty("userInfo")
    public UserInfo getUserInfo() {
        return userInfo;
    }

    @JsonProperty("userInfo")
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    */





    @JsonProperty("totalPriceActual")
    public Integer getTotalPriceActual() {
        return totalPriceActual;
    }

    @JsonProperty("totalPriceActual")
    public void setTotalPriceActual(Integer totalPriceActual) {
        this.totalPriceActual = totalPriceActual;
    }





    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
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



    @JsonProperty("totalPrice")
    public Double getTotalPrice() {
        return totalPrice;
    }

    @JsonProperty("totalPrice")
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }









}