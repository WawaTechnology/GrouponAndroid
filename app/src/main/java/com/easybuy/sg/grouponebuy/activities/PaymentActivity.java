package com.easybuy.sg.grouponebuy.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.adapter.CustomAlertAdapter;
import com.easybuy.sg.grouponebuy.adapter.ProductImageAdapter;
import com.easybuy.sg.grouponebuy.adapter.SelectDateAdapter;
import com.easybuy.sg.grouponebuy.helpers.CustomRequest;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.model.Customer;
import com.easybuy.sg.grouponebuy.model.Cycle;
import com.easybuy.sg.grouponebuy.model.Delivery;
import com.easybuy.sg.grouponebuy.model.District;
import com.easybuy.sg.grouponebuy.model.PrevOrder;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.model.ProductInfo;
import com.easybuy.sg.grouponebuy.model.ProductStock;
import com.easybuy.sg.grouponebuy.model.ResultProductList;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.easybuy.sg.grouponebuy.utils.DateChangeListener;
import com.easybuy.sg.grouponebuy.utils.OptionalChoiceListener;
import com.easybuy.sg.grouponebuy.utils.PaymentChoiceListener;
import com.easybuy.sg.grouponebuy.utils.UnitChoiceListener;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class PaymentActivity extends AppCompatActivity implements DateChangeListener,PaymentChoiceListener,OptionalChoiceListener,UnitChoiceListener {
    RecyclerView imageRecycler;
    GlobalProvider globalProvider;
    RelativeLayout paymentLayout;
    ImageView backButton;
    TextView addressText;
    TextView optionalChoiceText;

    TextView totalText;
    TextView numItemsText;
    LinearLayout freeDeliveryLayout;
    TextView freeDeliveryCostMsg;
    double freeDeliveryPrice;
    int res;
    int optionDefaultChoice=-1;
   // String previousOrderDate;

    TextView deliveryDateText;
    RelativeLayout prodLayout;
    RelativeLayout optionLayout;
   // TextView invoiceChoiceText;
    TextView paymentMethodChoice;
    TextView subTotalText;
    TextView netBalanceText;
    TextView deliveryCostText;
  //  LinearLayout ecoinLayout;
    TextView totalAmountText;
    EditText remarkText;
    double subTotal;
    double refundCost;
    Button submitButton;
    List<PrevOrder> prevOrderList;
   // RelativeLayout invoiceLayout;
    String deliveryWeek;
    String duration;
    PrevOrder prevOrder;
    int todayWEEK;
    int nextDeliveries[];
    String nextDeliveryDates[];
    String nextDeliveryTimimgs[];
    String deliveryDat;
    List<Delivery> deliveryList;
    float deliveryPrice=0;
    District district;
    String lang;
    double totalamt;
    Customer customer;
    public static final int DELIVERY_CONSTANT=212;
    ProductImageAdapter productImageAdapter;
    Date now;
    Delivery selectedDate;
    SimpleDateFormat simpleDateFormat1;
    List<Cycle> cycleList;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_layout);

        backButton = (ImageView) findViewById(R.id.back);
        freeDeliveryLayout=(LinearLayout) findViewById(R.id.freedeliverymsg_layout);
        freeDeliveryCostMsg=(TextView) findViewById(R.id.free_deliverycost_msg);
        prodLayout = (RelativeLayout) findViewById(R.id.pd_layout);
        optionLayout=(RelativeLayout) findViewById(R.id.optional_layout);
        optionalChoiceText=(TextView)findViewById(R.id.optional_choice) ;
        totalText=(TextView) findViewById(R.id.total);
        deliveryList = new ArrayList<>();
        prevOrderList = new ArrayList<>();
        nextDeliveries = new int[2];
        nextDeliveryDates = new String[2];
        nextDeliveryTimimgs = new String[2];
        remarkText = (EditText) findViewById(R.id.payment_msg);
        paymentLayout = (RelativeLayout) findViewById(R.id.payment_layout);
        customer = Constants.getCustomer(this);
        district = customer.getDistrict();
        cycleList=district.getCycle();
   simpleDateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
        now=new Date();
        lang = Constants.getLanguage(this);
        Intent intent = getIntent();
       // totalamt = intent.getDoubleExtra("totalamt", 0.0);
        prevOrderList = (ArrayList<PrevOrder>) intent.getSerializableExtra("prevOrderList");
        prevOrder= (PrevOrder) intent.getSerializableExtra("previousOrder");
        deliveryList=intent.getParcelableArrayListExtra("deliveryDateList");
       selectedDate= intent.getParcelableExtra("selectedDate");
       duration=selectedDate.getTime();
       deliveryWeek=selectedDate.getWeek();

        //  ecoinLayout = (LinearLayout) findViewById(R.id.ecoin_layout);
        addressText = (TextView) findViewById(R.id.delivery_address);
       // invoiceLayout = (RelativeLayout) findViewById(R.id.invoice_layout);
        deliveryCostText=(TextView) findViewById(R.id.delivery_cost);
        numItemsText = (TextView) findViewById(R.id.num_item);
        deliveryDateText = (TextView) findViewById(R.id.delivery_date);
       // invoiceChoiceText = (TextView) findViewById(R.id.invoice_choice);
        paymentMethodChoice = (TextView) findViewById(R.id.payment_choice);
        subTotalText = (TextView) findViewById(R.id.subtotal);
        netBalanceText = (TextView) findViewById(R.id.net_balance);
        totalAmountText = (TextView) findViewById(R.id.amt);
        submitButton = (Button) findViewById(R.id.submit);
        globalProvider = GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        imageRecycler = (RecyclerView) findViewById(R.id.img_Recyclers);
        productImageAdapter = new ProductImageAdapter(PaymentActivity.this, globalProvider.cartList);
        imageRecycler.setAdapter(productImageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        imageRecycler.setLayoutManager(linearLayoutManager);
      //  String total=String.format("%.2f",totalamt);

        //  addressText.setText(Constants.getCustomer(this).address);
       // totalAmountText.setText("$ " + total);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final View parent = (View) backButton.getParent();  // button: the view you want to enlarge hit area
        parent.post(new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                backButton.getHitRect(rect);
                rect.top -= 10;    // increase top hit area
                rect.left -= 10;   // increase left hit area
                rect.bottom += 10; // increase bottom hit area
                rect.right += 50;  // increase right hit area
                parent.setTouchDelegate(new TouchDelegate(rect, backButton));
            }
        });

        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
         res = sp.getInt("paymentkey", 0);
       // int billres = sp.getInt("billkey", 1);
        if (res == 0) {
            paymentMethodChoice.setText(R.string.cash);
        } else
            paymentMethodChoice.setText("PayNow");
      /*  if (billres == 0) {
            invoiceChoiceText.setText(R.string.yes);
        } else
            invoiceChoiceText.setText(R.string.no);
            */
        String address = null;
        if (lang.equals("english")) {
            if (customer.address != null&&customer.address.length()>1) {
                address = customer.address + ", " + district.getNameTertiaryEn() + ", " + district.getNameSecondaryEn();
            }
            else
            {
                address=district.getNameTertiaryEn() + ", " + district.getNameSecondaryEn();
            }
        }

        else {
            if (customer.address != null&&customer.address.length()>1) {
                address = customer.address + ", " + district.getNameTertiaryCh() + ", " + district.getNameSecondaryCh();
            }
            else
                address=district.getNameTertiaryCh()+", " + district.getNameSecondaryCh();
        }
       // Log.d("address",address);
        freeDeliveryPrice=Constants.getCustomer(PaymentActivity.this).getDistrict().getFreeDeliveryPrice();


        addressText.setText(address);

            setDeliveryDate();

        optionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogFragment dialogFragment = new OptionalMethodFragment();
                dialogFragment.show(fragmentManager, "optional");

            }
        });
        paymentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogFragment dialogFragment = new PaymentMethodFragment();
                Bundle bundle=new Bundle();
                bundle.putInt("res",res);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fragmentManager, "payment");
            }
        });
     /*   invoiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogFragment dialogFragment = new ChangeInvoiceFragment();
                dialogFragment.show(fragmentManager, "invoice");
            }
        });
        */
        deliveryDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogFragment dialogFragment = new ChangeDateFragment();
                Bundle args = new Bundle();
                args.putString("deliveryDate",deliveryDat);
                args.putParcelableArrayList("deliveryList", (ArrayList<? extends Parcelable>) deliveryList);
                dialogFragment.setArguments(args);
                dialogFragment.show(fragmentManager, "delivery");
            }
        });
        prodLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this, CartListActivity.class);
                startActivity(intent);
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final android.app.AlertDialog alertDialog= new android.app.AlertDialog.Builder(PaymentActivity.this).setTitle(getString(R.string.alert))
                        .setMessage(getString(R.string.submit_order)).setCancelable(false)
                        .setPositiveButton(getString(R.string.confm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("submitclicked","true");
                                //submit.setEnabled(false);
                                submitButton.setClickable(false);
                                if(customer.address==null||customer.address.length()<1) {
                                    showUnitNumberAlert();
                                    Log.d("unitalert","true");
                                    return;
                                }







                                if (prevOrder == null) {
                                   // Log.d("weareprev","here");

                                    if (subTotal < customer.getDistrict().getDeliveryCost()) {
                                        Toast.makeText(PaymentActivity.this, getString(R.string.min_spend) + " $ " + customer.getDistrict().getDeliveryCost(), Toast.LENGTH_LONG).show();
                                        submitButton.setClickable(true);
                                        return;
                                    } else {


                                        JSONObject jsonObject = new JSONObject();
                                        JSONObject jsonObject1 = new JSONObject();
                                        JSONObject jsonObject2 = new JSONObject();
                                        try {
                                            jsonObject.put("userName", customer.getUserName());
                                            jsonObject.put("phone", customer.phone);
                                            jsonObject.put("email", customer.getEmail());
                                            jsonObject1.put("inactive", jsonObject);
                                            jsonObject1.put("active", customer.customer_id);
                                            jsonObject2.put("userInfo", jsonObject1);
                                            jsonObject2.put("shippingDate", deliveryDat);

                                           // Log.d("totalpp",subTotal+"");
                                            jsonObject2.put("freeDeliveryPrice",freeDeliveryPrice);
                                            jsonObject2.put("deliveryPrice",deliveryPrice);
                                            jsonObject2.put("totalPrice", subTotal + "");
                                            jsonObject2.put("district", district.getId());
                                            if(optionDefaultChoice!=0)
                                            {
                                                String msg="";
                                                if(!remarkText.getText().toString().isEmpty())
                                                {
                                                    msg+=remarkText.getText().toString();
                                                }
                                                if(optionDefaultChoice==1)
                                                {
                                                    msg+="If no one is at home, inform by phone call"+" 人不在家，电话通知";
                                                }
                                                else
                                                {
                                                    msg+="If no one is at home, place products on doorway without phone call "+"人不在家，直接放门口，不需要通知";
                                                }
                                                jsonObject2.put("remark", msg);


                                            }
                                            else
                                            {
                                                if(!remarkText.getText().toString().isEmpty())
                                                jsonObject2.put("remark", remarkText.getText().toString());
                                                else
                                                    jsonObject2.put("remark","");
                                            }

                                            //todo use radiobutton value
                                            //  Log.d("invoicetext", invoiceChoiceText.getText().toString());
                                            //String invoiceValue = invoiceChoiceText.getText().toString();

                                            jsonObject2.put("isPrint", true);

                                            if (paymentMethodChoice.getText().toString().equals(getString(R.string.cash))) {
                                               // Log.d("valueis", "cash");
                                                jsonObject2.put("paymentMethod", "cash");
                                            } else {
                                                jsonObject2.put("paymentMethod", "PayNow");
                                               // Log.d("valueis", "paynow");
                                            }

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
                                                if (product.isAttention() == null) {
                                                    jsonObject3.put("isAttention", false);

                                                } else
                                                    jsonObject3.put("isAttention", product.isAttention());



                                                jsonObject3.put("productID", productId);
                                                jsonObject3.put("name_ch", productCh);
                                                jsonObject3.put("name_en", productName);
                                                jsonObject3.put("price", price + "");
                                                jsonObject3.put("unit_ch", unitch);
                                                jsonObject3.put("unit_en", uniten);
                                                jsonObject3.put("SKU", product.getsKU());
                                                jsonObject3.put("supplier", product.getSupplier());
                                                jsonObject3.put("ifWeigh",product.getIfWeigh());
                                                String category = globalProvider.categoryNameMap.get(productId);
                                                jsonObject3.put("category", category);
                                                jsonObject3.put("imageCover", imageCover);
                                                jsonObject3.put("specification_ch", specificationch);
                                                jsonObject3.put("specification_en", specificationen);
                                                jsonObject3.put("limitPurchase",product.limitPurchase);
                                                JSONObject jsonObject4 = new JSONObject();
                                                jsonObject4.put("productInfo", jsonObject3);
                                                jsonObject4.put("quantity", total + "");
                                                jsonArray.put(jsonObject4);
                                            }
                                            jsonObject2.put("productList", jsonArray);
                                            if (refundCost > 0) {
                                               // Log.d("hererefund", "here");
                                                jsonObject2.put("refundCost", refundCost);
                                            }
                                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.createOrderUrl, jsonObject2, new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                  //  Log.d("checkorderResponse", response.toString());

                                                    int status = 0;
                                                    try {
                                                        status = response.getInt("status");

                                                        if (status == 0) {
                                                            Toast.makeText(PaymentActivity.this, getString(R.string.order_success), Toast.LENGTH_SHORT).show();
                                                            globalProvider.categoryNameMap.clear();
                                                            globalProvider.cartList.clear();
                                                            totalamt = 0.0;
                                                            subTotal=0.0;
                                                            if (refundCost > 0) {
                                                                double ecoin = Constants.getCustomer(PaymentActivity.this).getRefund().getECoins();
                                                                double val=ecoin - refundCost;

                                                               // Log.d("shudecoin",val+"");
                                                               Customer customer= Constants.getCustomer(PaymentActivity.this);
                                                               customer.getRefund().setECoins(val);
                                                               Constants.setCustomer(PaymentActivity.this,customer);
                                                              //  Constants.getCustomer(PaymentActivity.this).getRefund().setECoins(ecoin - refundCost);
                                                            }
                                                           // Log.d("acecoint",Constants.getCustomer(PaymentActivity.this).getRefund().getECoins()+"");
                                                            String orderCode = response.getJSONObject("payload").getString("orderCode");
                                                            JSONArray jsonArray=response.getJSONObject("payload").getJSONArray("productList");
                                                            totalamt=response.getJSONObject("payload").getDouble("totalPrice");
                                                            refundCost=response.getJSONObject("payload").getDouble("refundCost");

                                                           // Log.d("orderCode", orderCode);


                                                            JSONObject object = new JSONObject();
                                                            object.put("productList", jsonArray);
                                                            object.put("orderCode", orderCode);
                                                            object.put("email", customer.getEmail());
                                                           // object.put("freeDeliveryPrice",freeDeliveryPrice);
                                                            if (refundCost > 0) {
                                                               // Log.d("hererefund", "here");
                                                                object.put("refundCost", refundCost);
                                                            }
                                                            object.put("deliveryPrice",deliveryPrice);
                                                            object.put("userName", customer.getUserName());

                                                            object.put("totalPrice",totalamt+"");
                                                            object.put("date", deliveryDat);
                                                          //  String week = GlobalProvider.deliveryTiming.get(deliveryWeek);
                                                          //  Log.d("deliveryweek", week);

                                                            object.put("week_en", deliveryWeek);
                                                            object.put("week", globalProvider.deliveryTimingChinese.get(deliveryWeek));
                                                            object.put("duration", duration);
                                                            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, Constants.emailOrderUrl, object, new Response.Listener<JSONObject>() {
                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                   // Log.d("emailresponse", response.toString());
                                                                    //  Toast.makeText(getContext(), "Order Successfully placed", Toast.LENGTH_LONG).show();

                                                                    finish();


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
                                                                List<ProductStock> productStockList = new ArrayList<>();
                                                                for (ProductInfo product : resultProductList.getPayload()) {
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

                                                            Toast.makeText(PaymentActivity.this, getResources().getString(R.string.some_item_na), Toast.LENGTH_LONG).show();

                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(PaymentActivity.this, getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                                                    }


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    String message= globalProvider.getErrorMessage(error);
                                                    Toast.makeText(PaymentActivity.this,message,Toast.LENGTH_LONG).show();
                                                    //  Log.d("checkerror", error.toString());

                                                }
                                            });
                                            globalProvider.addRequest(jsonObjectRequest);


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {


                                    String modifyOrderUrl = Constants.modifyOrderUrl + prevOrder.getOrderID();
                                    // Log.d("checkmofifyurl",modifyOrderUrl);
                                    JSONObject jsonObject = new JSONObject();
                                    JsonObjectRequest jsonObjectRequest = null;
                                    try {
                                        jsonObject.put("extraPrice", subTotal);
                                        JSONArray jsonArray = new JSONArray();
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
                                            jsonObject3.put("price", price);
                                            jsonObject3.put("unit_ch", unitch);
                                            jsonObject3.put("unit_en", uniten);
                                            jsonObject3.put("ifWeigh",product.getIfWeigh());
                                            String category = globalProvider.categoryNameMap.get(productId);
                                            jsonObject3.put("category", category);
                                            jsonObject3.put("category", category);
                                            jsonObject3.put("imageCover", imageCover);
                                            jsonObject3.put("specification_ch", specificationch);
                                            jsonObject3.put("specification_en", specificationen);
                                            jsonObject3.put("limitPurchase",product.limitPurchase);
                                            JSONObject jsonObject4 = new JSONObject();
                                            jsonObject4.put("productInfo", jsonObject3);
                                            jsonObject4.put("quantity", total);
                                            jsonArray.put(jsonObject4);
                                        }
                                        jsonObject.put("extraProducts", jsonArray);
                                        if (refundCost > 0) {
                                           // Log.d("refunddd", "here");
                                           // Log.d("refundcosts",refundCost+"");
                                            jsonObject.put("extraRefundCost", refundCost);
                                        }
                                        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, modifyOrderUrl, jsonObject, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {

                                                //Todo fetch product list from response and send it to sendEmail api
                                              //  Log.d("modifyresponse",response.toString());
                                                //todo check status
                                                int status = 0;

                                                try {


                                                    status = response.getInt("status");
                                                    if (status == 0) {
                                                        Toast.makeText(PaymentActivity.this, getString(R.string.order_success), Toast.LENGTH_SHORT).show();


                                                        globalProvider.cartList.clear();

                                                        //cartAdapter.notifyDataSetChanged();
                                                        globalProvider.categoryNameMap.clear();
                                                        totalamt = 0.0;
                                                        subTotal=0.0;

                                                        if (refundCost > 0) {
                                                           // Log.d("hereref",refundCost+"");
                                                            double ecoin = Constants.getCustomer(PaymentActivity.this).getRefund().getECoins();
                                                            double val=ecoin - refundCost;


                                                            Customer customer= Constants.getCustomer(PaymentActivity.this);
                                                            customer.getRefund().setECoins(val);
                                                            Constants.setCustomer(PaymentActivity.this,customer);
                                                        }




                                                        JSONArray jsonArray=response.getJSONObject("payload").getJSONArray("productList");
                                                        totalamt=response.getJSONObject("payload").getDouble("totalPrice");
                                                        refundCost=response.getJSONObject("payload").getDouble("refundCost");
                                                        String orderCode = response.getJSONObject("payload").getString("orderCode");
                                                        JSONObject object = new JSONObject();
                                                        object.put("productList", jsonArray);
                                                        object.put("orderCode", orderCode);
                                                        object.put("email", customer.getEmail());
                                                        object.put("userName", customer.getUserName());
                                                        object.put("totalPrice", totalamt + "");
                                                        object.put("refundCost",refundCost);
                                                        object.put("deliveryPrice",deliveryPrice);
                                                        object.put("date", deliveryDat);
                                                       // String week = GlobalProvider.deliveryTiming.get(deliveryWeek);
                                                        object.put("week_en", deliveryWeek);
                                                        object.put("week", globalProvider.deliveryTimingChinese.get(deliveryWeek));
                                                        object.put("duration", duration);


                                                        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, Constants.emailOrderUrl, object, new Response.Listener<JSONObject>() {
                                                            @Override
                                                            public void onResponse(JSONObject response) {
                                                                finish();
                                                            }
                                                        }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {

                                                            }
                                                        });
                                                        globalProvider.addRequest(objectRequest);

                                                    } else if (status == 1) {

                                                        Toast.makeText(PaymentActivity.this, "Some Items are not available", Toast.LENGTH_LONG).show();

                                                        ObjectMapper objectMapper = new ObjectMapper();
                                                        JsonFactory jsonFactory = new JsonFactory();
                                                        try {
                                                            JsonParser jsonParser = jsonFactory.createParser(response.toString());
                                                            ResultProductList resultProductList = (ResultProductList) objectMapper.readValue(jsonParser, ResultProductList.class);
                                                            List<ProductStock> productStockList = new ArrayList<>();
                                                            for (ProductInfo product : resultProductList.getPayload()) {
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


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // Log.d("modifyerror",error.toString());
                                                String message= globalProvider.getErrorMessage(error);
                                                Toast.makeText(PaymentActivity.this,message,Toast.LENGTH_LONG).show();


                                            }
                                        });


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    globalProvider.addRequest(jsonObjectRequest);


                                }

                            }





                        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                submitButton.setEnabled(true);
                            }
                        }).create();
                alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.grey));
                    }
                });

                alertDialog.show();
                // todo add alert box if want to submit order



            }
        });
    }

    private void showUnitNumberAlert() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment dialogFragment = new UnitInputDialogFragment();
        dialogFragment.show(fragmentManager, "unit");


    }


    private void displayStockList(final List<ProductStock> productStockList) {
        submitButton.setClickable(true);




        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PaymentActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_listalert, null, false);

       // ListView lv = (ListView) convertView.findViewById(R.id.lvw);

        final CustomAlertAdapter adapter = new CustomAlertAdapter(productStockList, PaymentActivity.this);
        alertDialog.setTitle(getResources().getString(R.string.sorry_insufficient));
        alertDialog.setCancelable(false);




        alertDialog.setView(convertView);

        alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                for(ProductStock productStock:productStockList)
                {
                    for(Product product:globalProvider.cartList)
                    {
                        String productName=null;

                        // todo change language of product name
                        if(lang.equals("english"))
                        {
                            productName= product.getNameEn();
                        }
                        else
                            productName=product.getNameCh();

                        if(productName.equals(productStock.getProductName()))
                        {
                            //todo check the logic
                            if(productStock.getStock()<=0)
                            {
                               globalProvider.cartList.remove(product);
                            }
                            else
                                product.setTotalNumber(productStock.getStock());
                            break;

                        }
                    }
                }

                calculateTotal();
                productImageAdapter.notifyDataSetChanged();
            }
        });
        alertDialog.setAdapter(adapter,null);





        alertDialog.show();


    }
    public void onResume()
    {
        super.onResume();
        productImageAdapter.notifyDataSetChanged();

        if(lang.equals("english")) {
            String size=globalProvider.cartList.size() + " ";
            String text=size+ getString(R.string.item);
            Spannable spannable = new SpannableString(text);

            spannable.setSpan(new ForegroundColorSpan(Color.RED), 0, size.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            numItemsText.setText(spannable, TextView.BufferType.SPANNABLE);
        }
        else {
            String size=globalProvider.cartList.size() + " ";
            String text="共 "+size+" 件"+getString(R.string.item);
            Spannable spannable = new SpannableString(text);
            spannable.setSpan(new ForegroundColorSpan(Color.RED), 2, 2+size.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            numItemsText.setText(spannable, TextView.BufferType.SPANNABLE);


        }

        calculateTotal();


    }
    private void calculateTotal() {
        totalamt=0.0;
        subTotal=0.0;



        for (Product cartproducts : globalProvider.cartList) {
            totalamt = totalamt + cartproducts.getTotalNumber() * cartproducts.getPrice();
        }
        totalamt=Double.parseDouble(new DecimalFormat("##.##").format(totalamt));
        subTotal=totalamt;
        //subtotal is the actual total of all the price of vegatables
        //totalamt is the final price after adding deliverycost and subtracting ecoin(if any)
        subTotalText.setText(" $ " + subTotal);
        //if subtotal is greater than free delivery ;delivery cost will be 0;no need to check prevorder in that case
        if(subTotal>=customer.getDistrict().getFreeDeliveryPrice())
        {
            deliveryCostText.setText("$ 0.0");
            deliveryPrice=0;
            freeDeliveryLayout.setVisibility(View.GONE);
        }
        //if prevorder is null;thn add delivery cost

        else if(prevOrder==null)
        {
            deliveryCostText.setText("$ "+customer.getDistrict().getDeliveryPrice());
            deliveryPrice=customer.getDistrict().getDeliveryPrice();
           float price= (float) (customer.getDistrict().getFreeDeliveryPrice()-totalamt);
           String val=String.format("%.2f",price);
           totalamt=totalamt+deliveryPrice;
            totalamt=Double.parseDouble(new DecimalFormat("##.##").format(totalamt));

           String k="( $"+val+" for free delivery )";
            freeDeliveryCostMsg.setText(k);
        }
        //if prevorder is not null;thn delivery cost will be 0
        else if(prevOrder!=null)
        {
            deliveryCostText.setText("$ 0.0");
            deliveryPrice=0;
            freeDeliveryLayout.setVisibility(View.GONE);

        }
        //readjust the total if customer has ecoin

        if(customer.getRefund().getECoins()==0) {

            String total=String.format("%.2f",totalamt);
            total="$ "+total;
            String[] each = total.split("\\.");

            each[0]=each[0]+".";
            //adding spannable so that textsize of amount before. is more

            Spannable spannable = new SpannableString(total);

            spannable.setSpan(new AbsoluteSizeSpan(18, true), 0, each[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            totalAmountText.setText(spannable, TextView.BufferType.SPANNABLE);
            totalText.setText(spannable, TextView.BufferType.SPANNABLE);




            netBalanceText.setText("-$ "+0.0);


        }
        else {


            if(customer.getRefund().getECoins()>totalamt)
            {
                refundCost=totalamt;



            }
            else
            {
                refundCost=customer.getRefund().getECoins();



            }
            String refundCostInString=String.format("%.2f",refundCost);
            netBalanceText.setText("-$ "+refundCostInString);
          totalamt=(totalamt-refundCost);
            String total=String.format("%.2f",totalamt);
            total="$ "+total;
            String[] each = total.split("\\.");

            each[0]=each[0]+".";
            //adding spannable so that textsize of amount before. is more

            Spannable spannable = new SpannableString(total);

            spannable.setSpan(new AbsoluteSizeSpan(18, true), 0, each[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            totalAmountText.setText(spannable, TextView.BufferType.SPANNABLE);
            totalText.setText(spannable, TextView.BufferType.SPANNABLE);

           // totalAmountText.setText("$ "+total);
           // totalText.setText("$ "+total);

        }

    }


    private void setDeliveryDate()  {
        //*1 First calculate today date ,today week;If today is monday,todayWeek will be 1
        /*2 Set DeliveryWeek,Duration to customer's 1st cycle for e.g if order can be delivered at tuesday,thursday,friday;cyclList
        will have values 2,4,5;
        By Default deliveryWeek=2(tuesday)
        As same day delivery cannot be done,find week from cycle whose value is more than today week.
        In that case,it wll be tuesday.
        So Now deliveryWeek=2(tuesday), durattion=deliveryweek duration(retrieve from cycle)
        But there can be a case  when todayweek is saturday(6) and delivery can be done on monday(1),so by default deliveryWeek will be monday(1)
        Now calculate deliveryDate using delivery Week.
        Calculate Other 2 Delivery Dates,as we have to give our customers options of selecting from 3 delivery date.


         */














        /*
        Deciding which date to display in deliveryDateText out of 3 deliverydates

        We will be checking if User has previous Orders that are in waiting state;List of prevOrder is already calculated in FragmentCart
        and is in variable prevOrderList.
        We will be checking if users want to create new order(doesnt want to merge with any of the previousOrder)
        We will be checking if variable prevOrder retrieved from Intent
        prevOrder is not null
        2 cases
        1 case: If user has selected to merge with the previous Order;In that case,prevOrder will not be null.
        As the prevOrder date is in MM/dd/yyyy format and we are sending to serve in YYYY/mm/dd.
        We will convert the prevOrderDate to YYYY/mm/dd; and thn calculate its deliveryWeek and duration;
        This date will be displayed in deliveryDateText

        2 Case: If user wants to create a new order and doesnt want to merge with previous order.we will select that date
        which is not in prevOrderList







         */
        //Case 1 User wants to merge
        deliveryDat=selectedDate.getDate();
        duration=selectedDate.getTime();
        deliveryWeek=selectedDate.getWeek();



        if(prevOrder!=null)
        {









            submitButton.setText("Merge Order");
        }



            deliveryDateText.setText(deliveryDat+" "+selectedDate.getWeek()+" "+selectedDate.getTime());





    }

    public  Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime();


    }

    @Override
    public void onChangeDate(Delivery delivery) {

        deliveryDateText.setText(delivery.getDate()+" "+delivery.getWeek()+" "+delivery.getTime());
        deliveryDat=delivery.getDate();
        duration=delivery.getTime();
        deliveryWeek=delivery.getWeek();

        if(prevOrderList.isEmpty())
        {
            //no Merge Order Option Available
            submitButton.setText("CheckOut");
        }
        else {
            try {
                Date deliveryDate = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
                        .parse(deliveryDat);

                //  If any of the previous order's date is equal to selected date,than order will be added to the prevOrder's id
                for (PrevOrder order : prevOrderList) {


                    Date shippingDate = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                            .parse(order.getShippingDate());

                    if (shippingDate.compareTo(deliveryDate) == 0) {
                        prevOrder = order;
                        submitButton.setText("Merge Order");
                        calculateTotal();

                        return;
                    }
                }
                // order date not matches with any of the previous date
                // prevOrder will be null;recalculate total;submitbutton text will be checkout
                prevOrder=null;
                calculateTotal();
                submitButton.setText("CheckOut");


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }
/*
    @Override
    public void onInvoiceChoiceChange(boolean val) {
        SharedPreferences.Editor e = getPreferences(Context.MODE_PRIVATE).edit();
        if(val)
        {
            invoiceChoiceText.setText(R.string.yes);
            e.putInt ("billkey",0);
        }
        else {
            invoiceChoiceText.setText(R.string.no);
            e.putInt ("billkey",1);
        }
        e.commit();

    }
    */

    @Override
    public void PaymentMethodSelected(String str) {
        SharedPreferences.Editor e = getPreferences(Context.MODE_PRIVATE).edit();
        if(str.equals("cash"))
        {
            paymentMethodChoice.setText(R.string.cash);
            e.putInt ("paymentkey",0);
            res=0;
        }
        else {
            paymentMethodChoice.setText("PayNow");
            e.putInt("paymentkey",1);
            res=1;
        }


        e.commit();

    }

    @Override
    public void OptionalMethodSelected(int pos) {
        if(pos==0)
        {
            optionDefaultChoice=0;
            optionalChoiceText.setText("");
            //optionalChoiceText.setText(getString(R.string.none));

        }
        else if(pos==1)
        {
            optionDefaultChoice=1;
            optionalChoiceText.setText(getString(R.string.inform_by_phone));
        }
        else
        {
            optionDefaultChoice=2;
            optionalChoiceText.setText(getString(R.string.inform_without_phone));
        }

    }

    @Override
    public void unitChoiceSelected(String res) {
        String url=Constants.updateProfileUrl+globalProvider.getCustomerId();
        Map<String, String> param = new HashMap<>();
        param.put("address", res);
        CustomRequest customRequest=new CustomRequest(Request.Method.POST, url, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getInt("status")==0)
                    {
                        Log.d("checkresponse",response.toString());
                        customer.setAddress(res);
                        Constants.setCustomer(PaymentActivity.this,null);
                        Constants.setCustomer(PaymentActivity.this,customer);
                        Log.d("checkcustomeraddress",customer.address);


                        submitButton.setClickable(true);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        globalProvider.addRequest(customRequest);

      /*  String url = Constants.favouriteUrl + "/" + globalProvider.getCustomerId();
        Map<String, String> param = new HashMap<>();
        param.put("address", res);
        CustomRequest unitChangeRequest = new CustomRequest(Request.Method.PATCH, url, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Log.d("checlres", response.toString());
                try {
                    if (response.getInt("status") == 0) {

                        Log.d("checkresponse",response.toString());
                        customer.setAddress(res);
                        Constants.setCustomer(PaymentActivity.this,null);
                        Constants.setCustomer(PaymentActivity.this,customer);
                        Log.d("checkcustomeraddress",customer.address);


                        submitButton.setClickable(true);






                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.d("bindingerror", error.toString());

            }
        });

        globalProvider.addRequest(unitChangeRequest);
        */

    }

    public static class UnitInputDialogFragment extends DialogFragment
    {
        EditText unitNumberText;
        TextView noUnitChoiceText;
        TextView confirmText;
        UnitChoiceListener unitChoiceListener;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);

            // request a window without the title
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            return dialog;
        }
        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            // Verify that the host activity implements the callback interface
            try {
                // Instantiate the EditNameDialogListener so we can send events to the host

                unitChoiceListener = (UnitChoiceListener) context;
            } catch (ClassCastException e) {
                // The activity doesn't implement the interface, throw exception
                throw new ClassCastException(context.toString()
                        + " must implement UnitChoiceListener");

            }
        }
        @Override
        public void onDetach()
        {
            unitChoiceListener=null;
            super.onDetach();

        }
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

           // setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view= inflater.inflate(R.layout.unitselect_dialog, container);
            //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
           // getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            unitNumberText=(EditText) view.findViewById(R.id.unit_value_edittext);
            noUnitChoiceText=(TextView)view.findViewById(R.id.no_unit_no);
            confirmText=(TextView)view.findViewById(R.id.confirm);

            noUnitChoiceText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    unitChoiceListener.unitChoiceSelected("*");
                    dismiss();
                }
            });
           confirmText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    unitChoiceListener.unitChoiceSelected(unitNumberText.getText().toString());
                    dismiss();
                }
            });







            return view;
        }
        @Override
        public void onStart()
        {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            super.onStart();

        }






    }

    public static class OptionalMethodFragment extends DialogFragment
    {
        TextView noneTextView;
        TextView informByPhoneTextView;
        TextView informDoorStepTextView;
        OptionalChoiceListener optionalChoiceListener;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);

            // request a window without the title
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            return dialog;
        }
        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            // Verify that the host activity implements the callback interface
            try {
                // Instantiate the EditNameDialogListener so we can send events to the host

                optionalChoiceListener = (OptionalChoiceListener) context;
            } catch (ClassCastException e) {
                // The activity doesn't implement the interface, throw exception
                throw new ClassCastException(context.toString()
                        + " must implement PaymentChoiceListener");

            }
        }
        @Override
        public void onDetach()
        {
            optionalChoiceListener=null;
            super.onDetach();

        }
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view= inflater.inflate(R.layout.optional_dialog, container);
            noneTextView=(TextView)view.findViewById(R.id.none);
            informByPhoneTextView=(TextView)view.findViewById(R.id.inform_phone);
            informDoorStepTextView=(TextView)view.findViewById(R.id.inform_without_phone);
            noneTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    optionalChoiceListener.OptionalMethodSelected(0);
                    dismiss();
                }
            });
            informByPhoneTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    optionalChoiceListener.OptionalMethodSelected(1);
                    dismiss();
                }
            });
            informDoorStepTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    optionalChoiceListener.OptionalMethodSelected(2);
                    dismiss();
                }
            });







            return view;
        }

    }

    public static class PaymentMethodFragment extends DialogFragment
    {
        TextView payNowText;

        TextView cashText;
        int res;

        PaymentChoiceListener paymentChoiceListener;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);

            // request a window without the title
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            return dialog;
        }
        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            // Verify that the host activity implements the callback interface
            try {
                // Instantiate the EditNameDialogListener so we can send events to the host

                paymentChoiceListener = (PaymentChoiceListener) context;
            } catch (ClassCastException e) {
                // The activity doesn't implement the interface, throw exception
                throw new ClassCastException(context.toString()
                        + " must implement PaymentChoiceListener");

            }
        }
        @Override
        public void onDetach()
        {
            paymentChoiceListener=null;
            super.onDetach();

        }
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle mArgs = getArguments();
            res=mArgs.getInt("res");
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view= inflater.inflate(R.layout.payment_dialog, container);
            payNowText=view.findViewById(R.id.pay_button);
           cashText=view.findViewById(R.id.cashButton);
           if(res==0)
           {
               cashText.setBackground(getContext().getResources().getDrawable(R.drawable.button_rectangle));

           }
           else
               payNowText.setBackground(getContext().getResources().getDrawable(R.drawable.button_rectangle));

            cashText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    paymentChoiceListener.PaymentMethodSelected("cash");
                    dismiss();
                }
            });
            payNowText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    paymentChoiceListener.PaymentMethodSelected("paynow");
                    dismiss();
                }
            });




            return view;
        }


    }

   /* public static class ChangeInvoiceFragment extends DialogFragment
    {
        TextView yesTextView;
        TextView noTextView;
        InvoiceChoiceListener invoiceChoiceListener;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);

            // request a window without the title
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            return dialog;
        }
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            // Verify that the host activity implements the callback interface
            try {
                // Instantiate the EditNameDialogListener so we can send events to the host
                invoiceChoiceListener = (InvoiceChoiceListener) context;
            } catch (ClassCastException e) {
                // The activity doesn't implement the interface, throw exception
                throw new ClassCastException(context.toString()
                        + " must implement InvoiceChoiceListener");
            }
        }
        @Override
        public void onDetach()
        {
            invoiceChoiceListener=null;
            super.onDetach();

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view= inflater.inflate(R.layout.invoice_fragment, container);
            yesTextView=view.findViewById(R.id.yesButton);
            noTextView=view.findViewById(R.id.noButton);
            yesTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    invoiceChoiceListener.onInvoiceChoiceChange(true);
                    dismiss();
                }
            });
            noTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    invoiceChoiceListener.onInvoiceChoiceChange(false);
                    dismiss();
                }
            });




            return view;
        }

    }
    */

    public static class ChangeDateFragment extends DialogFragment implements SelectDateAdapter.MyClickListener
    {

        RecyclerView recyclerView;
        List<Delivery> deliveryList;
        SelectDateAdapter selectDateAdapter;
        private DateChangeListener listener;
        String deliveryDate;
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
            deliveryDate=mArgs.getString("deliveryDate");
            deliveryList = mArgs.getParcelableArrayList("deliveryList");
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view= inflater.inflate(R.layout.nextdelivery_dialogfragment, container);
            view.findViewById(R.id.merge_notification_layout).setVisibility(View.GONE);
            recyclerView=(view).findViewById(R.id.selectdate_recycler);
            selectDateAdapter=new SelectDateAdapter(getContext(),deliveryList,deliveryDate,this);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            recyclerView.setAdapter(selectDateAdapter);
            recyclerView.setLayoutManager(linearLayoutManager);


            return view;
        }
        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            // Verify that the host activity implements the callback interface
            try {
                // Instantiate the EditNameDialogListener so we can send events to the host
                listener = (DateChangeListener) context;
            } catch (ClassCastException e) {
                // The activity doesn't implement the interface, throw exception
                throw new ClassCastException(context.toString()
                        + " must implement EditNameDialogListener");
            }
        }
        @Override
        public void onDetach()
        {
            listener=null;
            super.onDetach();

        }


        @Override
        public void onClick(int position) {
            Delivery delivery= deliveryList.get(position);
            /*Intent i = new Intent();
            i .putExtra("status", "ok"
            );
            i.putExtra("selectedDate",delivery);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
            */
            listener.onChangeDate(delivery);

            this.dismiss();


        }
    }
}
