package com.example.unsan.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "msg",
        "payload"
})
public class SpecialCategoryList implements Serializable {

    @JsonProperty("status")
    private Integer status;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("payload")
    private List<CategorySpecial> categorySpecialList = null;

    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<CategorySpecial> getCategorySpecialList() {
        return categorySpecialList;
    }

    public void setCategorySpecialList(List<CategorySpecial> categorySpecialList) {
        this.categorySpecialList = categorySpecialList;
    }

    @JsonProperty("msg")
    public String getMsg() {
        return msg;

    }

    @JsonProperty("msg")
    public void setMsg(String msg) {
        this.msg = msg;
    }



}