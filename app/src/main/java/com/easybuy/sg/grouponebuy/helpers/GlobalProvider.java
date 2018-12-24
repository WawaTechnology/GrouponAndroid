package com.easybuy.sg.grouponebuy.helpers;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.activities.MainActivity;
import com.easybuy.sg.grouponebuy.activities.MyApplication;
import com.easybuy.sg.grouponebuy.model.Category;
import com.easybuy.sg.grouponebuy.model.CategoryImage;
import com.easybuy.sg.grouponebuy.model.CategoryPrimary;
import com.easybuy.sg.grouponebuy.model.CategorySpecial;
import com.easybuy.sg.grouponebuy.model.CategorySummary;
import com.easybuy.sg.grouponebuy.model.Customer;
import com.easybuy.sg.grouponebuy.model.FlashSale;
import com.easybuy.sg.grouponebuy.model.Order;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.model.ProductImageId;
import com.easybuy.sg.grouponebuy.model.SpecialImage;
import com.easybuy.sg.grouponebuy.model.User;
import com.easybuy.sg.grouponebuy.network.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GlobalProvider {
    private static GlobalProvider globalProviderInstance;
    private Context context;
   public boolean hasSale;
   public Date saleDate;
  String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }


    private boolean isLogin;
    public boolean isNotFirstTime;
    public String selectedCategory;
  // private Customer customer;
    public ProductImageId specialBanner;

    public List<ProductImageId> getDoubleProductImageList() {
        return doubleProductImageList;
    }

    public void setDoubleProductImageList(List<ProductImageId> doubleProductImageList) {
        this.doubleProductImageList = doubleProductImageList;
    }

    public String lan="a";

    public FlashSale getFlashSale() {
        return flashSale;
    }

    public void setFlashSale(FlashSale flashSale) {
        this.flashSale = flashSale;
    }

    Map<String,String> params;
  // public List<Product> favoriteList=new ArrayList<>();
   public List<Product> cartList=new ArrayList<>();
   public List<CategoryImage> categoryImageList=new ArrayList<>();
   public Map<String,String> categoryNameMap=new HashMap();
   public Map<String,String> deliveryTimingChinese=new HashMap();
   public List<ProductImageId> doubleProductImageList=new ArrayList<>();
   private FlashSale flashSale;

    public List<ProductImageId> getThreeTopImageLayout() {
        return threeTopImageLayout;
    }

    public void setThreeTopImageLayout(List<ProductImageId> threeTopImageLayout) {
        this.threeTopImageLayout = threeTopImageLayout;
    }

    public List<ProductImageId> threeTopImageLayout=new ArrayList<>();



    public List<ProductImageId> getThreeImageLayout() {
        return threeImageLayout;
    }


    public void setThreeImageLayout(List<ProductImageId> threeImageLayout) {
        this.threeImageLayout = threeImageLayout;
    }

    public List<ProductImageId> threeImageLayout=new ArrayList<>();

   public List<ProductImageId> singleProductList=new ArrayList<>();
   public List<ProductImageId> doubleProductList=new ArrayList<>();

    public List<CategoryImage> getCategoryImageList() {
        return categoryImageList;
    }

    public void setCategoryImageList(List<CategoryImage> categoryImageList) {
        this.categoryImageList = categoryImageList;
    }

    public List<CategorySpecial> getCategorySpecialList() {
        return categorySpecialList;
    }

    public void setCategorySpecialList(List<CategorySpecial> categorySpecialList) {
        this.categorySpecialList = categorySpecialList;
    }

    public  List<CategorySpecial> categorySpecialList=new ArrayList<>();
   public int adjustToStart=0;
   public Order deletedOrder=null;



   public List<SpecialImage> specialMImages=new ArrayList<>();
    static public Map<Integer,String> deliveryTiming=new HashMap<>();
    public  Map<Integer, String> getDeliveryTiming() {
        return deliveryTiming;
    }

    public  void setDeliveryTiming(Map<Integer, String> deliveryTiming) {



        this.deliveryTiming = deliveryTiming;
    }





    public Order getDeletedOrder() {
        return deletedOrder;
    }

    public void setDeletedOrder(Order deletedOrder) {
        this.deletedOrder = deletedOrder;
    }

    public int index=0;
    public String selectedCategoryName;
    public Map<String,String> categoryMap=new HashMap<>();
    private static RequestQueue requestQueue;
    public List<CategorySummary> categoryList=new ArrayList<>();

 /*public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    */


    private GlobalProvider(Context context)
    {
        this.context=context;
        if(requestQueue==null)
        requestQueue= Volley.newRequestQueue(context.getApplicationContext());
        params=new HashMap<>();
    }




    public static synchronized GlobalProvider getGlobalProviderInstance(Context context) {
        if(globalProviderInstance==null)
        {
            globalProviderInstance=new GlobalProvider(context);

        }
        return globalProviderInstance;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
    public RequestQueue getRequestQueue() {

        return requestQueue;
    }


    public static void addRequest(Request request)
    {
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);

    }
    public Map<String,String> setToken(String token) {

        params.put("Authorization", token);
        return params;

    }

    /*
    public Map<String, String> addHeaderToken(Context context) {
        if ( context != null && Constants.getToken(context) != null ) {
            Log.d("checkheader",Constants.getToken(context));

            params.put("Authorization", Constants.getToken(context));
            if (Constants.getLanguage(context).equals("english")) {
                params.put("Language", "english");

                MyApplication ma=MyApplication.getInstance();

                // MainActivity ma=MainActivity.getDefault();




                ma.change("english");



                if(index == 0){
                    Intent intent = new Intent(context,MainActivity.class);
                    intent.putExtra("l","x");
                    context.startActivity(intent);
                    index++;
                }
            }else if(Constants.getLanguage(context).equals("chinese")){
                params.put("Language", "chinese");

                // MainActivity.getDefault().change("chinese");
                MyApplication ma=MyApplication.getInstance();






                ma.change("chinese");
                if(index == 0){
                    Intent intent = new Intent(context,MainActivity.class);
                    intent.putExtra("l","x");
                    context.startActivity(intent);
                    index++;
                }
            }else{
                if (globalProviderInstance.lan.equals("a")) {
                    if (Locale.getDefault().getLanguage().equals("en")) {
                        params.put("Language", "english");
                    } else {
                        params.put("Language", "chinese");
                    }
                } else if (globalProviderInstance.lan.equals("english")) {
                    params.put("Language", "english");
                } else {
                    params.put("Language", "chinese");
                }
            }
        }
        return params;
    }
    */

    public Map<String, String> getCategoryNameMap() {
        return categoryNameMap;
    }

    public void setCategoryNameMap(Map<String, String> categoryNameMap) {
        this.categoryNameMap = categoryNameMap;
    }
    public static ContextWrapper changeLang(Context context, String lang_code){
        Locale sysLocale;

        Resources rs = context.getResources();
        Configuration config = rs.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = config.getLocales().get(0);
        } else {
            sysLocale = config.locale;
        }
        Log.d("checksyslocale",sysLocale.getDisplayLanguage());
        Log.d("checklangcode",lang_code);
        if (!lang_code.equals("") && !sysLocale.getLanguage().equals(lang_code)) {
            Locale locale = new Locale(lang_code);
            Locale.setDefault(locale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(locale);
            } else {
                config.locale = locale;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                context = context.createConfigurationContext(config);
            } else {
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            }
        }

        return new ContextWrapper(context);
    }
}

