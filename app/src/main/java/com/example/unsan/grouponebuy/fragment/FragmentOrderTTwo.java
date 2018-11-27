package com.example.unsan.grouponebuy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.unsan.grouponebuy.R;
import com.example.unsan.grouponebuy.activities.MyApplication;
import com.example.unsan.grouponebuy.helpers.GlobalProvider;
import com.example.unsan.grouponebuy.model.Customer;

public class FragmentOrderTTwo extends Fragment {
    LinearLayout profileVisiblity,profileInvisibility;
    GlobalProvider globalProvider;
    TextView nameText,telText,districtText,unitDetail,postcodeText;



    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //MyApplication.getRefWatcher(getActivity()).watch(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_orderttwo,container,false);
        profileVisiblity=(LinearLayout)view.findViewById(R.id.profilevisible);
        profileInvisibility=(LinearLayout)view.findViewById(R.id.profileinvisible);
        globalProvider=GlobalProvider.getGlobalProviderInstance(getContext().getApplicationContext());
        nameText=(TextView)view.findViewById(R.id.name);
        telText=(TextView)view.findViewById(R.id.tel);
        postcodeText=(TextView)view.findViewById(R.id.postcode);
        unitDetail=(TextView)view.findViewById(R.id.unitdetails);
        districtText=(TextView)view.findViewById(R.id.district);





        boolean loginStatus=globalProvider.isLogin();
        if(loginStatus)
        {
            profileInvisibility.setVisibility(View.INVISIBLE);
            profileVisiblity.setVisibility(View.VISIBLE);
            Customer customer= globalProvider.getCustomer();
            if(customer!=null) {
                postcodeText.setText(customer.postcode);
                nameText.setText(customer.userName);
                telText.setText(customer.phone);
                if (customer.getAddress() != null) {
                    unitDetail.setText(customer.address);
                }
                if(customer.getDistrict()!=null)
                districtText.setText(customer.getDistrict().getNamePrimaryEn()+" - "+customer.getDistrict().getNameSecondaryEn()+" "+customer.getDistrict().getNameTertiaryEn());
            }
        }
        else
        {

            profileInvisibility.setVisibility(View.VISIBLE);
            profileVisiblity.setVisibility(View.INVISIBLE);




        }

        return view;
    }
public FragmentOrderTTwo()
{

}
}
