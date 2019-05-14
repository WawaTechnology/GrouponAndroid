package com.easybuy.sg.grouponebuy.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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
import com.easybuy.sg.grouponebuy.activities.DistrictSettingActivity;
import com.easybuy.sg.grouponebuy.activities.MainActivity;
import com.easybuy.sg.grouponebuy.activities.PaymentActivity;
import com.easybuy.sg.grouponebuy.adapter.CartAdapter;
import com.easybuy.sg.grouponebuy.adapter.CustomAlertAdapter;
import com.easybuy.sg.grouponebuy.adapter.MergeDateAdapter;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.helpers.Utf8JsonRequest;
import com.easybuy.sg.grouponebuy.model.CartProduct;
import com.easybuy.sg.grouponebuy.model.Category;
import com.easybuy.sg.grouponebuy.model.CategoryPrimaryList;
import com.easybuy.sg.grouponebuy.model.Order;
import com.easybuy.sg.grouponebuy.model.OrderPayload;
import com.easybuy.sg.grouponebuy.model.OrderResult;
import com.easybuy.sg.grouponebuy.model.PrevOrder;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.model.ProductOrderList;
import com.easybuy.sg.grouponebuy.model.ProductStock;
import com.easybuy.sg.grouponebuy.model.Result;
import com.easybuy.sg.grouponebuy.model.SingleOrderResult;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.easybuy.sg.grouponebuy.utils.DateChangeListener;
import com.easybuy.sg.grouponebuy.utils.PrevOrderSelectedListener;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInput;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


//check total 9.9 if any of the previous order's date is not equal to current's order date

public class FragmentCart extends Fragment implements CartAdapter.quantityChangedListener {
    private static final int DIALOG_FRAGMENT =123 ;
    public List<CartProduct> cartProductList;
    CartAdapter cartAdapter;
    RecyclerView recyclerView;
    List<ProductStock> limitPurchaseProductList=new ArrayList<>();
    GlobalProvider globalProvider;
    String language;

    RelativeLayout cartLayout;
    SeekBar seekBar;
    int progressStatusCounter = 0;
    float freeDeliveryamt;
   final String ebuyMart="EBuyMart";
    private  String colorCodeStart = "<font color='#C53635'>";


    List<PrevOrder> prevOrderList;

    Button payButton;
    final int Cart_Code=112;
    LinearLayout noCartLayout;
    LinearLayout checkLayout;
   // Button editButton;
    //LinearLayout saveBottomLayout;
    LinearLayout editBottomLayout;
   PrevOrder prevOrder;
    TextView minSpendTextView;
    boolean addToexistingOrder;
   // public static boolean isSaveClicked;





    TextView amountText;
    CheckBox allCheckBox;


    static double totalamt = 0.0;
    private float minDelivery;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartProductList = new ArrayList<>();
        prevOrderList=new ArrayList<>();
        language=Constants.getLanguage(getContext());
        cartAdapter = new CartAdapter(cartProductList, getContext(), this);
        globalProvider = GlobalProvider.getGlobalProviderInstance(getContext().getApplicationContext());
        if(globalProvider.isLogin())
        {
            if(Constants.getCustomer(getContext()).getDistrict()!=null)
            {
                freeDeliveryamt=Constants.getCustomer(getContext()).getDistrict().getFreeDeliveryPrice();
                minDelivery=Constants.getCustomer(getContext()).getDistrict().getDeliveryCost();
            }
        }

       if(globalProvider.cartList.size()>0) {
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
       /* if(Constants.getCustomer(getContext())!=null&&Constants.getCustomer(getContext()).getDistrict()!=null)
        if(totalamt>Constants.getCustomer(getContext()).getDistrict().getDeliveryCost())
        {
            minSpendTextView.setVisibility(View.GONE);
        }
        else
            minSpendTextView.setText(getString(R.string.min_spend)+" $ "+Constants.getCustomer(getContext()).getDistrict().getDeliveryCost());
            */

        if(globalProvider.cartList.isEmpty()||!globalProvider.isLogin())
        {
            noCartLayout.setVisibility(View.VISIBLE);
            cartLayout.setVisibility(View.GONE);
            //  checkLayout.setVisibility(View.GONE);

            //recyclerView.setVisibility(View.GONE);

            ((MainActivity)getContext()).HideCartNum();
        }
        else
            getCartProducts();



    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DIALOG_FRAGMENT:
                if (resultCode == Activity.RESULT_OK ) {
                   if(data.getSerializableExtra("previousOrder")!=null)
                    {
                        prevOrder= (PrevOrder) data.getSerializableExtra("previousOrder");
                        for(CartProduct product:cartProductList)
                        {
                            if(product.getProduct().limitPurchase>0)
                            {
                               // Log.d("limitfunccalled",product.getProduct().getNameEn());
                                checkLimitPreviousOrder(prevOrder);
                                return;
                            }
                        }





                    }
                    else
                   {

                           prevOrder=null;

                   }

                    startPaymentActivity();

                }

        }
    }

   private void checkLimitPreviousOrder(PrevOrder prevOrder) {
        String url=Constants.editOrderUrl+prevOrder.getOrderID();
      //  Log.d("hurl",url);

        Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest( Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonFactory jsonFactory = new JsonFactory();
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject res=jsonObject.getJSONObject("payload");
                  JSONArray jsonArray=  res.getJSONArray("productList");
                  limitPurchaseProductList.clear();

                  //  Log.d("checkstatus",jsonArray.length()+"");


                    for(int i=0;i<jsonArray.length();i++)
                    {
                        int limitOrder=jsonArray.getJSONObject(i).getJSONObject("productInfo").getInt("limitPurchase");
                        if(limitOrder>0) {
                            int quantity = jsonArray.getJSONObject(i).getInt("quantity");
                            String productId=jsonArray.getJSONObject(i).getJSONObject("productInfo").getString("productID");
                           // Log.d("checkqq",quantity+"");

                            for (Iterator<CartProduct> it = cartProductList.iterator(); it.hasNext(); ) {
                                CartProduct cartProduct = it.next();
                                if (productId.equals(cartProduct.getProduct().getId()))
                                {
                                    int limitP=0;
                                  //  Log.d("checkpdtotal",cartProduct.getProduct().getTotalNumber()+"");
                                if (cartProduct.getProduct().getTotalNumber()+quantity>cartProduct.getProduct().limitPurchase) {
                                    if(quantity==cartProduct.getProduct().limitPurchase)

                                    {
                                         limitP=0;
                                    }
                                    else
                                    {
                                       int val= (cartProduct.getProduct().getTotalNumber()+quantity)-cartProduct.getProduct().limitPurchase;
                                        limitP=cartProduct.getProduct().getTotalNumber()-val;
                                    }
                                  //  Log.d("limitP",limitP+"");

                                    if(language.equals("english"))
                                    limitPurchaseProductList.add(new ProductStock(cartProduct.getProduct().getNameEn(),limitP));
                                    else
                                        limitPurchaseProductList.add(new ProductStock(cartProduct.getProduct().getNameCh(),limitP));

                                //  String msg = getContext().getString(R.string.limit_sale_msg,cartProduct.getProduct().limitPurchase);
                                   // Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                                    if(limitP==0) {

                                        globalProvider.cartList.remove(cartProduct.getProduct());
                                       // Log.d("pdname", cartProduct.getProduct().getNameEn());

                                        it.remove();
                                    }
                                    else
                                    {
                                        cartProduct.getProduct().setTotalNumber(limitP);
                                        for(int j=0;j<globalProvider.cartList.size();j++)
                                        {


                                            if(globalProvider.cartList.get(j).getId().equals(cartProduct.getProduct().getId()))
                                            {
                                                globalProvider.cartList.get(j).setTotalNumber(limitP);
                                                break;
                                            }
                                        }

                                    }
                                    cartAdapter.notifyDataSetChanged();


                                }



                                }
                            }


                        }

                    }
                    if(limitPurchaseProductList.size()>0)
                    {
                        showLimitAlert();
                    }
                    else
                        startPaymentActivity();


                    //JsonParser jsonParser = jsonFactory.createParser( res.toString());
                   // Order order =  objectMapper.readValue(jsonParser, Order.class);

                } catch (JSONException e) {
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

    private void showLimitAlert() {
       // Log.d("showlimitalert","here");







            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.custom_listalert, null, false);


            // ListView lv = (ListView) convertView.findViewById(R.id.lvw);

            final CustomAlertAdapter adapter = new CustomAlertAdapter(limitPurchaseProductList, getContext());
            alertDialog.setTitle(getContext().getString(R.string.limit_sale_alert));
            alertDialog.setCancelable(false);




            alertDialog.setView(convertView);

            alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(globalProvider.cartList.size()>0)
                    startPaymentActivity();
                    else {



                        dialogInterface.dismiss();





                    }


                }
            });
            alertDialog.setAdapter(adapter,null);





            alertDialog.show();
            if(globalProvider.cartList.isEmpty())
            {
               // Log.d("showlimitalert","there");
                noCartLayout.setVisibility(View.VISIBLE);
                cartLayout.setVisibility(View.GONE);
                ((MainActivity)getContext()).HideCartNum();


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
                seekBar.removeCallbacks(null);
                super.onStop();
            }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // Log.d("fragmentoncreate","oncreateview");
        View view = inflater.inflate(R.layout.fragment_cart, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.cart_recycler);
        cartLayout=(RelativeLayout) view.findViewById(R.id.cart_layout);
        seekBar=(SeekBar) view.findViewById(R.id.horizontal_progress_bar);

        checkLayout=(LinearLayout) view.findViewById(R.id.check_layout);
      //  editButton=(Button)view.findViewById(R.id.edit_button);
        editBottomLayout=(LinearLayout) view.findViewById(R.id.edit_bottom);
       // saveBottomLayout =(LinearLayout)view.findViewById(R.id.save_bottom);
        minSpendTextView=(TextView) view.findViewById(R.id.min_spendmsg);

        amountText = (TextView) view.findViewById(R.id.amount);
        allCheckBox = (CheckBox) view.findViewById(R.id.all_check);
        payButton = (Button) view.findViewById(R.id.pay_button);


        noCartLayout=(LinearLayout) view.findViewById(R.id.cart_none);
        seekBar.setEnabled(false);
        checkPendingOrder();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cartAdapter);

        if(globalProvider.isLogin()&&globalProvider.cartList.size()>0) {

         // getCartProducts();
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
    if(freeDeliveryamt>0)
    {
       // Log.d("checkvalue",(int)Constants.getCustomer(getContext()).getDistrict().getFreeDeliveryPrice()+"");
        seekBar.setMax((int)freeDeliveryamt);

    }
    else
    {
        if(minDelivery>0)
        {
            seekBar.setMax((int)minDelivery);
        }

        else
        checkLayout.setVisibility(View.GONE);
    }

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
           // Log.d("checkpreco",prevOrder);
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

            //    if (globalProvider.getCustomer().getDistrict() == null) {
                if (Constants.getCustomer(getContext()).getDistrict() == null) {
                    Intent intent = new Intent(getContext(), DistrictSettingActivity.class);
                    startActivity(intent);

                } else {


                    if(prevOrderList!=null&&prevOrderList.size()>0)
                    {
                        FragmentManager fragmentManager = getFragmentManager();
                        DialogFragment dialogFragment = new FragmentCart.MergeOrderDialogFragment();
                        Bundle args = new Bundle();
                        args.putSerializable("prevOrderList", (Serializable) prevOrderList);
                        dialogFragment.setArguments(args);
                        dialogFragment.setTargetFragment(FragmentCart.this, DIALOG_FRAGMENT);
                        dialogFragment.show(fragmentManager, "merge");
                    }
                    else
                    {
                        startPaymentActivity();
                    }






                }
            }
        });



        return view;
    }

    public void startPaymentActivity()
    {
        float minOrderValue=Constants.getCustomer(getContext()).getDistrict().getDeliveryCost();

        if ((totalamt > minOrderValue|| prevOrder != null)&&globalProvider.cartList.size()>0) {



            Intent intent=new Intent(getContext(), PaymentActivity.class);


            intent.putExtra("prevOrderList", (Serializable) prevOrderList);
            intent.putExtra("previousOrder",prevOrder);
           // Log.d("cartlistSize",globalProvider.cartList.size()+"");
           // intent.putExtra("totalamt",totalamt);

            startActivity(intent);


                        /* FragmentTransaction ft = getFragmentManager().beginTransaction();
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
                        */
        } else {
            Toast.makeText(getContext(), getString(R.string.min_spend)+ " $ "+minOrderValue, Toast.LENGTH_LONG).show();
        }
    }

    private void getCartProducts() {
     //   Log.d("getcartprod","here");


            cartProductList.clear();
            calculateTotal();




            for(Product product:globalProvider.cartList)
            {
                cartProductList.add(new CartProduct(product,true));
            }
            allCheckBox.setChecked(true);





            cartAdapter.notifyDataSetChanged();


        }




    private void checkPendingOrder() {
        if (globalProvider.isLogin()) {

           // String url= Constants.checkOrderUrl+globalProvider.getCustomer().customer_id;
            String url= Constants.checkOrderUrl+globalProvider.getCustomerId();
          //  Log.d("checkprevorderurl",url);
            Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                   // Log.d("checkprevresponse",response);
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
                           // Log.d("prevorder is something","here");


                            //String shippingDate=resultClass.getPayload().get(0).getShippingDate();
                            Date todayDate= new Date();
                            SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
                           String tempDate= sdf.format(todayDate);
                           todayDate=sdf.parse(tempDate);



                            //Log.d("currentDate",currentDate);
                            for(PrevOrder order:resultClass.getPayload())
                            {
                                Date prevDate = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                                        .parse(order.getShippingDate());
                               // Log.d("odd",order.getShippingDate());
                                if(prevDate.compareTo(todayDate)>0)

                                prevOrderList.add(order);
                            }
                            if(prevOrderList.size()>0) {
                                prevOrder = prevOrderList.get(0);


                                minSpendTextView.setVisibility(View.GONE);
                                checkLayout.setVisibility(View.GONE);
                            }

                        }

                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
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
        String totalPrice="$"+totalamt;

        String[] each = totalPrice.split("\\.");

        each[0]=each[0]+".";
        //adding spannable so that textsize of amount before. is more

        Spannable spannable = new SpannableString(totalPrice);

        spannable.setSpan(new AbsoluteSizeSpan(18, true), 0, each[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        amountText.setText(spannable, TextView.BufferType.SPANNABLE);
       // amountText.setText("$ "+totalamt);
        if(Constants.getCustomer(getContext())!=null&&Constants.getCustomer(getContext()).getDistrict()!=null)
        {
          //  float minamt=Constants.getCustomer(getContext()).getDistrict().getDeliveryCost();
            if(totalamt<minDelivery)
            {


                minSpendTextView.setVisibility(View.VISIBLE);
                float reqamt= (float) ((float)minDelivery-totalamt);
               String amt= String.format("%.2f",reqamt);
                amt="$ "+amt;
                String msg=" ";

               if(language.equals("english")) {

                    msg=amt+" to Delivery with "+ebuyMart;






                   //minSpendTextView.setText(" $"+amt+" to Delivery with EBuyMart");
               }
               else {
                   msg="还差 "+amt+"  可以送货上门";
                   //minSpendTextView.setText("还差 " + "$ " + amt + " 可以送货上门");
               }
                setSpannableText(msg,amt);
                progressStatusCounter=(int)totalamt;
              /*  seekBar.post(new Runnable() {
                    @Override
                    public void run() {
                        seekBar.setProgress(progressStatusCounter);

                    }
                });
                */


            }
            else if(totalamt<freeDeliveryamt)
            {
                String msg=" ";
                float amtf= (float) (freeDeliveryamt-totalamt);
                String amt= String.format("%.2f",amtf);
                amt="$ "+amt;
                if(language.equals("english")) {


                     msg=amt+"  to Free Delivery with "+ebuyMart;


                }
                else {
                    msg="还差 "+amt+"  可以免费送货上门";

                   // minSpendTextView.setText("还差 $ " + amt + " 可以免费送货上门");
                }
                setSpannableText(msg,amt);
               // minSpendTextView.setTextColor(getContext().getResources().getColor(R.color.red));
            }
            else if(totalamt>=freeDeliveryamt) {

                if(language.equals("english"))
                minSpendTextView.setText("Free Delivery with EBuyMart!");
                else
                    minSpendTextView.setText("EBuymart 免费送货上门");
                minSpendTextView.setTextColor(getContext().getResources().getColor(R.color.green));
            }
            seekBar.post(new Runnable() {
                @Override
                public void run() {
                    progressStatusCounter=(int)totalamt;
                    seekBar.setProgress(progressStatusCounter);


                }
            });
        }
        else
        {
            minSpendTextView.setVisibility(View.GONE);
        }


    }

    private void setSpannableText(String msg,String amt) {
        Spannable spannable1=new SpannableString(msg) ;
        minSpendTextView.setTextColor(Color.BLACK);
       int vv= msg.indexOf("$");
      // Log.d("chekk",vv+"");

        spannable1.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.red)),vv, vv+amt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       // Log.d("indexof",msg.indexOf(ebuyMart)+"");
       // Log.d("lindexof",msg.indexOf(ebuyMart)+ebuyMart.length()+"");
        if(language.equals("english"))

        spannable1.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.red)),msg.indexOf(ebuyMart),msg.indexOf(ebuyMart)+ebuyMart.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // minSpendTextView.setText(" $ " + amt + " to Free Delivery with EBuyMart");
        minSpendTextView.setText(spannable1,TextView.BufferType.SPANNABLE);
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
       // Log.d("getchecked", checked + "");

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



    public static class MergeOrderDialogFragment extends DialogFragment  {

             //   RecyclerView mergeDateRecycler;
               // MergeDateAdapter mergeDateAdapter;

        TextView order1Text,order2Text,order3Text,newOrderText;

        Context context;

                List<PrevOrder> prevOrderList;
                //PrevOrderSelectedListener listener;








        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);

            // request a window without the title
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            return dialog;
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            prevOrderList=new ArrayList<>();

            Bundle mArgs = getArguments();
            prevOrderList = (List<PrevOrder>) mArgs.getSerializable("prevOrderList");
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.merge_order_dialog, container);
            order1Text=(TextView)view.findViewById(R.id.order_1);
            order2Text=(TextView)view.findViewById(R.id.order_2);
            order3Text=(TextView)view.findViewById(R.id.order_3);
            newOrderText=(TextView) view.findViewById(R.id.no_order);
            String formatted = getString(R.string.update_order_date, prevOrderList.get(0).getShippingDate());
           // order1Text.setText("Yes, update order to deliver on "+prevOrderList.get(0).getShippingDate());
            order1Text.setText(formatted);
            if(prevOrderList.size()>=3)
            {
                order2Text.setText(getString(R.string.update_order_date, prevOrderList.get(1).getShippingDate()));
                order3Text.setText(getString(R.string.update_order_date, prevOrderList.get(2).getShippingDate()));
                order2Text.setVisibility(View.VISIBLE);
                order3Text.setVisibility(View.VISIBLE);
                newOrderText.setVisibility(View.GONE);

            }
            else if (prevOrderList.size()==2)
            {
                order2Text.setText(getString(R.string.update_order_date, prevOrderList.get(1).getShippingDate()));
                order2Text.setVisibility(View.VISIBLE);
            }
            order1Text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent()
                            .putExtra("previousOrder", prevOrderList.get(0));

                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                    dismiss();

                }
            });
            order2Text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent()
                            .putExtra("previousOrder", prevOrderList.get(1));

                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                    dismiss();
                }
            });
            order3Text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent()
                            .putExtra("previousOrder", prevOrderList.get(2));

                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                    dismiss();
                }
            });
            newOrderText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent()
                            .putExtra("newOrder", "neworder");

                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                    dismiss();
                }
            });



           /* mergeDateRecycler=(view).findViewById(R.id.select_daterecycler);
            mergeDateAdapter=new MergeDateAdapter(getContext(),prevOrderList);
            mergeDateRecycler.setAdapter(mergeDateAdapter);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            mergeDateRecycler.setLayoutManager(linearLayoutManager);
            */

            return view;
        }



    }













}
