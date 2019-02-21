package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Sub implements Serializable {

    @JsonProperty("time")
    private String time;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("coin")
    private Double coin;
    @JsonProperty("order")
    private String order;

    @JsonProperty("time")
    public String getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(String time) {
        this.time = time;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("coin")
    public Double getCoin() {
        return coin;
    }

    @JsonProperty("coin")
    public void setCoin(Double coin) {
        this.coin = coin;
    }

    @JsonProperty("order")
    public String getOrder() {
        return order;
    }

    @JsonProperty("order")
    public void setOrder(String order) {
        this.order = order;
    }

}
