package com.easybuy.sg.grouponebuy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.activities.OrderDetailActivity;
import com.easybuy.sg.grouponebuy.model.Consume;
import com.easybuy.sg.grouponebuy.model.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ConsumeAdapter extends RecyclerView.Adapter<ConsumeAdapter.MyViewHolder> {
    Context context;
    List<Consume> consumeList;
    public ConsumeAdapter(Context context,List<Consume> consumeList)
    {
        this.context=context;
        this.consumeList=consumeList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.consume_item,null,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       Consume consume= consumeList.get(position);
     final  Order order=consume.getOrder();
     Double refundVal=consume.getRefundValue();
     String refundValue=String.format("%.2f",refundVal);
        if(consume.getType().equals("refundDone"))
        {
            holder.typeTextView.setText(context.getResources().getString(R.string.type)+" "+context.getResources().getString(R.string.consume));
            holder.transferTextView.setText("- $"+refundValue);
            holder.transferTextView.setTextColor(context.getResources().getColor(R.color.red));
            holder.moreButton.setVisibility(View.VISIBLE);
            holder.withdrawStatus.setVisibility(View.GONE);

        }
        else if(consume.getType().equals("refundPending"))
        {

            holder.typeTextView.setText(context.getResources().getString(R.string.type)+" "+context.getResources().getString(R.string.refund));
            holder.transferTextView.setText("+ $"+refundValue);
            holder.transferTextView.setTextColor(context.getResources().getColor(R.color.green));
            holder.moreButton.setVisibility(View.VISIBLE);
            holder.withdrawStatus.setVisibility(View.GONE);
        }
        else if(consume.getType().contains("withdraw"))
        {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
            Date d = null;
            try {
                d = input.parse(consume.getDate());
                String formatted = output.format(d);
                holder.dateTextView.setText(formatted);


            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.typeTextView.setText(context.getResources().getString(R.string.type)+" "+context.getResources().getString(R.string.withdraw));
            holder.transferTextView.setText("$"+refundValue);
            holder.moreButton.setVisibility(View.GONE);

            holder.withdrawStatus.setVisibility(View.VISIBLE);
            if(consume.getType().equals("withdrawwaiting"))
            {
                holder.withdrawStatus.setText("(Waiting)");
                holder.withdrawStatus.setTextColor(context.getResources().getColor(R.color.red));
            }
            else
            {
                holder.withdrawStatus.setText("(done)");
                holder.withdrawStatus.setTextColor(context.getResources().getColor(R.color.green));
            }

        }
        if(order!=null) {

            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

            String dateInString = order.getOrderDate();
            String deliveryDate = order.getShippingDate();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            dateInString = dateInString.substring(0, dateInString.length() - 1);
            deliveryDate = deliveryDate.substring(0, deliveryDate.length() - 1);

            Date d = null;
            try {
                d = input.parse(consume.getDate());
                Date date = formatter.parse(dateInString);
               // Log.d("gettime", date.toString());
                Date shippingDate = formatter.parse(deliveryDate);
                long ts = System.currentTimeMillis();
                Date localTime = new Date(ts);
                // Convert UTC to Local Time
                Date fromGmt = new Date(date.getTime() + TimeZone.getDefault().getOffset(localTime.getTime()));
                Date ShippingGmt = new Date(shippingDate.getTime() + TimeZone.getDefault().getOffset(localTime.getTime()));
               // Log.d("getgmttime", fromGmt.toString());
                SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                final String orderdate = simpleDateFormat.format(fromGmt);
                final String time = localDateFormat.format(fromGmt);
                final String delDate = simpleDateFormat.format(ShippingGmt);

                String formatted = output.format(d);
                holder.dateTextView.setText(formatted);
               // Log.d("formatted", formatted);


                holder.moreButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, OrderDetailActivity.class);
                        //Activity origin = (Activity)context;

                        intent.putExtra("Order", order);
                        intent.putExtra("deliverydate", delDate);
                        intent.putExtra("orderDateTime", orderdate + " ," + time);
                        context.startActivity(intent);
                    }
                });
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }




    }

    @Override
    public int getItemCount() {
        return consumeList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView dateTextView;
        Button moreButton;
        TextView typeTextView;
        TextView transferTextView;
        TextView withdrawStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            dateTextView=(TextView)itemView.findViewById(R.id.date);
            moreButton=(Button)itemView.findViewById(R.id.more);
            typeTextView=(TextView)itemView.findViewById(R.id.type);
            transferTextView=(TextView)itemView.findViewById(R.id.transfer);
            withdrawStatus=(TextView)itemView.findViewById(R.id.transfer_status);
        }
    }
}
