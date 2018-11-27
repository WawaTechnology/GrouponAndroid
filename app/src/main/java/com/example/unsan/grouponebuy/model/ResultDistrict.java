package com.example.unsan.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultDistrict {
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("payload")
    private District district;

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
    public District getPayload() {
        return district;
    }

    @JsonProperty("payload")
    public void setPayload(District district) {
        this.district = district;
    }
}
