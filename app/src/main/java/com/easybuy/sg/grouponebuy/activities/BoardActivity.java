package com.easybuy.sg.grouponebuy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.adapter.BoardAdapter;
import com.easybuy.sg.grouponebuy.model.SpecialImage;
import com.easybuy.sg.grouponebuy.network.Constants;

import java.util.List;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

public class BoardActivity extends AppCompatActivity {
    ImageView boardImage;
    ImageView backButton;
   // RecyclerView boardRecycler;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_activity);
        boardImage=(ImageView)findViewById(R.id.board_img);
        backButton=(ImageView) findViewById(R.id.back);
       // boardRecycler=(RecyclerView) findViewById(R.id.board_recycler);
        Intent intent=getIntent();
       SpecialImage specialImage= (SpecialImage) intent.getSerializableExtra("specialBoard");

        String cover="";


        if(specialImage.getImageHeaderSize()!=null)
        {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
           int width=displayMetrics.widthPixels;
            int height= (int) (width*specialImage.getImageHeaderSize());
            // Log.d("height",height+"");
            boardImage.getLayoutParams().height=height;
            boardImage.requestLayout();


        }

        if(Constants.getLanguage(this).equals("english"))
            cover= Constants.newImageUrl+ specialImage.getImageHeaderEn();
        else
            cover=Constants.newImageUrl+specialImage.getImageHeaderCh();
        Glide.with(BoardActivity.this).load(cover).thumbnail(0.1f).placeholder(R.drawable.ebuylogo).error(R.drawable.ebuylogo).into(boardImage);
       /* BoardAdapter boardAdapter=new BoardAdapter(BoardActivity.this,specialImageList);
        boardRecycler.setAdapter(boardAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(BoardActivity.this,LinearLayoutManager.VERTICAL,false);
        boardRecycler.setLayoutManager(linearLayoutManager);
        */

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
