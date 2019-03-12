package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)

public class Order implements Serializable {







        @JsonProperty("completedDate")
        private Object completedDate;
        @JsonProperty("remark")
        private String remark;
        @JsonProperty("totalPriceActual")
        private Double totalPriceActual;
        @JsonProperty("signature")
        private String signature;
       // @JsonProperty("driver")
       // private String driver;
        @JsonProperty("state")
        private String state;
        @JsonProperty("feedback")
        private String feedback;
        @JsonProperty("_id")
        private String id;
        @JsonProperty("shippingDate")
        private String shippingDate;
        @JsonProperty("totalPrice")
        private Double totalPrice;
        @JsonProperty("district")
        private String district;
        @JsonProperty("isPrint")
        private Boolean isPrint;

    public Double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(Double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    @JsonProperty("paymentMethod")
        private String paymentMethod;
        @JsonProperty("productList")
        private List<ProductOrderList> productList = null;
        @JsonProperty("orderNumber")
        private Integer orderNumber;
        @JsonProperty("deliveryPrice")
        Double deliveryPrice;
        @JsonProperty("orderCode")
        private String orderCode;

    public Boolean getPrint() {
        return isPrint;
    }

    public void setPrint(Boolean print) {
        isPrint = print;
    }

    public Double getRefundCostOrder() {
        return refundCostOrder;
    }

    public void setRefundCostOrder(Double refundCostOrder) {
        this.refundCostOrder = refundCostOrder;
    }

    @JsonProperty("orderDate")
        private String orderDate;
        @JsonProperty("refundCost")
        private Double refundCostOrder;




        @JsonProperty("completedDate")
        public Object getCompletedDate() {
            return completedDate;
        }

        @JsonProperty("completedDate")
        public void setCompletedDate(Object completedDate) {
            this.completedDate = completedDate;
        }

        @JsonProperty("remark")
        public String getRemark() {
            return remark;
        }

        @JsonProperty("remark")
        public void setRemark(String remark) {
            this.remark = remark;
        }

        @JsonProperty("totalPriceActual")
        public Double getTotalPriceActual() {
            return totalPriceActual;
        }

        @JsonProperty("totalPriceActual")
        public void setTotalPriceActual(Double totalPriceActual) {
            this.totalPriceActual = totalPriceActual;
        }

        @JsonProperty("signature")
        public String getSignature() {
            return signature;
        }

        @JsonProperty("signature")
        public void setSignature(String signature) {
            this.signature = signature;
        }

       /* @JsonProperty("driver")
        public String getDriver() {
            return driver;
        }

        @JsonProperty("driver")
        public void setDriver(String driver) {
            this.driver = driver;
        }
        */

        @JsonProperty("state")
        public String getState() {
            return state;
        }

        @JsonProperty("state")
        public void setState(String state) {
            this.state = state;
        }

        @JsonProperty("feedback")
        public String getFeedback() {
            return feedback;
        }

        @JsonProperty("feedback")
        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        @JsonProperty("_id")
        public String getId() {
            return id;
        }

        @JsonProperty("_id")
        public void setId(String id) {
            this.id = id;
        }

        @JsonProperty("shippingDate")
        public String getShippingDate() {
            return shippingDate;
        }

        @JsonProperty("shippingDate")
        public void setShippingDate(String shippingDate) {
            this.shippingDate = shippingDate;
        }

        @JsonProperty("totalPrice")
        public Double getTotalPrice() {
            return totalPrice;
        }

        @JsonProperty("totalPrice")
        public void setTotalPrice(Double totalPrice) {
            this.totalPrice = totalPrice;
        }

        @JsonProperty("district")
        public String getDistrict() {
            return district;
        }

        @JsonProperty("district")
        public void setDistrict(String district) {
            this.district = district;
        }

        @JsonProperty("isPrint")
        public Boolean getIsPrint() {
            return isPrint;
        }

        @JsonProperty("isPrint")
        public void setIsPrint(Boolean isPrint) {
            this.isPrint = isPrint;
        }

        @JsonProperty("paymentMethod")
        public String getPaymentMethod() {
            return paymentMethod;
        }

        @JsonProperty("paymentMethod")
        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        @JsonProperty("productList")
        public List<ProductOrderList> getProductList() {
            return productList;
        }

        @JsonProperty("productList")
        public void setProductList(List<ProductOrderList> productList) {
            this.productList = productList;
        }

        @JsonProperty("orderNumber")
        public Integer getOrderNumber() {
            return orderNumber;
        }

        @JsonProperty("orderNumber")
        public void setOrderNumber(Integer orderNumber) {
            this.orderNumber = orderNumber;
        }

        @JsonProperty("orderCode")
        public String getOrderCode() {
            return orderCode;
        }

        @JsonProperty("orderCode")
        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }



        @JsonProperty("orderDate")
        public String getOrderDate() {
            return orderDate;
        }

        @JsonProperty("orderDate")
        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }



    }

