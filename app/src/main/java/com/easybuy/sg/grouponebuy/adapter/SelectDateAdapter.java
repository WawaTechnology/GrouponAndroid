package com.easybuy.sg.grouponebuy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.activities.PaymentActivity;
import com.easybuy.sg.grouponebuy.model.Delivery;

import java.util.List;

public class SelectDateAdapter extends RecyclerView.Adapter<SelectDateAdapter.ViewHolder> {
    Context context;
List<Delivery> deliveryList;
MyClickListener myClickListener;
String deliveryDate;



  public SelectDateAdapter(Context context,List<Delivery> deliveryList,String deliveryDate,MyClickListener myClickListener)
  {
      this.context=context;
      this.deliveryList=deliveryList;
      this.myClickListener=myClickListener;
      this.deliveryDate=deliveryDate;

  }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(context).inflate(R.layout.choose_deliverylayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
     Delivery delivery= deliveryList.get(position);



     String date=delivery.getDate()+"  "+delivery.getWeek()+"  "+delivery.getTime();
     if(delivery.getDate().equals(deliveryDate))
     {
         holder.llayout.setBackground(context.getResources().getDrawable(R.drawable.button_rectangle));
     }
     else
         holder.llayout.setBackground(context.getResources().getDrawable(R.drawable.black_rectangle));
     holder.deliveryDate.setText(date);
   ////  holder.deliveryTime.setText(delivery.getTime());
    // holder.deliveryWeek.setText(delivery.getWeek());
     holder.itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             myClickListener.onClick(position);
             holder.llayout.setBackground(context.getResources().getDrawable(R.drawable.button_rectangle));

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
        LinearLayout llayout;
       // LinearLayout mergeMsgLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            deliveryDate=(itemView).findViewById(R.id.date);
            llayout=(itemView).findViewById(R.id.textview_bkg);
          //  mergeMsgLayout=(itemView).findViewById(R.id.merge_notification_layout);

        }
    }
}
