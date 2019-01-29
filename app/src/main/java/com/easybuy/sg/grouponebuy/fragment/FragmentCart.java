package com.easybuy.sg.grouponebuy.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.easybuy.sg.grouponebuy.activities.DistrictSettingActivity;
import com.easybuy.sg.grouponebuy.activities.MainActivity;
import com.easybuy.sg.grouponebuy.activities.PaymentActivity;
import com.easybuy.sg.grouponebuy.adapter.CartAdapter;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.helpers.Utf8JsonRequest;
import com.easybuy.sg.grouponebuy.model.CartProduct;
import com.easybuy.sg.grouponebuy.model.Category;
import com.easybuy.sg.grouponebuy.model.CategoryPrimaryList;
import com.easybuy.sg.grouponebuy.model.OrderResult;
import com.easybuy.sg.grouponebuy.model.PrevOrder;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


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
        if(Constants.getCustomer(getContext())!=null&&Constants.getCustomer(getContext()).getDistrict()!=null)
        if(totalamt>Constants.getCustomer(getContext()).getDistrict().getDeliveryCost())
        {
            minSpendTextView.setVisibility(View.GONE);
        }
        else
            minSpendTextView.setText(getString(R.string.min_spend)+" $ "+Constants.getCustomer(getContext()).getDistrict().getDeliveryCost());
        if(globalProvider.cartList.isEmpty())
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
    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DIALOG_FRAGMENT:
                if (resultCode == Activity.RESULT_OK&&data.getStringExtra("status").equals("ok")) {
                    Toast.makeText(getContext(), getString(R.string.order_success), Toast.LENGTH_SHORT).show();
                   cartProductList.clear();

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
        */





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

            //    if (globalProvider.getCustomer().getDistrict() == null) {
                if (Constants.getCustomer(getContext()).getDistrict() == null) {
                    Intent intent = new Intent(getContext(), DistrictSettingActivity.class);
                    startActivity(intent);

                } else {
                    float minOrderValue=Constants.getCustomer(getContext()).getDistrict().getDeliveryCost();
                    Log.d("deliveryCost",minOrderValue+"");


                    if (totalamt > minOrderValue|| prevOrder != null) {

                       Intent intent=new Intent(getContext(), PaymentActivity.class);


                        intent.putExtra("prevOrderList", (Serializable) prevOrderList);
                        intent.putExtra("totalamt",totalamt);
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
            }
        });



        return view;
    }

    private void getCartProducts() {


            cartProductList.clear();
            calculateTotal();




            for(Product product:globalProvider.cartList)
            {
                cartProductList.add(new CartProduct(product,true));
            }




            cartAdapter.notifyDataSetChanged();


        }




    private void checkPendingOrder() {
        if (globalProvider.isLogin()) {

           // String url= Constants.checkOrderUrl+globalProvider.getCustomer().customer_id;
            String url= Constants.checkOrderUrl+globalProvider.getCustomerId();
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
        if(Constants.getCustomer(getContext())!=null&&Constants.getCustomer(getContext()).getDistrict()!=null)
        {
            float minamt=Constants.getCustomer(getContext()).getDistrict().getDeliveryCost();
            if(totalamt<minamt)
            {
                minSpendTextView.setText(getString(R.string.min_spend)+" $ "+minamt);
                minSpendTextView.setVisibility(View.VISIBLE);

            }
            else
                minSpendTextView.setVisibility(View.GONE);
        }
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













}
