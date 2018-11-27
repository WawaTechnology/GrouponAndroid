package com.example.unsan.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class ResultOrder implements Serializable {
    @JsonProperty("status")
    Integer status;
    @JsonProperty("payload")
    private List<Order> order;

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }



    public void setStatus(Integer status) {
        this.status = status;
    }



    @JsonProperty("status")
    public Integer getStatus() {

        return status;
    }
}
