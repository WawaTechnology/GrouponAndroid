package com.easybuy.sg.grouponebuy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.activities.MainActivity;
import com.easybuy.sg.grouponebuy.activities.ProductDetailActivity;
import com.easybuy.sg.grouponebuy.activities.SignInActivity;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.network.Constants;

import java.util.List;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {
    List<Product> cartProducts;
    Context context;
    GlobalProvider globalProvider;
    String lang;

    public CartListAdapter(Context context,List<Product> cartProducts)
    {
        this.context=context;
        this.cartProducts=cartProducts;
        lang= Constants.getLanguage(context.getApplicationContext());
        globalProvider=GlobalProvider.getGlobalProviderInstance(context.getApplicationContext());
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.prod_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       holder.minusButton.setVisibility(View.VISIBLE);
       holder.quantityText.setVisibility(View.VISIBLE);


        final Product product = cartProducts.get(position);
        holder.quantityText.setText(product.getTotalNumber()+"");
        if(lang.equals("english")) {
           holder.prodName.setText(product.getNameEn());
            holder.prodDetail.setText(product.getSpecificationEn());
        }
        else
        {
            holder.prodName.setText(product.getNameCh());
            holder.prodDetail.setText(product.getSpecificationCh());

        }
      holder.prodPrice.setText("$ " + product.getPrice());
        if(product.getStock()==0)
        {
            holder.addButton.setVisibility(View.GONE);
            holder.stockStatusText.setText("Sold Out");
            holder.stockStatusText.setVisibility(View.VISIBLE);

        }
        else
        {
            holder.addButton.setVisibility(View.VISIBLE);

            holder.stockStatusText.setVisibility(View.GONE);
        }
        if (product.getPriceOriginal() != null&&product.getPriceOriginal()>0) {
           holder.originalPriceText.setVisibility(View.VISIBLE);
            holder.originalPriceText.setText("$ " + product.getPriceOriginal());
           holder.originalPriceText.setPaintFlags(holder.originalPriceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else
            holder.originalPriceText.setVisibility(View.INVISIBLE);





        Glide.with(context).load(Constants.newImageUrl+ product.getImageCover()).asBitmap().format(PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.ebuylogo).into(holder.imgView);
       // Log.d("ggimg", product.getImageCover() + "");
       holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String quant=holder.quantityText.getText().toString();
                int quantity= Integer.parseInt(quant)-1;
                if(quantity==0)
                {
                    holder.minusButton.setVisibility(View.GONE);
                    holder.quantityText.setVisibility(View.GONE);
                    for(Product cartProduct:globalProvider.cartList)
                    {
                        if(product.getId().equals(cartProduct.getId()))
                        {
                            globalProvider.cartList.remove(cartProduct);

                            break;
                        }
                    }
                    cartProducts.remove(product);
                    notifyItemRemoved(position);



                }
                else
                {
                   holder.quantityText.setText(quantity+"");
                    int n=globalProvider.cartList.size();

                    for (int i = 0; i < n; i++) {
                        if (product.getId().equals(globalProvider.cartList.get(i).getId())) {
                           // Log.d("getpid",product.getId());
                            globalProvider.cartList.get(i).setTotalNumber(quantity);
                            break;
                        }




                    }


                }


                product.setTotalNumber(quantity);
                notifyDataSetChanged();






            }
        });
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                    int quantity = 0;

                    if (TextUtils.isEmpty(holder.quantityText.getText())) {
                        quantity = 1;
                    } else {

                        quantity = Integer.parseInt(holder.quantityText.getText().toString()) + 1;

                    }
                if(product.limitPurchase!=null&&product.limitPurchase>0) {
                    if (quantity > product.limitPurchase) {

                        String msg = context.getString(R.string.limit_sale_msg,product.limitPurchase);
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                       // Toast.makeText(context,context.getString(R.string.limit_sale_msg),Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                    holder.quantityText.setText(quantity + "");
                           /* SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("customerid",globalProvider.getCustomer().customer_id);
                            editor.putInt(product.getId(),quantity);
                            editor.apply();
                            */

                    int n = globalProvider.cartList.size();
                    if (n > 0) {
                        for (int i = 0; i < n; i++) {
                            if (product.getId().equals(globalProvider.cartList.get(i).getId())) {
                               // Log.d("getpid", product.getId());
                                globalProvider.cartList.get(i).setTotalNumber(quantity);
                                break;
                            }
                            if (i == n - 1) {
                                globalProvider.cartList.add(product);
                            }


                        }
                    } else
                        globalProvider.cartList.add(product);
                   // Log.d("checkcategorynm",product.getCategory().getNameEn());









                    product.setTotalNumber(quantity);
                    notifyDataSetChanged();
                    //adding cart number if product is added on fav fragment
                    if(context instanceof MainActivity)
                    {
                        ((MainActivity)context).setCartNum();
                    }



                    // parent.balanceListOrderQuantity(product,quantity);
                    //  quantityChangedInterface.onQuantityChanged(product,quantity);





            }

        });





    }

    @Override
    public int getItemCount() {
        return cartProducts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgView,addButton,minusButton;
        TextView prodName,prodDetail,prodPrice,originalPriceText,quantityText,stockStatusText;


        public MyViewHolder(View itemView) {
            super(itemView);
            prodName=(TextView)itemView.findViewById(R.id.prod_name);
            prodDetail=(TextView)itemView.findViewById(R.id.prod_detail);
            prodPrice=(TextView)itemView.findViewById(R.id.priceunit);
            imgView=(ImageView)itemView.findViewById(R.id.product_img);
            originalPriceText=(TextView)itemView.findViewById(R.id.originalprice);
            addButton=(ImageView)itemView.findViewById(R.id.plusButton);
            minusButton=(ImageView)itemView.findViewById(R.id.minusButton);
            quantityText=(TextView)itemView.findViewById(R.id.quantity);
            stockStatusText=(TextView)itemView.findViewById(R.id.stockstatus);
            final View addParent = (View) addButton.getParent();
            addParent.post( new Runnable() {

                public void run() {
                    final Rect r = new Rect();
                    addButton.getHitRect(r);
                    r.top -= 100;
                    r.bottom += 4;
                    addParent.setTouchDelegate( new TouchDelegate( r , addButton));
                }
            });
        }
    }
}
