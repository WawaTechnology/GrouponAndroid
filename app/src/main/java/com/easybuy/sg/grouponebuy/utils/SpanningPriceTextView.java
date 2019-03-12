package com.easybuy.sg.grouponebuy.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class SpanningPriceTextView extends android.support.v7.widget.AppCompatTextView {
    public SpanningPriceTextView(Context context) {
        super(context);

    }


    @Override
    public void setText(final CharSequence text, BufferType type) {
        //super.setText(text, type);


        String actualPrice = text.toString();
        Log.d("actualPPP",text.length()+"" );
        // Log.d("prc",product.getPrice()+"");
        String[] each = actualPrice.split("\\.");
         Log.d("prc1",each[0]+"");
         Log.d("prc2",each[1]+"");
        each[0] = each[0] + ".";

        Spannable spannable = new SpannableString(actualPrice);

        spannable.setSpan(new AbsoluteSizeSpan(16, true), 0, each[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        super.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    public SpanningPriceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public SpanningPriceTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

}
