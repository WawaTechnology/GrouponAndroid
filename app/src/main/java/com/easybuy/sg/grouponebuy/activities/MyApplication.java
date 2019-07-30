package com.easybuy.sg.grouponebuy.activities;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.network.Constants;
//import com.squareup.leakcanary.LeakCanary;


import java.util.Locale;

public class MyApplication extends Application {


   // private RefWatcher refWatcher;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //whenever local language is changed ;change the share preference

        String languaged= Locale.getDefault().getDisplayLanguage();
       // Log.d("checkconfigchange",languaged);
        if(languaged.contains("中文"))
        {
            Constants.setLanguage(getApplicationContext(),"chinese");
        }
        else
        {
            Constants.setLanguage(getApplicationContext(),"english");
        }





    }
   /* public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }
    */






    @Override
    public void onCreate() {
        super.onCreate();




       // Log.d("application","oncreate");

       // refWatcher = LeakCanary.install(this);

       // ma =this;


     /*   if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        */


      /* String language= Constants.getGetLanguage(this);
       Log.d("chkl",language);
       if(language.equals("english"))
       {
           change("en");
           Log.d("hilang",Locale.getDefault().getDisplayLanguage());
           GlobalProvider.changeLang(getApplicationContext(),"en");


       }
       else {
           change("SIMPLIFIED_CHINESE");
           Log.d("hilang",Locale.getDefault().getDisplayLanguage());
           GlobalProvider.changeLang(getApplicationContext(),"SIMPLIFIED_CHINESE");
       }
       */


        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity,
                                          Bundle savedInstanceState) {

                // new activity created; force its orientation to portrait
                activity.setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


            }

            @Override
            public void onActivityStarted(Activity activity) {
               // Log.d("applicationstate","started");

            }

            @Override
            public void onActivityResumed(Activity activity) {
               // Log.d("applicationstate","resumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
               // Log.d("applicationstate","paused");

            }

            @Override
            public void onActivityStopped(Activity activity) {

               // Log.d("applicationstate","stopped");

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
               // Log.d("applicationstate","des");

            }


        });
    }



    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    @Override
    public void onTerminate() {
       // Log.d("onterminate","called");
        super.onTerminate();


    }


}
