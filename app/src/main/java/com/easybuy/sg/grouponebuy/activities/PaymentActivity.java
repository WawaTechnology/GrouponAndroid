package com.easybuy.sg.grouponebuy.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.easybuy.sg.grouponebuy.fragment.FragmentCart;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.model.CartProduct;
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
import com.easybuy.sg.grouponebuy.utils.InvoiceChoiceListener;
import com.easybuy.sg.grouponebuy.utils.PaymentChoiceListener;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import java.util.List;
import java.util.Locale;


public class PaymentActivity extends AppCompatActivity implements DateChangeListener,InvoiceChoiceListener,PaymentChoiceListener {
    RecyclerView imageRecycler;
    GlobalProvider globalProvider;
    RelativeLayout paymentLayout;
    ImageView backButton;
    TextView addressText;
    TextView numItemsText;
    TextView deliveryDateText;
    RelativeLayout prodLayout;
    TextView invoiceChoiceText;
    TextView paymentMethodChoice;
    TextView totalAmountText;
    EditText remarkText;
    Button submitButton;
    List<PrevOrder> prevOrderList;
    RelativeLayout invoiceLayout;
    int deliveryWeek;
    String duration;
    String prevOrder;
    int todayWEEK;
    int nextDeliveries[];
    String nextDeliveryDates[];
    String nextDeliveryTimimgs[];
    String deliveryDat;
    List<Delivery> deliveryList;
    District district;
    String lang;
    double totalamt;
    Customer customer;
    public static final int DELIVERY_CONSTANT=212;
    ProductImageAdapter productImageAdapter;


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_layout);
        backButton=(ImageView) findViewById(R.id.back);
        prodLayout=(RelativeLayout) findViewById(R.id.pd_layout);
        deliveryList=new ArrayList<>();
        prevOrderList=new ArrayList<>();
        nextDeliveries=new int[2];
        nextDeliveryDates=new String[2];
        nextDeliveryTimimgs=new String[2];
        remarkText=(EditText) findViewById(R.id.payment_msg);
        paymentLayout=(RelativeLayout) findViewById(R.id.payment_layout);
         customer=Constants.getCustomer(this);
        district= customer.getDistrict();
        lang=Constants.getLanguage(this);
        Intent intent=getIntent();
        totalamt= intent.getDoubleExtra("totalamt",0.0);
        prevOrderList = (ArrayList<PrevOrder>) intent.getSerializableExtra("prevOrderList");
        addressText=(TextView)findViewById(R.id.delivery_address) ;
        invoiceLayout=(RelativeLayout) findViewById(R.id.invoice_layout);
        numItemsText=(TextView)findViewById(R.id.num_item);
        deliveryDateText=(TextView) findViewById(R.id.delivery_date);
        invoiceChoiceText=(TextView) findViewById(R.id.invoice_choice);
        paymentMethodChoice=(TextView) findViewById(R.id.payment_choice);
        totalAmountText=(TextView) findViewById(R.id.amt);
        submitButton=(Button) findViewById(R.id.submit);
        globalProvider=GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        imageRecycler=(RecyclerView)findViewById(R.id.img_Recyclers);
        productImageAdapter=new ProductImageAdapter(PaymentActivity.this,globalProvider.cartList);
        imageRecycler.setAdapter(productImageAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        imageRecycler.setLayoutManager(linearLayoutManager);

      //  addressText.setText(Constants.getCustomer(this).address);
        totalAmountText.setText("$ "+totalamt);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final View parent = (View) backButton.getParent();  // button: the view you want to enlarge hit area
        parent.post( new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                backButton.getHitRect(rect);
                rect.top -= 10;    // increase top hit area
                rect.left -= 10;   // increase left hit area
                rect.bottom += 10; // increase bottom hit area
                rect.right += 50;  // increase right hit area
                parent.setTouchDelegate( new TouchDelegate( rect , backButton));
            }
        });

        SharedPreferences sp= getPreferences(Context.MODE_PRIVATE);
        int res=sp.getInt("paymentkey",0);
        int billres=sp.getInt("billkey",1);
        if(res==0)
        {
            paymentMethodChoice.setText(R.string.cash);
        }
        else
            paymentMethodChoice.setText("PayNow");
        if(billres==0)
        {
            invoiceChoiceText.setText(R.string.yes);
        }
        else
            invoiceChoiceText.setText(R.string.no);
        String address=null;
        if(lang.equals("english"))
            address = district.getNamePrimaryEn() + " - " + district.getNameSecondaryEn() + " - " + district.getNameTertiaryEn();
        else
            address = district.getNamePrimaryCh() + " - " + district.getNameSecondaryCh() + " - " + district.getNameTertiaryCh();

        addressText.setText(address);
        calculateDeliveryDate();
        paymentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager=getSupportFragmentManager();
                DialogFragment dialogFragment=new PaymentMethodFragment();
                dialogFragment.show(fragmentManager,"payment");
            }
        });
        invoiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           FragmentManager fragmentManager=getSupportFragmentManager();
           DialogFragment dialogFragment=new ChangeInvoiceFragment();
           dialogFragment.show(fragmentManager,"invoice");
            }
        });
        deliveryDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FragmentManager fragmentManager=getSupportFragmentManager();
                DialogFragment dialogFragment=new ChangeDateFragment();
                Bundle args=new Bundle();
                args.putParcelableArrayList("deliveryList", (ArrayList<? extends Parcelable>) deliveryList);
                dialogFragment.setArguments(args);
                dialogFragment.show(fragmentManager,"delivery");
            }
        });
        prodLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PaymentActivity.this,CartListActivity.class);
                startActivity(intent);
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButton.setClickable(false);
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

                    if (totalamt < customer.getDistrict().getDeliveryCost()) {
                        Toast.makeText(PaymentActivity.this,getString(R.string.min_spend)+ " $ "+customer.getDistrict().getDeliveryCost(), Toast.LENGTH_LONG).show();
                        submitButton.setClickable(true);
                        return;
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
                            Log.d("invoicetext",invoiceChoiceText.getText().toString());
                            String invoiceValue=invoiceChoiceText.getText().toString();
                            if (invoiceValue.equals(getString(R.string.yes))) {
                                Log.d("valuesis", "yes");
                                jsonObject2.put("isPrint", true);
                            }
                            else {
                                jsonObject2.put("isPrint", false);
                                Log.d("valuesis","no");
                            }
                            if (paymentMethodChoice.getText().toString().equals(getString(R.string.cash))) {
                                Log.d("valueis", "cash");
                                jsonObject2.put("paymentMethod", "cash");
                            }
                            else {
                                jsonObject2.put("paymentMethod", "PayNow");
                                Log.d("valueis","paynow");
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
                                                    Toast.makeText(PaymentActivity.this, getString(R.string.order_success), Toast.LENGTH_SHORT).show();
                                                    globalProvider.categoryNameMap.clear();
                                                    globalProvider.cartList.clear();
                                                    totalamt=0.0;
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

                                            Toast.makeText(PaymentActivity.this, getResources().getString(R.string.some_item_na), Toast.LENGTH_LONG).show();

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(PaymentActivity.this,getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
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

                    new AlertDialog.Builder(PaymentActivity.this).setCancelable(false).setTitle(getResources().getString(R.string.alert)).setMessage(getResources().getString(R.string.add_existing_order)).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            submitButton.setClickable(true);
                            dialogInterface.dismiss();


                        }
                    }).setPositiveButton(getResources().getString(R.string.confm), new DialogInterface.OnClickListener() {
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
                                                        Toast.makeText(PaymentActivity.this, getString(R.string.order_success), Toast.LENGTH_SHORT).show();



                                                        globalProvider.cartList.clear();

                                                        //cartAdapter.notifyDataSetChanged();
                                                        globalProvider.categoryNameMap.clear();
                                                        totalamt=0.0;
                                                        finish();


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

                                                Toast.makeText(PaymentActivity.this, "Some Items are not available", Toast.LENGTH_LONG).show();

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




                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // Log.d("modifyerror",error.toString());
                                        Toast.makeText(PaymentActivity.this,getResources().getString(R.string.something_wrong),Toast.LENGTH_LONG).show();


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

        

    }
    private void displayStockList(final List<ProductStock> productStockList) {
        submitButton.setClickable(true);




        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PaymentActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_listalert, null, false);

        ListView lv = (ListView) convertView.findViewById(R.id.lvw);

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
        if(lang.equals("english"))
        numItemsText.setText(globalProvider.cartList.size()+" "+getString(R.string.item));
        else
            numItemsText.setText("共 "+globalProvider.cartList.size()+" 件"+getString(R.string.item));

        calculateTotal();


    }
    private void calculateTotal() {
        totalamt=0.0;


        for (Product cartproducts : globalProvider.cartList) {
            totalamt = totalamt + cartproducts.getTotalNumber() * cartproducts.getPrice();

        }
        totalamt=Double.parseDouble(new DecimalFormat("##.##").format(totalamt));
        totalAmountText.setText("$ "+totalamt);
    }


    private void calculateDeliveryDate() {
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);
        todayWEEK = calendar.get(Calendar.DAY_OF_WEEK);
        todayWEEK-=1;
        // e.g todayWeek will be 4 if it is thursday
        List<Cycle> cycleList = district.getCycle();
        deliveryWeek=cycleList.get(0).getWeek();
        duration=cycleList.get(0).getDuration();
        //get deliveryweek greater than today
        for (Cycle cycle : cycleList) {
            int week = cycle.getWeek();

            if(week>todayWEEK)
            {
                deliveryWeek=week;
                duration= cycle.getDuration();
                break;
            }

        }
        //cases where todayweek is sunday i.e 7, adn delivery can go on mon,tuesday
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
        //calculating list of other delivery dates
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
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
        deliveryDat = simpleDateFormat1.format(now);
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
        if(lang.equals("english"))
            deliveryDateText.setText(deliveryDat+" "+globalProvider.getDeliveryTiming().get(deliveryWeek)+" "+duration);
        else {
            String week =globalProvider.getDeliveryTiming().get(deliveryWeek);
            week=globalProvider.deliveryTimingChinese.get(week);
            deliveryDateText.setText(deliveryDat+" "+week+" "+duration);

        }




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

    }

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

    @Override
    public void PaymentMethodSelected(String str) {
        SharedPreferences.Editor e = getPreferences(Context.MODE_PRIVATE).edit();
        if(str.equals("cash"))
        {
            paymentMethodChoice.setText(R.string.cash);
            e.putInt ("paymentkey",0);
        }
        else {
            paymentMethodChoice.setText("PayNow");
            e.putInt("paymentkey",1);
        }


        e.commit();

    }

    public static class PaymentMethodFragment extends DialogFragment
    {
        TextView payNowText;
        TextView cashText;
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
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view= inflater.inflate(R.layout.payment_dialog, container);
            payNowText=view.findViewById(R.id.pay_button);
           cashText=view.findViewById(R.id.cashButton);
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

    public static class ChangeInvoiceFragment extends DialogFragment
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

    public static class ChangeDateFragment extends DialogFragment implements SelectDateAdapter.MyClickListener
    {

        RecyclerView recyclerView;
        List<Delivery> deliveryList;
        SelectDateAdapter selectDateAdapter;
        private DateChangeListener listener;
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
