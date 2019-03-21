package com.easybuy.sg.grouponebuy.model;

import java.util.Date;

public class MyDate {
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Date)){
            return false;
        }
        Date other=(Date)o;
        if(other.compareTo(this.getDate())==0)
        {
            return true;
        }
        else
            return false;

    }
}
