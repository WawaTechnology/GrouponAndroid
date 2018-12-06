package com.easybuy.sg.grouponebuy.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.adapter.ProductListAdapter;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.helpers.Utf8JsonRequest;
import com.easybuy.sg.grouponebuy.model.Category;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.model.ProductList;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.easybuy.sg.grouponebuy.utils.PaginationScrollListener;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.TabLayout.*;
import static java.security.AccessController.getContext;

public class ProductActivity extends AppCompatActivity implements ProductListAdapter.ChangeListener {
    TabLayout tablayout;
    List<Product> productList;
    ProductListAdapter productListAdapter;
    RecyclerView recyclerView;
    LinearLayout linearLayout;
    ProgressBar progressBar;
    GlobalProvider globalProvider;
    TextView badgeText;
    ImageView topButton,cartButton;

    int sort, page;
    String productId;
    int pageSize = 10;
    String categoryId;
    TextView priceText;


    private boolean isLoading = false;
    private boolean isLastPage = false;
    int TOTAL_PAGES = 2;
    private ArrayList<String> categoryIdList;
    ImageView mImageViewCustom;
    ImageView backButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        tablayout = (TabLayout) findViewById(R.id.tab_layout);
        topButton=(ImageView) findViewById(R.id.uparrow);
        cartButton=(ImageView) findViewById(R.id.cart);
        recyclerView = (RecyclerView) findViewById(R.id.product_recycler);
        linearLayout = (LinearLayout) findViewById(R.id.searchbar_ht);
        badgeText=(TextView) findViewById(R.id.badge);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        backButton=(ImageView) findViewById(R.id.back);
        productList = new ArrayList<>();
        globalProvider = GlobalProvider.getGlobalProviderInstance(getApplicationContext());

        sort = 1;
        page = 1;
        categoryIdList = new ArrayList<>();


        // linearLayout.getLayoutParams().height =102;
        Intent intent = getIntent();
        categoryId = (String) intent.getStringExtra("categoryId");
        categoryIdList = intent.getStringArrayListExtra("categoryArray");


        productListAdapter = new ProductListAdapter(this, productList,ProductActivity.this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final View parent = (View) backButton.getParent();  // button: the view you want to enlarge hit area
        parent.post( new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                backButton.getHitRect(rect);
                rect.top -= 10;    // increase top hit area
                rect.left -= 10;   // increase left hit area
                rect.bottom += 10; // increase bottom hit area
                rect.right += 50;  // increase right hit area
                parent.setTouchDelegate( new TouchDelegate( rect , backButton));
            }
        });



        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                page += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);

            }

            @Override
            public int getTotalPageCount() {
                //Todo
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        topButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.scrollToPosition(0);
            }
        });
        cartButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, MainActivity.class);

                globalProvider.adjustToStart = 3;
                startActivity(intent);
            }
        });


        tablayout.addTab(tablayout.newTab().setText(getText(R.string.popular)), 0);
        tablayout.addTab(tablayout.newTab().setText(getText(R.string.new_tag)), 1);
        tablayout.addTab(tablayout.newTab().setText(getText(R.string.price)), 2);

        tablayout.addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {

                if (tab.getText().equals(getString(R.string.new_tag))) {
                    sort = 0;
                   // priceText.setTextColor(getResources().getColor(R.color.grey_dark));
                    tablayout.getTabAt(2).setCustomView(null);


                    page = 1;
                    isLastPage = false;
                    productList.clear();
                    productListAdapter.notifyDataSetChanged();
                    getProducts();


                } else if (tab.getText().toString().equals(getString(R.string.price))) {
                    if (sort != 2) {
                        sort = 2;
                        View mCustomView = LayoutInflater.from(ProductActivity.this).inflate(R.layout.price_tab, null);
                        mImageViewCustom = (ImageView) mCustomView.findViewById(R.id.icon);
                        priceText=(TextView) mCustomView.findViewById(R.id.price);
                        tablayout.getTabAt(2).setCustomView(mCustomView);


                        mImageViewCustom.setImageResource(R.drawable.lth);

                    } else {
                        sort = 3;
                        View mCustomView = LayoutInflater.from(ProductActivity.this).inflate(R.layout.price_tab, null);
                        mImageViewCustom = (ImageView) mCustomView.findViewById(R.id.icon);
                        priceText=(TextView) mCustomView.findViewById(R.id.price);
                        tablayout.getTabAt(2).setCustomView(mCustomView);
                        mImageViewCustom.setImageResource(R.drawable.htl);
                    }
                    priceText.setTextColor(getResources().getColor(R.color.red));
                    page = 1;
                    isLastPage = false;
                    productList.clear();
                    productListAdapter.notifyDataSetChanged();
                    getProducts();

                } else if (tab.getText().equals(getString(R.string.popular))) {
                    tablayout.getTabAt(2).setCustomView(null);
                    sort = 1;
                  //  priceText.setTextColor(getResources().getColor(R.color.grey_dark));


                    page = 1;
                    isLastPage = false;
                    productList.clear();
                    productListAdapter.notifyDataSetChanged();
                    getProducts();
                }

            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {

                if (tab.getText().equals(getString(R.string.price))) {
                    if (sort == 2) {
                        sort = 3;
                        View view = tab.getCustomView();
                        if (view != null) {
                            ImageView icon = view.findViewById(R.id.icon);
                            icon.setImageResource(R.drawable.htl);
                        }

                    } else {
                        sort = 2;
                        View view = tab.getCustomView();
                        if (view != null) {
                            ImageView icon = view.findViewById(R.id.icon);
                            icon.setImageResource(R.drawable.lth);
                        }
                    }
                    //change tab icon

                    page = 1;
                    isLastPage = false;
                    productList.clear();
                    productListAdapter.notifyDataSetChanged();

                    getProducts();
                }
            }


        });

        getProducts();
    }

    public void onResume() {
        super.onResume();


        if (globalProvider.cartList.isEmpty()) {
            for (Product product : productList) {
                if (product.getTotalNumber() > 0) {
                    product.setTotalNumber(0);
                }
            }
            productListAdapter.notifyDataSetChanged();
            badgeText.setVisibility(GONE);
        } else {
            Log.d("pdsize", productList.size() + "");
            badgeText.setText(globalProvider.cartList.size()+"");
            badgeText.setVisibility(VISIBLE);


            for (Product cartProduct : globalProvider.cartList) {
                for (Product product : productList) {

                    if (cartProduct.getId().equals(product.getId()) && cartProduct.getTotalNumber() != product.getTotalNumber()) {
                        Log.d("changingproduct", "here");
                        Log.d("cartProductNumber", cartProduct.getTotalNumber() + "");
                        product.setTotalNumber(cartProduct.getTotalNumber());
                        break;
                    }
                }
            }
            //trial


            for (Product product : productList) {
                if (product.getTotalNumber() > 0) {
                    if (!globalProvider.cartList.contains(product)) {
                        product.setTotalNumber(0);


                    }
                }
            }


            productListAdapter.notifyDataSetChanged();
        }



    }
 /*   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        Log.d("onactivitypdresult","called");
        Log.d("checkrequestcode",requestCode+"");
        Log.d("resultcode",resultCode+"");
        Log.d("resultok",Activity.RESULT_OK+"");
        //todo check why result code is -1
        if(requestCode==111&&resultCode==Activity.RESULT_OK)
        {
            Log.d("checkfunc","here");
            Product product=(Product)data.getSerializableExtra("productupdated");
            for(Product prod:productList)
            {
                if(prod.getId().equals(product.getId()))
                {
                    Log.d("checkpdupdate","here");
                    Log.d("checktotalnum",product.getTotalNumber()+"");
                    prod.setTotalNumber(product.getTotalNumber());
                    break;

                }
            }
            productListAdapter.notifyDataSetChanged();
        }
    }
    */


    public void getProducts() {
        if (categoryId != null) {

            String url = Constants.productUrl + "/" + categoryId + "/" + sort + "/" + page + "/" + pageSize;
            Log.d("url", url);
            Utf8JsonRequest utf8JsonRequest = new Utf8JsonRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("response", response);
                    JsonFactory jsonFactory = new JsonFactory();
                    ObjectMapper objectMapper = new ObjectMapper();

                    try {
                        JsonParser jsonParser = jsonFactory.createParser(response);
                        ProductList products = (ProductList) objectMapper.readValue(jsonParser, ProductList.class);
                        if(products.getStatus()==2)
                        {
                            progressBar.setVisibility(GONE);
                            Toast.makeText(ProductActivity.this,"Sorry!No Products Available in this category",Toast.LENGTH_LONG).show();
                        }
                       else if (products.getStatus() == 0) {
                            int totalproducts = products.getTotalNumber();
                            Log.d("totalprod", totalproducts + "");


                            TOTAL_PAGES = (pageSize + totalproducts - 1) / pageSize;


                            for (Product cartProduct : globalProvider.cartList) {
                                for (Product product : products.getProducts()) {
                                    if (product.getId().equals(cartProduct.getId())) {

                                        product.setTotalNumber(cartProduct.getTotalNumber());
                                        Log.d("getprodmnum", product.getNameEn() + " " + cartProduct.getTotalNumber());
                                        break;
                                    }
                                }
                            }

                            productList.addAll(products.getProducts());
                            progressBar.setVisibility(View.GONE);
                            productListAdapter.notifyDataSetChanged();
                            Log.d("checkpage", page + "");
                            Log.d("checktotalpage", TOTAL_PAGES + "");

                            if (page <= TOTAL_PAGES) productListAdapter.addLoadingFooter();
                            else {
                                // productListAdapter.addLoadingFooter();
                                isLastPage = true;

                            }
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
            globalProvider.addRequest(utf8JsonRequest);
        } else {
            String url = Constants.productUrl + "/" + "all" + "/" + sort + "/" + page + "/" + pageSize;

            JSONArray jsonArray = new JSONArray();
            for (String id : categoryIdList) {
                jsonArray.put(id);
            }
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("categories", jsonArray);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        JsonFactory jsonFactory = new JsonFactory();
                        ObjectMapper objectMapper = new ObjectMapper();

                        try {
                            JsonParser jsonParser = jsonFactory.createParser(response.toString());
                            ProductList products = (ProductList) objectMapper.readValue(jsonParser, ProductList.class);
                            if (products.getStatus() == 0) {
                                int totalproducts = products.getTotalNumber();
                                Log.d("totalprod", totalproducts + "");


                                TOTAL_PAGES = (pageSize + totalproducts - 1) / pageSize;


                                for (Product cartProduct : globalProvider.cartList) {
                                    for (Product product : products.getProducts()) {
                                        if (product.getId().equals(cartProduct.getId())) {

                                            product.setTotalNumber(cartProduct.getTotalNumber());
                                            Log.d("getprodmnum", product.getNameEn() + " " + cartProduct.getTotalNumber());
                                            break;
                                        }
                                    }
                                }

                                productList.addAll(products.getProducts());
                                progressBar.setVisibility(View.GONE);
                                productListAdapter.notifyDataSetChanged();
                                Log.d("checkpage", page + "");
                                Log.d("checktotalpage", TOTAL_PAGES + "");

                                if (page <= TOTAL_PAGES) productListAdapter.addLoadingFooter();
                                else {
                                    // productListAdapter.addLoadingFooter();
                                    isLastPage = true;

                                }
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
                globalProvider.addRequest(jsonObjectRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    @Override
    protected void onStop() {
        super.onStop();

        Gson gson = new Gson();
        String productList = gson.toJson(globalProvider.cartList);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("productListFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("productList", productList);
        editor.apply();


    }

    private void loadNextPage() {
        Log.d("TAG", "loadNextPage: " + page);
        if (categoryId != null) {
            String url = Constants.productUrl + "/" + categoryId + "/" + sort + "/" + page + "/" + pageSize;


            Utf8JsonRequest utf8JsonRequest = new Utf8JsonRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("nextresponse", response);
                    JsonFactory jsonFactory = new JsonFactory();
                    ObjectMapper objectMapper = new ObjectMapper();

                    try {
                        JsonParser jsonParser = jsonFactory.createParser(response);
                        ProductList products = (ProductList) objectMapper.readValue(jsonParser, ProductList.class);
                        productListAdapter.removeLoadingFooter();
                        isLoading = false;


                        if (products.getStatus() == 0) {
                            for (Product cartProduct : globalProvider.cartList) {
                                for (Product product : products.getProducts()) {
                                    if (product.getId().equals(cartProduct.getId())) {

                                        product.setTotalNumber(cartProduct.getTotalNumber());
                                        Log.d("getprodnum", product.getNameEn() + " " + cartProduct.getTotalNumber());
                                        break;
                                    }
                                }
                            }

                            productList.addAll(products.getProducts());
                            productListAdapter.notifyDataSetChanged();
                            // productListAdapter.notifyDataSetChanged();

                            if (page < TOTAL_PAGES) productListAdapter.addLoadingFooter();
                            else isLastPage = true;
                        } else if (products.getStatus() == 2) {
                            isLastPage = true;
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
            globalProvider.addRequest(utf8JsonRequest);
        } else {
            String url = Constants.productUrl + "/" + "all" + "/" + sort + "/" + page + "/" + pageSize;

            JSONArray jsonArray = new JSONArray();
            for (String id : categoryIdList) {
                jsonArray.put(id);
            }
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("categories", jsonArray);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("nextresponse", response.toString());
                        JsonFactory jsonFactory = new JsonFactory();
                        ObjectMapper objectMapper = new ObjectMapper();

                        try {
                            JsonParser jsonParser = jsonFactory.createParser(response.toString());
                            ProductList products = (ProductList) objectMapper.readValue(jsonParser, ProductList.class);
                            productListAdapter.removeLoadingFooter();
                            isLoading = false;


                            if (products.getStatus() == 0) {
                                for (Product cartProduct : globalProvider.cartList) {
                                    for (Product product : products.getProducts()) {
                                        if (product.getId().equals(cartProduct.getId())) {

                                            product.setTotalNumber(cartProduct.getTotalNumber());
                                            Log.d("getprodnum", product.getNameEn() + " " + cartProduct.getTotalNumber());
                                            break;
                                        }
                                    }
                                }

                                productList.addAll(products.getProducts());
                                productListAdapter.notifyDataSetChanged();
                                // productListAdapter.notifyDataSetChanged();

                                if (page <= TOTAL_PAGES) productListAdapter.addLoadingFooter();
                                else isLastPage = true;
                            } else if (products.getStatus() == 2) {
                                isLastPage = true;
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
                globalProvider.addRequest(jsonObjectRequest);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onChange() {
        if(globalProvider.cartList.isEmpty())
        {
            badgeText.setVisibility(GONE);
        }
        else
        {
            badgeText.setText(globalProvider.cartList.size()+"");
            badgeText.setVisibility(VISIBLE);
        }
    }
}
