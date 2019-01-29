package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Refund implements Serializable {
    @JsonProperty("eCoins")
    private Integer eCoins;
    @JsonProperty("sub")
    private List<Object> sub = null;

    @JsonProperty("eCoins")
    public Integer getECoins() {
        return eCoins;
    }

    @JsonProperty("eCoins")
    public void setECoins(Integer eCoins) {
        this.eCoins = eCoins;
    }

    @JsonProperty("sub")
    public List<Object> getSub() {
        return sub;
    }

    @JsonProperty("sub")
    public void setSub(List<Object> sub) {
        this.sub = sub;
    }
}
