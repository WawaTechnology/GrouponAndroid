package com.easybuy.sg.grouponebuy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.easybuy.sg.grouponebuy.R;

public class ScanReceiptActivity extends AppCompatActivity {
    ImageView imageView;
    ImageView backButton;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_receipt_activity);
        backButton=(ImageView) findViewById(R.id.back);
        imageView=(ImageView)findViewById(R.id.img_enter);
        Glide.with(this).load(R.drawable.icon_enter_order_id).into(imageView);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
