package com.easybuy.sg.grouponebuy.model;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Consume implements Serializable{

   Order order;
    String date;
    String type;
    Double refundValue;


    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Consume(Order order, String date, String type, Double refundValue) {
        this.order = order;
        this.date = date;
        this.type = type;
        this.refundValue = refundValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Consume() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getRefundValue() {
        return refundValue;
    }

    public void setRefundValue(Double refundValue) {
        this.refundValue = refundValue;
    }



}
