package com.easybuy.sg.grouponebuy.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.adapter.CustomAlertAdapter;
import com.easybuy.sg.grouponebuy.adapter.OrderDetailAdapter;
import com.easybuy.sg.grouponebuy.helpers.CustomRequest;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.helpers.Utf8JsonRequest;
import com.easybuy.sg.grouponebuy.model.Order;
import com.easybuy.sg.grouponebuy.model.OrderPayload;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.model.ProductInfo;
import com.easybuy.sg.grouponebuy.model.ProductOrderList;
import com.easybuy.sg.grouponebuy.model.ProductStock;
import com.easybuy.sg.grouponebuy.model.ResultClass;
import com.easybuy.sg.grouponebuy.model.ResultOrder;
import com.easybuy.sg.grouponebuy.model.ResultProductList;
import com.easybuy.sg.grouponebuy.model.SingleOrderResult;
import com.easybuy.sg.grouponebuy.model.Sub;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailActivity  extends AppCompatActivity implements OrderDetailAdapter.QuantityChangedListener{
    RecyclerView orderListRecycler;
    OrderDetailAdapter orderDetailAdapter;
    List<ProductOrderList> productList;
    TextView orderIdText,orderDateText,deliveryDateText,statusText,paymentStatusText,refundStatusText,deliveryPriceLabel;
    Order order;
   // TextView totalNumberText;
    TextView totalPriceText;
    Button cancelButton,editOrderButton;
    TextView delivery_price;

   // LinearLayout netBalanceLayout,subTotalLayout;
    TextView subTotalLabel,netBalanceLabel;

    TextView subTotalText,netBalanceText;
    GlobalProvider globalProvider;
    boolean editClicked;
    String lang;
    // View Types
    private static final int NO_EDIT = 0;
    private static final int EDIT = 1;
    private double changedtotal=0.0;
    private double totalDouble=0.0;
    private double deliveryPriceDouble=0.0;
    private ImageView backButton;
    boolean orderEdited;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_trial);

        productList=new ArrayList<>();
       // netBalanceLayout=(LinearLayout)findViewById(R.id.net_balancelayout);
       // subTotalLayout=(LinearLayout) findViewById(R.id.subtotal_layout);
        deliveryPriceLabel=(TextView) findViewById(R.id.textViewDelivery);
        subTotalLabel=(TextView) findViewById(R.id.textView13);
        netBalanceLabel=(TextView) findViewById(R.id.textView15);
        orderListRecycler=(RecyclerView)findViewById(R.id.order_productlist);
        orderIdText=(TextView) findViewById(R.id.order_id);
        orderDateText=(TextView) findViewById(R.id.order_date);
        subTotalText=(TextView) findViewById(R.id.sub_total);
        netBalanceText=(TextView) findViewById(R.id.net_balance);
        delivery_price=(TextView) findViewById(R.id.delivery_price);
        refundStatusText=(TextView) findViewById(R.id.refund_status);
        deliveryDateText=(TextView) findViewById(R.id.delivery_date);
        lang=Constants.getLanguage(this);
        statusText=(TextView) findViewById(R.id.status);
        paymentStatusText=(TextView) findViewById(R.id.payment_status);

       // totalNumberText=(TextView) findViewById(R.id.num_item);
        totalPriceText=(TextView) findViewById(R.id.total_amt);
        cancelButton=(Button) findViewById(R.id.cancel);
        editOrderButton=(Button) findViewById(R.id.edit_order);
        backButton=(ImageView) findViewById(R.id.back);
        globalProvider=GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        Intent intent=getIntent();
        order=(Order)intent.getSerializableExtra("Order");

        String orderId=order.getId();
        orderDetailAdapter=new OrderDetailAdapter(this,productList,this,order.getState());


        orderListRecycler.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);


        orderListRecycler.setLayoutManager(linearLayoutManager);
        orderListRecycler.setAdapter(orderDetailAdapter);

        Log.d("checkid",orderId);


        String deliveryDate=  intent.getStringExtra("deliverydate");
        String orderDateTime= intent.getStringExtra("orderDateTime");
        checkOrderStatus(orderId);

       // totalNumberText.setText(getString(R.string.items)+" "+order.getProductList().size());
        String price = String.format("%.2f", order.getTotalPrice());
        Log.d("ppprice",price+"");
        //totalPriceText.setText(" $"+price);
        subTotalText.setText(" $ "+price);
        changedtotal=order.getTotalPrice();

        orderIdText.setText(order.getOrderCode());
        orderDateText.setText(order.getOrderDate());
      // String  state = order.getState().substring(0,1).toUpperCase() + order.getState().substring(1);
        Log.d("checkorderstate",order.getState()+"");
        if(order.getState().equalsIgnoreCase("processing"))
        {
            statusText.setText(getString(R.string.processing));
            editOrderButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
        }

        if(order.getState().equalsIgnoreCase("shipping"))
        {
            statusText.setText(getString(R.string.shipping));

            editOrderButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
        }
       else if(order.getState().equalsIgnoreCase("completed"))
        {
            statusText.setText(getString(R.string.completed));

            statusText.setTextColor(getResources().getColor(R.color.green));
        }
        else if(order.getState().equalsIgnoreCase("waiting"))
        {
            statusText.setText(getString(R.string.waiting));

           /* if(order.getRefundCostOrder()>0)
            {
                subTotalText.setVisibility(View.VISIBLE);
                netBalanceText.setVisibility(View.VISIBLE);
                subTotalText.setText(getString(R.string.sub_total)+" : "+price);
                netBalanceText.setText(getString(R.string.net_balance)+" : "+order.getRefundCostOrder());
               double val=order.getTotalPrice()-order.getRefundCostOrder();
                String total = String.format("%.2f", val);
                totalPriceText.setText(" $"+total);
            }
            else
            {
                subTotalText.setVisibility(View.GONE);
                netBalanceText.setVisibility(View.GONE);
            }
            */

        }
        if(order.getRefundCostOrder()>0)
        {
           // subTotalLayout.setVisibility(View.VISIBLE);
           // netBalanceLayout.setVisibility(View.VISIBLE);
            //subTotalLabel.setVisibility(View.VISIBLE);
            netBalanceLabel.setVisibility(View.VISIBLE);


            String totalNet=String.format("%.2f",order.getRefundCostOrder());
            netBalanceText.setText(" $ "+totalNet);
            double val=order.getTotalPrice()-order.getRefundCostOrder();
            price = String.format("%.2f", val);
            //totalPriceText.setText(" $ "+total);
        }
        else
        {
           // subTotalLayout.setVisibility(View.GONE);
           // netBalanceLayout.setVisibility(View.GONE);
           // subTotalLabel.setVisibility(View.GONE);
            netBalanceLabel.setVisibility(View.GONE);
            netBalanceText.setVisibility(View.GONE);
          //  subTotalText.setVisibility(View.GONE);


        }
        if(order.getDeliveryPrice()>0)
        {
            delivery_price.setText("$ "+order.getDeliveryPrice());
            delivery_price.setVisibility(View.VISIBLE);
            deliveryPriceLabel.setVisibility(View.VISIBLE);
           double val= Double.parseDouble(price)+order.getDeliveryPrice();
           price=String.format("%.2f",val);

        }
        else
        {
            delivery_price.setVisibility(View.GONE);
            deliveryPriceLabel.setVisibility(View.GONE);
        }
        totalPriceText.setText("$ "+price);
        if(order.getState().equalsIgnoreCase("processing")||order.getState().equalsIgnoreCase("waiting"))
        {
            statusText.setTextColor(getResources().getColor(R.color.red));

        }
        else {


            if (Constants.getCustomer(OrderDetailActivity.this).getRefund() != null && Constants.getCustomer(OrderDetailActivity.this).getRefund().getSub() != null) {
                Log.d("price", order.getTotalPrice() + "");
                Log.d("priceactual", order.getTotalPriceActual() + "");


                List<Sub> subList = Constants.getCustomer(OrderDetailActivity.this).getRefund().getSub();
                Log.d("sublistsize", subList.size() + "");

                boolean found = false;
                Double coin = 0.0;

                for (Sub sub : subList) {
                    if (sub.getOrder().equals(orderId)) {
                        Log.d("subid", sub.getOrder());
                        coin = sub.getCoin();
                        found = true;
                        break;

                    }
                }
                if (found) {
                    String refundStatus = "(" + getString(R.string.paid) + ": $" + order.getTotalPriceActual() + " ," + getString(R.string.refund) + ": $" + coin + ")";
                    refundStatusText.setText(refundStatus);
                    refundStatusText.setTextColor(getResources().getColor(R.color.green));
                    refundStatusText.setVisibility(View.VISIBLE);
                } else {
                    Log.d("here", "visible");
                    Log.d("checkprice", order.getTotalPrice() + "");
                    Log.d("priceactual", order.getTotalPriceActual() + "");
                    Log.d("refundCost", order.getRefundCostOrder() + "");

                    if (order.getTotalPriceActual() > order.getTotalPrice() - order.getRefundCostOrder()) {

                        refundStatusText.setText("(" + getString(R.string.paid) + ": $" + order.getTotalPriceActual() + " " + getString(R.string.not_refund) + ")");
                        refundStatusText.setTextColor(getResources().getColor(R.color.red));
                        refundStatusText.setVisibility(View.VISIBLE);
                    }
                }



            }
        }


        deliveryDateText.setText(deliveryDate);
        orderDateText.setText(orderDateTime);
        productList.addAll(order.getProductList());
        OrderDetailAdapter.editClicked=false;
        orderDetailAdapter.notifyDataSetChanged();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(orderEdited)
                {

                }

                finish();
            }
        });
        editOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editOrderButton.getText().toString().equalsIgnoreCase(getString(R.string.edit))) {
                    OrderDetailAdapter.editClicked = true;
                    orderDetailAdapter.notifyDataSetChanged();
                    editOrderButton.setText(getString(R.string.save));
                }
                else
                {
                    editOrder();
                    editOrderButton.setText(getString(R.string.edit));
                    OrderDetailAdapter.editClicked=false;
                    orderDetailAdapter.notifyDataSetChanged();

                }




            }
        });
       paymentStatusText.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(paymentStatusText.getText().toString().equals(getString(R.string.paid)))
               {

               }
               else
               {
                   final Dialog dialog = new Dialog(OrderDetailActivity.this);
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

           }
       });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(OrderDetailActivity.this).setTitle(getString(R.string.alert)).setMessage(getString(R.string.cancel_order_alert)).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cancelOrder();
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                }).create().show();



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















    }

  private void checkOrderStatus(String id) {
        String url= Constants.editOrderUrl+id;
        Log.d("hereurl",url);
    Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonFactory jsonFactory = new JsonFactory();
            try {
                JsonParser jsonParser = jsonFactory.createParser(response);
                SingleOrderResult resultOrder = (SingleOrderResult) objectMapper.readValue(jsonParser, SingleOrderResult.class);
                Log.d("checkres",resultOrder.getStatus()+"");
                OrderPayload orderPayload=resultOrder.getPayload();
                if(orderPayload.getTotalPriceActual()!=null&&orderPayload.getTotalPriceActual()>0)
                {

                    paymentStatusText.setText(getString(R.string.paid));
                    paymentStatusText.setTextColor(getResources().getColor(R.color.green));
                    editOrderButton.setVisibility(View.GONE);
                    cancelButton.setVisibility(View.GONE);
                }
                else {
                    paymentStatusText.setText(getString(R.string.unpaid));
                    paymentStatusText.setTextColor(getResources().getColor(R.color.orange));

                    //editOrderButton.setVisibility(View.VISIBLE);
                   // cancelButton.setVisibility(View.VISIBLE);
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


    private void editOrder()  {
        if(changedtotal>Constants.getCustomer(OrderDetailActivity.this).getDistrict().getDeliveryCost()) {
            String url = Constants.editOrderUrl + order.getId();
            Log.d("checkediturl", url);
            JSONObject jsonObject = new JSONObject();
            JsonObjectRequest jsonObjectRequest = null;

            try {
                jsonObject.put("totalPrice", changedtotal);
                Log.d("deliveryPrice",deliveryPriceDouble+"");
                jsonObject.put("deliveryPrice",deliveryPriceDouble);
                if (order.getRefundCostOrder() > 0) {
                    Log.d("hererefund", "here");
                    jsonObject.put("refundCost", order.getRefundCostOrder());
                }
                //jsonObject.put

                JSONArray jsonArray = new JSONArray();
                for (ProductOrderList productOrder : productList) {

                    JSONObject jsonObject4 = new JSONObject();

                    ProductInfo productInfo = productOrder.getProductInfo();
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("productID", productInfo.getProductID());
                    jsonObject1.put("name_ch", productInfo.getNameCh());
                    jsonObject1.put("name_en", productInfo.getNameEn());
                    jsonObject1.put("price", productInfo.getPrice());
                    jsonObject1.put("imageCover", productInfo.getImageCover());
                    jsonObject1.put("unit_ch", productInfo.getUnitCh());
                    jsonObject1.put("unit_en", productInfo.getUnitEn());
                    jsonObject1.put("specification_ch", productInfo.getSpecificationCh());
                    jsonObject1.put("specification_en", productInfo.getSpecificationEn());
                    jsonObject1.put("category", productInfo.getCategory());


                    jsonObject4.put("productInfo", jsonObject1);
                    jsonObject4.put("quantity", productOrder.getQuantity() + "");
                    jsonArray.put(jsonObject4);
                }
                jsonObject.put("productList", jsonArray);
                jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("checkeditresponse", response.toString());
                        try {
                            if (response.getInt("status") == 0) {
                               Toast.makeText(OrderDetailActivity.this,getString(R.string.order_updated_success), Toast.LENGTH_LONG).show();
                                orderEdited=true;


                            }
                            else if(response.getInt("status")==1)
                            {

                                //Toast.makeText(OrderDetailActivity.this, "Some Items are not available", Toast.LENGTH_LONG).show();
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
                                    Log.d("productoutofstock",productStockList.get(0).getProductName());
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
                        Log.d("editerror", error.toString());

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            globalProvider.addRequest(jsonObjectRequest);
        }
        else
        {
            Toast.makeText(this,getString(R.string.min_spend)+" $ "+Constants.getCustomer(OrderDetailActivity.this).getDistrict().getDeliveryCost(), Toast.LENGTH_LONG).show();
        }




    }
    private void displayStockList(final List<ProductStock> productStockList) {
       // submitButton.setClickable(true);




        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderDetailActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_listalert, null, false);

        ListView lv = (ListView) convertView.findViewById(R.id.lvw);

        final CustomAlertAdapter adapter = new CustomAlertAdapter(productStockList, OrderDetailActivity.this);
        alertDialog.setTitle(getResources().getString(R.string.sorry_insufficient));



        alertDialog.setView(convertView);
        alertDialog.setCancelable(false);


        alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();

         /*  String url=Constants.editOrderUrl+order.getId();
           Log.d("editrl",url);
           Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
               @Override
               public void onResponse(String response) {
                   try {

                       JSONObject jsonObj = new JSONObject(response);
                       JsonFactory jsonFactory = new JsonFactory();
                       ObjectMapper objectMapper = new ObjectMapper();
                      JSONObject newobj= jsonObj.getJSONObject("payload");
                      JSONArray jsonArray=  newobj.getJSONArray("productList");
                      Log.d("checkjsonArraysize",jsonArray.length()+"");
                     // List<ProductOrderList> productOrderLists=new ArrayList<>();
                       JsonParser jsonParser = jsonFactory.createParser(jsonArray.toString());
                      List<ProductOrderList> productOrderLists = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ProductOrderList>>(){}.getType());

                       Log.d("productorderli",productOrderLists.get(0).getId());




                      




                   } catch (JSONException e) {
                       e.printStackTrace();
                   } catch (JsonParseException e) {
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
           */


            }
        });
        alertDialog.setAdapter(adapter,null);





        alertDialog.show();


    }
    @Override
    public void onQuantityChanged(ProductOrderList product, int quantity,String operation) {
        if (quantity == 0) {

            productList.remove(product);


            orderDetailAdapter.notifyDataSetChanged();

        }
        changedtotal=0.0;
        totalDouble=0.0;
        deliveryPriceDouble=0.0;
        for(ProductOrderList productOrderList:productList)

        {

            changedtotal+=productOrderList.getQuantity()*productOrderList.getProductInfo().getPrice();
        }

        changedtotal=Double.parseDouble(new DecimalFormat("##.##").format(changedtotal));
        totalDouble=changedtotal;
        String total=String.format("%.2f",changedtotal);

        subTotalText.setText(" $"+total);
        if(totalDouble>=Constants.getCustomer(getApplicationContext()).getDistrict().getFreeDeliveryPrice())
        {

            delivery_price.setVisibility(View.GONE);
            deliveryPriceLabel.setVisibility(View.GONE);
        }
        else
        {
            deliveryPriceDouble=Constants.getCustomer(getApplicationContext()).getDistrict().getDeliveryPrice();
            totalDouble=totalDouble+deliveryPriceDouble;
            delivery_price.setText(" $ "+Constants.getCustomer(getApplicationContext()).getDistrict().getDeliveryPrice());
            delivery_price.setVisibility(View.VISIBLE);
            deliveryPriceLabel.setVisibility(View.VISIBLE);
        }
        if(order.getRefundCostOrder()>0)
        {
            totalDouble=totalDouble-order.getRefundCostOrder();
        }
        String totalamt=String.format("%.2f",totalDouble);
        totalPriceText.setText(" $"+totalamt);
       // totalNumberText.setText(getString(R.string.items)+" "+productList.size());



    }



    private void cancelOrder() {
        Map<String,String> params=new HashMap<>();
        params.put("state","canceled");
        String url= Constants.deleteOrderUrl+order.getId();
        CustomRequest customRequest=new CustomRequest(Request.Method.PATCH, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("getres",response.toString());
                Gson gson = new Gson();
                ResultClass resultClass= gson.fromJson(response.toString() , ResultClass.class);
                if(resultClass.getStatus()==0)
                {
                    Toast.makeText(OrderDetailActivity.this,getString(R.string.order_cancelled_success),Toast.LENGTH_LONG).show();
                   /* Intent returnIntent = new Intent();
                     returnIntent.putExtra("orderEdited",true);
                     setResult(Activity.RESULT_OK,returnIntent);

                    globalProvider.setDeletedOrder(order);
                    */

                    finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        globalProvider.addRequest(customRequest);
        /*Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.PATCH, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getres",response);
                Gson gson = new Gson();
                ResultClass resultClass= gson.fromJson(response , ResultClass.class);
                if(resultClass.getStatus()==0)
                {
                    Toast.makeText(OrderDetailActivity.this,"Order cancelled Successfully!",Toast.LENGTH_LONG).show();
                   // Intent returnIntent = new Intent();
                   // returnIntent.putExtra("ordercancelled",order);
                   // setResult(Activity.RESULT_OK,returnIntent);
                    globalProvider.setDeletedOrder(order);

                    finish();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        globalProvider.addRequest(utf8JsonRequest);
        */

    }




}
