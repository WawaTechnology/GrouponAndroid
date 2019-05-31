package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResult implements Serializable{
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("payload")
    private List<PrevOrder> payload = null;

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
    public List<PrevOrder> getPayload() {
        return payload;
    }

    @JsonProperty("payload")
    public void setPayload(List<PrevOrder> payload) {
        this.payload = payload;
    }
    @JsonProperty("payload2")
    Payload2 payload2;

    public Payload2 getPayload2() {
        return payload2;
    }

    public void setPayload2(Payload2 payload2) {
        this.payload2 = payload2;
    }
}
