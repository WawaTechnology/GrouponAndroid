package com.easybuy.sg.grouponebuy.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.databinding.QrPayActivityBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class QrPayActivity extends AppCompatActivity {
    QrPayActivityBinding qrPayActivityBinding;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        qrPayActivityBinding= DataBindingUtil.setContentView(this, R.layout.qr_pay_activity);
        QrClickListener listener=new QrClickListener(this);
        qrPayActivityBinding.setClicklistener(listener);
        qrPayActivityBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Glide.with(this).load(R.drawable.qr).into(qrPayActivityBinding.img);
    }



   public  class QrClickListener
    {
        Context context;
        QrClickListener(Context context)
        {
            this.context=context;

        }
        public void onClicked(View view)  {
            int id=view.getId();
            switch(id)
            {
                case R.id.button_1:
                {
                    Log.d("buttonclicked","one");
                    Intent intent=new Intent(context,ScanReceiptActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.button_2:
                {
                    Log.d("buttonclicked","two");
                    Intent intent=new Intent(context,QrCodeActivity2.class);
                    startActivity(intent);
                    break;

                }
                case R.id.button_3:
                {
                    Log.d("buttonclicked","three");
                    Intent intent=new Intent(context,QrCodeActivity3.class);
                    startActivity(intent);
                    break;
                }
                case R.id.save:
                {
                   // ImageView iv= view.findViewById(R.id.save);
                    //to get the image from the ImageView (say iv)
                    BitmapDrawable draw = (BitmapDrawable) context.getDrawable(R.drawable.qr);
                    Bitmap bitmap = draw.getBitmap();

                    FileOutputStream outStream = null;

                    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                    File dir = new File(root + "/ebuymart");
                    dir.mkdirs();
                    String fileName = String.format("%d.jpg", System.currentTimeMillis());
                    Log.d("filename",fileName);
                    File outFile = new File(dir, fileName);
                    try {
                        outStream = new FileOutputStream(outFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        File f = new File(outFile.getPath());
                        Uri contentUri = Uri.fromFile(f);
                        mediaScanIntent.setData(contentUri);
                        context.sendBroadcast(mediaScanIntent);
                        outStream.flush();
                        outStream.close();
                        Toast.makeText(context,"Saved to Gallery",Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                     catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }

        }
    }

}
