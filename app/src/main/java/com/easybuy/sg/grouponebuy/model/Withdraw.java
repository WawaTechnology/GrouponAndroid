package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)

public class Withdraw implements Serializable {

    @JsonProperty("status")
    private String status;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("user")
    private String user;
    @JsonProperty("account")
    private String account;
    @JsonProperty("withdraw")
    private Double withdraw;
    @JsonProperty("time")
    private String time;


    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("user")
    public String getUser() {
        return user;
    }

    @JsonProperty("user")
    public void setUser(String user) {
        this.user = user;
    }

    @JsonProperty("account")
    public String getAccount() {
        return account;
    }

    @JsonProperty("account")
    public void setAccount(String account) {
        this.account = account;
    }

    @JsonProperty("withdraw")
    public Double getWithdraw() {
        return withdraw;
    }

    @JsonProperty("withdraw")
    public void setWithdraw(Double withdraw) {
        this.withdraw = withdraw;
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