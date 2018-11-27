package com.example.unsan.grouponebuy.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.database.Observable;

import com.example.unsan.grouponebuy.model.User;

public class UserModel extends ViewModel {
    String Tag=UserModel.class.getSimpleName();
   Observable<User> userObservable;

}
