package com.example.unsan.grouponebuy.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.unsan.grouponebuy.R;
import com.example.unsan.grouponebuy.helpers.GlobalProvider;
import com.example.unsan.grouponebuy.network.Constants;
import com.google.gson.Gson;

import java.io.File;

public class SettingActivity extends AppCompatActivity {
    Button logoutButton;
    GlobalProvider globalProvider;
    RelativeLayout changePwdLayout,changeLangLayout,aboutUsLayout,accountLayout;
    ImageView backButton;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        logoutButton=(Button)findViewById(R.id.logout);
        changePwdLayout=(RelativeLayout) findViewById(R.id.change_pwd);
        changeLangLayout=(RelativeLayout) findViewById(R.id.change_langlayout);
        accountLayout=(RelativeLayout) findViewById(R.id.account_settinglayout);
        aboutUsLayout=(RelativeLayout) findViewById(R.id.about_usLayout);
        backButton=(ImageView) findViewById(R.id.back);
        globalProvider=GlobalProvider.getGlobalProviderInstance(getApplicationContext());
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

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalProvider.setLogin(false);
                globalProvider.setCustomer(null);
              //  globalProvider.favoriteList.clear();
                globalProvider.cartList.clear();


                Constants.setCustomer(SettingActivity.this,null);
                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("productListFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("productList",null);
                editor.apply();





                Intent intent=new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);

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
                Intent intent=new Intent(SettingActivity.this,ChangePasswordActivity.class);
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

        File cacheDirectory=getApplicationContext().getCacheDir();
        long size = 0;
        File[] files = cacheDirectory.listFiles();
        Log.d("checkfilesize",files.length+"");
        for (File f:files) {


            size = size+f.length();
        }
        Log.d("internalFile",size+"");
        File externalFiles=getApplicationContext().getExternalCacheDir();
        Log.d("externalFile",externalFiles.length()+"");
        Log.d("checkss",getApplicationContext().getCodeCacheDir().length()+"");
/*       File datacache= getApplicationContext().getDataDir();
       long si=0;
       for(File f:datacache.listFiles())
       {
           si+=f.length();
       }
       Log.d("datasize",si+"");
       */




        Log.d("appsizeis",size+externalFiles.length()+getApplicationContext().getCodeCacheDir().length()+"");
      // int total= (int) (getApplicationContext().getCacheDir().length() + getApplicationContext().getExternalCacheDir().length());
      // Log.d("total",total+"");
    }
}
