package com.easybuy.sg.grouponebuy.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.adapter.HotAdapter;
import com.easybuy.sg.grouponebuy.adapter.ProductListAdapter;
import com.easybuy.sg.grouponebuy.helpers.CustomRequest;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.helpers.Utf8JsonRequest;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.model.ProductList;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.easybuy.sg.grouponebuy.utils.GridAutofitLayoutManager;
import com.easybuy.sg.grouponebuy.utils.VarColumnGridLayoutManager;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements  HotAdapter.HotInterface{

    List<String> hotItems;
    HotAdapter hotAdapter;
    RecyclerView hotRecyclerView;
    String language;
    RecyclerView searchRecycler;
    ProductListAdapter productListAdapter;
    List<Product> productResults;
    TextView kewordText;
    int pageNumber=1;
    int pageSize=5;
    TextView canceText;
    ImageView searchView;
    LinearLayout endOfPageLayout;

    EditText searchText;

    GlobalProvider globalProvider;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        globalProvider=GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        setContentView(R.layout.search_activity);
        hotRecyclerView=(RecyclerView) findViewById(R.id.hot_list);
        endOfPageLayout=(LinearLayout) findViewById(R.id.end_of_page);
        kewordText=(TextView) findViewById(R.id.keyword);
        searchText=(EditText) findViewById(R.id.searchText);
        canceText=(TextView) findViewById(R.id.cancel);
        searchRecycler=(RecyclerView) findViewById(R.id.product_search_recycler);
        searchView=(ImageView) findViewById(R.id.searchview);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        int width = displayMetrics.widthPixels;
        productResults=new ArrayList<>();
        productListAdapter=new ProductListAdapter(this,productResults);
        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        searchRecycler.setAdapter(productListAdapter);
        searchRecycler.setLayoutManager(linearLayoutManager1);
        language=Constants.getLanguage(getApplicationContext());


        hotItems=new ArrayList<>();
        hotAdapter=new HotAdapter(this,hotItems,this);
        hotRecyclerView.setAdapter(hotAdapter);
        //VarColumnGridLayoutManager varColumnGridLayoutManager=new VarColumnGridLayoutManager(this,width/2-40);
        FlexboxLayoutManager flexboxLayoutManager=new FlexboxLayoutManager(SearchActivity.this);

        hotRecyclerView.setLayoutManager(flexboxLayoutManager);



/**
 * Helper class to set span size for grid items based on orientation and device type
 */





    /*    GridLayoutManager.SpanSizeLookup onSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mHomeListAdapter.getItemViewType(position) == TYPE_PREMIUM ? 2 : 1;
            }
        };
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        hotRecyclerView.setLayoutManager(linearLayoutManager);
        */
        canceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(searchText.getText().toString()))
                {

                    hotRecyclerView.setVisibility(View.VISIBLE);
                    productResults.clear();
                    productListAdapter.notifyDataSetChanged();


                }
                else {
                    getProducts(searchText.getText().toString());
                    // searchRecycler.setVisibility(View.VISIBLE);
                    hotRecyclerView.setVisibility(View.GONE);
                }

            }
        });





                getHotItems();
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(searchText.getText().toString()))
                {

                   hotRecyclerView.setVisibility(View.VISIBLE);
                   productResults.clear();
                   productListAdapter.notifyDataSetChanged();


                }
                else {
                    getProducts(searchText.getText().toString());
                   // searchRecycler.setVisibility(View.VISIBLE);
                    hotRecyclerView.setVisibility(View.GONE);
                }

            }
        });
     }


    private void getHotItems() {
        Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.POST, Constants.hotKeywordUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Log.d("checkresponse",response);
                JsonFactory jsonFactory = new JsonFactory();
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonParser jsonParser = jsonFactory.createParser(response);
                    ProductList products = (ProductList) objectMapper.readValue(jsonParser, ProductList.class);
                    if (products.getStatus() == 0) {
                        int totalproducts = products.getProducts().size();
                       // Log.d("checktotalproducts",totalproducts+"");
                        if(language.equals("english")) {

                            for (Product product : products.getProducts()) {
                                hotItems.add(product.getNameEn());
                            }
                        }else
                        {
                            for (Product product : products.getProducts()) {
                                hotItems.add(product.getNameCh());
                            }
                        }
                        endOfPageLayout.setVisibility(View.GONE);
                        hotAdapter.notifyDataSetChanged();


                    }
                }
                catch (JsonParseException e) {
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

            }
        });
        globalProvider.addRequest(utf8JsonRequest);


    }
    public void onResume()
    {
        if(globalProvider.cartList.isEmpty())
        {
            for(Product product:productResults)
            {
                if(product.getTotalNumber()>0)
                {
                    product.setTotalNumber(0);
                }
            }
            productListAdapter.notifyDataSetChanged();
        }
        else
        {
            for(Product cartProduct:globalProvider.cartList)
            {
                for(Product product:productResults) {

                    if(cartProduct.getId().equals(product.getId()))
                    {
                       // Log.d("changingproduct","here");
                        product.setTotalNumber(cartProduct.getTotalNumber());
                        break;
                    }
                }
            }
            productListAdapter.notifyDataSetChanged();
        }

        super.onResume();

    }
    protected void onStop()
    {
        super.onStop();

        Gson gson=new Gson();
        String productList=gson.toJson(globalProvider.cartList);
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("productListFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("productList",productList);
        editor.apply();




    }


    @Override
    public void onHotClicked(String productName) {
        searchText.setText(productName);




    }

    private void getProducts(String productName) {
        if(productName!=null) {

            productResults.clear();
            productListAdapter.notifyDataSetChanged();
            Map<String, String> params = new HashMap<>();
            params.put("keyword", productName);
            params.put("isAdmin", "false");
            String url = Constants.searchUrl + pageNumber + "/" + pageSize;
            CustomRequest customRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                   // Log.d("checkresponse", response.toString());
                    JsonFactory jsonFactory = new JsonFactory();
                    ObjectMapper objectMapper = new ObjectMapper();

                    try {
                        JsonParser jsonParser = jsonFactory.createParser(response.toString());
                        ProductList products = (ProductList) objectMapper.readValue(jsonParser, ProductList.class);
                        if (products.getStatus() == 0) {
                            int totalproducts = products.getProducts().size();
                           // Log.d("checktotalproducts", totalproducts + "");
                            for (Product product : products.getProducts()) {
                                for (Product cartProduct : globalProvider.cartList) {

                                    if (product.getId().equals(cartProduct.getId())) {

                                        product.setTotalNumber(cartProduct.getTotalNumber());
                                        break;
                                    }
                                }
                            }
                            productResults.addAll(products.getProducts());
                            productListAdapter.notifyDataSetChanged();
                            hotRecyclerView.setVisibility(View.GONE);
                            kewordText.setVisibility(View.GONE);
                            endOfPageLayout.setVisibility(View.GONE);


                        }
                        else if(products.getStatus()==2)
                        {
                            endOfPageLayout.setVisibility(View.VISIBLE);

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

                }
            });
            globalProvider.addRequest(customRequest);
        }

    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}


