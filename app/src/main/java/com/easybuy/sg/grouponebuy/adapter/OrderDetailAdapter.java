package com.easybuy.sg.grouponebuy.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.activities.OrderDetailActivity;
import com.easybuy.sg.grouponebuy.model.Order;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.model.ProductOrderList;
import com.easybuy.sg.grouponebuy.network.Constants;

import java.util.List;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>  {
    Context context;
   List<ProductOrderList> productList;
   QuantityChangedListener quantityChangedListener;
   String lang;
   String state;

 public static boolean editClicked;

    public OrderDetailAdapter(Context context,List<ProductOrderList> productList,QuantityChangedListener quantityChangedListener,String state)
    {
        this.context=context;
        this.productList=productList;
        this.quantityChangedListener=quantityChangedListener;
        lang=Constants.getLanguage(context.getApplicationContext());
        this.state=state;


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
        String actualPrice="$ "+product.getProductInfo().getPrice();
        // Log.d("prc",product.getPrice()+"");
        String[] each = actualPrice.split("\\.");
        // Log.d("prc1",each[0]+"");
        // Log.d("prc2",each[1]+"");
        each[0]=each[0]+".";

        Spannable spannable = new SpannableString(actualPrice);

        spannable.setSpan(new AbsoluteSizeSpan(16, true), 0, each[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.priceText.setText(spannable, TextView.BufferType.SPANNABLE);
       // holder.priceText.setText("$ "+product.getProductInfo().getPrice());
        holder.quantityText.setText("X "+product.getQuantity());
        Glide.with(context).load(Constants.newImageUrl+product.getProductInfo().getImageCover()).asBitmap().format(PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.productImage);

        if(!state.equalsIgnoreCase("processing")&&product.getQuantityActual()!=null&&product.getQuantityActual()!=0&&product.getQuantityActual()<product.getQuantity())
        {
          holder.quantityRequestedText.setVisibility(View.VISIBLE);
          int space=0;

          if(lang.equals("english"))
          {
              space=11;
          }
          else {
              space = 0;
          }
          String quantityRequested=context.getResources().getString(R.string.qty);
          for(int i=0;i<space;i++)
          {
              quantityRequested+=" ";
          }





           holder.quantityRequestedText.setText(quantityRequested+" X "+product.getQuantity());
            holder.quantityText.setText(context.getResources().getString(R.string.qty_given)+" X "+product.getQuantityActual());

        }

        else
        {
            holder.quantityRequestedText.setVisibility(View.GONE);
        }
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
               // Log.d("minuscalled", "here");


                double price = ((OrderDetailActivity) context).changedtotal - product.getProductInfo().getPrice();
                if (price > Constants.getCustomer(context).getDistrict().getDeliveryCost()) {
                    final int quant = Integer.parseInt(holder.quantityEditText.getText().toString());
                    if ((quant - 1) == 0) {
                        String productName = null;
                        if (lang.equals("english")) {
                            productName = product.getProductInfo().getNameEn();
                        } else
                            productName = product.getProductInfo().getNameCh();


                        new AlertDialog.Builder(context).setTitle(productName).setMessage(context.getString(R.string.remove_product)).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                product.setQuantity(quant - 1);
                                holder.quantityEditText.setText(quant - 1 + "");
                                quantityChangedListener.onQuantityChanged(product, quant - 1, "sub");
                                dialogInterface.dismiss();


                            }
                        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();


                            }
                        }).create().show();

                    } else {

                        product.setQuantity(quant - 1);
                        holder.quantityEditText.setText(quant - 1 + "");
                        quantityChangedListener.onQuantityChanged(product, quant - 1, "sub");
                    }


                }
                else
                {
                    Toast.makeText(context,context.getResources().getString(R.string.min_spend)+" $ "+Constants.getCustomer(context).getDistrict().getDeliveryCost(), Toast.LENGTH_LONG).show();
                }
            }

        });
        holder.plusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quant=Integer.parseInt(holder.quantityEditText.getText().toString());
                quant+=1;
                Log.d("checklimitpur",product.getProductInfo().getLimitPurchase()+"");
                if(product.getProductInfo().getLimitPurchase()>0) {
                    if (quant > product.getProductInfo().getLimitPurchase()) {
                        String msg=context.getResources().getString(R.string.limit_sale_msg,product.getProductInfo().getLimitPurchase());

                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


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
        TextView priceText,quantityText,productNameText,quantityRequestedText;
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
            quantityRequestedText=(itemView).findViewById(R.id.quantity_requested);



        }
    }
    public interface QuantityChangedListener
    {
        public void onQuantityChanged(ProductOrderList product,int quantity,String operation);

    }
}
