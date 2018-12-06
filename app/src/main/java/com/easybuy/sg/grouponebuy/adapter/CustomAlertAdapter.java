package com.easybuy.sg.grouponebuy.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.model.ProductStock;

import java.util.List;

public class CustomAlertAdapter extends BaseAdapter {


    List<ProductStock> mData;
    Context mContext;
    LayoutInflater inflater;
    public CustomAlertAdapter(List<ProductStock> data, Context context) {
        mData = data;
        mContext = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null ) {

            LayoutInflater mInflater = (LayoutInflater) mContext
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.custom_alert, null);

        }

        TextView pdName = (TextView) convertView.findViewById(R.id.productName);
       // TextView pdStock = (TextView) convertView.findViewById(R.id.productstock);



        int pos=position+1;
        pdName.setText(pos+". "+mData.get(position).getProductName()+"   Stock Available: "+mData.get(position).getStock());




        return convertView;
    }

}
