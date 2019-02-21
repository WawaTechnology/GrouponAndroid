package com.easybuy.sg.grouponebuy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.activities.OrderDetailActivity;
import com.easybuy.sg.grouponebuy.model.Order;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.network.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    Context context;
    List<Order> orderList;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
   public boolean isLoadingAdded=false;
    String lang;

    public OrderAdapter(Context context,List<Order> orderList)
    {
        this.context=context;
        this.orderList=orderList;
        lang= Constants.getLanguage(context.getApplicationContext());
    }


    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;

    }
    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.order_item, parent, false);
        viewHolder = new OrderViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM: {
                final Order order = orderList.get(position);
                final OrderAdapter.OrderViewHolder myholder = (OrderAdapter.OrderViewHolder) holder;
                myholder.orderNumberText.setText(order.getOrderCode() + "");


                //holder.orderTimeText.setText(order.get);

                if(order.getState().equalsIgnoreCase("completed"))
                {

                        myholder.orderStatusText.setText(context.getString(R.string.completed));

                    myholder.orderStatusText.setTextColor(context.getResources().getColor(R.color.green));

                }
                else {

                    myholder.orderStatusText.setTextColor(context.getResources().getColor(R.color.orange));
                    if(order.getState().equalsIgnoreCase("waiting"))
                    {
                        myholder.orderStatusText.setText(context.getString(R.string.waiting));
                    }
                   else if(order.getState().equalsIgnoreCase("shipping"))
                    {
                        myholder.orderStatusText.setText(context.getString(R.string.shipping));
                    }
                    else if(order.getState().equalsIgnoreCase("processing"))
                    {
                        myholder.orderStatusText.setText(context.getString(R.string.processing));
                    }

                    else
                    {
                        myholder.orderStatusText.setText(order.getState());
                    }

                }


                String price = String.format("%.2f", order.getTotalPrice());
              if(order.getRefundCostOrder()!=null||order.getRefundCostOrder()>0)
              {
                  double val=order.getTotalPrice()-order.getRefundCostOrder();
                  price=String.format("%.2f",val);

              }


                myholder.totalText.setText("$ " + price);
                String dateInString = order.getOrderDate();
                String deliveryDate = order.getShippingDate();



                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                dateInString = dateInString.substring(0, dateInString.length() - 1);
                deliveryDate = deliveryDate.substring(0, deliveryDate.length() - 1);
                Log.d("checkdeateInstring", dateInString);
                try {
                    Date date = formatter.parse(dateInString);
                    Log.d("gettime", date.toString());
                    Date shippingDate = formatter.parse(deliveryDate);

                    long ts = System.currentTimeMillis();
                    Date localTime = new Date(ts);
                    // Convert UTC to Local Time
                    Date fromGmt = new Date(date.getTime() + TimeZone.getDefault().getOffset(localTime.getTime()));
                    Date ShippingGmt = new Date(shippingDate.getTime() + TimeZone.getDefault().getOffset(localTime.getTime()));
                    Log.d("getgmttime", fromGmt.toString());
                    SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    final String orderdate = simpleDateFormat.format(fromGmt);
                    final String time = localDateFormat.format(fromGmt);
                    final String delDate = simpleDateFormat.format(ShippingGmt);
                    Log.d("gettime", time);
                    myholder.orderDateText.setText(orderdate + " ," + time);
                    myholder.deliveryDateText.setText(delDate);
                    myholder.orderDetailButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, OrderDetailActivity.class);
                           Activity origin = (Activity)context;

                            intent.putExtra("Order",order);
                            intent.putExtra("deliverydate",delDate);
                            intent.putExtra("orderDateTime",orderdate + " ," + time);
                            context.startActivity(intent);
                         //   origin.startActivityForResult(intent,113);

                        }
                    });


                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            case LOADING:
                if(isEmpty())
                {
                    final OrderAdapter.LoadingVH mholder = (OrderAdapter.LoadingVH) holder;
                    mholder.progressBar.setVisibility(View.GONE);

                }
//                Do nothing
                break;


            //holder.deliveryTimeText.setText(order.);


        }
    }

    @Override
    public int getItemCount() {
        Log.d("checkordersize",orderList.size()+"");

        return orderList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return (position == orderList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

     /*
   Helpers
   _________________________________________________________________________________________________
    */



    public void add(Order order) {

        orderList.add(order);
        notifyItemInserted(orderList.size() - 1);
    }

    public void addAll(List<Order> orders) {
        for (Order order : orders) {
            add(order);
        }
    }

    public void remove(Order order) {
        int position = orderList.indexOf(order);
        if (position > -1) {
            orderList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Order());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = orderList.size() - 1;
        Order order = getItem(position);

        if (order != null) {
            orderList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Order getItem(int position) {
        return orderList.get(position);
    }



           /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    class OrderViewHolder extends RecyclerView.ViewHolder
    {
        TextView orderNumberText,orderDateText,orderTimeText,deliveryDateText,deliveryTimeText,orderStatusText,totalText;
        Button orderDetailButton;


        public OrderViewHolder(View itemView) {
            super(itemView);
            orderNumberText=(TextView)itemView.findViewById(R.id.order_num);
            orderDateText=(TextView)itemView.findViewById(R.id.order_date);
            orderTimeText=(TextView)itemView.findViewById(R.id.o_time);
            deliveryDateText=(TextView)itemView.findViewById(R.id.delivery_date);

            orderStatusText=(TextView)itemView.findViewById(R.id.order_status);
            orderDetailButton=(Button)itemView.findViewById(R.id.o_detail);
            totalText=(TextView)itemView.findViewById(R.id.total_num);

        }

    }
    protected class LoadingVH extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingVH(View itemView) {
            super(itemView);
            progressBar=(ProgressBar)itemView.findViewById(R.id.loadmore_progress);
        }
    }


}
