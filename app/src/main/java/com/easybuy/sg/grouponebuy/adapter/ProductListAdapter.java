package com.easybuy.sg.grouponebuy.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.activities.MainActivity;
import com.easybuy.sg.grouponebuy.activities.ProductActivity;
import com.easybuy.sg.grouponebuy.activities.ProductDetailActivity;
import com.easybuy.sg.grouponebuy.activities.SearchActivity;
import com.easybuy.sg.grouponebuy.activities.SignInActivity;
import com.easybuy.sg.grouponebuy.fragment.FavoriteFragment;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.model.Cycle;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Product> productList;
    Context context;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    GlobalProvider globalProvider;
    String lang;
    ChangeListener changeListener;
    FragmentManager fragmentManager;

    /*QuantityChangedInterface quantityChangedInterface;
    public interface QuantityChangedInterface {
        void onQuantityChanged(Product product, int quantity);
    }
    */
    public ProductListAdapter(Context context, List<Product> productList, ChangeListener changeListener, FragmentManager fragmentManager)
    {
        this.context=context;
        this.productList=productList;
        lang=Constants.getLanguage(context.getApplicationContext());
        globalProvider=GlobalProvider.getGlobalProviderInstance(context.getApplicationContext());
        this.changeListener=changeListener;
        this.fragmentManager=fragmentManager;

       // setHasStableIds(true);
    }
    public ProductListAdapter(Context context, List<Product> productList,FragmentManager fragmentManager)
    {
        this.context=context;
        this.productList=productList;
        lang=Constants.getLanguage(context.getApplicationContext());
        globalProvider=GlobalProvider.getGlobalProviderInstance(context.getApplicationContext());
        setHasStableIds(true);
        this.fragmentManager=fragmentManager;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }
    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.prod_item, parent, false);
        viewHolder = new MyViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM: {

                final MyViewHolder myholder = (MyViewHolder) holder;
                final Product product = productList.get(position);
                if(lang.equals("english")) {
                    myholder.prodName.setText(product.getNameEn());
                    myholder.prodDetail.setText(product.getSpecificationEn());
                }
                else
                {
                    myholder.prodName.setText(product.getNameCh());
                    myholder.prodDetail.setText(product.getSpecificationCh());

                }
                String actualPrice="$ "+product.getPrice();
                // Log.d("prc",product.getPrice()+"");
                String[] each = actualPrice.split("\\.");
                // Log.d("prc1",each[0]+"");
                // Log.d("prc2",each[1]+"");
                each[0]=each[0]+".";

                Spannable spannable = new SpannableString(actualPrice);

                spannable.setSpan(new AbsoluteSizeSpan(16, true), 0, each[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                myholder.prodPrice.setText(spannable, TextView.BufferType.SPANNABLE);

              //  myholder.prodPrice.setText("$ " + product.getPrice());
                if(product.getStock()==0)
                {
                    myholder.addButton.setVisibility(View.GONE);
                    myholder.stockStatusText.setText("Sold Out");
                    myholder.stockStatusText.setVisibility(View.VISIBLE);
                    if(lang.equals("english"))
                    myholder.soldOutImage.setImageDrawable(context.getDrawable(R.drawable.soldout));
                    else
                        myholder.soldOutImage.setImageDrawable(context.getDrawable(R.drawable.soldout_cn));
                    myholder.soldOutImage.setVisibility(View.VISIBLE);

                }
                else
                {
                    myholder.addButton.setVisibility(View.VISIBLE);

                    myholder.stockStatusText.setVisibility(View.GONE);
                    myholder.soldOutImage.setVisibility(View.GONE);
                }
                if (product.getPriceOriginal() != null&&product.getPriceOriginal()>0) {
                    myholder.originalPriceText.setVisibility(View.VISIBLE);
                    myholder.originalPriceText.setText("$ " + product.getPriceOriginal());
                    myholder.originalPriceText.setPaintFlags(myholder.originalPriceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else
                    myholder.originalPriceText.setVisibility(View.INVISIBLE);
                if(product.getIfWeigh())
                {
                    myholder.weighingIcon.setVisibility(View.VISIBLE);
                }
                else
                    myholder.weighingIcon.setVisibility(View.GONE);



                    if(product.getTotalNumber()>0)
                    {
                       // Log.d("checkprouctquantity",product.getTotalNumber()+"");
                        myholder.minusButton.setVisibility(View.VISIBLE);
                        myholder.quantityText.setVisibility(View.VISIBLE);
                        myholder.quantityText.setText(product.getTotalNumber()+"");
                    }
                    else
                    {
                        myholder.minusButton.setVisibility(View.INVISIBLE);
                        myholder.quantityText.setVisibility(View.INVISIBLE);

                    }




                Glide.with(context).load(Constants.newImageUrl + product.getImageCover()).asBitmap().format(PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.ebuylogo).into(myholder.imgView);
               // Log.d("ggimg", product.getImageCover() + "");
                myholder.subLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (myholder.quantityText.getText().length() > 0) {

                            String quant = myholder.quantityText.getText().toString();

                            int quantity = Integer.parseInt(quant) - 1;
                            if (quantity == 0) {

                                myholder.minusButton.setVisibility(View.GONE);
                                myholder.quantityText.setVisibility(View.GONE);
                                for (Product cartProduct : globalProvider.cartList) {
                                    if (product.getId().equals(cartProduct.getId())) {
                                        globalProvider.cartList.remove(cartProduct);
                                        break;
                                    }
                                }

                            } else {
                                myholder.quantityText.setText(quantity + "");
                                int n = globalProvider.cartList.size();

                                for (int i = 0; i < n; i++) {
                                    if (product.getId().equals(globalProvider.cartList.get(i).getId())) {
                                       // Log.d("getpid", product.getId());
                                        globalProvider.cartList.get(i).setTotalNumber(quantity);
                                        break;
                                    }


                                }


                            }


                            product.setTotalNumber(quantity);
                            notifyDataSetChanged();
                            if (changeListener != null)
                                changeListener.onChange();
                            if (context instanceof MainActivity) {
                                ((MainActivity) context).setCartNum();
                            }


                        }
                    }
                });


           /*     myholder.minusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String quant=myholder.quantityText.getText().toString();
                        int quantity= Integer.parseInt(quant)-1;
                        if(quantity==0)
                        {
                            myholder.minusButton.setVisibility(View.GONE);
                            myholder.quantityText.setVisibility(View.GONE);
                            for(Product cartProduct:globalProvider.cartList)
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
                            myholder.quantityText.setText(quantity+"");
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
                            notifyDataSetChanged();
                            if(changeListener!=null)
                            changeListener.onChange();
                        if(context instanceof MainActivity)
                        {
                            ((MainActivity)context).setCartNum();
                        }





                    }
                });
                */

           myholder.weighingIcon.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   MyDialog myDialog=new MyDialog();
                   myDialog.show(fragmentManager,"tag");
               }
           });
                myholder.addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (globalProvider.isLogin()) {
                            if (myholder.minusButton.getVisibility() == View.INVISIBLE) {
                                myholder.minusButton.setVisibility(View.VISIBLE);
                                myholder.quantityText.setVisibility(View.VISIBLE);
                            }
                            int quantity = 0;

                            if (TextUtils.isEmpty(myholder.quantityText.getText())) {
                                quantity = 1;
                            } else {

                                quantity = Integer.parseInt(myholder.quantityText.getText().toString()) + 1;

                            }

                            if(product.limitPurchase!=null&&product.limitPurchase>0) {
                                if (quantity > product.limitPurchase) {

                                    String msg=context.getResources().getString(R.string.limit_sale_msg,product.limitPurchase);
                                    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();





                                    return;
                                }
                            }


                            myholder.quantityText.setText(quantity + "");
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
                          //  Log.d("checkcategorynm",product.getCategory().getNameEn());









                            product.setTotalNumber(quantity);
                            notifyDataSetChanged();
                            //adding cart number if product is added on fav fragment
                            if(context instanceof MainActivity)
                            {
                                ((MainActivity)context).setCartNum();
                            }
                            if(changeListener!=null)
                          changeListener.onChange();


                            // parent.balanceListOrderQuantity(product,quantity);
                            //  quantityChangedInterface.onQuantityChanged(product,quantity);


                        }
                        else {
                            Intent intent = new Intent(context, SignInActivity.class);
                            context.startActivity(intent);
                        }


                    }

                });
                myholder.prodName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ProductDetailActivity.class);
                        intent.putExtra("product", product);
                        ((Activity) context).startActivity(intent);

                    }
                });



                myholder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ProductDetailActivity.class);
                        intent.putExtra("product", product);
                      /*  String transitionName = product.getNameEn();

                        View viewStart = myholder.prodName;
                        View viewImg = myholder.imgView;
                        Pair<View,String> p1=Pair.create(viewImg, product.getImageCover());
                        Pair<View,String> p2=Pair.create(viewStart,transitionName);
                        */


                        //ActivityOptionsCompat options =ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,viewStart,transitionName);
                     //   ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, p1, p2);


                      //  intent.putStringArrayListExtra("imglist", (ArrayList<String>) product.getImageDisplay());
                       //intent.putExtra("imgtransition", product.getImageCover() + "");
                       // intent.putExtra("imgtransition", "coverimg");

                      //  Activity origin = (Activity)context;


                  //  ((Activity) context).startActivityForResult(intent,111);

                      ((Activity) context).startActivity(intent);


                    }
                });
                break;
            }
            case LOADING:
//                Do nothing
                break;
        }

     /*   Drawable dr=product.getImage();
        Glide.with(context)
                .load("")
                .placeholder(R.drawable.brocolli)
                .into(holder.imgView);
                */

       // holder.imgView.setImageDrawable(dr);



    }


    @Override
    public int getItemCount() {


            return productList == null ? 0 : productList.size();

    }
    @Override
    public int getItemViewType(int position) {
        return (position == productList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public static class MyDialog extends DialogFragment
    {
        Button okButton;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);

            // request a window without the title
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            return dialog;
        }
        public MyDialog()
        {

        }
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            View view=inflater.inflate(R.layout.weighing_alert,container,false);

            okButton=(Button) view.findViewById(R.id.ok);

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });


            return view;
        }
    }



     /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(Product product) {

        productList.add(product);
        notifyItemInserted(productList.size() - 1);
    }

    public void addAll(List<Product> products) {
        for (Product product : products) {
            add(product);
        }
    }

    public void remove(Product product) {
        int position = productList.indexOf(product);
        if (position > -1) {
            productList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Product());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = productList.size() - 1;
        Product product = getItem(position);

        if (product != null) {
            productList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Product getItem(int position) {
        return productList.get(position);
    }
   public interface ChangeListener
    {
        public void onChange();
    }



       /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView,addButton,minusButton,soldOutImage,weighingIcon;
        TextView prodName,prodDetail,prodPrice,originalPriceText,quantityText,stockStatusText;
        FrameLayout subLayout;

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
            subLayout=(FrameLayout)itemView.findViewById(R.id.sublayout);
            soldOutImage=(ImageView)itemView.findViewById(R.id.soldout_img);
            weighingIcon=(ImageView)itemView.findViewById(R.id.weighing_icon);
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
            final View weighImageViewParent=(View)weighingIcon.getParent();
            weighImageViewParent.post(new Runnable() {
                @Override
                public void run() {
                    final Rect r = new Rect();
                    weighingIcon.getHitRect(r);
                    r.right+=50;
                    r.left-=50;
                    r.top -= 100;
                    r.bottom += 4;
                    weighImageViewParent.setTouchDelegate( new TouchDelegate( r , weighingIcon));

                }
            });

        }
    }
    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }



}
