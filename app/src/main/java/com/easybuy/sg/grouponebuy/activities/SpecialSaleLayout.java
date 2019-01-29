package com.easybuy.sg.grouponebuy.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.adapter.SaleAdapter;
import com.easybuy.sg.grouponebuy.model.Layer;
import com.easybuy.sg.grouponebuy.model.SpecialImage;
import com.easybuy.sg.grouponebuy.network.Constants;

import java.util.ArrayList;
import java.util.List;

public class SpecialSaleLayout extends AppCompatActivity {

    ImageView imageCoverView;
    List<Layer> layerList;
    RecyclerView blinkRecycler;
    SaleAdapter saleAdapter;
    ImageView backButton;
    ImageView bottomImage;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sale);

        blinkRecycler=(RecyclerView) findViewById(R.id.blink_recycler);
        imageCoverView=(ImageView)findViewById(R.id.imagecover);
        backButton=(ImageView) findViewById(R.id.back);
        bottomImage=(ImageView) findViewById(R.id.bottomImage);
        layerList=new ArrayList<>();
        Intent intent=getIntent();
        SpecialImage specialImage=(SpecialImage)intent.getSerializableExtra("specialImage");
        String lang=Constants.getLanguage(getApplicationContext());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        bottomImage.getLayoutParams().height= (int) (width/4.5);
        bottomImage.requestLayout();
        if(specialImage.getImageHeaderSize()!=null)
        {
            int height= (int) (width*specialImage.getImageHeaderSize());
            imageCoverView.getLayoutParams().height=height;
            imageCoverView.requestLayout();


        }

        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if(lang.equals("english")) {

            Glide.with(this).load(Constants.newImageUrl + specialImage.getImageHeaderEn()).thumbnail(0.1f).placeholder(R.drawable.ebuylogo).error(R.drawable.ebuylogo).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(imageCoverView);

        }
        else
        {

            Glide.with(this).load(Constants.newImageUrl + specialImage.getImageHeaderCh()).placeholder(R.drawable.ebuylogo).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(imageCoverView);
        }


        layerList.addAll(specialImage.getLayers());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        blinkRecycler.setLayoutManager(linearLayoutManager);
        blinkRecycler.setNestedScrollingEnabled(false);

        saleAdapter=new SaleAdapter(this,layerList);
        blinkRecycler.setAdapter(saleAdapter);
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
    public void onDestroy(){
        Glide.get(this).clearMemory();

        super.onDestroy();
    }
}
