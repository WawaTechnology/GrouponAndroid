package com.easybuy.sg.grouponebuy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.model.Layer;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.easybuy.sg.grouponebuy.utils.BoundaryItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.MyViewHolder> {
    List<Layer> layerList;
    Context context;
    String lang;
   // SaleMultProdAdapter saleMultProdAdapter;
   // SaleProductAdapter saleProductAdapter;
   // List<String> productList;

    public SaleAdapter(Context context,List<Layer> layerList)
    {
        this.context=context;
        this.layerList=layerList;
        lang= Constants.getLanguage(context.getApplicationContext());
       // productList=new ArrayList<>();
       // saleProductAdapter=new SaleProductAdapter(context,productList);
        //saleMultProdAdapter=new SaleMultProdAdapter(context,productList);


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sale_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d("checkposi",position+"");
       Layer layer= layerList.get(position);
       if(layer.imageTitleCh==null) {
           holder.imgHeader.setVisibility(View.GONE);
           holder.titleHeader.setVisibility(View.VISIBLE);


           if (layer.getBgColor() != null) {
               Log.d("getcolor", layer.getBgColor());
               if(layer.getBgColor().contains("rgb"))
               {
                   String layercolor=layer.getBgColor();
                   String rgbColor=layercolor.substring(4,layercolor.length()-1);
                   String rgb[]=rgbColor.split(",");
                   int red=Integer.parseInt(rgb[0]);
                   int green=Integer.parseInt(rgb[1]);
                   int blue=Integer.parseInt(rgb[2]);
                   holder.titleHeader.setBackgroundColor(Color.rgb(red,green,blue));




               }
               else {



                   holder.titleHeader.setBackgroundColor(Color.parseColor(layer.getBgColor()));

               }

           }
           else {
               holder.titleHeader.setBackgroundColor(Color.parseColor("#D32F2E"));

           }
      //     Log.d("checkcolor",layer.getColor());
           try {
               if (layer.getColor() != null) {
                   Log.d("checkcolor",layer.getColor());
                   holder.titleHeader.setTextColor(Color.parseColor(layer.getColor()));
               }
           }
           catch(IndexOutOfBoundsException e)
           {
               e.printStackTrace();
           }
           if(lang.equals("english"))

               holder.titleHeader.setText(layer.getNameEn());
           else
               holder.titleHeader.setText(layer.getNameCh());
           Log.d("checkname",layer.getNameEn());
       }
       else
       {
           holder.imgHeader.setVisibility(View.VISIBLE);
           holder.titleHeader.setVisibility(View.GONE);

           String bkg="";
           if(lang.equals("english")) {
                bkg = Constants.newImageUrl + layer.imageTitleEn;
           }
           else
               bkg=Constants.newImageUrl+layer.imageTitleCh;
           Glide.with(context).load(bkg).fitCenter().into(holder.imgHeader);
       }
       Log.d("checktitleheader",layer.getNameEn());
       if(layer.getNameEn().equalsIgnoreCase("Favourite choice"))
       {
           Log.d("layers",layer.getLayer()+"");
       }






      List<String> productList=new ArrayList<>();



       productList.addAll(layer.getProductList());


       // holder.productListRecycler.setNestedScrollingEnabled(false);
       /* holder.productListRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("scrolled","yes");
            }
        });
        */



       if(layer.getDisplay()==1) {
          SaleProductAdapter saleProductAdapter=new SaleProductAdapter(context,productList);

           holder.productListRecycler.setAdapter(saleProductAdapter);
           saleProductAdapter.notifyDataSetChanged();
       /*    holder.productListRecycler.addItemDecoration(new DividerItemDecoration(context,
                   DividerItemDecoration.VERTICAL));
                   */
           LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
           linearLayoutManager.setAutoMeasureEnabled(true);


           holder.productListRecycler.setLayoutManager(linearLayoutManager);


       }
       else {


           GridLayoutManager gridLayoutManager = new GridLayoutManager(context, layer.getDisplay());
           gridLayoutManager.setAutoMeasureEnabled(true);
           holder.productListRecycler.setLayoutManager(gridLayoutManager);
         /* holder.productListRecycler.addItemDecoration(new DividerItemDecoration(context,
                   DividerItemDecoration.HORIZONTAL));
           holder.productListRecycler.addItemDecoration(new DividerItemDecoration(context,
                   DividerItemDecoration.VERTICAL));
                   */


       /*    RecyclerView.ItemDecoration dividerItemDecoration = new BoundaryItemDecoration(context,context.getResources().getColor(R.color.grey_dark),4);
           holder.productListRecycler.addItemDecoration(dividerItemDecoration);
           */



           SaleMultProdAdapter saleMultProdAdapter = new SaleMultProdAdapter(context, productList,position);
           holder.productListRecycler.setAdapter(saleMultProdAdapter);
           saleMultProdAdapter.notifyDataSetChanged();


       }





    }

    @Override
    public int getItemCount() {
        Log.d("checklayerlistsize",layerList.size()+"");
        return layerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView titleHeader;
        ImageView imgHeader;
        RecyclerView productListRecycler;


        public MyViewHolder(View itemView) {
            super(itemView);
            titleHeader=(TextView)itemView.findViewById(R.id.cat_name);
            productListRecycler=(RecyclerView)itemView.findViewById(R.id.prod_list_recycler);
            imgHeader=(ImageView)itemView.findViewById(R.id.img_cat);

        }
    }
}
