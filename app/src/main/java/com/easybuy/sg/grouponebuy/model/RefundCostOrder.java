package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)

public class RefundCostOrder implements Serializable {

    @JsonProperty("order")
    private Order order;
    @JsonProperty("refundCost")
    private Double refundCost;
    @JsonProperty("time")
    private String time;

    @JsonProperty("order")
    public Order getOrder() {
        return order;
    }

    @JsonProperty("order")
    public void setOrder(Order order) {
        this.order = order;
    }

    @JsonProperty("refundCost")
    public Double getRefundCost() {
        return refundCost;
    }

    @JsonProperty("refundCost")
    public void setRefundCost(Double refundCost) {
        this.refundCost = refundCost;
    }

    @JsonProperty("time")
    public String getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(String time) {
        this.time = time;
    }

}