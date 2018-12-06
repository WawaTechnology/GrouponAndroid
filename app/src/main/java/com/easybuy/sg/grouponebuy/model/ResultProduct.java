package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ResultProduct implements Serializable {
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("payload")
    private Product payload;

    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonProperty("msg")
    public String getMsg() {
        return msg;
    }

    @JsonProperty("msg")
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @JsonProperty("payload")
    public Product getPayload() {
        return payload;
    }

    @JsonProperty("payload")
    public void setPayload(Product payload) {
        this.payload = payload;
    }

}


