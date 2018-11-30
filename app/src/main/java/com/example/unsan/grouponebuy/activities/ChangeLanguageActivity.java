package com.example.unsan.grouponebuy.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.unsan.grouponebuy.R;
import com.example.unsan.grouponebuy.helpers.GlobalProvider;
import com.example.unsan.grouponebuy.network.Constants;

import java.util.Locale;

public class ChangeLanguageActivity extends AppCompatActivity  {
    RadioButton englishButton;
    RadioButton chineseButton;
    GlobalProvider globalProvider;
    RadioGroup radioGroup;
    Button confirmButton;
    String langChoosed;
    ImageView backButton;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_language);
        confirmButton=(Button) findViewById(R.id.confirm);
        radioGroup=(RadioGroup) findViewById(R.id.radioGroup);
        backButton=(ImageView) findViewById(R.id.back);
        Log.d("langis", Constants.getLanguage(getApplicationContext()));
        globalProvider=GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        englishButton = (RadioButton) findViewById(R.id.english_button);
        chineseButton = (RadioButton) findViewById(R.id.chinese_button);


        if (Constants.getLanguage(getApplicationContext()).equals("english")) {
            radioGroup.check(R.id.english_button);
            langChoosed="english";
            //englishButton.setSelected(true);
        } else {
            radioGroup.check(R.id.chinese_button);
            langChoosed="chinese";
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                Configuration config = getBaseContext().getResources().getConfiguration();


                Locale locale = new Locale("en");
                //Locale.setDefault(locale);

                if(langChoosed.equals("english"))
                {
                    //config.locale = Locale.ENGLISH;
                    locale =Locale.ENGLISH;
                    Locale.setDefault(locale);
                    config.locale=locale;
                    Constants.setLanguage(getApplicationContext(), "english");
                }
                else
                {
                    Log.d("langchanged","chinese");

                   // config.locale = Locale.SIMPLIFIED_CHINESE;

                   // locale = new Locale("zh-rCN");
                    locale=Locale.SIMPLIFIED_CHINESE;
                   // locale=new Locale("hi");
                    Locale.setDefault(locale);
                    config.locale = locale;
                    Constants.setLanguage(getApplicationContext(), "chinese");
                }
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    config.setLocale(locale);
                    createConfigurationContext(config);
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());

                 // getResources().updateConfiguration(config, getResources().getDisplayMetrics());

                }
                else {
                    config.locale=locale;
                   // config.setLocale(locale);
                    //createConfigurationContext(config);


                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());


                }

                globalProvider.isNotFirstTime=false;
                Log.d("checkchangedlang",config.locale.getDisplayLanguage());
                GlobalProvider.changeLang(ChangeLanguageActivity.this,config.locale.getDisplayLanguage());
                Intent intent=new Intent(ChangeLanguageActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });




    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();


        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.chinese_button: {
                if (checked) {
                    langChoosed="chinese";


                }
                break;
            }
                    // Pirates are the best

            case R.id.english_button: {
                if (checked) {
                    langChoosed="english";


                }
                break;
            }
                    // Ninjas rule

        }





    }
}
