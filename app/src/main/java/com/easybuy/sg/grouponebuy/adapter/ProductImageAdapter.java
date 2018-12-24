package com.easybuy.sg.grouponebuy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.network.Constants;

import java.util.List;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.MyViewHolder> {
    List<Product> productList;
    Context context;

    public ProductImageAdapter(Context context,List<Product> productList)
    {
        this.context=context;
        this.productList=productList;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.product_image,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       Product product= productList.get(position);
        Glide.with(context).load(Constants.baseUrlStr +product.getImageCover()).asBitmap().format(PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.imgView);


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgView=(ImageView)itemView.findViewById(R.id.prod_img);
        }
    }
}
