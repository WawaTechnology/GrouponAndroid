package com.easybuy.sg.grouponebuy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

public class PaymentInformationActivity extends AppCompatActivity {
   ImageView backButton;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_information_layout);
        backButton=(ImageView) findViewById(R.id.back);
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        if(!Constants.getLanguage(this).equals("english"))
        photoView.setImageResource(R.drawable.scanpayment);
        else
            photoView.setImageResource(R.drawable.scaneng);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

       /* imgView=(ImageView)findViewById(R.id.img_payment);
        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(imgView);
        pAttacher.update();
        */


    }

}
