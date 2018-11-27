package com.example.unsan.grouponebuy.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.unsan.grouponebuy.R;
import com.example.unsan.grouponebuy.activities.AboutUsActivity;
import com.example.unsan.grouponebuy.activities.CustomerServiceActivity;
import com.example.unsan.grouponebuy.activities.DistrictSettingActivity;
import com.example.unsan.grouponebuy.helpers.GlobalProvider;
import com.example.unsan.grouponebuy.model.Cycle;

import java.util.ArrayList;
import java.util.List;

public class FragmentOrderTThree extends Fragment {
    RelativeLayout aboutLayout;
    RelativeLayout feedbackLayout;
    Button groupApplyButton;
    RelativeLayout districtLayout;
   RelativeLayout payNowLayout;
    RelativeLayout deliveryTimeButton;
    RelativeLayout qualityAssuarance;
    GlobalProvider globalProvider;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        globalProvider=GlobalProvider.getGlobalProviderInstance(getContext().getApplicationContext());
       // MyApplication.getRefWatcher(getActivity()).watch(this);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_orderthree,container,false);
        aboutLayout=view.findViewById(R.id.aboutus_layout);
        districtLayout=view.findViewById(R.id.district_layout);
        qualityAssuarance=view.findViewById(R.id.replacement_layout);
        feedbackLayout =view.findViewById(R.id.feedback_layout);
        deliveryTimeButton=view.findViewById(R.id.deliverytime_layout);
        payNowLayout=view.findViewById(R.id.paynow_layout);
      //  groupApplyButton=(Button)view.findViewById(R.id.groupon_applybutton);
        districtLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), DistrictSettingActivity.class);
                startActivity(intent);
            }
        });
        aboutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("clicked","here");
                Intent intent=new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
            }
        });

        payNowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //  AlertDialog.Builder alertDialogBuilder=  new AlertDialog.Builder(getContext()).setView(R.layout.custom_paynowalert);


            /* AlertDialog alerDialog= alertDialogBuilder.setPositiveButton(new String("OK"), new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {

                  }
              }).create();
             alerDialog.show();

             Button b=alerDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            b .setTextColor(getResources().getColor(R.color.white));
            b.setBackgroundColor(getResources().getColor(R.color.red));
            alertDialog.show();
            */



//trial
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_paynowalert);

                Button doneButton = (Button) dialog.findViewById(R.id.ok);
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                dialog.show();








            }
        });
        qualityAssuarance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.replacement_dialog);

                Button doneButton = (Button) dialog.findViewById(R.id.ok);
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        deliveryTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!globalProvider.isLogin()) {

                    new AlertDialog.Builder(getActivity()).setTitle("Alert").setMessage("Please login first!").setPositiveButton(new String("OK"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create().show();
                }
                else
                {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("dialogdelivery");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    DialogFragment dialogFragment = new FragmentOrderTThree.MyDialog();



                    dialogFragment.show(ft, "dialogdelivery");
                }


            }
        });



        feedbackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), CustomerServiceActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    public FragmentOrderTThree()
    {

    }
    public static class MyDialog extends DialogFragment
    {
        GlobalProvider globalProvider;
        TextView timingText;
        List<Cycle> cycleList;
        Button okButton;

        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            cycleList=new ArrayList<>();
            globalProvider=GlobalProvider.getGlobalProviderInstance(getContext());
            if(globalProvider.getCustomer().getDistrict()!=null&&globalProvider.getCustomer().getDistrict().getCycle()!=null)

            cycleList.addAll(globalProvider.getCustomer().getDistrict().getCycle()) ;


        }
        public MyDialog()
        {

        }
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            View view=inflater.inflate(R.layout.custom_timedialog,container,false);
            timingText=(TextView)view.findViewById(R.id.timingText);
            okButton=(Button) view.findViewById(R.id.ok);
            String deliveryString="";

            if(cycleList.size()>0) {


                for (Cycle cycle : cycleList) {
                    Log.d("checkcycle",cycle.getWeek()+"");
                    int week = cycle.getWeek();
                    String weekDay = globalProvider.deliveryTiming.get(week);
                    String duration = cycle.getDuration();
                    deliveryString += weekDay + ":    " + duration + "\n" + "\n";
                }
            }
            else
                deliveryString="District not set";
            Log.d("checkdeliverytext",deliveryString);
                timingText.setText(deliveryString);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });


            return view;
        }
    }

}
