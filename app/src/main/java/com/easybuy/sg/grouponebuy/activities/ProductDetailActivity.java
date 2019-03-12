package com.easybuy.sg.grouponebuy.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.adapter.ProductDetailViewPagerAdapter;
import com.easybuy.sg.grouponebuy.adapter.ProductSpecAdapter;
import com.easybuy.sg.grouponebuy.adapter.SpecialCategory;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.helpers.Utf8JsonRequest;
import com.easybuy.sg.grouponebuy.model.Category;
import com.easybuy.sg.grouponebuy.model.CategoryProduct;
import com.easybuy.sg.grouponebuy.model.CategoryS;
import com.easybuy.sg.grouponebuy.model.CategorySpecial;
import com.easybuy.sg.grouponebuy.model.CategorySpecialList;
import com.easybuy.sg.grouponebuy.model.Customer;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.model.SpecialCategoryList;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.easybuy.sg.grouponebuy.utils.CircleBadgeView;
import com.easybuy.sg.grouponebuy.utils.SpanningPriceTextView;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;
import static java.lang.Integer.parseInt;

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailViewPagerAdapter.OnItemClickListener {

    ViewPager viewPager;
    List<ViewGroup> listViews;
    TextView productNameText, originText, specText,  originalPriceText, favText, soldOutText;
    TextView priceText;
    ProductDetailViewPagerAdapter productDetailViewPagerAdapter;
    ProductSpecAdapter productSpecAdapter;
    List<CategoryProduct> categoryProducts;
    int saleAdapterposition;
    //TextView spec1TextView,spec2TextView;
    LinearLayout multipleSpecialLayout;
    LinearLayout indicator;
   TextView descriptionText;
    ImageView favImgView;
    boolean favClicked;
    GlobalProvider globalProvider;
    Product product;
    LinearLayout cartLinearLayout, productSelectedLayout;
    Button cartAddButton;
    ImageView addButton, subButton, backButton;
    TextView quantityText;
    LinearLayout replacementLayout;
    List<Product> favoriteList;
    ScrollView scrollView;
    SpecialCategory multipleSameProduct;
    RecyclerView multipleSpecLayout;


    private CircleBadgeView buyNumView;
    private CircleBadgeView numBadge;

    ImageView cartImageButton, cartButton2;
    private int DEFAULT_LR_PADDING_DIP = 5;
   // private int saleMultipleAdapterPosition;


    @Override
    public void onBackPressed() {

    Intent returnIntent = getIntent();

        returnIntent.putExtra("productupdated", product);
        if(saleAdapterposition>-1)
        {
            returnIntent.putExtra("saleAdapterPosition",saleAdapterposition);
           // returnIntent.putExtra("saleMultipleAdapterPosition",saleMultipleAdapterPosition);
        }

        setResult(Activity.RESULT_OK, returnIntent);


        finish();

        super.onBackPressed();

    }

    @Override
    public void onStop() {
        if(globalProvider.isLogin()) {

            Gson gson = new Gson();
            String productList = gson.toJson(globalProvider.cartList);
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("productListFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("productList", productList);
            editor.apply();
            Customer customer = Constants.getCustomer(getApplicationContext());
            customer.setFavoriteList(favoriteList);
            Constants.setCustomer(getApplicationContext(), customer);
        }
        super.onStop();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        categoryProducts=new ArrayList<>();
        //spec1TextView=(TextView) findViewById(R.id.spec1);
        //spec2TextView=(TextView) findViewById(R.id.spec2);


       // scrollView=(ScrollView) findViewById(R.id.scroll_view);
        Display display = getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        int width = size. x;
        int height = size. y;
      //  multipleSpecialLayout=(LinearLayout) findViewById(R.id.multipleViewLayout);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        multipleSpecLayout=(RecyclerView) findViewById(R.id.spec_list);

        Log.d("heights",dpHeight+"");
        multipleSameProduct=new SpecialCategory();
        favoriteList=new ArrayList<>();
        backButton = (ImageView) findViewById(R.id.bkbutton);
        cartImageButton = (ImageView) findViewById(R.id.carticonbutton);
        cartButton2 = (ImageView) findViewById(R.id.carticonbutton2);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        productNameText = (TextView) findViewById(R.id.product_name);
        originText = (TextView) findViewById(R.id.origin);

        specText = (TextView) findViewById(R.id.specification);
        favText = (TextView) findViewById(R.id.favtext);
        favImgView = (ImageView) findViewById(R.id.fav);
        priceText = (TextView) findViewById(R.id.price);
        soldOutText = (TextView) findViewById(R.id.soldout_text);
        quantityText = (TextView) findViewById(R.id.quantity);
        addButton = (ImageView) findViewById(R.id.add_quantity);
        subButton = (ImageView) findViewById(R.id.sub_quantity);
        originalPriceText = (TextView) findViewById(R.id.origprice);
        descriptionText = (TextView) findViewById(R.id.desc);
        cartLinearLayout = (LinearLayout) findViewById(R.id.prodCartLayout);
        productSelectedLayout = (LinearLayout) findViewById(R.id.prodSelectedLayout);
        cartAddButton = (Button) findViewById(R.id.carttext);
        replacementLayout = (LinearLayout) findViewById(R.id.replacement_layout);
        listViews = new ArrayList<>();
        indicator = (LinearLayout) findViewById(R.id.indicator);
        globalProvider = GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        buyNumView = new CircleBadgeView(this, cartButton2);
        buyNumView.setTextColor(Color.WHITE);
        buyNumView.setBackgroundColor(getResources().getColor(R.color.red));
        numBadge = new CircleBadgeView(this, cartImageButton);
        numBadge.setBackgroundColor(getResources().getColor(R.color.red));



        cartButton2.setPadding(0,-25,10,0);
        cartImageButton.setPadding(0,-25,10,0);

        //cycleTextViewExpansion(descriptionText);
        // expandCollapsedByMaxLines(descriptionText);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("productupdated", product);
                if(saleAdapterposition>-1)
                {
                    returnIntent.putExtra("saleAdapterPosition",saleAdapterposition);
                   // returnIntent.putExtra("saleMultipleAdapterPosition",saleMultipleAdapterPosition);
                }
                setResult(Activity.RESULT_OK, returnIntent);

                finish();

            }
        });
        if(globalProvider.isLogin())
        {
            favoriteList.addAll(Constants.getCustomer(getApplicationContext()).getFavoriteList());
        }

        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");
        saleAdapterposition=intent.getIntExtra("saleAdapterPosition",-1);
      //  saleMultipleAdapterPosition=intent.getIntExtra("saleMultipleAdapterPosition",-1);
        Log.d("pdid",product.getId());
        getProductDetail(product.getId());







            /*for(CategoryS categorySpl:product.getCategorySList())
            {
                if(categorySpl.getSequence()==666)
                {
                    getSpecialCategories(categorySpl);
                    break;
                }
            }
            */





      /*  spec1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(product.getNameEn().equals(multipleSameProduct.getProductList().get(0).getNameEn()))
                {
                    Log.d("donothing","spec1");

                }
                else
                {
                   finish();
                   Intent intent=getIntent();


                   intent.putExtra("product",multipleSameProduct.getProductList().get(0));
                   startActivity(intent);
                }
            }
        });
        spec2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(product.getNameEn().equals(multipleSameProduct.getProductList().get(1).getNameEn()))
                {
                    Log.d("donothing","spec2");

                }
                else
                {
                    finish();
                    Intent intent=getIntent();
                    intent.putExtra("product",multipleSameProduct.getProductList().get(1));
                    startActivity(intent);
                }
            }
        });
        */


        List<String> imgList = product.getImageDisplay();
        if (Constants.getLanguage(this).equals("english")) {

            String origin = product.getOriginEn();

            //String imgTransitionName = intent.getStringExtra("imgtransition");
            productNameText.setText(product.getNameEn());
            originText.setText(origin);
            Log.d("getspec", product.getSpecificationEn());
            specText.setText(product.getSpecificationEn());
            if (product.getDescriptionEn() != null) {
                descriptionText.setText(product.getDescriptionEn());
            }
        } else {
            productNameText.setText(product.getNameCh());
            originText.setText(product.getOriginCh());
            specText.setText(product.getSpecificationCh());
            if (product.getDescriptionCh() != null) {
                descriptionText.setText(product.getDescriptionCh());
            }
        }


       // boolean prodSelected = false;


        //todo paste here

        cartImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);

                //todo get activity from backstack,use new intent in mainactivity
               // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

            //    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );


                globalProvider.adjustToStart = 3;
                startActivity(intent);
                //todo remove finish() and pass changed data
               // finish();
            }
        });
        cartButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
              // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );

                globalProvider.adjustToStart = 3;
                startActivity(intent);
                //todo remove finish() and pass changed data
               // finish();

            }
        });
        replacementLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ProductDetailActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.replacement_dialog);

                Button doneButton = (Button) dialog.findViewById(R.id.ok);
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                dialog.show();


             /* AlertDialog.Builder builder=  new AlertDialog.Builder(ProductDetailActivity.this).setTitle("We Promise").setMessage(getString(R.string.after_sale)).setPositiveButton(R.string.ok,new DialogInterface.OnClickListener()
              {

                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {

                  }
              });
                AlertDialog dialog = builder.create();
                dialog.show();
                */


            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product.getStock() > 0) {
                    String num = quantityText.getText().toString();
                    int quant = Integer.parseInt(num);
                    quant += 1;
                    quantityText.setText(String.valueOf(quant));
                    int n = globalProvider.cartList.size();
                    product.setTotalNumber(quant);
                    if (n == 0) {

                        globalProvider.cartList.add(product);
                    } else {
                        for (int i = 0; i < n; i++) {
                            if (globalProvider.cartList.get(i).getId().equals(product.getId())) {
                                globalProvider.cartList.get(i).setTotalNumber(quant);


                                break;
                            }
                            if (i == n - 1) {

                                globalProvider.cartList.add(product);
                            }
                        }

                    }
                   /* SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("customerid",globalProvider.getCustomer().customer_id);
                    editor.putInt(product.getId(),quant);
                    editor.apply();
                    */
                    setCartNum(buyNumView);
                }
                //todo replace imageview with textview saying no stock

                //TODO NOTIFY FRAGMENT PRODUCT OF QUANTITY CHANGED

            }
        });
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = quantityText.getText().toString();
                int quant = Integer.parseInt(num);
                quant -= 1;
                product.setTotalNumber(quant);
                if (quant == 0) {
                    productSelectedLayout.setVisibility(View.GONE);
                    cartLinearLayout.setVisibility(View.VISIBLE);
                   /* SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor= prefs.edit();


                    editor.remove(product.getId());
                    editor.apply();
                    */


                } else {
                    quantityText.setText(String.valueOf(quant));
                  /*  SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor= prefs.edit();


                    editor.putInt(product.getId(),quant);
                    editor.apply();
                    */
                }
                int n = globalProvider.cartList.size();
                for (int i = 0; i < n; i++) {
                    if (globalProvider.cartList.get(i).getId().equals(product.getId())) {
                        if (quant > 0)
                            globalProvider.cartList.get(i).setTotalNumber(quant);
                        else
                            globalProvider.cartList.remove(i);


                        break;
                    }

                }
                if (globalProvider.cartList.isEmpty()) {

                    HideCartNum();

                }
                else


                    setCartNum(numBadge);
            }
        });


       // productNameText.setTransitionName(product.getNameEn());


        if (product.getPriceOriginal() != null && product.getPriceOriginal() > 0) {
            originalPriceText.setVisibility(View.VISIBLE);
            originalPriceText.setText("$ " + product.getPriceOriginal());
            originalPriceText.setPaintFlags(originalPriceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            originalPriceText.setVisibility(View.GONE);
        }
        cartAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (globalProvider.isLogin()) {
                    if (product.getStock() > 0) {


                        productSelectedLayout.setVisibility(View.VISIBLE);
                        product.setTotalNumber(1);
                        globalProvider.cartList.add(product);

                        cartLinearLayout.setVisibility(View.GONE);
                        setCartNum(buyNumView);
                    } else {
                        soldOutText.setVisibility(View.VISIBLE);
                        cartAddButton.setVisibility(View.GONE);
                    }
                } else {
                    Intent intent = new Intent(ProductDetailActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            }
        });

        favImgView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if (globalProvider.isLogin()) {
                    if (!favClicked) {
                        favClicked = true;
                        favImgView.setImageResource(R.drawable.fav_red);
                        favText.setTextColor(getResources().getColor(R.color.red));
                        favText.setText(getString(R.string.favorited));

                      /*  Map<String, String> map = new HashMap<String, String>();
                        // postParam.put("friendIds["+(i++)+"]", object);
                        int val = globalProvider.getCustomer().getFavoriteList().size();
                        Log.d("getfav", "favoriteList[" + (val) + "]");
                        Log.d("prodid", product.getId());
                        map.put("favoriteList[" + (val) + "]", product.getId());
                        */

                        //globalProvider.favoriteList.add(product);
                        favoriteList.add(product);


                    } else {
                        favImgView.setImageResource(R.drawable.fav_grey);
                        favClicked = false;
                        favText.setTextColor(getResources().getColor(R.color.grey_dark));
                        favText.setText(getString(R.string.favorite));
                        Log.d("removefav", product.getId());
                        for (Product globalpd : favoriteList) {
                            if (globalpd.getId().equals(product.getId())) {

                                Log.d("foundeq", "here");

                                favoriteList.remove(globalpd);
                                break;
                            }
                        }

                    }
                    int n = favoriteList.size();
                    String favprds[] = new String[n];
                    for (int i = 0; i < n; i++) {
                        favprds[i] = favoriteList.get(i).getId();
                        Log.d("getfavprodid", favprds[i]);
                    }

                    JSONArray jsonArray = new JSONArray();
                    for (String s : favprds) {
                        jsonArray.put(s);
                    }

                    JsonObjectRequest jsonObjectRequest = null;
                    try {

                        JSONObject obj = new JSONObject();
                        obj.put("favoriteList", jsonArray);
                        String url = Constants.favouriteUrl + "/" + globalProvider.getCustomerId();


                        jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, obj, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("checkfavres", response.toString());

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("geterror", error.toString());


                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    GlobalProvider.addRequest(jsonObjectRequest);


                } else {
                    Intent intent = new Intent(ProductDetailActivity.this, SignInActivity.class);
                    startActivity(intent);

                }
            }
        });
        String price="$ " + product.getPrice();
        String[] each = price.split("\\.");
        each[0]=each[0]+".";
        Spannable spannable = new SpannableString(price);

        spannable.setSpan(new AbsoluteSizeSpan(17, true), 0, each[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        priceText.setText(spannable, TextView.BufferType.SPANNABLE);



       // priceText.setText("$ " + product.getPrice(),TextView.BufferType.SPANNABLE);
        listViews.clear();


        //imgs.clear();
        indicator.removeAllViews();

        if (imgList.size() > 0)

        {
            for (int a = 0; a < imgList.size(); a++) {
                ViewGroup pager = (ViewGroup) getLayoutInflater().inflate(R.layout.viewpager_image, null);

                ImageView imageView = (ImageView) pager.findViewById(R.id.viewpagerimg);
               /* if (a == 0) {

                        imageView.setTransitionName(imgTransitionName);

                }
                */
                Glide.with(ProductDetailActivity.this).load(Constants.newImageUrl + imgList.get(a)).asBitmap().format(PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.ebuylogo).into(imageView);

               //Glide.with(ProductDetailActivity.this).load(Constants.baseUrlStr + imgList.get(a)).asBitmap().format(PREFER_ARGB_8888).override(500, 500).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
                listViews.add(pager);


            }
            productDetailViewPagerAdapter = new ProductDetailViewPagerAdapter(listViews, this);
            viewPager.setAdapter(productDetailViewPagerAdapter);


            if (imgList.size() > 1) {
                for (int a = 0; a < imgList.size(); a++) {
                    if (a == 0) {
                        ImageView imgView = new ImageView(this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15, 15);
                        imgView.setLayoutParams(lp);
                        imgView.setImageResource(R.drawable.selected_dot);
                        indicator.addView(imgView);


                    } else {
                        ImageView imgView = new ImageView(this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15, 15);
                        lp.setMargins(20, 0, 0, 0);
                        imgView.setLayoutParams(lp);
                        imgView.setImageResource(R.drawable.default_dot);
                        // imgs.add(img);
                        indicator.addView(imgView);


                    }
                }
            } else {

                ImageView img = new ImageView(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                        (15, 15);
                //lp.setMargins(5,0,0,0);
                img.setLayoutParams(lp);
                img.setImageResource(R.drawable.selected_dot);
                //imgs.add(img);
                indicator.addView(img);

            }
        }
        Log.d("checkpd", product.getId());
        if (globalProvider != null&&globalProvider.isLogin()) {
            for (Product globalproduct : favoriteList) {
                Log.d("favids", globalproduct.getId());

                if (globalproduct.getId().equals(product.getId())) {
                    favImgView.setImageResource(R.drawable.fav_red);
                    favText.setText(getString(R.string.favorited));
                    favClicked = true;
                    break;
                }
            }
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()

        {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < indicator.getChildCount(); i++) {
                    if (i == position) {
                        ((ImageView) indicator.getChildAt(i)).setImageResource(R.drawable.selected_dot);

                    } else {
                        ((ImageView) indicator.getChildAt(i)).setImageResource(R.drawable.default_dot);
                    }
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
    private void getProductDetail(String id)
    {
        String url=Constants.productUrl+"/"+id;

        Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray categorySArray = jsonObj.getJSONObject("payload").getJSONArray("categorySpecial");
                    for(int j=0;j<categorySArray.length();j++) {
                        int val = categorySArray.getJSONObject(j).getInt("sequence");
                        if (val == 666) {
                            Log.d("categoryId",categorySArray.getJSONObject(j).getString("_id"));


                            getSpecialCategories(categorySArray.getJSONObject(j).getString("_id"));


                          /*  JSONArray productArray = categorySArray.getJSONObject(j).getJSONArray("productList");
                            List<String> productList = new ArrayList<>();
                            Log.d("checkval", val + "");

                            if (productArray != null) {
                                for (int i = 0; i < productArray.length(); i++) {
                                    productList.add(productArray.getString(i));
                                }
                                Log.d("sizeis", productList.size() + "");

                            }
                            */
                            break;

                            //JSONObject categoryS= jsonObj.getJSONObject("payload").getJSONObject("categorySpecial");
                            // int val= categoryS.getInt("sequence");


                        }
                    }
                } catch (JSONException e) {
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


private void getSpecialCategories(final String categoryId) {
        String url=Constants.specialCategoryUrl;
        Log.d("thisurl",url);
        Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                JsonFactory jsonFactory = new JsonFactory();
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonParser jsonParser = jsonFactory.createParser(response);
                    SpecialCategoryList splcategories = (SpecialCategoryList) objectMapper.readValue(jsonParser, SpecialCategoryList.class);

                    int status = splcategories.getStatus();
                    Log.d("statusres",status+"");

                    if (status == 0) {

                        List<SpecialCategory> specialCategoryList = splcategories.getSpecialCategoryList();

                        for (SpecialCategory specialCategory : specialCategoryList) {
                            if (specialCategory.getSequence() != null && (specialCategory.getSequence() == 666 )) {



                                if(categoryId.equals(specialCategory.getId()))
                                {
                                    multipleSpecLayout.setVisibility(View.VISIBLE);
                                    Log.d("pdname",specialCategory.getNameEn());


                                    multipleSameProduct=specialCategory;
                                List<CategoryProduct> products= specialCategory.getProductList();
                                    productSpecAdapter=new ProductSpecAdapter(ProductDetailActivity.this,products,product.getId());
                                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ProductDetailActivity.this,LinearLayoutManager.HORIZONTAL,false);
                                    multipleSpecLayout.setAdapter(productSpecAdapter);
                                    multipleSpecLayout.setLayoutManager(linearLayoutManager);
                                    productSpecAdapter.notifyDataSetChanged();
                                 /*   spec1TextView.setText("$"+specialCategory.getProductList().get(0).getPrice()+"/ "+specialCategory.getProductList().get(0).getSpecificationEn());
                                    spec2TextView.setText("$"+specialCategory.getProductList().get(1).getPrice()+"/ "+specialCategory.getProductList().get(1).getSpecificationEn());
                                    if(product.getNameEn().equals(specialCategory.getProductList().get(0).getNameEn()))
                                    {
                                        spec1TextView.setBackground(getResources().getDrawable(R.drawable.yellow_new));
                                        spec2TextView.setBackground(getResources().getDrawable(R.drawable.table_bkg));
                                    }
                                    else
                                    {
                                        spec2TextView.setBackground(getResources().getDrawable(R.drawable.yellow_new));
                                        spec1TextView.setBackground(getResources().getDrawable(R.drawable.table_bkg));
                                    }
                                    */


                                    break;
                                }






                            }

                        }
                    }

                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }}
        , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        globalProvider.addRequest(utf8JsonRequest);
    }





    public void setCartNum(CircleBadgeView badgeView) {
        Log.d("cartset", "here");


        badgeView.setText(globalProvider.cartList.size() + "");//

        if (globalProvider.cartList.size() >= 10) {

            DEFAULT_LR_PADDING_DIP = 3;
            int paddingPixels = dipToPixels(DEFAULT_LR_PADDING_DIP);
            badgeView.setPadding(paddingPixels, 0, paddingPixels, 0);
            badgeView.setTextSize(10);

        } else {
            DEFAULT_LR_PADDING_DIP = 5;
            int paddingPixels_ = dipToPixels(DEFAULT_LR_PADDING_DIP);
            badgeView.setPadding(paddingPixels_, 0, paddingPixels_, 0);

            badgeView.setTextSize(11);

        }

        //buyNumView.setTextSize(12);

       // badgeView.setBadgeMargin(8, 0);


        badgeView.setBadgePosition(CircleBadgeView.POSITION_TOP_RIGHT);
        badgeView.setGravity(Gravity.RIGHT);
        badgeView.show();


    }

    public void HideCartNum() {
        if (globalProvider.cartList.isEmpty()) {
            buyNumView.hide();
            if(numBadge.isShown())
            numBadge.hide();
        }



    }
    public void onResume()
    {
        boolean prodSelected=false;
        if (globalProvider.cartList.size() > 0) {


            for (int i=0;i< globalProvider.cartList.size();i++) {
                if (globalProvider.cartList.get(i).getId().equals(product.getId())) {
                    cartLinearLayout.setVisibility(View.GONE);
                    productSelectedLayout.setVisibility(View.VISIBLE);
                    prodSelected = true;
                    Log.d("checkquntity",globalProvider.cartList.get(i).getTotalNumber() + "");

                    quantityText.setText(globalProvider.cartList.get(i).getTotalNumber() + "");
                    break;
                }
                if(i==globalProvider.cartList.size()-1)
                {
                    productSelectedLayout.setVisibility(View.GONE);
                    cartLinearLayout.setVisibility(View.VISIBLE);
                }
            }

            if (prodSelected) {
                setCartNum(buyNumView);
            } else
              setCartNum(numBadge);



        }
        else {
            productSelectedLayout.setVisibility(View.GONE);
            cartLinearLayout.setVisibility(View.VISIBLE);
            HideCartNum();
        }

        super.onResume();

    }

    private int dipToPixels(int dip) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        return (int) px;
    }

    @Override
    public void onItemClick(int position) {

    }


}
