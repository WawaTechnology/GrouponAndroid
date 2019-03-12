package com.easybuy.sg.grouponebuy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.model.PrevOrder;

import java.util.List;

public class MergeDateAdapter extends RecyclerView.Adapter<MergeDateAdapter.MyViewHolder> {

    Context context;
    List<PrevOrder> prevOrderList;

    public MergeDateAdapter(Context context,List<PrevOrder> prevOrderList)
    {
        this.context=context;
        this.prevOrderList=prevOrderList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.select_date_merge,null,false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       PrevOrder prevOrder= prevOrderList.get(position);
       holder.dateText.setText("Yes, update order to deliver on "+prevOrder.getShippingDate());


    }

    @Override
    public int getItemCount() {
        return prevOrderList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView dateText;




        public MyViewHolder(View itemView) {
            super(itemView);
            dateText=(itemView).findViewById(R.id.order_date_merge);
        }
    }
}
