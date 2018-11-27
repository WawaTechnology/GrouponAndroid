package com.example.unsan.grouponebuy.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.unsan.grouponebuy.R;
import com.example.unsan.grouponebuy.activities.MyApplication;
import com.example.unsan.grouponebuy.adapter.ProductListAdapter;
import com.example.unsan.grouponebuy.helpers.CustomRequest;
import com.example.unsan.grouponebuy.helpers.GlobalProvider;
import com.example.unsan.grouponebuy.helpers.Utf8JsonRequest;
import com.example.unsan.grouponebuy.model.FavouriteList;
import com.example.unsan.grouponebuy.model.Product;
import com.example.unsan.grouponebuy.model.ProductList;
import com.example.unsan.grouponebuy.model.Result;
import com.example.unsan.grouponebuy.network.Constants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment  {
    RecyclerView favrecyclerView;
    List<Product> favouriteList;
    ProductListAdapter productListAdapter;
    GlobalProvider globalProvider;
    ImageView emptyFavImg;
  public static final String TAG = "FavTag";
    RequestQueue requestQueue;
    Utf8JsonRequest utf8JsonRequest;

    //Utf8JsonRequest utf8JsonRequest;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //MyApplication.getRefWatcher(getActivity()).watch(this);
        favouriteList=new ArrayList<>();
        globalProvider=GlobalProvider.getGlobalProviderInstance(getContext().getApplicationContext());






             //   Product product=new Product("Brocolli","Product Description",getContext().getDrawable(R.drawable.brocolli),5,"kg","India",ar);
               // productList.add(product);




    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_favourite,container,false);
        favrecyclerView=view.findViewById(R.id.show_fav);
        productListAdapter=new ProductListAdapter(getContext(),favouriteList);
        favrecyclerView.setAdapter(productListAdapter);
        emptyFavImg=(ImageView) view.findViewById(R.id.empty_fav);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        favrecyclerView.setLayoutManager(linearLayoutManager);
        if(globalProvider.isLogin()) {

            emptyFavImg.setVisibility(View.GONE);
        }
        else
        {
            emptyFavImg.setVisibility(View.VISIBLE);
            favrecyclerView.setVisibility(View.GONE);
        }

        return view;
    }
    public void onResume()
    {
        Log.d("favresumecalled","called");
        if(globalProvider.isLogin())
        {
            getFavourite();
        }
        super.onResume();

    }

    public void onPause()
    {
        super.onPause();
        Log.d("favpausecalled","called");
    }
    public void onStop()
    {
        super.onStop();
        Log.d("favstopcalled","called");


            Gson gson=new Gson();
            String productList=gson.toJson(globalProvider.cartList);
            SharedPreferences sharedPreferences=getContext().getApplicationContext().getSharedPreferences("productListFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("productList",productList);
            editor.apply();
          if(globalProvider.getRequestQueue()!=null)
          {
              globalProvider.getRequestQueue().cancelAll(TAG);
          }




            super.onStop();

    }
    public FavoriteFragment()
    {

    }



    private void getFavourite() {
        favouriteList.clear();
        if(globalProvider.getCustomer()!=null)
        {
            String id=globalProvider.getCustomer().customer_id;
            String url= Constants.favouriteUrl+"/"+id;
          //  Log.d("urlr",url);
             utf8JsonRequest=new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    JsonFactory jsonFactory = new JsonFactory();
                    ObjectMapper objectMapper = new ObjectMapper();

                    try {
                        JsonParser jsonParser = jsonFactory.createParser(response);
                        Result favResult = (Result) objectMapper.readValue(jsonParser, Result.class);

                        for(Product cartProduct:globalProvider.cartList)
                        {
                            for(Product product:favResult.getCustomer().getFavoriteList())
                            {
                                if(product.getId().equals(cartProduct.getId()))
                                {
                                    product.setTotalNumber(cartProduct.getTotalNumber());
                                    //todo check if break will be added
                                    break;

                                }
                            }
                        }

                     favouriteList.addAll(favResult.getCustomer().getFavoriteList());
                     if(favouriteList.size()>0)
                     {
                         emptyFavImg.setVisibility(View.GONE);
                     }
                     else
                         emptyFavImg.setVisibility(View.VISIBLE);

                     productListAdapter.notifyDataSetChanged();


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

                    String message=null;
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
                    Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();

                }
            });
           utf8JsonRequest.setTag(TAG);

            globalProvider.addRequest(utf8JsonRequest);



        }



    }



        }

