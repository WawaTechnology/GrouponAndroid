package com.easybuy.sg.grouponebuy.activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.adapter.ImageRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AboutUsActivity extends AppCompatActivity {
    ImageView backButton;
    RecyclerView recyclerView;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        backButton=(ImageView)findViewById(R.id.back);
        recyclerView=(RecyclerView) findViewById(R.id.img_Recyclers);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
       List<Integer> li=new ArrayList<>();
       li.add(R.drawable.img1);
       li.add(R.drawable.img2);
        li.add(R.drawable.img3);
        li.add(R.drawable.img4);
        li.add(R.drawable.img5);
        li.add(R.drawable.img6);
        li.add(R.drawable.img7);
        li.add(R.drawable.img8);
        ImageRecyclerAdapter imageRecyclerAdapter=new ImageRecyclerAdapter(this,li);
        recyclerView.setAdapter(imageRecyclerAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
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
    public void onDestroy()
    {
        Glide.get(this).clearMemory();
        super.onDestroy();
    }


}
