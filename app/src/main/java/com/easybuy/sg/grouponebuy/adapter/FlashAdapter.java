package com.easybuy.sg.grouponebuy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.activities.ProductDetailActivity;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.network.Constants;

import java.util.List;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

public class FlashAdapter extends RecyclerView.Adapter<FlashAdapter.ViewHolder> {
    List<Product> productList;
    Context context;
    String lang;

    public FlashAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        lang = Constants.getLanguage(context.getApplicationContext());
    }

    @Override
    public FlashAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.flash_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FlashAdapter.ViewHolder holder, int position) {
        final Product bp = productList.get(position);
        boolean isSoldOut=false;

        if (lang.equals("english")) {
            holder.tvname.setText(bp.getNameEn());
            holder.tvquantity.setText(bp.getSpecificationEn());
        } else {
            holder.tvname.setText(bp.getNameCh());
            holder.tvquantity.setText(bp.getSpecificationCh());
        }


        if(bp.getStartStock()!=null)
        {
            Log.d("checkbpstock",bp.getStock()+"");
            Log.d("checkbpstartstock",bp.getStartStock()+"");

         int per=  (int)(bp.getStock()*100)/bp.getStartStock();
         per=100-per;

         if(per>65&&per<100)
         {
             holder.soldOutStatusText.setTextColor(context.getResources().getColor(R.color.red));


             holder.soldOutStatusText.setText(context.getResources().getString(R.string.almost_sold_out));

         }
         else if(per==100)
         {
             holder.soldOutStatusText.setTextColor(context.getResources().getColor(R.color.soldout_color));
             isSoldOut=true;
             holder.soldOutStatusText.setText(context.getResources().getString(R.string.sold_out));
         }
         else if(per<65)
         {
             holder.soldOutStatusText.setTextColor(context.getResources().getColor(R.color.red));
             holder.soldOutStatusText.setText(context.getResources().getString(R.string.sold)+" "+per+"%");
         }
         holder.progressBar.setProgress(per);
        }



        double price = bp.getPrice();
        //  String dr= Constants.baseUrlStr+bp.getImageCover();
        String dr = "https://s3-ap-southeast-1.amazonaws.com/ebuymart/" + bp.getImageCover();
        Log.d("checkimage", dr);
        String actualPrice = "$ " + price;

        String[] each = actualPrice.split("\\.");

        each[0] = each[0] + ".";

        Spannable spannable = new SpannableString(actualPrice);

        spannable.setSpan(new AbsoluteSizeSpan(16, true), 0, each[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.tvprice.setText(spannable, TextView.BufferType.SPANNABLE);
        holder.tvprice.setTextColor(context.getResources().getColor(R.color.red));


        // Glide.with(context).load(dr).into(holder.imgview);
        Glide.with(context)
                .load(dr).asBitmap().format(PREFER_ARGB_8888).error(R.drawable.ebuylogo).placeholder(R.drawable.ebuylogo).diskCacheStrategy(DiskCacheStrategy.SOURCE)

                .into(holder.imgview);
        if(isSoldOut)
        {
            Log.d("soldout","true");
            if(lang.equals("english"))
                holder.soldOutImg.setImageDrawable(context.getDrawable(R.drawable.soldout));
            else
                holder.soldOutImg.setImageDrawable(context.getDrawable(R.drawable.soldout_cn));
            holder.soldOutImg.setVisibility(View.VISIBLE);
        }
        else
            holder.soldOutImg.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product", bp);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgview,soldOutImg;
        TextView tvname, tvprice, tvquantity,soldOutStatusText;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            imgview = (ImageView) itemView.findViewById(R.id.img);
            tvname = (TextView) itemView.findViewById(R.id.prod_nm);
            tvprice = (TextView) itemView.findViewById(R.id.price);
            tvquantity = (TextView) itemView.findViewById(R.id.product_quantity);
            progressBar=(ProgressBar)itemView.findViewById(R.id.prg_bar);
            soldOutStatusText=(TextView)itemView.findViewById(R.id.prg_per_text);
            soldOutImg=(ImageView)itemView.findViewById(R.id.sold_out);


        }
    }
}