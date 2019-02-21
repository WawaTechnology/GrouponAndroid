package com.easybuy.sg.grouponebuy.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class ConsumePayload implements Serializable {

    @JsonProperty("refundOrders")
    private List<RefundOrder> refundOrders = null;
    @JsonProperty("refundCostOrders")
    private List<RefundCostOrder> refundCostOrders = null;
    @JsonProperty("withdraws")
    private List<Withdraw> withdraws = null;

    @JsonProperty("refundOrders")
    public List<RefundOrder> getRefundOrders() {
        return refundOrders;
    }

    @JsonProperty("refundOrders")
    public void setRefundOrders(List<RefundOrder> refundOrders) {
        this.refundOrders = refundOrders;
    }

    @JsonProperty("refundCostOrders")
    public List<RefundCostOrder> getRefundCostOrders() {
        return refundCostOrders;
    }

    @JsonProperty("refundCostOrders")
    public void setRefundCostOrders(List<RefundCostOrder> refundCostOrders) {
        this.refundCostOrders = refundCostOrders;
    }

    @JsonProperty("withdraws")
    public List<Withdraw> getWithdraws() {
        return withdraws;
    }

    @JsonProperty("withdraws")
    public void setWithdraws(List<Withdraw> withdraws) {
        this.withdraws = withdraws;
    }
}

