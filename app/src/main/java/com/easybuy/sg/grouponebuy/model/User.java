package com.easybuy.sg.grouponebuy.model;

import android.arch.lifecycle.ViewModel;
import android.databinding.BaseObservable;

import com.easybuy.sg.grouponebuy.R;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)

public class User extends BaseObservable implements Serializable{
   private String email,userName,password;
   String phone,postcode;
   String address;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(R.id.email);

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(R.id.name);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(R.id.password);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(R.id.phone);
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
        notifyPropertyChanged(R.id.postcode);
    }

    public User() {
    }

    public User(String email, String userName, String password, String phone, String postcode,String address) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.phone = phone;
        this.postcode = postcode;
        this.address=address;
    }
}
