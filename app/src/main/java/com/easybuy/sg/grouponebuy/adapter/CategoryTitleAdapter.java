package com.easybuy.sg.grouponebuy.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.model.CategorySummary;
import com.easybuy.sg.grouponebuy.network.Constants;

import java.util.List;

public class CategoryTitleAdapter extends RecyclerView.Adapter<CategoryTitleAdapter.ViewHolder>  {
    Context context;
    List<CategorySummary> categorySummaryList;
    TitleClickListener titleClickListener;
    GlobalProvider globalProvider;
    int row_index=0;
    private int selectedItem = 0;
    private int lastSelected = 0;
    String lang;

    public CategoryTitleAdapter(Context context,List<CategorySummary> categorySummaryList,TitleClickListener titleClickListener)
    {
       this.context=context;
       this.categorySummaryList=categorySummaryList;
       this.titleClickListener=titleClickListener;
       globalProvider=GlobalProvider.getGlobalProviderInstance(context.getApplicationContext());
       lang= Constants.getLanguage(context.getApplicationContext());
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      View view=LayoutInflater.from(context).inflate(R.layout.category_title,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       if(globalProvider.selectedCategory==null) {
           if (row_index == position) {
               holder.titleTextView.setBackgroundColor(Color.parseColor("#ffffff"));
               holder.titleTextView.setTextSize(15);
               holder.titleTextView.setTextColor(context.getResources().getColor(R.color.red));
               holder.viewSelected.setVisibility(View.VISIBLE);
               titleClickListener.onClick(categorySummaryList.get(position).getId());
           } else {
               holder.titleTextView.setBackgroundColor(context.getResources().getColor(R.color.new_grey));
               holder.titleTextView.setTextColor(context.getResources().getColor(R.color.grey_dark));
               holder.titleTextView.setTextSize(13);
               holder.viewSelected.setVisibility(View.GONE);
           }
       }
       else
       {
           if(globalProvider.selectedCategory.equals(categorySummaryList.get(position).getId()))
           {
               holder.titleTextView.setBackgroundColor(Color.parseColor("#ffffff"));
               holder.titleTextView.setTextColor(context.getResources().getColor(R.color.red));
               titleClickListener.onClick(globalProvider.selectedCategory);
               holder.viewSelected.setVisibility(View.VISIBLE);
           }
           else
           {
               holder.titleTextView.setBackgroundColor(context.getResources().getColor(R.color.new_grey));
               holder.titleTextView.setTextColor(context.getResources().getColor(R.color.grey_dark));
               holder.viewSelected.setVisibility(View.GONE);
           }
       }
       String title=null;
       if(lang.equals("english"))
       {
           title= categorySummaryList.get(position).getNameEn();

       }
       else
           title=categorySummaryList.get(position).getNameCh();

       final String categoryName=title;
        holder.titleTextView.setText(title);

        holder.titleTextView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               row_index=position;
               globalProvider.selectedCategoryName=categoryName;
               globalProvider.selectedCategory=categorySummaryList.get(position).getId();


             /*  lastSelected = selectedItem;
               //Save the position of the current selected item
               selectedItem = position;

               //This update the last item selected
               notifyItemChanged(lastSelected);

               //This update the item selected
               notifyItemChanged(selectedItem);
               */
             notifyDataSetChanged();

             // titleClickListener.onClick(globalProvider.selectedCategory);

           }
       });





    }

    @Override
    public int getItemCount() {

        return categorySummaryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView titleTextView;
        View viewSelected;


        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView=(TextView)itemView.findViewById(R.id.categoryTitle);
            viewSelected=(View)itemView.findViewById(R.id.viewSelected);

        }
    }
    public interface TitleClickListener
    {
        public void onClick(String id);
    }
}
