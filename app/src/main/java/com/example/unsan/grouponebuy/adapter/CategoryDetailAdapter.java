package com.example.unsan.grouponebuy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.unsan.grouponebuy.R;
import com.example.unsan.grouponebuy.activities.ProductActivity;
import com.example.unsan.grouponebuy.model.Category;
import com.example.unsan.grouponebuy.model.CategorySummary;
import com.example.unsan.grouponebuy.network.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

public class CategoryDetailAdapter extends RecyclerView.Adapter<CategoryDetailAdapter.MyViewHolder>  {

    List<CategorySummary> categoryList;
    Context context;
    String lang;
    public CategoryDetailAdapter(Context context,List<CategorySummary> categoryList)
    {
        this.context=context;
        this.categoryList=categoryList;
        lang=Constants.getLanguage(context.getApplicationContext());

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.categorydetail_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
       final CategorySummary category= categoryList.get(position);
        Glide.with(context).load(Constants.baseUrlStr+category.getImage()).asBitmap().format(PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.productImageView);
        if(lang.equals("english"))
        holder.productNameTextView.setText(category.getNameEn());
        else
            holder.productNameTextView.setText(category.getNameCh());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position!=0) {
                    Intent intent = new Intent(context, ProductActivity.class);
                    intent.putExtra("categoryId", category.getId());
                    context.startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(context, ProductActivity.class);
                    ArrayList<String> categoryIdList=new ArrayList<>();
                    for(int i=1;i<categoryList.size();i++) {
                         categoryIdList.add(categoryList.get(i).getId()) ;
                    }
                    intent.putStringArrayListExtra("categoryArray",  categoryIdList);

                    context.startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        Log.d("checkdsize",categoryList.size()+"");
        return categoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView productImageView;
        TextView productNameTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            productImageView=(ImageView)itemView.findViewById(R.id.prod_image);
            productNameTextView=(TextView)itemView.findViewById(R.id.prod_name);
        }
    }
}
