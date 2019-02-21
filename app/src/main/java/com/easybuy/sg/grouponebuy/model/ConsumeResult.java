package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)

public class ConsumeResult implements Serializable {

    @JsonProperty("status")
    private Integer status;
    @JsonProperty("payload")
    private ConsumePayload payload;

    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonProperty("payload")
    public ConsumePayload getPayload() {
        return payload;
    }

    @JsonProperty("payload")
    public void setPayload(ConsumePayload payload) {
        this.payload = payload;
    }

}

