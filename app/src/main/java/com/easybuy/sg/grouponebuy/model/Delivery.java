package com.easybuy.sg.grouponebuy.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Delivery implements Parcelable {

    String date;
    String week;
    String time;

    protected Delivery(Parcel in) {

        String[] data=new String[3];
        in.readStringArray(data);
       this.date = data[0];
        this.week = data[1];
        this.time = data[2];
    }
  public  Delivery()
    {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.date,
                this.week,
                this.time});

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Delivery createFromParcel(Parcel in) {
            return new Delivery(in);
        }

        @Override
        public Delivery[] newArray(int size) {
            return new Delivery[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Delivery(String date, String week, String time) {
        this.date = date;
        this.week = week;
        this.time = time;
    }
}
