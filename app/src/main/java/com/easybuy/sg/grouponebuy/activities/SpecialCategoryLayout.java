package com.easybuy.sg.grouponebuy.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.adapter.SpecialDetailAdapter;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.model.CategorySpecial;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.network.Constants;

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





        String img=null;
        if(lang.equals("english")&&categorySpecial.getImageCorner()!=null)
        {
           img=categorySpecial.getImageCorner();
        }
        else
        {
           img=categorySpecial.getImage();
        }
        Glide.with(this).load(Constants.newImageUrl+img).override(width,height).into(splCategoryImg);
         productList=new ArrayList<>();
        for(Product product:categorySpecial.getProductList())
        {
            if(product.isOnShelf()==true)
            {
                productList.add(product);
            }
        }
        productRecycler.setNestedScrollingEnabled(false);
        specialDetailAdapter=new SpecialDetailAdapter(SpecialCategoryLayout.this,productList);
        productRecycler.setAdapter(specialDetailAdapter);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);





        productRecycler.setLayoutManager(gridLayoutManager);

        backButton.setOnClickListener(new View.OnClickListener() {
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
        //boolean updateAdapter=false;


        if(globalProvider.cartList.size()>0) {

                for (Product product : categorySpecial.getProductList()) {
                    if(globalProvider.cartList.contains(product)) {
                        int index=globalProvider.cartList.indexOf(product);
                        Log.d("checkindex",index+"");
                        Product cartProduct=globalProvider.cartList.get(index);
                        Log.d("checkcartpdname",cartProduct.getNameEn()+" "+cartProduct.getTotalNumber());
                        Log.d("checkprod",product.getNameEn()+" "+product.getTotalNumber());
                        Log.d("checkrr",product.isModified(cartProduct)+"");




                            if (product.isModified(cartProduct)) {

                                product.setTotalNumber(cartProduct.getTotalNumber());
                                Log.d("afterchangprod ",product.getTotalNumber()+"");
                                // updateAdapter=true;
                                Log.d("setquantityfor", product.getNameEn());
                               // break;
                            }

                    }
                    else if(product.getTotalNumber()>0)
                    {
                        product.setTotalNumber(0);
                    }

            }
            // if cart doesntot have product,but product has quantity>0
            /*
            for(Product product:productList)
            {
                if(product.getTotalNumber()>0) {
                    if (!globalProvider.cartList.contains(product)) {
                        product.setTotalNumber(0);
                        //updateAdapter=true;


                    }
                }
            }
            */

        }
        else
        {
            for (Product product : productList) {
                if (product.getTotalNumber()>0) {
                   // updateAdapter=true;

                    product.setTotalNumber(0);

                }
            }
        }
     //   if(updateAdapter)


        specialDetailAdapter.notifyDataSetChanged();
        super.onResume();
    }
    public void onDestroy()
    {

        super.onDestroy();
    }
}
