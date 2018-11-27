package com.example.unsan.grouponebuy.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.unsan.grouponebuy.R;
import com.example.unsan.grouponebuy.adapter.SpecialDetailAdapter;
import com.example.unsan.grouponebuy.helpers.GlobalProvider;
import com.example.unsan.grouponebuy.model.CategorySpecial;
import com.example.unsan.grouponebuy.model.Product;
import com.example.unsan.grouponebuy.network.Constants;

import java.util.ArrayList;
import java.util.List;

public class SpecialCategoryLayout extends AppCompatActivity {
    TextView categoryName;
    ImageView splCategoryImg;
    SpecialDetailAdapter specialDetailAdapter;
    RecyclerView productRecycler;
    GlobalProvider globalProvider;
    CategorySpecial categorySpecial;
    ImageView backButton;
    List<Product> productList;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.special_categorylayout);
        categoryName=(TextView) findViewById(R.id.categoryname);
        splCategoryImg=(ImageView) findViewById(R.id.img_cover);
        backButton=(ImageView) findViewById(R.id.back);
        globalProvider=GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        productRecycler=(RecyclerView) findViewById(R.id.item_recycler);
        Intent intent=getIntent();
         categorySpecial=(CategorySpecial)intent.getSerializableExtra("category");
         String lang=Constants.getLanguage(this);

         if(lang.equals("english"))
        categoryName.setText(categorySpecial.getNameEn());
         else
             categoryName.setText(categorySpecial.getNameCh());

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;






        Glide.with(this).load(Constants.baseUrlStr+categorySpecial.getImage()).override(width,height).into(splCategoryImg);
         productList=new ArrayList<>();
        for(Product product:categorySpecial.getProductList())
        {
            if(product.isOnShelf()==true)
            {
                productList.add(product);
            }
        }
        specialDetailAdapter=new SpecialDetailAdapter(SpecialCategoryLayout.this,productList);
        productRecycler.setAdapter(specialDetailAdapter);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2){
            @Override
            public boolean canScrollVertically() {
                return false;
            }

        };

        productRecycler.setLayoutManager(gridLayoutManager);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





    }
    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        Log.d("onactivitypdresult","called");
        Log.d("checkrequestcode",requestCode+"");
        Log.d("resultcode",resultCode+"");
        Log.d("resultok", Activity.RESULT_OK+"");
        //todo check why result code is -1
        if(requestCode==101&&resultCode==Activity.RESULT_OK)
        {
            Log.d("checkfunc","here");
            Product product=(Product)data.getSerializableExtra("productupdated");
            for(Product prod:productList)
            {
                if(prod.getId().equals(product.getId()))
                {
                    Log.d("checkpdupdate","here");
                    prod.setTotalNumber(product.getTotalNumber());
                    break;

                }
            }
            specialDetailAdapter.notifyDataSetChanged();
        }
    }
    */
    public void onResume()
    {

        if(globalProvider.cartList.size()>0) {
            for (Product cartProduct : globalProvider.cartList) {
                for (Product product : categorySpecial.getProductList()) {
                    if (product.getId().equals(cartProduct.getId())&&product.getTotalNumber()!=cartProduct.getTotalNumber()) {

                        product.setTotalNumber(cartProduct.getTotalNumber());
                        Log.d("setquantityfor", product.getNameEn());
                        break;
                    }
                }
            }
            // if cart doesntot have product,but product has quantity>0
            for(Product product:productList)
            {
                if(product.getTotalNumber()>0) {
                    if (!globalProvider.cartList.contains(product)) {
                        product.setTotalNumber(0);


                    }
                }
            }

        }
        else
        {
            for (Product product : productList) {
                if (product.getTotalNumber()>0) {

                    product.setTotalNumber(0);

                }
            }
        }


        specialDetailAdapter.notifyDataSetChanged();
        super.onResume();
    }
    public void onDestroy()
    {

        super.onDestroy();
    }
}
