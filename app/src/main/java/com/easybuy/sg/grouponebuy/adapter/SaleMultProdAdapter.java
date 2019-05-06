package com.easybuy.sg.grouponebuy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.easybuy.sg.grouponebuy.utils.SpanningPriceTextView;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

public class SaleMultProdAdapter extends RecyclerView.Adapter<SaleMultProdAdapter.MyViewHolder> {

    private static final int REQUEST_FOR_ACTIVITY_CODE =333 ;
    Context context;
    List<String> productList;
    GlobalProvider globalProvider;
    String lan;
    int pos;
   /* interface SaleMultClickListener
    {
        public void onSaleMultipleItemClick(int salePosition,int saleMultiplePosition);
    }
    SaleMultClickListener saleMultClickListener;
    */
    public SaleMultProdAdapter(Context context,List<String> productList,int position)
    {
        this.context=context;
        this.productList=productList;
        this.pos=position;
        globalProvider=GlobalProvider.getGlobalProviderInstance(context.getApplicationContext());
        lan=Constants.getLanguage(context.getApplicationContext());
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.hot_multipleimage,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
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
                    for (Product cartProduct : globalProvider.cartList) {

                            if (product.getId().equals(cartProduct.getId())) {

                                product.setTotalNumber(cartProduct.getTotalNumber());
                                //todo uncheck
                              // holder.mainLayout.setBackground(context.getResources().getDrawable(R.drawable.border));
                                holder.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.red));


                               // Log.d("getprodmnum", product.getNameEn() + " " + cartProduct.getTotalNumber());
                                break;
                            }

                    }

                    String actualPrice="$ "+product.getPrice();

                    String[] each = actualPrice.split("\\.");

                    each[0]=each[0]+".";

                    Spannable spannable = new SpannableString(actualPrice);

                    spannable.setSpan(new AbsoluteSizeSpan(16, true), 0, each[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);




                    holder.offerText.setText(spannable, TextView.BufferType.SPANNABLE);
                    if(lan.equals("english")) {

                        holder.prodName.setText(product.getNameEn());
                        holder.prodSpec.setText(product.getSpecificationEn());
                    }
                    else
                    {
                        holder.prodName.setText(product.getNameCh());
                        holder.prodSpec.setText(product.getSpecificationCh());

                    }
                    if(product.getStock()==0)
                    {

                        holder.buyButton.setText(context.getString(R.string.outOfStock));

                        holder.buyButton.setTextColor(context.getResources().getColor(R.color.black));
                        if(lan.equals("english"))
                        holder.soldOutImage.setImageDrawable(context.getDrawable(R.drawable.soldout));
                        else
                            holder.soldOutImage.setImageDrawable(context.getDrawable(R.drawable.soldout_cn));
                        holder.soldOutImage.setVisibility(View.VISIBLE);


                        holder.buyButton.setEnabled(false);
                        holder.buyButton.setClickable(false);


                    }
                    else
                    {
                        holder.buyButton.setText(context.getString(R.string.add_to_cart));
                        holder.buyButton.setTextColor(context.getResources().getColor(R.color.red));
                        holder.buyButton.setBackgroundColor(context.getResources().getColor(R.color.add_tocart_bkg));
                        holder.buyButton.setEnabled(true);
                        holder.buyButton.setClickable(true);
                        holder.soldOutImage.setVisibility(View.GONE);
                    }
                    if(product.getPriceOriginal()!=null&&product.getPriceOriginal()>0)
                    {
                        holder.originalPrice.setVisibility(View.VISIBLE);
                      holder.originalPrice.setText("$ " + product.getPriceOriginal());
                        holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    else
                        holder.originalPrice.setVisibility(View.GONE);
                    Glide.with(context).load(Constants.newImageUrl+product.getImageCover()).asBitmap().format(PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ebuylogo).into(holder.prodCover);
                    holder.buyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.buyButton.setVisibility(View.GONE);
                            holder.cartAddedLayout.setVisibility(View.VISIBLE);
                            holder.quantityText.setText(1+"");
                            product.setTotalNumber(1);


                            /*
                            Intent intent=new Intent(context, ProductDetailActivity.class);
                            intent.putExtra("product",product);
                            context.startActivity(intent);
                            */
                        }
                    });
                    if(product.getTotalNumber()>0)
                    {
                       // Log.d("checkprouctquantity",product.getTotalNumber()+"");
                        holder.cartAddedLayout.setVisibility(View.VISIBLE);
                        holder.buyButton.setVisibility(View.GONE);

                        holder.quantityText.setText(product.getTotalNumber()+"");
                    }
                    else
                    {
                        holder.cartAddedLayout.setVisibility(View.GONE);
                        holder.buyButton.setVisibility(View.VISIBLE);
                    }
                    holder.subButtonText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String quant=holder.quantityText.getText().toString();
                            int quantity= Integer.parseInt(quant)-1;
                            if(quantity==0)
                            {
                                holder.buyButton.setVisibility(View.VISIBLE);
                                holder.cartAddedLayout.setVisibility(View.GONE);

                                //todo uncheck

                                holder.mainLayout.setBackground(context.getResources().getDrawable(R.drawable.border_grey));
                                product.setTotalNumber(quantity);
                                for(Product cartProduct:globalProvider.cartList)
                                {
                                    if(product.getId().equals(cartProduct.getId()))
                                    {
                                        globalProvider.cartList.remove(cartProduct);

                                       // break;
                                        return;
                                    }
                                }

                                //cartProducts.remove(product);
                                //notifyItemRemoved(position);



                            }
                            holder.quantityText.setText(quantity+"");






                            //notifyDataSetChanged();



                        }
                    });
                    holder.quantityText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                            String quantity=editable.toString();

                            if(editable.toString().length()>0&&Integer.parseInt(quantity)>0) {


                                int n = globalProvider.cartList.size();
                                int quant=Integer.parseInt(quantity);
                               // Log.d("getquant",quant+"");
                                if (n > 0) {
                                    for (int i = 0; i < n; i++) {
                                        if (product.getId().equals(globalProvider.cartList.get(i).getId())) {
                                           // Log.d("getpid", product.getId());
                                            globalProvider.cartList.get(i).setTotalNumber(quant);
                                            //break;
                                            return;
                                        }
                                    /*    if (i == n - 1) {
                                            Log.d("cartlistglobal", product.getNameEn());
                                            globalProvider.cartList.add(product);
                                        }
                                        */


                                    }

                                }
                                holder.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.red));
                                //holder.mainLayout.setBackground(context.getResources().getDrawable(R.drawable.border));
                                    globalProvider.cartList.add(product);
                               // Log.d("checkcategorynm", product.getCategory().getNameEn());
                            }



                        }
                    });
                    holder.addButtonText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int quantity = 0;

                            if (TextUtils.isEmpty(holder.quantityText.getText())) {
                                quantity = 1;
                            } else {

                                quantity = Integer.parseInt(holder.quantityText.getText().toString()) + 1;

                            }
                            if(product.limitPurchase>0) {
                                if (quantity > product.limitPurchase) {
                                    Toast.makeText(context,context.getString(R.string.limit_sale_msg),Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                           // Log.d("quantityadd",quantity+"");


                            holder.quantityText.setText(quantity + "");
                           /* SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("customerid",globalProvider.getCustomer().customer_id);
                            editor.putInt(product.getId(),quantity);
                            editor.apply();
                            */










                            product.setTotalNumber(quantity);
                            //notifyDataSetChanged();
                        }
                    });

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent=new Intent(context, ProductDetailActivity.class);
                            intent.putExtra("product",product);
                            intent.putExtra("saleAdapterPosition",pos);
                          //  intent.putExtra("saleMultipleAdapterPosition",position);
                           ((Activity) context).startActivityForResult(intent, REQUEST_FOR_ACTIVITY_CODE);
                           // context.startActivity(intent);
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
       // Log.d("productListSize",productList.size()+"");

        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView prodCover;
        TextView prodName,prodSpec,originalPrice;
        TextView offerText;
        Button buyButton;
        LinearLayout cartAddedLayout;
        TextView addButtonText,subButtonText;
        TextView quantityText;
      LinearLayout mainLayout;
      ImageView soldOutImage;




        public MyViewHolder(View itemView) {
            super(itemView);
           // offerText= new SpanningPriceTextView(context);
            prodCover=(itemView).findViewById(R.id.img);
            prodName=(itemView).findViewById(R.id.prod_title);
            quantityText=(itemView).findViewById(R.id.quantity);
            prodSpec=(itemView).findViewById(R.id.prod_spec);
            offerText=(itemView).findViewById(R.id.prod_price);
            buyButton=(itemView).findViewById(R.id.buyButton);
            addButtonText=(itemView).findViewById(R.id.add_button);
            subButtonText=(itemView).findViewById(R.id.sub_button);
            originalPrice=(itemView).findViewById(R.id.original_price);
            cartAddedLayout=(itemView).findViewById(R.id.cartadded_layout);
            mainLayout=(itemView).findViewById(R.id.main_layout);
            soldOutImage=(itemView).findViewById(R.id.soldout_img);
        }
    }
}
