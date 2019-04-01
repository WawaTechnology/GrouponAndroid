package com.easybuy.sg.grouponebuy.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.fragment.FavoriteFragment;
import com.easybuy.sg.grouponebuy.fragment.FragmentCart;
import com.easybuy.sg.grouponebuy.fragment.FragmentHome;
import com.easybuy.sg.grouponebuy.fragment.FragmentOrder;
import com.easybuy.sg.grouponebuy.fragment.FragmentProductCategory;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.helpers.Utf8JsonRequest;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.model.Result;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.easybuy.sg.grouponebuy.utils.CategoryListener;
import com.easybuy.sg.grouponebuy.utils.CircleBadgeView;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class MainActivity extends AppCompatActivity implements CategoryListener {
    private static final int PERMISSION_REQUEST_CODE =123 ;
    TextView homeText,aboutText,favText,productText,cartText;
    ImageView homeImage,aboutImage,favImage,productImage,cartImage;
    LinearLayout homeLinearLayout,productLinearLayout,aboutLinearLayout,favLinearLayout,cartLinearLayout;
    GlobalProvider globalProvider;
   private CircleBadgeView buyNumView;
    private  int DEFAULT_LR_PADDING_DIP = 5;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("maincalled","yes");
        setContentView(R.layout.activity_main);

        //todo check if splashactivity is loaded when app goes in background with many apps opened
        globalProvider=GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        if(!globalProvider.isNotFirstTime)
        {
            globalProvider.isNotFirstTime=true;
            Intent intent=new Intent(MainActivity.this,SplashActivity.class);
            startActivity(intent);
            finish();
        }
      //  badgeTextView=(TextView)findViewById(R.id.badge);




                homeText=(TextView) findViewById(R.id.hometext);
        aboutText=(TextView) findViewById(R.id.about_text);
        favText=(TextView) findViewById(R.id.favorite_text);
        productText=(TextView)findViewById(R.id.product_text) ;
        cartText=(TextView) findViewById(R.id.cart_text);
        homeImage=(ImageView)findViewById(R.id.homeimg);
        cartImage=(ImageView)findViewById(R.id.cart_image);
        aboutImage=(ImageView)findViewById(R.id.about_image);
        favImage=(ImageView) findViewById(R.id.favourite_image);
        productImage=(ImageView)findViewById(R.id.product_image);
        aboutLinearLayout=(LinearLayout) findViewById(R.id.about_layout);
        homeLinearLayout=(LinearLayout) findViewById(R.id.home_layout);
        productLinearLayout=(LinearLayout) findViewById(R.id.product_layout);
        cartLinearLayout=(LinearLayout) findViewById(R.id.cart_linearlayout);
        favLinearLayout=(LinearLayout) findViewById(R.id.fav_linearlayout);
       buyNumView = new CircleBadgeView(this, cartImage);
       buyNumView.setTextColor(Color.WHITE);
       buyNumView.setBackgroundColor(getResources().getColor(R.color.red));
        homeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelect(0);
            }
        });
        productLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelect(1);
            }
        });
        aboutLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelect(4);
            }
        });
      //  new GooglePlayAppVersion(getPackageName(), version -> Log.d("TAG", String.format("App version: %s", version))).execute();
        new GooglePlayAppVersion(getPackageName(), new GooglePlayAppVersion.Listener() {
            @Override
            public void result(String version) {

                Log.d("TAG", String.format("App version: %s", version));

            }
        }).execute();










       // setDeliveryMap();



        //todo do this in splash activity
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // Log.d("checkpermission"," "+permissionCheck);
        if(permissionCheck==-1)
        {
            requestPermission();
        }


        //check if there customer record is in sharedpreference, reload it with api data;add products cart in global provider variable
        if(Constants.getCustomer(this)!=null) {
            Log.d("logintrue","here");
            Log.d("checkfavl",Constants.getCustomer(this).getFavoriteList().size()+"");

            String customerid=Constants.getCustomer(this).customer_id;
            Log.d("checkcustomerid",customerid);
           // Log.d("getdistrictid",Constants.getCustomer(this).getDistrict().getId());


            String url= Constants.favouriteUrl+"/"+customerid;




          //  globalProvider.setCustomer(null);
           // Constants.setCustomer(this,null);





            Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("getcuresponse",response);

                    JsonFactory jsonFactory = new JsonFactory();
                    ObjectMapper objectMapper = new ObjectMapper();

                    try {
                        JsonParser jsonParser = jsonFactory.createParser(response);
                        Result favResult = (Result) objectMapper.readValue(jsonParser, Result.class);
                     //   globalProvider.favoriteList.clear();
                        if(favResult.getStatus()==0&&favResult.getCustomer()!=null) {
                            globalProvider.setLogin(true);
                            if(globalProvider.cartList.isEmpty())
                            {
                                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("productListFile", Context.MODE_PRIVATE);
                                Gson gson = new Gson();
                                String res=sharedPreferences.getString("productList" , null);
                                if(res!=null) {
                                    List<Product> lstArrayList = new ArrayList<>();
                                    lstArrayList = gson.fromJson(res,
                                            new TypeToken<List<Product>>() {
                                            }.getType());
                                    if (!lstArrayList.isEmpty())
                                        globalProvider.cartList.addAll(lstArrayList);
                                }
                            }

                          //  globalProvider.favoriteList.addAll(favResult.getCustomer().getFavoriteList());

                            Constants.setCustomer(MainActivity.this, favResult.getCustomer());
                            globalProvider.setCustomerId(Constants.getCustomer(MainActivity.this).customer_id);
                           // globalProvider.setCustomer(Constants.getCustomer(MainActivity.this));
                        }
                        else
                        {
                            //todo set login false
                            //set customer null
                            globalProvider.setLogin(false);
                            Constants.setCustomer(MainActivity.this,null);
                            globalProvider.setCustomerId(null);
                            //globalProvider.setCustomer(null);

                            Gson gson=new Gson();
                          //  String productList=gson.toJson(globalProvider.cartList);
                            SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("productListFile", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("productList",null);
                            editor.apply();



                        }






                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("geterror",error.toString());
                    String message = null;
                    if (error instanceof NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                    } else if (error instanceof ServerError) {
                        message = "The server could not be found. Please try again after some time!!";
                    } else if (error instanceof AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                    } else if (error instanceof ParseError) {
                        message = "Parsing error! Please try again after some time!!";


                    } else if (error instanceof NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                    } else if (error instanceof TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection.";
                    }
                    Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();



                }
            });
            globalProvider.addRequest(utf8JsonRequest);
        }





        favLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setSelect(2);
            }
        });
        cartLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setSelect(3);
            }
        });
        Display display=getWindowManager().getDefaultDisplay();

        Point point=new Point();
        display.getSize(point);
        Log.d("getsize",point.x+" " +point.y);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        Log.d("checkwidthindp",dpWidth+" "+dpHeight);
        Log.d("checkdensity",displayMetrics.density+" "+displayMetrics.densityDpi);


        if(globalProvider.adjustToStart!=0)
        {
            setSelect(globalProvider.adjustToStart);
        }
        else
        setSelect(0);





        //Drawable img=getResources().getDrawable(R.drawable.brocollo,null);




    }

    public void onResume()
    {
        super.onResume();
       // calculateSize();
        if(!globalProvider.isNotFirstTime)
        {
            globalProvider.isNotFirstTime=true;
            Intent intent=new Intent(MainActivity.this,SplashActivity.class);
            startActivity(intent);
            finish();
        }
        if(globalProvider.isLogin()) {
            if (globalProvider.cartList.size() > 0) {
                setCartNum();
            } else
                HideCartNum();
        }
    }


    public void setCartNum() {
        Log.d("cartset","here");
        if(!globalProvider.cartList.isEmpty()) {
          /*  badgeTextView.setText(globalProvider.cartList.size()+"");
            if(globalProvider.cartList.size()>=10)
            {
                badgeTextView.setPadding(3,0,3,0);
            }
            else
                badgeTextView.setPadding(5,0,5,0);
            badgeTextView.setVisibility(View.VISIBLE);
            */


           buyNumView.setText(globalProvider.cartList.size() + "");//
            buyNumView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            if (globalProvider.cartList.size() >= 10) {
                buyNumView.setGravity(Gravity.CENTER_VERTICAL);
                 buyNumView.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);

                DEFAULT_LR_PADDING_DIP = 3;
                int paddingPixels = dipToPixels(DEFAULT_LR_PADDING_DIP);
                Log.d("paddingpos",paddingPixels+"");
                buyNumView.setPadding(paddingPixels, 0, paddingPixels, 0);

                buyNumView.setTextSize(11);

            } else {
                DEFAULT_LR_PADDING_DIP = 5;
                int paddingPixels_ = dipToPixels(DEFAULT_LR_PADDING_DIP);
                Log.d("paddingpos",paddingPixels_+"");
                buyNumView.setPadding(paddingPixels_, 0, paddingPixels_, 0);
              // buyNumView.setPadding(paddingPixels_, 0, 0, 0);
                buyNumView.setTextSize(11);
               // buyNumView.setGravity(Gravity.CENTER_VERTICAL);
               // buyNumView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


            }

            //buyNumView.setTextSize(12);
            buyNumView.setBadgePosition(CircleBadgeView.POSITION_TOP_RIGHT);
            buyNumView.setGravity(Gravity.RIGHT);
            buyNumView.show();

        }
        else
            HideCartNum();


    }

    public void HideCartNum(){
      //  badgeTextView.setVisibility(View.GONE);
        buyNumView.hide();

    }

    private void setSelect(int i) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        switch (i)
        {
            case 0:
            {
                Fragment homeFragment=new FragmentHome();
                fragmentTransaction.replace(R.id.main,homeFragment);
                homeText.setTextColor(getResources().getColor(R.color.red));
                cartText.setTextColor(0xff666666);
                productText.setTextColor(0xff666666);
                aboutText.setTextColor(0xff666666);
                favText.setTextColor(0xff666666);
                homeImage.setImageResource(R.drawable.home_red);
                productImage.setImageResource(R.drawable.productblack);
                favImage.setImageResource(R.drawable.favblack);
                cartImage.setImageResource(R.drawable.cart_black);
                aboutImage.setImageResource(R.drawable.profile_black);

                fragmentTransaction.commit();

                break;


            }
            case 1:
            {
                Fragment fragmentProduct=new FragmentProductCategory();
                fragmentTransaction.replace(R.id.main,fragmentProduct,"productTag");
                homeText.setTextColor(0xff666666);
                cartText.setTextColor(0xff666666);
                productText.setTextColor(getResources().getColor(R.color.red));
                aboutText.setTextColor(0xff666666);
                favText.setTextColor(0xff666666);
                homeImage.setImageResource(R.drawable.home_black);
                productImage.setImageResource(R.drawable.productred);
                favImage.setImageResource(R.drawable.favblack);
                cartImage.setImageResource(R.drawable.cart_black);
                aboutImage.setImageResource(R.drawable.profile_black);
                fragmentTransaction.commit();
                break;

            }
            case 2:
            {
                Fragment fragmentFavorite=new FavoriteFragment();
                fragmentTransaction.replace(R.id.main,fragmentFavorite);
                homeText.setTextColor(0xff666666);
                cartText.setTextColor(0xff666666);
                productText.setTextColor(0xff666666);
                aboutText.setTextColor(0xff666666);
                favText.setTextColor(getResources().getColor(R.color.red));
                homeImage.setImageResource(R.drawable.home_black);
                productImage.setImageResource(R.drawable.productblack);
                favImage.setImageResource(R.drawable.favred);
                cartImage.setImageResource(R.drawable.cart_black);
                aboutImage.setImageResource(R.drawable.profile_black);
                fragmentTransaction.commit();
                break;


            }
            case 3:
            {
                Fragment fragmentCart=new FragmentCart();
                fragmentTransaction.replace(R.id.main,fragmentCart,"cartTag");
                homeText.setTextColor(0xff666666);
                cartText.setTextColor(getResources().getColor(R.color.red));
                productText.setTextColor(0xff666666);
                aboutText.setTextColor(0xff666666);
                favText.setTextColor(0xff666666);
                homeImage.setImageResource(R.drawable.home_black);
                productImage.setImageResource(R.drawable.productblack);
                favImage.setImageResource(R.drawable.favblack);
                cartImage.setImageResource(R.drawable.cart_red);
                aboutImage.setImageResource(R.drawable.profile_black);
                fragmentTransaction.commit();
                break;
            }

            case 4:
            {
                Fragment fragmentOrder=new FragmentOrder();
                fragmentTransaction.replace(R.id.main,fragmentOrder,"orderTag");
                homeText.setTextColor(0xff666666);
                cartText.setTextColor(0xff666666);
                productText.setTextColor(0xff666666);
                aboutText.setTextColor(getResources().getColor(R.color.red));
                favText.setTextColor(0xff666666);
                homeImage.setImageResource(R.drawable.home_black);
                productImage.setImageResource(R.drawable.productblack);
                favImage.setImageResource(R.drawable.favblack);
                cartImage.setImageResource(R.drawable.cart_black);
                aboutImage.setImageResource(R.drawable.profile_red);
                fragmentTransaction.commit();
                break;
            }



        }
    }

    // changing Fragments from Fragment Home to Fragment Product

    @Override
    public void onSelectedCategory() {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();

        Fragment fragmentProduct=new FragmentProductCategory();
        //Bundle args=new Bundle();
        //args.putInt("pos",pos);
       // fragmentProduct.setArguments(args);
        fragmentTransaction.replace(R.id.main,fragmentProduct);
        homeText.setTextColor(0xff666666);
        cartText.setTextColor(0xff666666);
        productText.setTextColor(0xffee2400);
        aboutText.setTextColor(0xff666666);
        favText.setTextColor(0xff666666);
        homeImage.setImageResource(R.drawable.home_black);
        productImage.setImageResource(R.drawable.productred);
        favImage.setImageResource(R.drawable.favblack);
        cartImage.setImageResource(R.drawable.cart_black);
        aboutImage.setImageResource(R.drawable.profile_black);

        fragmentTransaction.commit();
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



                } else {



                }
                break;
        }
    }
    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){



        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        }
    }

    //Getting updated response from Productdetail
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityresult","called");
      /*  if(requestCode==111) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("productTag");
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
        */
        if(requestCode==112)
        {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("cartTag");
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }



    }
    private int dipToPixels(int dip) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        return (int) px;
    }
    public void onDestroy()
    {
        Log.d("maindestory","here");

        Glide.get(this).clearMemory();

        super.onDestroy();
    }





}

