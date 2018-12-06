package com.easybuy.sg.grouponebuy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.model.Delivery;

import java.util.List;

public class SelectDateAdapter extends RecyclerView.Adapter<SelectDateAdapter.ViewHolder> {
    Context context;
List<Delivery> deliveryList;
MyClickListener myClickListener;



  public SelectDateAdapter(Context context,List<Delivery> deliveryList,MyClickListener myClickListener)
  {
      this.context=context;
      this.deliveryList=deliveryList;
      this.myClickListener=myClickListener;
  }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(context).inflate(R.layout.choose_deliverylayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
     Delivery delivery= deliveryList.get(position);

     String date=delivery.getDate()+" "+delivery.getWeek()+" "+delivery.getTime();
     holder.deliveryDate.setText(date);
   ////  holder.deliveryTime.setText(delivery.getTime());
    // holder.deliveryWeek.setText(delivery.getWeek());
     holder.itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             myClickListener.onClick(position);

         }
     });

    }
    public interface MyClickListener
    {
        public void onClick(int position);
    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView deliveryDate;

        public ViewHolder(View itemView) {
            super(itemView);
            deliveryDate=(itemView).findViewById(R.id.date);

        }
    }
}
