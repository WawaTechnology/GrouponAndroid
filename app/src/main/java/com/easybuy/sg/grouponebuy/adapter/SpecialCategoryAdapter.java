package com.easybuy.sg.grouponebuy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.activities.ProductDetailActivity;
import com.easybuy.sg.grouponebuy.activities.SpecialCategoryLayout;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.model.CategorySpecial;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.model.ProductImageId;
import com.easybuy.sg.grouponebuy.network.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

public class SpecialCategoryAdapter extends RecyclerView.Adapter<SpecialCategoryAdapter.ViewHolder> {

    Context context;
    List<CategorySpecial> categorySpecials;
    String lang;
    int width;
    List<ProductImageId> singleProducts;



    public SpecialCategoryAdapter(Context context,List<CategorySpecial> categorySpecials)
    {

       this.context=context;
       this.categorySpecials=categorySpecials;
       singleProducts=new ArrayList<>();
       lang=Constants.getLanguage(context.getApplicationContext());
        GlobalProvider globalProvider=GlobalProvider.getGlobalProviderInstance(context.getApplicationContext());
       singleProducts.addAll(globalProvider.singleProductList);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        width=displayMetrics.widthPixels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.layout_special,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        final CategorySpecial categorySpecial=categorySpecials.get(position);


        String img=null;
        if(lang.equals("english")&&categorySpecial.getImageCorner()!=null)
            img = Constants.newImageUrl +categorySpecial.getImageCorner();
        else
            img=Constants.newImageUrl+categorySpecial.getImage();


       holder.splImage.getLayoutParams().height=width/2;
        holder.splImage.requestLayout();

        Glide.with(context).load(img).asBitmap().format(PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ebuylogo).into(holder.splImage);

        if(lang.equals("english")) {

            holder.splCategoryName.setText(categorySpecial.getNameEn());

        }
        else {
            holder.splCategoryName.setText(categorySpecial.getNameCh());

        }
        if(!categorySpecial.getColorList().isEmpty()) {
           holder.splCategoryName.setBackgroundColor(Color.parseColor(categorySpecial.getColorList().get(0)));

            holder.splCategoryName.post(new Runnable() {
                @Override
                public void run() {

                    int lengthCount =holder.splCategoryName.getLineCount(); //check whether your line count goes more
                    if(lengthCount == 2){ //check how much lines maximum it goes and give your wish.
                        holder.splCategoryName.setTextSize(TypedValue.COMPLEX_UNIT_SP,10); //reduce font size
                    }
                }});
            ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
                @Override
                public Shader resize(int width, int height) {
                    return new LinearGradient(width/2, 0, width, height,
                            new int[]{Color.parseColor(categorySpecial.getColorList().get(0)), Color.parseColor(categorySpecial.getColorList().get(0)), Color.parseColor(categorySpecial.getColorList().get(1))},
                            new float[]{0, 0.1f, 1},  // start, center and end position
                            Shader.TileMode.CLAMP);
                }
            };

            PaintDrawable pd = new PaintDrawable();
       pd.setShape(new RectShape());
       // rounded corner for left-top,right-top,right-bottom,left-bottom
       pd.setCornerRadii(new float[] { 0, 0, 50, 50, 50, 50, 0, 0});
           // pd.setShape(R.drawable.rounded_corneright);

            pd.setShaderFactory(sf);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.splCategoryName.setBackground(pd);
            } else
                holder.splCategoryName.setBackgroundDrawable(pd);



        /*

          int colors[]=new int[2];
           colors[0]=Color.parseColor(categorySpecial.getColorList().get(0));
            colors[1]=Color.parseColor(categorySpecial.getColorList().get(1));
            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.TL_BR, colors);

            gd.setGradientType(GradientDrawable.RADIAL_GRADIENT);
            gd.setGradientRadius(300f);
            gd.setCornerRadius(0f);
            holder.splCategoryName.setBackground(gd);
            */




        }
        List<Product> productList=new ArrayList<>();

       // Log.d("splprdfinals",productList.size()+"");
        for(Product product:categorySpecial.getProductList())
        {
            if(product.isOnShelf()==true)
            {
                productList.add(product);
            }
        }



        ProductAdapter productAdapter=new ProductAdapter(context,productList);
        holder.splRecycler.setAdapter(productAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);

        holder.splRecycler.setLayoutManager(linearLayoutManager);

        holder.splImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(context, SpecialCategoryLayout.class);
                intent.putExtra("category",categorySpecial);

                context.startActivity(intent);

            }
        });
        holder.splCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, SpecialCategoryLayout.class);
                intent.putExtra("category",categorySpecial);
                context.startActivity(intent);
            }
        });
        String cover=null;


        for(ProductImageId productImageId:singleProducts)
        {
            if(productImageId.getCategory().equals(categorySpecial.getNameCh()))
            {
               holder.singleImage.getLayoutParams().height=width/2;
                holder.singleImage.requestLayout();

                  cover= Constants.newImageUrl+ productImageId.getProductCover();
               final Product  product=productImageId.getProduct();
                Glide.with(context).load(cover).asBitmap().format(PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ebuylogo).fitCenter().into(holder.singleImage);
                holder.singleImage.setVisibility(View.VISIBLE);
                holder.greySeparator.setVisibility(View.VISIBLE);

                holder.singleImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, ProductDetailActivity.class);
                        intent.putExtra("product", product);
                        context.startActivity(intent);
                    }
                });

                break;
            }
        }

        if(cover==null)
        {
            holder.singleImage.setVisibility(View.GONE);
            holder.greySeparator.setVisibility(View.GONE);

        }


       /* holder.splRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        */



    }

    @Override
    public int getItemCount() {
       // Log.d("categoryspecialsize",categorySpecials.size()+"");
        return categorySpecials.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        RecyclerView splRecycler;
        TextView splCategoryName;
        ImageView splImage;
        ImageView singleImage;
        View greySeparator;


        public ViewHolder(View itemView) {
            super(itemView);
            splRecycler=(RecyclerView)itemView.findViewById(R.id.spl_catprodrecycler);
            splCategoryName=(TextView)itemView.findViewById(R.id.spl_catname);
            splImage=(ImageView)itemView.findViewById(R.id.spl_img);
            singleImage=(ImageView)itemView.findViewById(R.id.single_layoutimage);
            greySeparator=(View)itemView.findViewById(R.id.grey_separator);

        }
    }






}
