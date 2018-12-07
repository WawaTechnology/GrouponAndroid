package com.easybuy.sg.grouponebuy.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.activities.DistrictSettingActivity;
import com.easybuy.sg.grouponebuy.activities.MainActivity;
import com.easybuy.sg.grouponebuy.activities.MyApplication;
import com.easybuy.sg.grouponebuy.adapter.CartAdapter;
import com.easybuy.sg.grouponebuy.adapter.CustomAlertAdapter;
import com.easybuy.sg.grouponebuy.adapter.SelectDateAdapter;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.helpers.Utf8JsonRequest;
import com.easybuy.sg.grouponebuy.model.CartProduct;
import com.easybuy.sg.grouponebuy.model.Category;
import com.easybuy.sg.grouponebuy.model.CategoryPrimaryList;
import com.easybuy.sg.grouponebuy.model.Customer;
import com.easybuy.sg.grouponebuy.model.Cycle;
import com.easybuy.sg.grouponebuy.model.Delivery;
import com.easybuy.sg.grouponebuy.model.District;
import com.easybuy.sg.grouponebuy.model.OrderResult;
import com.easybuy.sg.grouponebuy.model.PrevOrder;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.model.ProductInfo;
import com.easybuy.sg.grouponebuy.model.ProductStock;
import com.easybuy.sg.grouponebuy.model.ResultProductList;
import com.easybuy.sg.grouponebuy.model.SpecialCategoryList;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;



//check total 9.9 if any of the previous order's date is not equal to current's order date

public class FragmentCart extends Fragment implements CartAdapter.quantityChangedListener {
    private static final int DIALOG_FRAGMENT =123 ;
    public List<CartProduct> cartProductList;
    CartAdapter cartAdapter;
    RecyclerView recyclerView;
    GlobalProvider globalProvider;
    String language;
RelativeLayout cartLayout;


    List<PrevOrder> prevOrderList;
    Button payButton;
    final int Cart_Code=112;
    LinearLayout noCartLayout;
    LinearLayout checkLayout;
   // Button editButton;
    //LinearLayout saveBottomLayout;
    LinearLayout editBottomLayout;
   String prevOrder;
    TextView minSpendTextView;
    boolean addToexistingOrder;
   // public static boolean isSaveClicked;





    TextView amountText;
    CheckBox allCheckBox;


    static double totalamt = 0.0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Log.d("fragmentoncreate","oncreate");
       // MyApplication.getRefWatcher(getActivity()).watch(this);
        cartProductList = new ArrayList<>();
        prevOrderList=new ArrayList<>();
        language=Constants.getLanguage(getContext());





        cartAdapter = new CartAdapter(cartProductList, getContext(), this);

        globalProvider = GlobalProvider.getGlobalProviderInstance(getContext().getApplicationContext());
       if(globalProvider.cartList.size()>0) {
           //Finding categoryPrimary
           String url = Constants.baseUrlStr + "categoryPrimarys";
           Utf8JsonRequest utf8JsonRequest = new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
               @Override
               public void onResponse(String response) {
                 //  Log.d("catresponse", response);
                   JsonFactory jsonFactory = new JsonFactory();
                   ObjectMapper objectMapper = new ObjectMapper();
                   try {
                       JsonParser jsonParser = jsonFactory.createParser(response);
                       CategoryPrimaryList categoryPList = (CategoryPrimaryList) objectMapper.readValue(jsonParser, CategoryPrimaryList.class);
                       for (Product product : globalProvider.cartList) {
                           String catNam = product.getCategory().getNameEn();
                           //Log.d("catName", catNam);
                           for (int i = 0; i < categoryPList.getPayload().size(); i++) {
                               //Log.d("catgoryprimaryname", categoryPList.getPayload().get(i).getNameEn());
                              // Log.d("categoryprimaryid", categoryPList.getPayload().get(i).getId());
                               //Log.d("catname", categoryPList.getPayload().get(i).getCategories().get(0).getNameEn());
                               for (Category categoryP : categoryPList.getPayload().get(i).getCategories()) {
                                 //  Log.d("checkpm", categoryP.getNameEn());
                                   if (categoryP.getNameEn().equals(catNam)) {
                                       String categoryChinese = categoryPList.getPayload().get(i).getNameCh();

                                      // Log.d("chiese", categoryChinese);
                                     //  Log.d("engnm", categoryPList.getPayload().get(i).getNameEn());
                                       globalProvider.categoryNameMap.put(product.getId(), categoryChinese);

                                       break;
                                   }
                               }


                           }
                       }



                   } catch (JsonParseException e) {
                       e.printStackTrace();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }


               }
           }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   String message=null;
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
                   Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                  // Log.d("errorcat", error.toString());

               }
           });
           globalProvider.addRequest(utf8JsonRequest);

       }



    }
    public void onResume()
    {
        super.onResume();
        if(totalamt>9.9)
        {
            minSpendTextView.setVisibility(View.GONE);
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DIALOG_FRAGMENT:
                if (resultCode == Activity.RESULT_OK&&data.getStringExtra("status").equals("ok")) {
                    Toast.makeText(getContext(), getString(R.string.order_success), Toast.LENGTH_SHORT).show();
                   cartProductList.clear();
                   /* SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
                    SharedPreferences.Editor editor= prefs.edit();
                    editor.clear();
                    */
                   globalProvider.cartList.clear();

                   //cartAdapter.notifyDataSetChanged();
                   globalProvider.categoryNameMap.clear();
                   totalamt=0.0;
                  // amountText.setText(totalamt+"");
                 //   editBottomLayout.setVisibility(View.GONE);


                    noCartLayout.setVisibility(View.VISIBLE);
                    cartLayout.setVisibility(View.GONE);
                  //  checkLayout.setVisibility(View.GONE);

                    //recyclerView.setVisibility(View.GONE);

                    ((MainActivity)getContext()).HideCartNum();

                }
                else
                {
                    if(data.getStringExtra("status").equals("not available"))
                    {
                        List<ProductStock> productStockList=new ArrayList<>();
                        //productStockList.addAll((List<ProductStock>) data.getSerializableExtra("notavailList"));
                        productStockList= (List<ProductStock>) data.getSerializableExtra("notavailList");

                           for(ProductStock productStock:productStockList)
                            {
                                for(CartProduct product:cartProductList)
                                {
                                    String productName=null;

                                    // todo change language of product name
                                    if(language.equals("english"))
                                    {
                                       productName= product.getProduct().getNameEn();
                                    }
                                    else
                                        productName=product.getProduct().getNameCh();

                                if(productName.equals(productStock.getProductName()))
                                {
                                    //todo check the logic
                                    if(productStock.getStock()<=0)
                                    {
                                        cartProductList.remove(product);
                                    }
                                    else
                                    product.getProduct().setTotalNumber(productStock.getStock());
                                    break;

                                }
                            }
                        }
                        globalProvider.cartList.clear();
                           for(CartProduct cartProduct:cartProductList)
                           {
                               globalProvider.cartList.add(cartProduct.getProduct());
                           }
                        calculateTotal();
                        cartAdapter.notifyDataSetChanged();
                    }
                }

                break;
            case Cart_Code:
            {
              //  Log.d("cartupdated","here");
                Product product=(Product)data.getSerializableExtra("productupdated");
                for(CartProduct prod:cartProductList)
                {
                    if(prod.getProduct().getId().equals(product.getId()))
                    {
                        prod.getProduct().setTotalNumber(product.getTotalNumber());
                        if(prod.getProduct().getTotalNumber()==0||prod.getProduct().getTotalNumber()==0.0)
                        {
                            cartProductList.remove(prod);
                        }
                        break;

                    }
                }
                calculateTotal();

                cartAdapter.notifyDataSetChanged();
            }
            }
        }




            public void onStop()
            {

                Gson gson=new Gson();
                String productList=gson.toJson(globalProvider.cartList);
                SharedPreferences sharedPreferences=getContext().getApplicationContext().getSharedPreferences("productListFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("productList",productList);
                editor.apply();
                super.onStop();
            }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("fragmentoncreate","oncreateview");
        View view = inflater.inflate(R.layout.fragment_cart, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.cart_recycler);
        cartLayout=(RelativeLayout) view.findViewById(R.id.cart_layout);

        checkLayout=(LinearLayout) view.findViewById(R.id.check_layout);
      //  editButton=(Button)view.findViewById(R.id.edit_button);
        editBottomLayout=(LinearLayout) view.findViewById(R.id.edit_bottom);
       // saveBottomLayout =(LinearLayout)view.findViewById(R.id.save_bottom);
        minSpendTextView=(TextView) view.findViewById(R.id.min_spendmsg);

        amountText = (TextView) view.findViewById(R.id.amount);
        allCheckBox = (CheckBox) view.findViewById(R.id.all_check);
        payButton = (Button) view.findViewById(R.id.pay_button);


        noCartLayout=(LinearLayout) view.findViewById(R.id.cart_none);
        checkPendingOrder();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cartAdapter);

        if(globalProvider.isLogin()&&globalProvider.cartList.size()>0) {

            getCartProducts();
            noCartLayout.setVisibility(View.GONE);
            cartLayout.setVisibility(View.VISIBLE);
           // recyclerView.setVisibility(View.VISIBLE);
          //  checkLayout.setVisibility(View.VISIBLE);


        }
        else
        {
            noCartLayout.setVisibility(View.VISIBLE);
            cartLayout.setVisibility(View.GONE);
            //recyclerView.setVisibility(View.GONE);
           // editBottomLayout.setVisibility(View.GONE);
            //checkLayout.setVisibility(View.GONE);

           // editButton.setVisibility(View.GONE);

        }



        if(globalProvider.cartList!=null&&globalProvider.cartList.size()>0)
        {
            allCheckBox.setChecked(true);
        }
        else
            allCheckBox.setChecked(false);
    /*    editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editButton.getText().equals(getString(R.string.edit)))
                {
                   // isSaveClicked=true;
                  editButton.setText(R.string.save);
                    saveBottomLayout.setVisibility(View.VISIBLE);
                    editBottomLayout.setVisibility(View.GONE);
                   // cartAdapter.notifyDataSetChanged();
                   // allCheckBox.setChecked(false);
                }
                else {
                   // isSaveClicked=false;
                    editButton.setText(R.string.edit);
                    saveBottomLayout.setVisibility(View.GONE);
                    editBottomLayout.setVisibility(View.VISIBLE);
                    //cartAdapter.notifyDataSetChanged();
                   // allCheckBox.setChecked(true);
                }

            }
        });
        */
        allCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allCheckBox.isChecked())
                {
                    for(int i=0;i<cartProductList.size();i++)
                    {
                        if(cartProductList.get(i).isCheck()==false) {
                            globalProvider.cartList.add(cartProductList.get(i).getProduct());

                            cartProductList.get(i).setCheck(true);
                        }

                    }
                    cartAdapter.notifyDataSetChanged();
                    calculateTotal();


                }
                else
                {
                    for(int i=0;i<cartProductList.size();i++)
                    {


                        cartProductList.get(i).setCheck(false);

                    }
                    globalProvider.cartList.clear();
                    cartAdapter.notifyDataSetChanged();
                    totalamt=0.0;
                    amountText.setText("$ "+totalamt);

                }
                if(globalProvider.cartList.size()>0)
                    ((MainActivity)getContext()).setCartNum();
                else
                    ((MainActivity)getContext()).HideCartNum();
            }
        });
        if(prevOrder!=null) {
            minSpendTextView.setVisibility(View.GONE);
            Log.d("checkpreco",prevOrder);
        }
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  /*  new AlertDialog.Builder(getContext()).setView(R.layout.dialog_confirmorder).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    });
                    */
                //   Log.d("checkprevorder",prevOrder);

                if (globalProvider.getCustomer().getDistrict() == null) {
                    Intent intent = new Intent(getContext(), DistrictSettingActivity.class);
                    startActivity(intent);

                } else {
                    if (totalamt > 9.9 || prevOrder != null) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);
                        DialogFragment dialogFragment = new FragmentCart.MyDialogFragment();
                        Bundle args = new Bundle();
                        args.putSerializable("prevOrderList", (Serializable) prevOrderList);
                       // args.putString("prevOrder", prevOrder);
                        dialogFragment.setArguments(args);
                        dialogFragment.setTargetFragment(FragmentCart.this, DIALOG_FRAGMENT);


                        dialogFragment.show(ft, "dialog");
                    } else {
                        Toast.makeText(getContext(), getString(R.string.min_spend), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });



        return view;
    }

    private void getCartProducts() {

        if (globalProvider.cartList.size() > 0) {
            Log.d("checksize", globalProvider.cartList.size() + "");
            calculateTotal();


            recyclerView.setVisibility(View.VISIBLE);

            for(Product product:globalProvider.cartList)
            {
                cartProductList.add(new CartProduct(product,true));
            }




            cartAdapter.notifyDataSetChanged();


        } else {

            recyclerView.setVisibility(View.GONE);

        }


    }

    private void checkPendingOrder() {
        if (globalProvider.isLogin()) {

            String url= Constants.checkOrderUrl+globalProvider.getCustomer().customer_id;
            Log.d("checkprevorderurl",url);
            Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ObjectMapper objectMapper=new ObjectMapper();
                    JsonFactory jsonFactory=new JsonFactory();
                    try
                    {
                        JsonParser jsonParser =   jsonFactory.createParser(response);
                        OrderResult resultClass = (OrderResult ) objectMapper.readValue(jsonParser, OrderResult .class);
                        if(resultClass.getStatus()==1)
                        {
                           prevOrder=null;

                        }
                        else
                        {
                            Log.d("prevorder is something","here");
                            prevOrder=resultClass.getPayload().get(0).getOrderID();
                            String shippingDate=resultClass.getPayload().get(0).getShippingDate();
                            for(PrevOrder order:resultClass.getPayload())
                            {
                                prevOrderList.add(order);
                            }


                          Log.d("prevorder",prevOrder);
                            minSpendTextView.setVisibility(View.GONE);

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

                }
            });
            globalProvider.addRequest(utf8JsonRequest);

        }
    }

    private void calculateTotal() {
        totalamt=0.0;


        for (Product cartproducts : globalProvider.cartList) {
            totalamt = totalamt + cartproducts.getTotalNumber() * cartproducts.getPrice();

        }
        totalamt=Double.parseDouble(new DecimalFormat("##.##").format(totalamt));
        amountText.setText("$ "+totalamt);
    }


    @Override
    public void onQuantityChanged(Product product, int quantity) {
        if (quantity == 0) {
            globalProvider.cartList.remove(product);
            for(CartProduct cp:cartProductList)
            {
                if(cp.getProduct().getId().equals(product.getId()))
                {
                    cartProductList.remove(cp);
                    break;
                }
            }
         /*   SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
            SharedPreferences.Editor editor= prefs.edit();


            editor.remove(product.getId());
            editor.apply();
            */


            cartAdapter.notifyDataSetChanged();
            if(globalProvider.cartList.isEmpty())
            {
                //editBottomLayout.setVisibility(View.GONE);
                cartLayout.setVisibility(View.GONE);
                noCartLayout.setVisibility(View.VISIBLE);

               // checkLayout.setVisibility(View.GONE);

               // recyclerView.setVisibility(View.GONE);

            }

        } else {
            for (Product cartProduct : globalProvider.cartList) {
                if (cartProduct.getId().equals(product.getId())) {
                    cartProduct.setTotalNumber(quantity);

                    break;

                }
            }

            product.setTotalNumber(quantity);
           /* SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
            SharedPreferences.Editor editor= prefs.edit();


            editor.putInt(product.getId(),quantity);
            editor.apply();
            */
            cartAdapter.notifyDataSetChanged();
        }
        calculateTotal();

        if(globalProvider.cartList.size()>0)
            ((MainActivity)getContext()).setCartNum();
        else
            ((MainActivity)getContext()).HideCartNum();


    }

    @Override
    public void onCheckedChanged(Product product, int quantity, boolean checked) {
        Log.d("getchecked", checked + "");

        if (checked) {

            globalProvider.cartList.add(product);
          /*  SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
            SharedPreferences.Editor editor= prefs.edit();


            editor.putInt(product.getId(),quantity);
            editor.apply();
            */
            for(int i=0;i<cartProductList.size();i++)
            {
                if(cartProductList.get(i).isCheck()==false)
                {
                    break;
                }
                if(i==cartProductList.size()-1)
                {
                    allCheckBox.setChecked(true);
                }


            }
            calculateTotal();
        } else {



            for(CartProduct cartProduct:cartProductList)
            {
                if(cartProduct.getProduct().getId().equals(product.getId()))
                {
                    globalProvider.cartList.remove(product);
                    break;
                }
            }
           /* SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
            SharedPreferences.Editor editor= prefs.edit();


            editor.remove(product.getId());
            editor.apply();
            */
            allCheckBox.setChecked(false);
            calculateTotal();
        }

        if(globalProvider.cartList.size()>0)
        ((MainActivity)getContext()).setCartNum();
        else
            ((MainActivity)getContext()).HideCartNum();



    }




    public static class MyDialogFragment extends DialogFragment {
        GlobalProvider globalProvider;
        TextView districtName, unitNumber, deliveryDate;
        District district;
        int deliveryWeek;
        String deli;
        Button cancel,confirm;
        String prevOrder;
        String duration;

        Customer customer;
        int todayWEEK;
        int nextDeliveries[];
        String nextDeliveryDates[];
        String nextDeliveryTimimgs[];
        String deliveryDat;
        String actualDeliveryDate;
        EditText remarkText;
        RadioButton cashRadioButton,payNowRadioButton,yesRadioButton,noRadioButton;
        TextView changeText;
        List<Delivery> deliveryList;
        public static final int DELIVERY_CONSTANT=212;
        ArrayList<PrevOrder> prevOrderList;
        String lang;
        RadioGroup paymentGroup,paperGroup;

        // make sure the Activity implemented it

        public void onStart()
        {
            super.onStart();

        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            lang=Constants.getLanguage(getContext());
            globalProvider = GlobalProvider.getGlobalProviderInstance(getContext().getApplicationContext());
            deliveryList=new ArrayList<>();
            prevOrderList=new ArrayList<>();


            customer = globalProvider.getCustomer();
            district = customer.getDistrict();
            Calendar calendar = Calendar.getInstance();

            nextDeliveries=new int[2];
            nextDeliveryDates=new String[2];
            nextDeliveryTimimgs=new String[2];
            Date now = new Date();
            calendar.setTime(now);


            todayWEEK = calendar.get(Calendar.DAY_OF_WEEK);
            todayWEEK-=1;
            Log.d("checktoday",todayWEEK+"");

            Bundle mArgs = getArguments();

            prevOrderList = (ArrayList<PrevOrder>) mArgs.getSerializable("prevOrderList");
            if(!prevOrderList.isEmpty())
            {
                Log.d("checkplistsize",prevOrderList.size()+"");
            }


            Log.d("gettt", todayWEEK + "");



            List<Cycle> cycleList = district.getCycle();
            Log.d("checksize",cycleList.size()+"");
            deliveryWeek=cycleList.get(0).getWeek();
            duration=cycleList.get(0).getDuration();

//get deliveryweek greater than today
            for (Cycle cycle : cycleList) {
                int week = cycle.getWeek();
                Log.d("checkweek",week+"");
                if(week>todayWEEK)
                {
                    deliveryWeek=week;
                   duration= cycle.getDuration();
                    break;
                }

               /* if (todayWEEK == week) {
                    deliveryWeek = todayWEEK;

                    break;
                } else {
                    if (week > todayWEEK) {
                        deliveryWeek = week;
                        break;
                    }
                }
                */
            }
            Log.d("checkdeliveryweek", deliveryWeek + "");
            Log.d("checktodayweek", todayWEEK + "");
            if (deliveryWeek == todayWEEK) {


            } else {
                while (deliveryWeek != todayWEEK) {
                    if (todayWEEK == 7) {
                        todayWEEK = 0;
                    }
                    todayWEEK += 1;
                    now = addDays(now, 1);


                }
            }
            Date nextDate=now;

            int count=1;

                int i=0;
                for(int k=0;k<cycleList.size();k++)
                {
                    Log.d("chceckkk",cycleList.get(k).getWeek()+"");

                    if(cycleList.get(k).getWeek()>deliveryWeek)
                    {


                        nextDeliveries[i]=cycleList.get(k).getWeek();
                        nextDeliveryTimimgs[i]=cycleList.get(k).getDuration();
                        int numOfDays=cycleList.get(k).getWeek()-deliveryWeek;
                        nextDate=addDays(now,numOfDays);
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy/MM/dd");

                        String nextdeliveryDate= simpleDateFormat1.format(nextDate);
                        nextDeliveryDates[i]=nextdeliveryDate;


                        i+=1;
                        count+=1;
                        if(i==2)
                        {
                            break;
                        }

                    }

                }
                int j=0;
                while(count!=3)
                {
                    nextDeliveries[i]=cycleList.get(j).getWeek();
                    nextDeliveryTimimgs[i]=cycleList.get(j).getDuration();
                    if(cycleList.get(j).getWeek()<deliveryWeek)
                    {
                        int numOfDays=7-deliveryWeek+cycleList.get(j).getWeek();
                        nextDate=addDays(now,numOfDays);
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
                        String nextdeliveryDate= simpleDateFormat1.format(nextDate);
                        nextDeliveryDates[i]=nextdeliveryDate;

                    }

                    j+=1;
                    count+=1;
                    i+=1;

                }

             /*   for(int l:nextDeliveries)
                {
                    Log.d("checkdelivery",l+"");
                }
                for(String s:nextDeliveryDates)
                {
                    Log.d("nextDeliveries",s);
                }





            Log.d("getdeliverydate", now.toString());
            */


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy E");
            deli = simpleDateFormat.format(now);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
          //  Log.d("checkdeli", deli);
            deliveryDat = simpleDateFormat1.format(now);

           // actualDeliveryDate=deliveryDat;



            if(lang.equals("english")) {
                Delivery currentDelivery=new Delivery(deliveryDat,globalProvider.getDeliveryTiming().get(deliveryWeek),duration);
                deliveryList.add(currentDelivery);
                for (int k = 0; k < 2; k++) {
                    String week = globalProvider.getDeliveryTiming().get(nextDeliveries[k]);
                    Delivery delivery = new Delivery(nextDeliveryDates[k], week, nextDeliveryTimimgs[k]);
                    deliveryList.add(delivery);
                }
            }
            else
            {
                String week=globalProvider.getDeliveryTiming().get(deliveryWeek);
                week=globalProvider.deliveryTimingChinese.get(week);

                Delivery currentDelivery=new Delivery(deliveryDat,week,duration);
                deliveryList.add(currentDelivery);
                for (int k = 0; k < 2; k++) {
                    String weekString = globalProvider.getDeliveryTiming().get(nextDeliveries[k]);
                    weekString=globalProvider.deliveryTimingChinese.get(weekString);
                    Delivery delivery = new Delivery(nextDeliveryDates[k], weekString, nextDeliveryTimimgs[k]);
                    deliveryList.add(delivery);
                }
            }





                //**** Here*****//




        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case DELIVERY_CONSTANT:
                    if(resultCode==Activity.RESULT_OK) {
                        Delivery delivery=data.getParcelableExtra("selectedDate");



                        deliveryDate.setText(delivery.getDate()+" "+delivery.getWeek()+" "+delivery.getTime());
                        deliveryDat=delivery.getDate();
                    }
                    break;



                    }


            }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);

            // request a window without the title
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            return dialog;
        }



        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_confirmorder, container, false);
            districtName = (TextView) view.findViewById(R.id.district_name);
            unitNumber = (TextView) view.findViewById(R.id.unit_number);
            //todo change deliverydate to chinese
            deliveryDate = (TextView) view.findViewById(R.id.delivery_date);
            changeText=(TextView) view.findViewById(R.id.change_text);
            cancel=(Button)view.findViewById(R.id.cancel);
            confirm=(Button) view.findViewById(R.id.confirm);
            remarkText=(EditText) view.findViewById(R.id.remark);
            cashRadioButton=(RadioButton) view.findViewById(R.id.cashRadioButton);
            payNowRadioButton=(RadioButton) view.findViewById(R.id.payNowRadioButton);
            yesRadioButton=(RadioButton)view.findViewById(R.id.yesRadioButton);
            paperGroup=(RadioGroup) view.findViewById(R.id.paper_radiogroup);

            paymentGroup=(RadioGroup) view.findViewById(R.id.payment_group);

            noRadioButton=(RadioButton)view.findViewById(R.id.noRadioButton);

           SharedPreferences sp= getActivity().getPreferences(Context.MODE_PRIVATE);
           int res=sp.getInt("paymentkey",0);
           int billres=sp.getInt("billkey",1);
           if(res==0)
           {
               paymentGroup.check(R.id.cashRadioButton);
           }
           else
               paymentGroup.check(R.id.payNowRadioButton);
           if(billres==0)
           {
               paperGroup.check(R.id.yesRadioButton);
           }
           else
               paperGroup.check(R.id.noRadioButton);
           paperGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(RadioGroup radioGroup, int i) {
                   Log.d("here","radio");
                   SharedPreferences.Editor e = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
                   if(i==R.id.yesRadioButton)
                   {

                       e.putInt ("billkey",0);

                   }
                   else
                   {
                       e.putInt("billkey",1);
                   }
                   e.commit();

               }
           });


            paymentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    SharedPreferences.Editor e = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
                    if(i==R.id.cashRadioButton)
                    {

                        e.putInt ("paymentkey",0);

                    }
                    else
                    {
                        e.putInt("paymentkey",1);
                    }
                    e.commit();
                }
            });



            String address=null;
            if(lang.equals("english"))
          address = district.getNamePrimaryEn() + " - " + district.getNameSecondaryEn() + " - " + district.getNameTertiaryEn();
            else
                address = district.getNamePrimaryCh() + " - " + district.getNameSecondaryCh() + " - " + district.getNameTertiaryCh();

            districtName.setText(address);
            if (customer.address != null) {
                unitNumber.setText(customer.address);
            }
            if(lang.equals("english"))
            deliveryDate.setText(deliveryDat+" "+globalProvider.getDeliveryTiming().get(deliveryWeek)+" "+duration);
            else {
                String week =globalProvider.getDeliveryTiming().get(deliveryWeek);
                week=globalProvider.deliveryTimingChinese.get(week);
                deliveryDate.setText(deliveryDat+" "+week+" "+duration);

            }
            int n=globalProvider.cartList.size();
            Product cartprds[] = new Product[n];
            for(int i=0;i<n;i++)
            {
                cartprds[i]=globalProvider.cartList.get(i);
            }
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // remarkText.setCursorVisible(false);

                    dismiss();

                }
            });
           // checkPendingOrder();
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirm.setClickable(false);
                    try {
                    Date deliveryDate = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
                            .parse(deliveryDat);

      //  If any of the previous order's date is equal to selected date,than order will be added to the prevOrder's id
                    for(PrevOrder order:prevOrderList)
                    {
                       Log.d("prevorderdate",order.getShippingDate());
                       Log.d("deliveryDar",deliveryDate.toString());

                            Date shippingDate = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                                    .parse(order.getShippingDate());
                        Log.d("prevSHIPrdate",shippingDate.toString());

                            if (shippingDate.compareTo(deliveryDate) == 0) {
                                prevOrder=order.getOrderID();
                                Log.d("it is ","equal");
                                break;
                            }

                          /*  if(order.getShippingDate().equals(deliveryDat))
                            {
                                prevOrder=order.getOrderID();
                                break;
                            }
                            */
                        }


                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }



                    if(prevOrder==null) {

                        if (totalamt < 9.9) {
                            Toast.makeText(getContext(),getContext().getResources().getString(R.string.min_spend), Toast.LENGTH_LONG).show();
                            dismiss();
                        } else {


                            JSONObject jsonObject = new JSONObject();
                            JSONObject jsonObject1 = new JSONObject();
                            JSONObject jsonObject2 = new JSONObject();
                            try {
                                jsonObject.put("userName", customer.getUserName());
                                jsonObject.put("phone", customer.phone);
                                jsonObject.put("email",customer.getEmail());
                                jsonObject1.put("inactive", jsonObject);
                                jsonObject1.put("active", customer.customer_id);
                                jsonObject2.put("userInfo", jsonObject1);
                                jsonObject2.put("shippingDate", deliveryDat);
                                jsonObject2.put("remark", remarkText.getText().toString());
                                jsonObject2.put("totalPrice", totalamt + "");
                                jsonObject2.put("district", district.getId());

                                //todo use radiobutton value
                                if (yesRadioButton.isChecked())
                                    jsonObject2.put("isPrint", true);
                                else
                                    jsonObject2.put("isPrint", false);
                                if (cashRadioButton.isChecked())
                                    jsonObject2.put("paymentMethod", "cash");
                                else
                                    jsonObject2.put("paymentMethod", "PayNow");

                                final JSONArray jsonArray = new JSONArray();
                                for (Product product : globalProvider.cartList) {

                                    String productId = product.getId();
                                    Double price = product.getPrice();
                                    String specificationch = product.getSpecificationCh();
                                    String specificationen = product.getSpecificationEn();

                                    String uniten = product.getUnitEn();
                                    String unitch = product.getUnitCh();
                                    String imageCover = product.getImageCover();
                                    // String category = product.getCategory().getId();
                                    String productName = product.getNameEn();
                                    String productCh = product.getNameCh();
                                    int total = product.getTotalNumber();
                                    JSONObject jsonObject3 = new JSONObject();
                                    if(product.isAttention()==null)
                                    {
                                        jsonObject3.put("isAttention",false);

                                    }
                                    else
                                        jsonObject3.put("isAttention",product.isAttention());


                                    jsonObject3.put("productID", productId);
                                    jsonObject3.put("name_ch", productCh);
                                    jsonObject3.put("name_en", productName);
                                    jsonObject3.put("price", price + "");
                                    jsonObject3.put("unit_ch", unitch);
                                    jsonObject3.put("unit_en", uniten);
                                    jsonObject3.put("SKU", product.getsKU());
                                    jsonObject3.put("supplier", product.getSupplier());
                                    String category = globalProvider.categoryNameMap.get(productId);
                                    jsonObject3.put("category", category);
                                    jsonObject3.put("imageCover", imageCover);
                                    jsonObject3.put("specification_ch", specificationch);
                                    jsonObject3.put("specification_en", specificationen);
                                    JSONObject jsonObject4 = new JSONObject();
                                    jsonObject4.put("productInfo", jsonObject3);
                                    jsonObject4.put("quantity", total + "");
                                    jsonArray.put(jsonObject4);
                                }
                                jsonObject2.put("productList", jsonArray);
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.createOrderUrl, jsonObject2, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("checkorderResponse", response.toString());

                                        int status = 0;
                                        try {
                                            status = response.getInt("status");

                                            if (status == 0) {
                                                String orderCode=response.getJSONObject("payload").getString("orderCode");
                                                Log.d("orderCode",orderCode);


                                                JSONObject object=new JSONObject();
                                                object.put("productList", jsonArray);
                                                object.put("orderCode",orderCode);
                                                object.put("email",customer.getEmail());
                                                object.put("userName",customer.getUserName());
                                                object.put("totalPrice",totalamt + "");
                                                object.put("date",deliveryDat);
                                               String week= GlobalProvider.deliveryTiming.get(deliveryWeek);
                                                Log.d("deliveryweek",week);
                                                object.put("week_en",week);
                                                object.put("week",globalProvider.deliveryTimingChinese.get(week));
                                                object.put("duration",duration);
                                                JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.POST, Constants.emailOrderUrl, object, new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        Log.d("emailresponse",response.toString());
                                                      //  Toast.makeText(getContext(), "Order Successfully placed", Toast.LENGTH_LONG).show();


                                                        Intent i = new Intent()
                                                                .putExtra("status", "ok"
                                                                );
                                                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);

                                                        dismiss();

                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {

                                                    }
                                                });
                                                globalProvider.addRequest(objectRequest);







                                            } else if (status == 1) {
                                                // Log.d("checknoitemResponse", response.toString());
                                                JsonFactory jsonFactory = new JsonFactory();
                                                ObjectMapper objectMapper = new ObjectMapper();
                                                try {
                                                    JsonParser jsonParser = jsonFactory.createParser(response.toString());
                                                    ResultProductList resultProductList = (ResultProductList) objectMapper.readValue(jsonParser, ResultProductList.class);
                                                    List<ProductStock> productStockList=new ArrayList<>();
                                                    for(ProductInfo product:resultProductList.getPayload()) {
                                                        if (lang.equals("english"))

                                                            productStockList.add(new ProductStock(product.getNameEn(), product.getStock()));
                                                        else
                                                            productStockList.add(new ProductStock(product.getNameCh(), product.getStock()));
                                                    }
                                                    displayStockList(productStockList);

                                                      /*  final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                                        LayoutInflater inflater = getLayoutInflater();
                                                        View convertView = (View) inflater.inflate(R.layout.custom_listalert, null, false);

                                                       ListView lv = (ListView) convertView.findViewById(R.id.lvw);

                                                        final CustomAlertAdapter adapter = new CustomAlertAdapter(productStockList, getContext());
                                                        alertDialog.setTitle("Sorry,Following items are not available");



                                                        alertDialog.setView(convertView);

                                                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dismiss();
                                                            }
                                                        });
                                                        alertDialog.setAdapter(adapter,null);





                                                        alertDialog.show();
                                                        */





                                                } catch (JsonParseException e) {
                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                Toast.makeText(getContext(), getContext().getResources().getString(R.string.some_item_na), Toast.LENGTH_LONG).show();
                                                dismiss();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getContext(),getContext().getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                                        }


                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                      //  Log.d("checkerror", error.toString());

                                    }
                                });
                                globalProvider.addRequest(jsonObjectRequest);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else
                    {
                        dismiss();
                        new AlertDialog.Builder(getContext()).setTitle(getContext().getResources().getString(R.string.alert)).setMessage(getContext().getResources().getString(R.string.add_existing_order)).setNegativeButton(getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                dismiss();

                            }
                        }).setPositiveButton(getContext().getResources().getString(R.string.confm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String modifyOrderUrl=Constants.modifyOrderUrl+prevOrder;
                               // Log.d("checkmofifyurl",modifyOrderUrl);
                                JSONObject jsonObject = new JSONObject();
                                JsonObjectRequest jsonObjectRequest=null;
                                try {
                                    jsonObject.put("extraPrice",totalamt);
                                    final JSONArray jsonArray = new JSONArray();
                                    for (Product product : globalProvider.cartList) {
                                        String productId = product.getId();
                                        Double price = product.getPrice();
                                        String specificationch = product.getSpecificationCh();
                                        String specificationen = product.getSpecificationEn();
                                        String uniten = product.getUnitEn();
                                        String unitch = product.getUnitCh();
                                        String imageCover = product.getImageCover();
                                       // String category = product.getCategory().getId();
                                        String productName = product.getNameEn();
                                        String productCh = product.getNameCh();
                                        int total = product.getTotalNumber();
                                        JSONObject jsonObject3 = new JSONObject();
                                        jsonObject3.put("productID", productId);
                                        jsonObject3.put("name_ch", productCh);
                                        jsonObject3.put("name_en", productName);
                                        jsonObject3.put("price", price );
                                        jsonObject3.put("unit_ch", unitch);
                                        jsonObject3.put("unit_en", uniten);
                                        String category=globalProvider.categoryNameMap.get(productId);
                                        jsonObject3.put("category", category);
                                        jsonObject3.put("category", category);
                                        jsonObject3.put("imageCover", imageCover);
                                        jsonObject3.put("specification_ch", specificationch);
                                        jsonObject3.put("specification_en", specificationen);
                                        JSONObject jsonObject4 = new JSONObject();
                                        jsonObject4.put("productInfo", jsonObject3);
                                        jsonObject4.put("quantity", total);
                                        jsonArray.put(jsonObject4);
                                    }
                                    jsonObject.put("extraProducts",jsonArray);
                                    jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, modifyOrderUrl, jsonObject, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                          //  Log.d("modifyresponse",response.toString());
                                            //todo check status
                                            int status=0;

                                            try {


                                                status = response.getInt("status");
                                                if(response.getInt("status")==0) {

                                                    status = response.getInt("status");
                                                    String orderCode=response.getJSONObject("payload").getString("orderCode");
                                                    Log.d("orderCode",orderCode);

                                                    JSONObject object=new JSONObject();
                                                    object.put("productList", jsonArray);
                                                    object.put("orderCode",orderCode);
                                                    object.put("email",customer.getEmail());
                                                    object.put("userName",customer.getUserName());
                                                    object.put("totalPrice",totalamt + "");
                                                    object.put("date",deliveryDat);
                                                    String week= GlobalProvider.deliveryTiming.get(deliveryWeek);
                                                    Log.d("deliveryweek",week);
                                                    object.put("week_en",week);
                                                    object.put("week",globalProvider.deliveryTimingChinese.get(week));
                                                    object.put("duration",duration);



                                                    JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.POST, Constants.emailOrderUrl, object, new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            Log.d("emailresponse",response.toString());



                                                            Intent i = new Intent()
                                                                    .putExtra("status", "ok"
                                                                    );
                                                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);

                                                            dismiss();

                                                        }
                                                    }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {

                                                        }
                                                    });
                                                    globalProvider.addRequest(objectRequest);

                                                }
                                                else if(status==1) {


                                                   Log.d("checknoitemResponse", response.toString());

                                                    Toast.makeText(getContext(), "Some Items are not available", Toast.LENGTH_LONG).show();

                                                    ObjectMapper objectMapper=new ObjectMapper();
                                                    JsonFactory jsonFactory = new JsonFactory();
                                                    try
                                                    {
                                                        JsonParser jsonParser = jsonFactory.createParser(response.toString());
                                                        ResultProductList resultProductList = (ResultProductList) objectMapper.readValue(jsonParser, ResultProductList.class);
                                                        List<ProductStock> productStockList=new ArrayList<>();
                                                        for(ProductInfo product:resultProductList.getPayload()) {
                                                            if (lang.equals("english"))

                                                                productStockList.add(new ProductStock(product.getNameEn(), product.getStock()));
                                                            else
                                                                productStockList.add(new ProductStock(product.getNameCh(), product.getStock()));
                                                        }
                                                        displayStockList(productStockList);
                                                    } catch (JsonParseException e) {
                                                        e.printStackTrace();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }

                                                }


                                                else
                                                    dismiss();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                           // Log.d("modifyerror",error.toString());
                                            Toast.makeText(getContext(),getContext().getResources().getString(R.string.something_wrong),Toast.LENGTH_LONG).show();


                                        }
                                    });



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                globalProvider.addRequest(jsonObjectRequest);


                            }
                        }).create().show();
                    }




                }
            });
            //todo set view
            changeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("delivery");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                  //  DialogFragment dialogFragment = new FragmentCart.MyDialogFragment();
                    DialogFragment dialogFragment = new MyDialogFragment.ChangeDateFragment();
                    Bundle args=new Bundle();
                    args.putParcelableArrayList("deliveryList", (ArrayList<? extends Parcelable>) deliveryList);

                    //argsargs.putString("prevOrder",prevOrder);
                    dialogFragment.setArguments(args);
                    dialogFragment.setTargetFragment(MyDialogFragment.this, DELIVERY_CONSTANT);

                    dialogFragment.show(ft, "delivery");
                }
            });


            return view;
        }

        private void displayStockList(final List<ProductStock> productStockList) {




            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.custom_listalert, null, false);

            ListView lv = (ListView) convertView.findViewById(R.id.lvw);

            final CustomAlertAdapter adapter = new CustomAlertAdapter(productStockList, getContext());
            alertDialog.setTitle(getContext().getResources().getString(R.string.sorry_insufficient));



            alertDialog.setView(convertView);

            alertDialog.setPositiveButton(getContext().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent intent= new Intent()
                            .putExtra("status", "not available"
                            );
                    intent.putExtra("notavailList", (Serializable) productStockList);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    dialogInterface.dismiss();

                    dismiss();
                }
            });
            alertDialog.setAdapter(adapter,null);





            alertDialog.show();


        }


      /*  private void checkPendingOrder() {
            if (globalProvider.isLogin()) {

              String url= Constants.checkOrderUrl+globalProvider.getCustomer().customer_id;
                Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       ObjectMapper objectMapper=new ObjectMapper();
                       JsonFactory jsonFactory=new JsonFactory();
                       try
                       {
                           JsonParser jsonParser =   jsonFactory.createParser(response);
                           OrderResult resultClass = (OrderResult ) objectMapper.readValue(jsonParser, OrderResult .class);
                           if(resultClass.getStatus()==1)
                           {
                               prevOrder=null;
                           }
                           else
                           {
                               Log.d("prevorder is something","here");
                               prevOrder=resultClass.getPayload();
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

                    }
                });
                globalProvider.addRequest(utf8JsonRequest);

            }
        }
        */


        public static Date addDays(Date date, int days) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            cal.add(Calendar.DATE, days);

            return cal.getTime();


        }
        public static class ChangeDateFragment extends DialogFragment implements SelectDateAdapter.MyClickListener
        {

            RecyclerView recyclerView;
            List<Delivery> deliveryList;
            SelectDateAdapter selectDateAdapter;
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                Dialog dialog = super.onCreateDialog(savedInstanceState);

                // request a window without the title
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                return dialog;
            }



            public void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                deliveryList=new ArrayList<>();
                Bundle mArgs = getArguments();
               deliveryList = mArgs.getParcelableArrayList("deliveryList");
            }
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {

                View view= inflater.inflate(R.layout.nextdelivery_dialogfragment, container);
                recyclerView=(view).findViewById(R.id.selectdate_recycler);
                selectDateAdapter=new SelectDateAdapter(getContext(),deliveryList,this);
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                recyclerView.setAdapter(selectDateAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);


                return view;
            }


            @Override
            public void onClick(int position) {
               Delivery delivery= deliveryList.get(position);
                Intent i = new Intent();
                       i .putExtra("status", "ok"
                        );
                       i.putExtra("selectedDate",delivery);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);

                dismiss();


            }
        }




    }

}
