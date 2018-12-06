package com.easybuy.sg.grouponebuy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.activities.MainActivity;
import com.easybuy.sg.grouponebuy.fragment.FragmentHome;
import com.easybuy.sg.grouponebuy.fragment.FragmentOrder;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.model.Category;
import com.easybuy.sg.grouponebuy.model.CategorySummary;
import com.easybuy.sg.grouponebuy.network.Constants;

import java.util.List;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    Context context;
    List<CategorySummary> categoryList;
    MyClickListener myClickListener;
    String lang;


    public CategoryAdapter(Context context,List<CategorySummary> categoryList,MyClickListener myClickListener)
    {
        this.context=context;
        this.categoryList=categoryList;
        this.myClickListener=myClickListener;
        lang=Constants.getLanguage(context.getApplicationContext());


    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.category_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CategorySummary category=categoryList.get(position);
       final int width=holder.categoryName.getLayoutParams().width;

        if(lang.equals("english")) {

            holder.categoryName.setText(category.getNameEn());


        }
        else {
            holder.categoryName.setText(category.getNameCh());

        }

       Log.d("getii",category.getImage().substring(8));
       String imgName=category.getImage().substring(8);
       String imgUrl=Constants.baseUrlStr+"uploads/"+imgName;
        Log.d("imgs",imgUrl);
        Glide.with(context)
                .load(imgUrl).asBitmap().format(PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.ebuylogo).error(R.drawable.ebuylogo)

                .into(holder.imgView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalProvider.getGlobalProviderInstance(context).selectedCategory=category.getId();
                GlobalProvider.getGlobalProviderInstance(context).selectedCategoryName=category.getNameEn();
                myClickListener.onCategorySelected();





            }


    });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgView;
        TextView categoryName;

        public ViewHolder(View itemView) {
            super(itemView);
            imgView=(ImageView)itemView.findViewById(R.id.img_cat);
            categoryName=(TextView)itemView.findViewById(R.id.category_name);


        }
    }
    public interface MyClickListener
    {
        public void onCategorySelected();
    }
}
