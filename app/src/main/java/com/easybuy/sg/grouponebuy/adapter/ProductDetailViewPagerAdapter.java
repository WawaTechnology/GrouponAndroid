package com.easybuy.sg.grouponebuy.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailViewPagerAdapter extends PagerAdapter {
    OnItemClickListener onItemClickListener;

    private List<ViewGroup> listViews = new ArrayList<ViewGroup>();
    public ProductDetailViewPagerAdapter(List<ViewGroup> listViews,OnItemClickListener onItemClickListener)
    {
        this.listViews=listViews;
        this.onItemClickListener=onItemClickListener;
    }

    @Override
    public int getCount() {
        return listViews.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        try {
            if(listViews.get(position).getParent()==null)
                ((ViewPager) container).addView(listViews.get(position), 0);
            else{
                ((ViewGroup)listViews.get(position).getParent()).removeView(listViews.get(position));
                ((ViewPager) container).addView(listViews.get(position), 0);
            }
            listViews.get(position).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("clicked","here");
                    onItemClickListener.onItemClick(position);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return listViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
