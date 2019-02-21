package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Refund implements Serializable {
    @JsonProperty("eCoins")
    private Double eCoins;
    @JsonProperty("sub")
    private List<Sub> sub = null;

    @JsonProperty("eCoins")
    public Double getECoins() {
        return eCoins;
    }

    @JsonProperty("eCoins")
    public void setECoins(Double eCoins) {
        this.eCoins = eCoins;
    }

    @JsonProperty("sub")
    public List<Sub> getSub() {
        return sub;
    }

    @JsonProperty("sub")
    public void setSub(List<Sub> sub) {
        this.sub = sub;
    }
}
