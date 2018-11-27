package com.example.unsan.grouponebuy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.unsan.grouponebuy.R;

import java.util.ArrayList;
import java.util.List;

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.MyViewHolder> {
Context context;
List<Integer> imageList=new ArrayList();
public ImageRecyclerAdapter(Context context,List<Integer> imageList)
{
    this.context=context;
    this.imageList=imageList;
}


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(context).inflate(R.layout.img_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    if(position==2)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        int dpWidthInPx  = (int) (200 * scale);
      holder.imgView.getLayoutParams().height=dpWidthInPx;
      holder.imgView.requestLayout();

    }
    int imageId=imageList.get(position);
    Glide.with(context).load(imageId).placeholder(R.drawable.ebuylogo).into(holder.imgView);
    //holder.imgView.setImageDrawable(context.getResources().getDrawable(imageId));

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgView=(ImageView)itemView.findViewById(R.id.img);
        }
    }
}
