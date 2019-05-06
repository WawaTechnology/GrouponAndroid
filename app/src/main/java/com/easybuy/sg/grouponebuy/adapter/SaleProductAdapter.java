package com.easybuy.sg.grouponebuy.adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.activities.ProductDetailActivity;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.helpers.Utf8JsonRequest;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.model.ResultProduct;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

public class SaleProductAdapter extends RecyclerView.Adapter<SaleProductAdapter.MyViewHolder> {

Context context;
List<String> productList;
GlobalProvider globalProvider;
String lan;
public SaleProductAdapter(Context context,List<String> productList)
{
    this.context=context;
    this.productList=productList;
    globalProvider=GlobalProvider.getGlobalProviderInstance(context.getApplicationContext());
    lan=Constants.getLanguage(context.getApplicationContext());
}



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.hot_image,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
    String productId=productList.get(position);
    String url= Constants.productUrl+"/"+productId;
    Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest( Request.Method.GET,url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            ObjectMapper objectMapper=new ObjectMapper();
            JsonFactory jsonFactory = new JsonFactory();
            try {
                JsonParser jsonParser = jsonFactory.createParser(response);
                ResultProduct resultProduct = (ResultProduct) objectMapper.readValue(jsonParser, ResultProduct.class);
                final Product product=resultProduct.getPayload();
               // Log.d("descpen",product.getDescriptionEn());
                String actualPrice="$ "+product.getPrice();

                String[] each = actualPrice.split("\\.");

                each[0]=each[0]+".";

                Spannable spannable = new SpannableString(actualPrice);

                spannable.setSpan(new AbsoluteSizeSpan(16, true), 0, each[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
              holder.offerText.setText(spannable, TextView.BufferType.SPANNABLE);
                if(lan.equals("english")) {
                    holder.prodDesc.setText(product.getDescriptionEn());

                    holder.prodName.setText(product.getNameEn());
                    holder.prodSpec.setText(product.getSpecificationEn());
                }
                else
                {
                    holder.prodDesc.setText(product.getDescriptionCh());

                    holder.prodName.setText(product.getNameCh());
                    holder.prodSpec.setText(product.getSpecificationCh());
                }
                if (!((Activity)context).isDestroyed())
                {

                    Glide.with(context).load(Constants.newImageUrl + product.getImageCover()).asBitmap().format(PREFER_ARGB_8888).error(R.drawable.ebuylogo).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.prodCover);
                }
                if(product.getStock()==0)
                {

                   holder.buyButton.setText("Sold Out");
                    DrawableCompat.setTint(holder.buyButton.getBackground(), ContextCompat.getColor(context, R.color.grey));
                   //holder.buyButton.setBackgroundColor(context.getResources().getColor(R.color.grey));
                   holder.buyButton.setEnabled(false);
                   holder.buyButton.setClickable(false);
                 /*   if(lan.equals("english")) {
                        holder.soldOutImage.setImageDrawable(context.getDrawable(R.drawable.soldout));
                    }
                    else
                        holder.soldOutImage.setImageDrawable(context.getDrawable(R.drawable.soldout_cn));
                    holder.soldOutImage.setVisibility(View.VISIBLE);
                    */




                }



                holder.buyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, ProductDetailActivity.class);
                        intent.putExtra("product",product);
                        context.startActivity(intent);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, ProductDetailActivity.class);
                        intent.putExtra("product",product);
                        context.startActivity(intent);

                    }
                });
                

            }
            catch (JsonParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    });
    globalProvider.addRequest(utf8JsonRequest);


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView prodCover;
        TextView prodDesc,prodName,prodSpec;
        TextView offerText;
        Button buyButton;
       // ImageView soldOutImage;


        public MyViewHolder(View itemView) {
            super(itemView);
            prodCover=(itemView).findViewById(R.id.prodcover);
            prodDesc=(itemView).findViewById(R.id.prod_desc);
            offerText=(itemView).findViewById(R.id.offer_text);
            buyButton=(itemView).findViewById(R.id.buyButton);
            prodName=(itemView).findViewById(R.id.prod_name);
            prodSpec=(itemView).findViewById(R.id.prod_spec);
           // soldOutImage=(itemView).findViewById(R.id.soldout_img);
        }
    }

}
