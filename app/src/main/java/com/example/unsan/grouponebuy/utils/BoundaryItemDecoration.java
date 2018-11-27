package com.example.unsan.grouponebuy.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class BoundaryItemDecoration  extends RecyclerView.ItemDecoration{
    private Paint paint;
    private Context context;
    private int dividerHeight;

    private int layoutOrientation = -1;

    public BoundaryItemDecoration(Context ctx, int color, int dHeight){
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(dHeight);

        dividerHeight = dHeight;
        context = ctx;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.set(dividerHeight, dividerHeight, dividerHeight, dividerHeight);

    }
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if(parent.getLayoutManager() instanceof LinearLayoutManager && layoutOrientation == -1){
            layoutOrientation = ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
        }
        if(layoutOrientation == LinearLayoutManager.HORIZONTAL){
            horizontal(c, parent);
        }else if(layoutOrientation == LinearLayoutManager.VERTICAL){
            vertical(c, parent);
        }
        else
        {
            horizontalGrid(c,parent);
        }
    }
    private void horizontal(Canvas c, RecyclerView parent){
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int itemCount = parent.getChildCount();
        for (int i = 0; i < itemCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = child.getLeft() + dividerHeight;
            c.drawRect(left,top,right,bottom, paint);
        }
    }
    private void vertical(Canvas c, RecyclerView parent){
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = child.getTop() + dividerHeight;
            c.drawRect(left,top,right,bottom, paint);
        }
    }
    private void horizontalGrid(Canvas c, RecyclerView parent){
        Log.d("gridcalled","here");
        final int itemCount = parent.getChildCount();
        for (int i = 0; i < itemCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = child.getLeft() + dividerHeight;
            c.drawRect(left,child.getTop()+dividerHeight,right,child.getBottom()+dividerHeight, paint);
        }
    }
}
