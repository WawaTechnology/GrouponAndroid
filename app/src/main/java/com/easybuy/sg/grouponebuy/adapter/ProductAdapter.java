package com.easybuy.sg.grouponebuy.adapter;

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
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.activities.ProductDetailActivity;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.network.Constants;

import java.util.List;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

/**
 * Created by Unsan on 25/5/18.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<Product> productList;
    Context context;
    String lang;

    public ProductAdapter(Context context,List<Product> productList)
    {
        this.context=context;
        this.productList=productList;
        lang=Constants.getLanguage(context.getApplicationContext());
    }
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.image_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        final Product bp=productList.get(position);
        if(lang.equals("english")) {
            holder.tvname.setText(bp.getNameEn());
            holder.tvquantity.setText(bp.getSpecificationEn());
        }
        else {
            holder.tvname.setText(bp.getNameCh());
            holder.tvquantity.setText(bp.getSpecificationCh());
        }
        Log.d("prodname",bp.getNameEn());

        double price=bp.getPrice();
      //  String dr= Constants.baseUrlStr+bp.getImageCover();
        String dr="https://s3-ap-southeast-1.amazonaws.com/ebuymart/"+bp.getImageCover();
        Log.d("checkimage",dr);

        holder.tvprice.setText("$ "+price);
        holder.tvprice.setTextColor(context.getResources().getColor(R.color.red));


       // Glide.with(context).load(dr).into(holder.imgview);
        Glide.with(context)
                .load(dr).asBitmap().format(PREFER_ARGB_8888).error(R.drawable.ebuylogo).placeholder(R.drawable.ebuylogo).diskCacheStrategy(DiskCacheStrategy.SOURCE)

                .into(holder.imgview);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product",bp);
                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgview;
        TextView tvname,tvprice,tvquantity;

        public ViewHolder(View itemView) {
            super(itemView);
            imgview=(ImageView)itemView.findViewById(R.id.img);
            tvname=(TextView)itemView.findViewById(R.id.prod_nm);
            tvprice=(TextView)itemView.findViewById(R.id.price);
            tvquantity=(TextView)itemView.findViewById(R.id.product_quantity);


        }
    }

}
