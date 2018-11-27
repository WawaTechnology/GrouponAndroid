package com.example.unsan.grouponebuy.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "status",
            "msg"
    })
    public class ResultClass {

        @JsonProperty("status")
        private Integer status;
        @JsonProperty("msg")
        private String msg;

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

    }

