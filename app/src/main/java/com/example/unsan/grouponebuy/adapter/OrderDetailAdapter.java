package com.example.unsan.grouponebuy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.unsan.grouponebuy.R;
import com.example.unsan.grouponebuy.activities.OrderDetailActivity;
import com.example.unsan.grouponebuy.model.Order;
import com.example.unsan.grouponebuy.model.Product;
import com.example.unsan.grouponebuy.model.ProductOrderList;
import com.example.unsan.grouponebuy.network.Constants;

import java.util.List;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>  {
    Context context;
   List<ProductOrderList> productList;
   QuantityChangedListener quantityChangedListener;
   String lang;

 public static boolean editClicked;

    public OrderDetailAdapter(Context context,List<ProductOrderList> productList,QuantityChangedListener quantityChangedListener)
    {
        this.context=context;
        this.productList=productList;
        this.quantityChangedListener=quantityChangedListener;
        lang=Constants.getLanguage(context.getApplicationContext());


    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.order_product_item,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
       final ProductOrderList product=productList.get(position);
       if(lang.equals("english"))
        holder.productNameText.setText(product.getProductInfo().getNameEn());
       else
           holder.productNameText.setText(product.getProductInfo().getNameCh());
        holder.priceText.setText("$ "+product.getProductInfo().getPrice());
        holder.quantityText.setText("X "+product.getQuantity());
        Glide.with(context).load(Constants.baseUrlStr+product.getProductInfo().getImageCover()).asBitmap().format(PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.productImage);
        if(editClicked)
        {
            holder.quantityText.setVisibility(View.GONE);
            holder.editLayout.setVisibility(View.VISIBLE);
            holder.quantityEditText.setText(product.getQuantity()+"");



        }
        else {
            holder.editLayout.setVisibility(View.GONE);
            holder.quantityText.setVisibility(View.VISIBLE);
        }
        holder.minusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quant=Integer.parseInt(holder.quantityEditText.getText().toString());
                quant-=1;
                product.setQuantity(quant);
                holder.quantityEditText.setText(quant+"");
                quantityChangedListener.onQuantityChanged(product,quant,"sub");

            }
        });
        holder.plusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quant=Integer.parseInt(holder.quantityEditText.getText().toString());
                quant+=1;
                holder.quantityEditText.setText(quant+"");
                product.setQuantity(quant);
                quantityChangedListener.onQuantityChanged(product,quant,"add");

            }
        });



    }

    @Override
    public int getItemCount() {
        return productList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView productImage;
        TextView priceText,quantityText,productNameText;
        LinearLayout editLayout;
        TextView quantityEditText;
        ImageView minusImage,plusImage;

        public ViewHolder(View itemView) {
            super(itemView);
            productImage=(itemView).findViewById(R.id.product_image);

            priceText=(itemView).findViewById(R.id.price);
            quantityText=(itemView).findViewById(R.id.quantity);
            productNameText=(itemView).findViewById(R.id.product_name);
            editLayout=(itemView).findViewById(R.id.edit_layout);
            quantityEditText=(itemView).findViewById(R.id.quantityedit);
            minusImage=(itemView).findViewById(R.id.sub_quantity);
            productImage=(itemView).findViewById(R.id.product_image);
            plusImage=(itemView).findViewById(R.id.add_quantity);



        }
    }
    public interface QuantityChangedListener
    {
        public void onQuantityChanged(ProductOrderList product,int quantity,String operation);

    }
}
