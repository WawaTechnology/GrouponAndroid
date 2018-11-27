package com.example.unsan.grouponebuy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.unsan.grouponebuy.R;
import com.example.unsan.grouponebuy.model.Product;
import com.example.unsan.grouponebuy.network.Constants;

import java.util.List;

public class HotAdapter extends RecyclerView.Adapter<HotAdapter.Viewholder> {

   List<String> hotProductList;
   Context context;
   HotInterface hotInterface;

   public HotAdapter(Context context,List<String> hotProductList,HotInterface hotInterface)
   {
       this.context=context;
       this.hotProductList=hotProductList;
       this.hotInterface=hotInterface;

   }


    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(context).inflate(R.layout.hot_item,parent,false);
      return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
      final String product= hotProductList.get(position);

      holder.hotProductText.setText(product);

      holder.hotProductText.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              hotInterface.onHotClicked(product);
          }
      });


    }

    @Override
    public int getItemCount() {
        return hotProductList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
        TextView hotProductText;

        public Viewholder(View itemView) {
            super(itemView);
            hotProductText=(TextView)itemView.findViewById(R.id.hot_productname);
        }
    }
    public interface HotInterface
    {
        public void onHotClicked(String productName);
    }
}
