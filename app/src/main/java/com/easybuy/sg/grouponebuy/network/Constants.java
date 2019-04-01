package com.easybuy.sg.grouponebuy.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.easybuy.sg.grouponebuy.activities.MainActivity;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.model.Customer;
import com.easybuy.sg.grouponebuy.utils.CategoryListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Constants  {
static public String baseUrlStr = "http://13.250.234.103:3000/";
//buisness url
//static public String baseUrlStr = "http://13.251.164.198:3000/";
    static public String loginUrlStr = baseUrlStr + "users/login";
    static public String newImageUrl="https://s3-ap-southeast-1.amazonaws.com/ebuymart/";
    static public String signUpUrl=baseUrlStr+"users/signUp";
    static public String districtUrl=baseUrlStr+"districts/search";
    static public String categoryUrl=baseUrlStr+"categories";
    static public String specialCategoryUrl=baseUrlStr+"categoriesSpecial";
    static public String adwordsUrl=baseUrlStr+"adwords";
    static public String tokenStr = "token";
    static public String Language= "language";
    static public String productUrl=baseUrlStr+"products";
    static public String favouriteUrl=baseUrlStr+"users";
    static public String checkOrderUrl=baseUrlStr+"users/extra/";
    static public String changeDistrictUrl=baseUrlStr+"users/binding/";
    static public String changePasswordUrl=baseUrlStr+"users/password/B/";
    static public String createOrderUrl=baseUrlStr+"orders/create/";
    static public String getOrderUrl=baseUrlStr+"orders/userGet/";
    static public String deleteOrderUrl=baseUrlStr+"orders/state/";
    static public String hotKeywordUrl=baseUrlStr+"products/hotKeyword";
    static public String searchUrl=baseUrlStr+"products/search/";
    static public String editOrderUrl=baseUrlStr+"orders/";
    static public String applyDistrictUrl=baseUrlStr+"applies/create";
    static public String customerServiceUrl=baseUrlStr+"messages/create";
    static public String specialImageUrl=baseUrlStr+"specialSales";
    static public String emailOrderUrl=baseUrlStr+"emails/sendNewOrder";
    static public String ChangePasswordUrlA=baseUrlStr+"users/password/A/";
    static public String modifyOrderUrl=baseUrlStr+"orders/addExtra/";
    static public String flashSaleUrl=baseUrlStr+"activities/1";
    static public String singleProductUrl=baseUrlStr+"singleProducts";
    static public String orderBadgeUrl=baseUrlStr+"orders/getOrdersTip";
    static  public String orderRefundUrl=baseUrlStr+"orders/refundCostHistory";
    static public String withdrawUrl=baseUrlStr+"withdraws/create";
    static public String updatePasswordUrl=baseUrlStr+"users/checkEmail";
    static public String checkPhoneUrl=baseUrlStr+"users/checkPhone";
    static public String checkAppUpdateUrl=baseUrlStr+"notifications/getUpdate";
    static public String sendEmailCode=baseUrlStr+"emails/sendEmailCode";


    public static String getToken(Context context) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        String auth_token_string = settings.getString(tokenStr, ""/*default value is ""*/);
        Log.v("err", tokenStr);
        // Log.d("mytoken",auth_token_string);
        return auth_token_string;
    }
    public static void setToken(Context context,String token)
    {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        GlobalProvider globalProvider=GlobalProvider.getGlobalProviderInstance(context);
        editor.putString(tokenStr,token);
        globalProvider.setToken(token);
        editor.commit();

    }
    public static void setCustomer(Context context,Customer customer)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(customer!=null) {

            Gson gson = new Gson();
            String json = gson.toJson(customer);
            editor.putString("customer", json);
            editor.commit();
        }
        else
        {
            editor.putString("customer",null);
            editor.commit();
        }

    }
    public static void setLanguage(Context context,String language)
    {
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(Language, language);

        editor.commit();
    }
    public static String getLanguage(Context context) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);

        String language_str = settings.getString(Language,"english");
        //Log.v("err", tokenStr);
        return language_str;
    }
    public static  Customer getCustomer(Context context) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json=settings.getString("customer",null);
       Customer customer = gson.fromJson(json, Customer.class);

       return customer;


    }


   
}
