package com.easybuy.sg.grouponebuy.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.activities.ProductDetailActivity;
import com.easybuy.sg.grouponebuy.helpers.SpannedGridLayoutManager;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.model.ProductImageId;
import com.easybuy.sg.grouponebuy.network.Constants;

import java.util.List;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

public class SingleTopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<ProductImageId> productImageIdList;
    public static final int SINGLEVIEW=0;
    public static final int DOUBLEVIEW=1;
    public static final int TRIPLEVIEWLEFT=2;
    public static final int TRIPLEVIEWTOPUP=3;
    public static final int TRIPLEVIEWTOPDOWN=4;
    int width;


    public SingleTopAdapter(Context context,List<ProductImageId> productImageIdList)
    {
        this.context=context;
        this.productImageIdList=productImageIdList;
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    width=displayMetrics.widthPixels;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch(viewType)
        {
            case SINGLEVIEW: {
               View view= inflater.inflate(R.layout.single_imagelayout,parent,false);
                viewHolder= new SingleViewHolder(view);
                break;

            }
            case DOUBLEVIEW:
            {
                View view= inflater.inflate(R.layout.single_toplayout,parent,false);
                viewHolder= new MultipleViewHolder(view);
                break;
            }
            case TRIPLEVIEWLEFT:
            {
                View view= inflater.inflate(R.layout.three_left,parent,false);

                viewHolder= new TripleLeftViewHolder(view);
                break;
            }
            case TRIPLEVIEWTOPUP:
            {
                View view= inflater.inflate(R.layout.three_topupimage,parent,false);
                viewHolder= new TripleTopUpViewHolder(view);
                break;
            }
            case TRIPLEVIEWTOPDOWN:

            {
                View view= inflater.inflate(R.layout.three_topdownimage,parent,false);
                viewHolder= new TripleTopDownViewHolder(view);
                break;
            }
        }
        return viewHolder;

    }



    /*  @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view= LayoutInflater.from(context).inflate(R.layout.single_toplayout,parent,false);
            return new ViewHolder(view);
        }
        */
    @Override
    public int getItemViewType(int position) {
    // Log.d("checkpos",productImageIdList.get(position).getViewType()+"");
        switch (productImageIdList.get(position).getViewType()) {
            case 0:
                return SingleTopAdapter.SINGLEVIEW;
            case 1:
                return SingleTopAdapter.DOUBLEVIEW;
            case 2:
                return SingleTopAdapter.TRIPLEVIEWLEFT;
            case 3:
                return SingleTopAdapter.TRIPLEVIEWTOPUP;
            case 4:
                return SingleTopAdapter.TRIPLEVIEWTOPDOWN;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

       ProductImageId productImageId= productImageIdList.get(position);
        final Product product=productImageId.getProduct();

        String cover= Constants.newImageUrl+ productImageId.getProductCover();
       switch(productImageId.getViewType())
       {
           case SINGLEVIEW:
           {
               final SingleViewHolder myholder = (SingleViewHolder) holder;


               myholder.imgView.getLayoutParams().height=width/2;
               myholder.imgView.requestLayout();
               Glide.with(context).load(cover).asBitmap().format(PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.ebuylogo).error(R.drawable.ebuylogo).fitCenter().into(myholder.imgView);
               myholder.imgView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(context, ProductDetailActivity.class);
                       intent.putExtra("product",product);
                       context.startActivity(intent);
                   }
               });
               break;

           }
           case DOUBLEVIEW:
           {

               final MultipleViewHolder multipleViewHolder=(MultipleViewHolder)holder;
            /*   Log.d("checkwidth",width+"");
              multipleViewHolder.imageView.getLayoutParams().height= (int) (width*0.6);
               Log.d("checkwidth",width+"");
               multipleViewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
               multipleViewHolder.imageView.requestLayout();
               */

               Glide.with(context).load(cover).asBitmap().format(PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.ebuylogo).error(R.drawable.ebuylogo).into(multipleViewHolder.imageView);
               multipleViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(context, ProductDetailActivity.class);
                       intent.putExtra("product",product);
                       context.startActivity(intent);
                   }
               });
               break;

           }
           case TRIPLEVIEWLEFT:
           {
               final TripleLeftViewHolder tripleLeftViewHolder=(TripleLeftViewHolder)holder;
              // Log.d("tripleleftcover",cover);
               tripleLeftViewHolder.imageView.getLayoutParams().width=(width/2);
               tripleLeftViewHolder.imageView.getLayoutParams().height=(width/2);
               tripleLeftViewHolder.imageView.requestLayout();


               Glide.with(context).load(cover).asBitmap().format(PREFER_ARGB_8888).fitCenter().diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.ebuylogo).into(tripleLeftViewHolder.imageView);
               tripleLeftViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(context, ProductDetailActivity.class);
                       intent.putExtra("product",product);
                       context.startActivity(intent);
                   }
               });
               break;

           }

           case TRIPLEVIEWTOPUP:
           {

               final TripleTopUpViewHolder tripleTopUpViewHolder=(TripleTopUpViewHolder)holder;
               tripleTopUpViewHolder.imageView.getLayoutParams().width=(width/2);
               tripleTopUpViewHolder.imageView.getLayoutParams().height=(width/4);
               tripleTopUpViewHolder.imageView.requestLayout();
              // Log.d("topup",cover);
               Glide.with(context).load(cover).asBitmap().format(PREFER_ARGB_8888).fitCenter().diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.ebuylogo).into(tripleTopUpViewHolder.imageView);
               tripleTopUpViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(context, ProductDetailActivity.class);
                       intent.putExtra("product",product);
                       context.startActivity(intent);
                   }
               });
               break;
           }
           case TRIPLEVIEWTOPDOWN:
           {
               final TripleTopDownViewHolder tripleTopDownViewHolder=(TripleTopDownViewHolder)holder;
               tripleTopDownViewHolder.imageView.getLayoutParams().width=(width/2);
               tripleTopDownViewHolder.imageView.getLayoutParams().height=(width/4)-8;
               tripleTopDownViewHolder.imageView.requestLayout();
              // Log.d("topdown",cover);
             /*  if(productImageId.getCategory().contains("three-bottom"))
               {
                   if(productImageId.getCategory().equals("three-bottom-left"))
                   {
                       int height= multipleViewHolder.imageView.getHeight()*2;
                       multipleViewHolder.imageView.setMinimumHeight(height);
                   }
                   else
                   {
                       multipleViewHolder.imageView.setMinimumHeight(300);
                   }


               }
               */

               Glide.with(context).load(cover).asBitmap().format(PREFER_ARGB_8888).fitCenter().diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.ebuylogo).into(tripleTopDownViewHolder.imageView);
               tripleTopDownViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(context, ProductDetailActivity.class);
                       intent.putExtra("product",product);
                       context.startActivity(intent);
                   }
               });
               break;
           }
       }






    }

    @Override
    public int getItemCount() {

       // Log.d("productsize",productImageIdList.size()+"");
        return productImageIdList.size();
    }
    public class SingleViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgView;

        public SingleViewHolder(View itemView) {

            super(itemView);
            imgView=(itemView).findViewById(R.id.single_layoutimage);
        }
    }
    public class MultipleViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;

        public MultipleViewHolder(View itemView) {

            super(itemView);
            imageView=(itemView).findViewById(R.id.single_image);
          imageView.getLayoutParams().height= (int) (width*0.6);
         // Log.d("herewidth",width+"");

          imageView.requestLayout();
         // Log.d("checkhh",imageView.getLayoutParams().height+"");
        }
    }
    public class TripleLeftViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;

        public TripleLeftViewHolder(View itemView) {

            super(itemView);
            imageView=(itemView).findViewById(R.id.three_leftimg);
        }
    }
    public class TripleTopUpViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;

        public TripleTopUpViewHolder(View itemView) {

            super(itemView);
            imageView=(itemView).findViewById(R.id.three_topup);
        }
    }
    public class TripleTopDownViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;

        public TripleTopDownViewHolder(View itemView) {

            super(itemView);
            imageView=(itemView).findViewById(R.id.three_topdown);
        }
    }





}
