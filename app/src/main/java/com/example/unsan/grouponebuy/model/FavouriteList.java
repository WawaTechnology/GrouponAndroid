package com.example.unsan.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class FavouriteList implements Serializable {
    int status;
    @JsonProperty("msg")
    String msg;
    @JsonProperty("payload.favoriteList")
    List<Product> favouriteList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Product> getFavouriteList() {
        return favouriteList;
    }

    public void setFavouriteList(List<Product> favouriteList) {
        this.favouriteList = favouriteList;
    }
}
