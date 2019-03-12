package com.easybuy.sg.grouponebuy.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.easybuy.sg.grouponebuy.BuildConfig;

import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.google.gson.Gson;

import java.io.File;

public class SettingActivity extends AppCompatActivity {
    Button logoutButton;
    GlobalProvider globalProvider;
    RelativeLayout changePwdLayout,changeLangLayout,aboutUsLayout,accountLayout;
    ImageView backButton;
    RelativeLayout callLayout;
    TextView versionNameTextView;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        versionNameTextView=(TextView) findViewById(R.id.version_name);
        logoutButton=(Button)findViewById(R.id.logout);
        changePwdLayout=(RelativeLayout) findViewById(R.id.change_pwd);
        changeLangLayout=(RelativeLayout) findViewById(R.id.change_langlayout);
        accountLayout=(RelativeLayout) findViewById(R.id.account_settinglayout);
        aboutUsLayout=(RelativeLayout) findViewById(R.id.about_usLayout);
        backButton=(ImageView) findViewById(R.id.back);
        callLayout=(RelativeLayout) findViewById(R.id.call_layout);
        globalProvider=GlobalProvider.getGlobalProviderInstance(getApplicationContext());
       // int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        versionNameTextView.setText("V "+versionName);

        if(!globalProvider.isLogin())
        {
            logoutButton.setVisibility(View.GONE);
        }
        else
            logoutButton.setVisibility(View.VISIBLE);
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(globalProvider.isLogin())
                {
                    Intent intent=new Intent(SettingActivity.this,EditInfoActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(SettingActivity.this,"You need to login first!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final View parent = (View) backButton.getParent();  // button: the view you want to enlarge hit area
        parent.post( new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                backButton.getHitRect(rect);
                rect.top -= 10;    // increase top hit area
                rect.left -= 10;   // increase left hit area
                rect.bottom += 10; // increase bottom hit area
                rect.right += 50;  // increase right hit area
                parent.setTouchDelegate( new TouchDelegate( rect , backButton));
            }
        });
        callLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_DIAL);

                intent.setData(Uri.parse("tel:" + "85189139"));
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalProvider.setLogin(false);
                globalProvider.setCustomerId(null);
              //  globalProvider.favoriteList.clear();
                globalProvider.cartList.clear();


                Constants.setCustomer(SettingActivity.this,null);
                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("productListFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("productList",null);
                editor.apply();





                Intent intent=new Intent(SettingActivity.this,MainActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                //finish();

            }
        });
        aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this,AboutUsActivity.class);
                startActivity(intent);
            }
        });
        changePwdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this,ChangePwdActivity.class);
                startActivity(intent);
            }
        });
        changeLangLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this,ChangeLanguageActivity.class);
                startActivity(intent);
            }
        });

       calculateSize();
      // Log.d("total",total+"");
    }
    @Override
    public void onResume()
    {
        if(!globalProvider.isLogin())
        {
            logoutButton.setVisibility(View.GONE);
        }
        else
            logoutButton.setVisibility(View.VISIBLE);
        super.onResume();
    }
    public void calculateSize()
    {
        long size = 0;
        long exsize=0;
        long totalsize=0;



        File[] files = getCacheDir().listFiles();
        for (File f:files) {
            size = size+f.length();
        }
        File[] filex = getExternalCacheDir().listFiles();
        for (File f:filex) {
            exsize = exsize+f.length();
        }
        totalsize=size+exsize;
        Log.d("tss",totalsize+"");


    }
}
