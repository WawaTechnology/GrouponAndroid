package com.easybuy.sg.grouponebuy.model;

import com.easybuy.sg.grouponebuy.adapter.SpecialCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)

@JsonInclude(JsonInclude.Include.NON_NULL)

public class SpecialCategoryList implements Serializable {


    @JsonPropertyOrder({
            "status",
            "msg",
            "payload"
    })


        @JsonProperty("status")
        private Integer status;
        @JsonProperty("msg")
        private String msg;
        @JsonProperty("payload")
        private List<SpecialCategory> specialCategoryList = null;

        @JsonProperty("status")
        public Integer getStatus() {
            return status;
        }

        @JsonProperty("status")
        public void setStatus(Integer status) {
            this.status = status;
        }

        public List<SpecialCategory> getSpecialCategoryList() {
            return specialCategoryList;
        }

        public void setSpecialCategoryList(List<SpecialCategory> specialCategoryList) {
            this.specialCategoryList = specialCategoryList;
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
