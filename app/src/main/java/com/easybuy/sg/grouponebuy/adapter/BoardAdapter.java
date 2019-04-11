package com.easybuy.sg.grouponebuy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.model.SpecialImage;
import com.easybuy.sg.grouponebuy.network.Constants;

import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.MyViewHolder> {
    List<SpecialImage> specialImageList;
    Context context;
    int width;
    String lang;

    public BoardAdapter(Context context,List<SpecialImage> specialImageList)
    {
        this.context=context;
        this.specialImageList=specialImageList;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        width=displayMetrics.widthPixels;
        lang=Constants.getLanguage(context.getApplicationContext());


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.board_image_layout,null,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
      SpecialImage specialImage=  specialImageList.get(position);
        String cover="";


        if(specialImage.getImageHeaderSize()!=null)
        {
            int height= (int) (width*specialImage.getImageHeaderSize());
            Log.d("height",height+"");
            holder.imgView.getLayoutParams().height=height;
            holder.imgView.requestLayout();


        }

        if(lang.equals("english"))
            cover= Constants.newImageUrl+ specialImage.getImageHeaderEn();
        else
            cover=Constants.newImageUrl+specialImage.getImageHeaderCh();
        Glide.with(context).load(cover).thumbnail(0.1f).placeholder(R.drawable.ebuylogo).error(R.drawable.ebuylogo).into(holder.imgView);


    }

    @Override
    public int getItemCount() {
        return specialImageList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgView=(ImageView)itemView.findViewById(R.id.board_img);
        }
    }
}
