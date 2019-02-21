package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class RefundOrder implements Serializable {

    @JsonProperty("order")
    private Order order;
    @JsonProperty("refund")
    private Double refund;
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

    @JsonProperty("refund")
    public Double getRefund() {
        return refund;
    }

    @JsonProperty("refund")
    public void setRefund(Double refund) {
        this.refund = refund;
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

