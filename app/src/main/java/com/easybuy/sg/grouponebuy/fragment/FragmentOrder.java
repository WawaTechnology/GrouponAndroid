package com.easybuy.sg.grouponebuy.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.activities.ConsumeHistoryActivity;
import com.easybuy.sg.grouponebuy.activities.EditInfoActivity;
import com.easybuy.sg.grouponebuy.activities.SettingActivity;
import com.easybuy.sg.grouponebuy.activities.SignInActivity;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.helpers.Utf8JsonRequest;
import com.easybuy.sg.grouponebuy.model.Customer;
import com.easybuy.sg.grouponebuy.model.Result;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragmentOrder extends Fragment {

    TabLayout tabLayout;
    TextView userNameText;


    ImageView settingView,settingViewN;
    ViewPager viewPager;
    Button loginText;
    GlobalProvider globalProvider;
    ImageView imgProfile;
    RelativeLayout frameRelative;
 LinearLayout loggedInLayout;
 TextView balanceTextView;
 Button historyButton;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  MyApplication.getRefWatcher(getActivity()).watch(this);

    }


    public FragmentOrder() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.order_tab);
        viewPager=(ViewPager)view.findViewById(R.id.viewpager);
        settingView = (ImageView) view.findViewById(R.id.setting);
        settingViewN=(ImageView) view.findViewById(R.id.settingn);
        frameRelative=(RelativeLayout)view.findViewById(R.id.frame_relative) ;
        loginText=(Button) view.findViewById(R.id.loginButton);
        userNameText=(TextView) view.findViewById(R.id.userName);
        loggedInLayout=(LinearLayout)view.findViewById(R.id.loggedin_layout) ;
        balanceTextView=(TextView) view.findViewById(R.id.balance);
        historyButton=(Button) view.findViewById(R.id.history);

        Log.d("fragmentordertag","here");
        imgProfile=(ImageView) view.findViewById(R.id.change_info);
     DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

      int width = displayMetrics.widthPixels;
        frameRelative.getLayoutParams().height= (int) (width/2.5);
        frameRelative.requestLayout();






        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        globalProvider=GlobalProvider.getGlobalProviderInstance(getContext().getApplicationContext());
        Customer customer= Constants.getCustomer(getContext());
        if(globalProvider.isLogin()&&customer!=null)
        {
            loggedInLayout.setVisibility(View.VISIBLE);
            frameRelative.setVisibility(View.GONE);
            String name=customer.getUserName();
            //Log.d("username",name);
            name=  name.substring(0,1).toUpperCase()+name.substring(1);



            userNameText.setText(getString(R.string.name)+" "+name);
            String ecoins=String.format("%.2f", customer.getRefund().getECoins());
            balanceTextView.setText(getString(R.string.balance)+" $"+ecoins);
        }
        else
        {
            loggedInLayout.setVisibility(View.GONE);
            frameRelative.setVisibility(View.VISIBLE);
        }



      // Customer customer=globalProvider.getCustomer();


        settingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });
        settingViewN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);

            }
        });
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(getActivity(), SignInActivity.class);
                    startActivity(intent);



            }
        });
        userNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditInfoActivity.class);
                startActivity(intent);
            }
        });
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(globalProvider.isLogin()) {
                    Intent intent = new Intent(getContext(), EditInfoActivity.class);
                    startActivity(intent);
                }

            }
        });
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ConsumeHistoryActivity.class);
                startActivity(intent);
            }
        });
        wrapTabIndicatorToTitle(tabLayout,70,70);
       // tabLayout.setTabIndicatorFullWidth(false);

      /*  tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 2) {
                    Log.d("orderst","here");
                    FragmentOrderOther fragment2 = new FragmentOrderOther();
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_or, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        */
        return view;
    }
    public void onResume()
    {
        super.onResume();
        Log.d("fragmentordertag","hereresume");
        if(globalProvider.isLogin())
        {
            getEcoins();
            userNameText.setText(Constants.getCustomer(getContext()).userName);
        }

             else
            {
                loggedInLayout.setVisibility(View.GONE);
                frameRelative.setVisibility(View.VISIBLE);
            }



    }
    private void getEcoins() {
        String url = Constants.favouriteUrl + "/" +Constants.getCustomer(getContext()).customer_id;


        //  globalProvider.setCustomer(null);
        // Constants.setCustomer(this,null);


        Utf8JsonRequest utf8JsonRequest = new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getcuresponse", response);

                JsonFactory jsonFactory = new JsonFactory();
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonParser jsonParser = jsonFactory.createParser(response);
                    Result favResult = (Result) objectMapper.readValue(jsonParser, Result.class);
                    //   globalProvider.favoriteList.clear();
                    if (favResult.getStatus() == 0 && favResult.getCustomer() != null) {
                        globalProvider.setLogin(true);


                        //  globalProvider.favoriteList.addAll(favResult.getCustomer().getFavoriteList());
                        Customer customer=favResult.getCustomer();
                        if(getContext()!=null) {

                            Constants.setCustomer(getContext(), customer);
                            globalProvider.setCustomerId(Constants.getCustomer(getContext()).customer_id);
                            if(customer.getRefund().getECoins()==-0)
                            {
                                customer.getRefund().setECoins(0.0);
                            }
                            String ecoins = String.format("%.2f", customer.getRefund().getECoins());

                            balanceTextView.setText(getString(R.string.balance) + " $" + ecoins);
                        }
                        // globalProvider.setCustomer(Constants.getCustomer(MainActivity.this));
                    }
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("geterror", error.toString());
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";


                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();


            }
        });
        globalProvider.addRequest(utf8JsonRequest);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FragmentOrderTOne(), getString(R.string.my_order));
        //adapter.addFragment(new FragmentOrderTTwo(), "Profile");
        adapter.addFragment(new FragmentOrderTThree(), getString(R.string.service));
        viewPager.setAdapter(adapter);
    }
    public void onStop()
    {
        super.onStop();
        Log.d("fragmentordertag","herestop");

    }
    public void onDestroy()
    {
        super.onDestroy();
        Log.d("fragmentordertag","hereDestroy");

    }
    public void wrapTabIndicatorToTitle(TabLayout tabLayout, int externalMargin, int internalMargin) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            int childCount = ((ViewGroup) tabStrip).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View tabView = tabStripGroup.getChildAt(i);
                //set minimum width to 0 for instead for small texts, indicator is not wrapped as expected
                tabView.setMinimumWidth(0);
                // set padding to 0 for wrapping indicator as title
                tabView.setPadding(0, tabView.getPaddingTop(), 0, tabView.getPaddingBottom());
                // setting custom margin between tabs
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                    if (i == 0) {
                        // left
                        settingMargin(layoutParams, externalMargin, internalMargin);
                    } else if (i == childCount - 1) {
                        // right
                        settingMargin(layoutParams, internalMargin, externalMargin);
                    } else {
                        // internal
                        settingMargin(layoutParams, internalMargin, internalMargin);
                    }
                }
            }


            tabLayout.requestLayout();
        }
    }

    private void settingMargin(ViewGroup.MarginLayoutParams layoutParams, int start, int end) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(start);
            layoutParams.setMarginEnd(end);
        } else {
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }
}
