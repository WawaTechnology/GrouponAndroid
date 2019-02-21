package com.easybuy.sg.grouponebuy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.activities.ProductDetailActivity;
import com.easybuy.sg.grouponebuy.fragment.FragmentCart;
import com.easybuy.sg.grouponebuy.helpers.TouchDelegateComposite;
import com.easybuy.sg.grouponebuy.model.CartProduct;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.network.Constants;

import java.util.List;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;
import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    List<CartProduct> cartProductList;
    Context context;
    quantityChangedListener quantityListener;
    String lang;



    public CartAdapter(List<CartProduct> cartProductList,Context context,quantityChangedListener quantityListener)
    {
        this.context=context;
        this.cartProductList=cartProductList;
        this.quantityListener=quantityListener;
        lang=Constants.getLanguage(context.getApplicationContext());

    }



    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cart_test,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartViewHolder holder, final int position) {
       final CartProduct cartProduct= cartProductList.get(position);
       final Product product=cartProduct.getProduct();
      /* if(FragmentCart.isSaveClicked)
       {
           holder.addButton.setVisibility(View.GONE);
           holder.subButton.setVisibility(View.GONE);
           holder.quantityText.setText("Quantity: "+product.getTotalNumber());


       }
       else
       {
           holder.addButton.setVisibility(View.VISIBLE);
           holder.subButton.setVisibility(View.VISIBLE);
           holder.quantityText.setText(product.getTotalNumber()+"");
       }
       */

       if(lang.equals("english"))
       {
           holder.productNameText.setText(product.getNameEn());
           holder.descTextView.setText(product.getSpecificationEn());
       }
       else
       {
           holder.productNameText.setText(product.getNameCh());
           holder.descTextView.setText(product.getSpecificationCh());
       }



       String actualPrice="$"+product.getPrice();
      // Log.d("prc",product.getPrice()+"");
        String[] each = actualPrice.split("\\.");
       // Log.d("prc1",each[0]+"");
       // Log.d("prc2",each[1]+"");
        each[0]=each[0]+".";

        Spannable spannable = new SpannableString(actualPrice);

        spannable.setSpan(new AbsoluteSizeSpan(16, true), 0, each[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

       holder.priceText.setText(spannable, TextView.BufferType.SPANNABLE);


       // holder.priceText.setText("$"+product.getPrice()+"");
       // holder.priceText.setText(finalText);
        holder.quantityText.setText(product.getTotalNumber()+"");
        if(product.getPriceOriginal()!=null&&product.getPriceOriginal()>0)
        {
            holder.origPrice.setText("$ "+product.getPriceOriginal());
            holder.origPrice.setPaintFlags(holder.origPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.origPrice.setVisibility(View.VISIBLE);
        }
        else
            holder.origPrice.setVisibility(View.GONE);


       Glide.with(context).load(Constants.newImageUrl +product.getImageCover()).asBitmap().format(PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.imgView);
       if(cartProduct.isCheck()==true)
       {
           holder.checkBox.setChecked(true);
       }
       else
           holder.checkBox.setChecked(false);
       holder.checkBox.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String quanityString= holder.quantityText.getText().toString();
               int value=Integer.parseInt(quanityString);

               if(holder.checkBox.isChecked())
               {


                   holder.checkBox.setChecked(true);
                   cartProduct.setCheck(true);
                   quantityListener.onCheckedChanged(product,value,true);

               }
               else {


                   holder.checkBox.setChecked(false);
                   cartProduct.setCheck(false);

                   quantityListener.onCheckedChanged(product,value,false);
               }
               notifyDataSetChanged();




           }
       });


       holder.addButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              String quanityString= holder.quantityText.getText().toString();
               int value=Integer.parseInt(quanityString);
               value+=1;
               holder.quantityText.setText(value+"");
               quantityListener.onQuantityChanged(product,value);




           }
       });
       holder.subButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String quanityString= holder.quantityText.getText().toString();
               int value=Integer.parseInt(quanityString);
                value=value-1;
                   holder.quantityText.setText(value+"");





               quantityListener.onQuantityChanged(product,value);

           }
       });
//       int value=Integer.parseInt(holder.quantityText.getText().toString());
     /*   holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product", product);



               // Activity origin = (Activity)context;

               // origin.startActivityForResult(intent,112);
                ((Activity) context).startActivity(intent);



            }
        });
        */


    }

    @Override
    public int getItemCount() {
        Log.d("checkadapsize",cartProductList.size()+"");
        return cartProductList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder
    {
        CheckBox checkBox;
        ImageView imgView;
        TextView productNameText;
        TextView priceText;
        ImageView subButton,addButton;
        TextView quantityText;
        TextView descTextView;
        TextView origPrice;







        public CartViewHolder(View itemView) {
            super(itemView);
            checkBox=(CheckBox)itemView.findViewById(R.id.cart_checkbox);
            imgView=(ImageView)itemView.findViewById(R.id.pd_img);
            productNameText=(TextView)itemView.findViewById(R.id.pd_nm);
            priceText=(TextView)itemView.findViewById(R.id.price);
            subButton=(ImageView) itemView.findViewById(R.id.sub);
            addButton=(ImageView) itemView.findViewById(R.id.add);
            origPrice=(TextView) itemView.findViewById(R.id.orig_price);

            quantityText=(TextView)itemView.findViewById(R.id.quantity);
            descTextView=(TextView)itemView.findViewById(R.id.desc);

            final View addParent = (View) addButton.getParent();
         Log.d("parentadd",addParent.toString());
            addParent.post( new Runnable() {

                public void run() {
                    final Rect r = new Rect();
                    addButton.getHitRect(r);
                    r.top -= 100;
                    r.bottom += 4;
                    addParent.setTouchDelegate( new TouchDelegate( r , addButton));
                }
            });
         /* final View subParent = (View) subButton.getParent();
            Log.d("parentsub",subParent.toString());
            subParent.post( new Runnable() {

                public void run() {
                    final Rect r = new Rect();
                    subButton.getHitRect(r);
                    r.top -= 100;
                    r.bottom += 4;
                    subParent.setTouchDelegate( new TouchDelegate( r , subButton));
                }
            });
            */







         /*   addButton.post( new Runnable() {
                public void run() {
                    final Rect rect = new Rect();
                    addButton.getHitRect(rect);
                    rect.top -= 100;    // increase top hit area
                    rect.left -= 10;   // increase left hit area
                    rect.bottom += 100; // increase bottom hit area
                    rect.right += 10;  // increase right hit area
                    addButton.setTouchDelegate( new TouchDelegate( rect , addButton));
                }
            });
            */
        }
    }
    public interface quantityChangedListener
    {
        public void onQuantityChanged(Product product,int quantity);
        public void onCheckedChanged(Product product,int quantity,boolean checked);
    }

}
