package com.example.unsan.grouponebuy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.unsan.grouponebuy.R;
import com.example.unsan.grouponebuy.activities.MainActivity;
import com.example.unsan.grouponebuy.activities.ProductDetailActivity;
import com.example.unsan.grouponebuy.activities.SignInActivity;
import com.example.unsan.grouponebuy.helpers.GlobalProvider;
import com.example.unsan.grouponebuy.model.Product;
import com.example.unsan.grouponebuy.network.Constants;

import java.util.List;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

public class SpecialDetailAdapter extends RecyclerView.Adapter<SpecialDetailAdapter.MyViewHolder> {
    Context context;
    List<Product> productList;
    GlobalProvider globalProvider;
    String lang;
    public SpecialDetailAdapter(Context context, List<Product> productList)
    {
        this.context=context;
        this.productList=productList;
        globalProvider=GlobalProvider.getGlobalProviderInstance(context.getApplicationContext());
        lang=Constants.getLanguage(context.getApplicationContext());
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.category_item_image,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       final Product product=productList.get(position);
       if(lang.equals("english"))
        holder.tv1.setText(product.getNameEn());
       else
           holder.tv1.setText(product.getNameCh());
       holder.tv2.setTextColor(context.getResources().getColor(R.color.red));
        holder.tv2.setText("$ "+product.getPrice());
        if(product.getPriceOriginal()!=null&&product.getPriceOriginal()>0) {
            holder.tv3.setText("$ "+product.getPriceOriginal());
            holder.tv3.setPaintFlags(holder.tv3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if(product.getStock()==0)
        {
           holder.addImg.setVisibility(View.GONE);
            holder.soldOutText.setText("Sold Out");
            holder.soldOutText.setVisibility(View.VISIBLE);

        }
        else
        {
            holder.addImg.setVisibility(View.VISIBLE);

            holder.soldOutText.setVisibility(View.GONE);
        }
        Glide.with(context).load(Constants.baseUrlStr+product.getImageCover()).asBitmap().format(PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.ebuylogo).fitCenter().into(holder.imgview);
        if(product.getTotalNumber()>0)
        {
            Log.d("checksplprouctquantity",product.getTotalNumber()+"");
            holder.subImg.setVisibility(View.VISIBLE);
            holder.quantityText.setVisibility(View.VISIBLE);
            holder.quantityText.setText(product.getTotalNumber()+"");
        }
        else
        {
            holder.subImg.setVisibility(View.INVISIBLE);
            holder.quantityText.setVisibility(View.INVISIBLE);

        }
        holder.imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product",product);
               // ((Activity) context).startActivityForResult(intent,101);
                ((Activity) context).startActivity(intent);
              //  context.startActivity(intent);
            }
        });

        holder.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(globalProvider.isLogin()) {
                    if (holder.subImg.getVisibility() == View.INVISIBLE) {
                        holder.subImg.setVisibility(View.VISIBLE);
                        holder.quantityText.setVisibility(View.VISIBLE);

                    }
                    int quantity = 0;

                    if (TextUtils.isEmpty(holder.quantityText.getText())) {
                        quantity = 1;
                    } else {

                        quantity = Integer.parseInt(holder.quantityText.getText().toString()) + 1;

                    }



                    holder.quantityText.setText(quantity + "");

                    int n = globalProvider.cartList.size();
                    if (n > 0) {
                        for (int i = 0; i < n; i++) {
                            if (product.getId().equals(globalProvider.cartList.get(i).getId())) {
                                Log.d("getpid", product.getId());
                                globalProvider.cartList.get(i).setTotalNumber(quantity);
                                break;
                            }
                            if (i == n - 1) {
                                globalProvider.cartList.add(product);
                            }


                        }
                    } else
                        globalProvider.cartList.add(product);
                    Log.d("checkcategorynm",product.getCategory().getNameEn());









                    product.setTotalNumber(quantity);

                   // notifyDataSetChanged();
                    notifyItemChanged(position);



                    // parent.balanceListOrderQuantity(product,quantity);
                    //  quantityChangedInterface.onQuantityChanged(product,quantity);


                }
                else {
                    Intent intent = new Intent(context, SignInActivity.class);
                    context.startActivity(intent);
                }


            }

        });


                    holder.subImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String quant=holder.quantityText.getText().toString();
                            int quantity= Integer.parseInt(quant)-1;
                            if(quantity==0)
                            {
                                holder.subImg.setVisibility(View.GONE);
                                holder.quantityText.setVisibility(View.GONE);
                                for (Product cartProduct:globalProvider.cartList)
                                {
                                    if(product.getId().equals(cartProduct.getId()))
                                    {
                                        globalProvider.cartList.remove(cartProduct);
                                        break;
                                    }
                                }

                            }
                            else
                            {
                                holder.quantityText.setText(quantity+"");
                                int n=globalProvider.cartList.size();

                                for (int i = 0; i < n; i++) {
                                    if (product.getId().equals(globalProvider.cartList.get(i).getId())) {
                                        Log.d("getpid",product.getId());
                                        globalProvider.cartList.get(i).setTotalNumber(quantity);
                                        break;
                                    }




                                }


                            }


                            product.setTotalNumber(quantity);
                           // notifyDataSetChanged();
                            notifyItemChanged(position);
                            if(context instanceof MainActivity)
                            {
                                ((MainActivity)context).setCartNum();
                            }


                        }

                    });

                    //TODO add cart

                }















    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgview;
        TextView tv1,tv2,tv3,quantityText,soldOutText;
        ImageView subImg,addImg;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgview=(ImageView)itemView.findViewById(R.id.prodimg);
            tv1=(TextView)itemView.findViewById(R.id.prod_name);
            tv2=(TextView)itemView.findViewById(R.id.price);
            tv3=(TextView)itemView.findViewById(R.id.origprice);
            subImg=(ImageView)itemView.findViewById(R.id.subutton);
            addImg=(ImageView)itemView.findViewById(R.id.plusButton);
            quantityText=(TextView)itemView.findViewById(R.id.quantity);
            soldOutText=(TextView)itemView.findViewById(R.id.soldout_text);

        }
    }
}
