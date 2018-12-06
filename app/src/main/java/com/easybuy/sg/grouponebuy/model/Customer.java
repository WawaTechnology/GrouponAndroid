package com.easybuy.sg.grouponebuy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Customer implements Serializable {
   public String address;
  // public String avatar;
  // public String birthday;
   public String email;
    @JsonProperty("PayNowAccount")
    public String payNowAccount;

    public String getPayNowAccount() {
        return payNowAccount;
    }

    public void setPayNowAccount(String payNowAccount) {
        this.payNowAccount = payNowAccount;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    @JsonProperty("district")
    private District district;
    @JsonProperty("favoriteList")
    private List<Product> favoriteList = null;

    public List<Product> getFavoriteList() {
        return favoriteList;
    }

    public void setFavoriteList(List<Product> favoriteList) {
        this.favoriteList = favoriteList;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }





   public String postcode;


    public Customer(String address, String email, String phone, String userName, String customer_id, String postcode,List<Product> favoriteList) {
        this.address = address;

        this.email = email;

        this.phone = phone;
        this.userName = userName;
        this.customer_id = customer_id;
        this.postcode=postcode;
        this.favoriteList=favoriteList;

    }
    public Customer()
    {

    }

   public String phone;
   public String userName;

    @JsonProperty("_id")
   public String customer_id;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty("_id")
    public String getCustomer_id() {
        return customer_id;
    }
    @JsonProperty("_id")
    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
}
