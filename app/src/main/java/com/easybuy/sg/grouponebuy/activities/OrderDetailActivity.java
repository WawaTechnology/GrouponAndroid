package com.easybuy.sg.grouponebuy.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
    double eCoin;
    ImageView copyImageView;

   // LinearLayout netBalanceLayout,subTotalLayout;
    TextView subTotalLabel,netBalanceLabel;

    TextView subTotalText,netBalanceText;
    GlobalProvider globalProvider;
    boolean editClicked;
    String lang;
    // View Types
    private static final int NO_EDIT = 0;
    private static final int EDIT = 1;
    public double changedtotal=0.0;
    private double totalDouble=0.0;
    private double deliveryPriceDouble=0.0;
    private ImageView backButton;
    boolean orderEdited;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_trial);
        eCoin=Constants.getCustomer(getApplicationContext()).getRefund().getECoins();

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
        totalPriceText=(TextView) findViewById(R.id.total_amt);
        cancelButton=(Button) findViewById(R.id.cancel);
        editOrderButton=(Button) findViewById(R.id.edit_order);
        backButton=(ImageView) findViewById(R.id.back);
        copyImageView=(ImageView) findViewById(R.id.copy_img);
        globalProvider=GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        Intent intent=getIntent();
        order=(Order)intent.getSerializableExtra("Order");
        String orderId=order.getId();
        Log.d("checkorderid",orderId);
        orderDetailAdapter=new OrderDetailAdapter(this,productList,this,order.getState());
        orderListRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        orderListRecycler.setLayoutManager(linearLayoutManager);
        orderListRecycler.setAdapter(orderDetailAdapter);
        String deliveryDate=  intent.getStringExtra("deliverydate");
        String orderDateTime= intent.getStringExtra("orderDateTime");
        String price = String.format("%.2f", order.getTotalPrice());
        subTotalText.setText("$"+price);
        changedtotal=order.getTotalPrice();
        orderIdText.setText(order.getOrderCode());
        orderDateText.setText(order.getOrderDate());
        //If the order status is processing,shipping or completed,order can not be edited and cancelled.
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
            editOrderButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
        }
        else if(order.getState().equalsIgnoreCase("waiting"))
        {
            statusText.setText(getString(R.string.waiting));
        }
        //If order has used ecoins(refundCostOrder), display it in netbalance TextView;subtract that value with the  price
        if(order.getRefundCostOrder()>0)
        {
            netBalanceLabel.setVisibility(View.VISIBLE);
            String totalNet=String.format("%.2f",order.getRefundCostOrder());
            netBalanceText.setText("-$"+totalNet);
            double val=order.getTotalPrice()-order.getRefundCostOrder();
            price = String.format("%.2f", val);
        }
        else
        {
            netBalanceLabel.setVisibility(View.GONE);
            netBalanceText.setVisibility(View.GONE);
        }
        // Display deliveryPrice even when it is 0;add it to the price
        if(order.getDeliveryPrice()==null)
        {
            order.setDeliveryPrice(0.0);
        }
            delivery_price.setText("$"+order.getDeliveryPrice());
           double val= Double.parseDouble(price)+order.getDeliveryPrice();
           price=String.format("%.2f",val);
           //TotalPrice =Order.getTotalPrice()-Order.getRefundPrice()+Order.deliveryPrice()
        totalPriceText.setText("$"+price);
        //  checking the payment status of order
        //If the value (totalPrice) is 0 ,which means that customer has used his ecoins to pay,we dont need to check payment status as it is paid
        if(val==0.0)
        {
            paymentStatusText.setText(getString(R.string.paid));
            paymentStatusText.setTextColor(getResources().getColor(R.color.green));
        }
        else
        checkOrderPaymentStatus(orderId);
        copyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("orderId copied", order.getOrderCode());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(OrderDetailActivity.this, "Saved to clip board", Toast.LENGTH_SHORT).show();
            }
        });
        // Checking If we need to refund anything to customer,cases where user has paid more than the total amount
        //If the Order state is processing or waiting,we will not check
        if(order.getState().equalsIgnoreCase("processing")||order.getState().equalsIgnoreCase("waiting"))
        {
            statusText.setTextColor(getResources().getColor(R.color.red));

        }
        else {
            /* We will check the sub value in customer's record,if any of the sub's order id is equal to orderId,It means that user has paid more and we have refunded
               Otherwise we will check if customer has paid more than the price(order.getTotalPriceActual() > deliveryPrice+priceOrder - order.getRefundCostOrder()
               If he has paid more,we will put the status paid and refund status as not refunded yet
             */

            if (Constants.getCustomer(OrderDetailActivity.this).getRefund() != null && Constants.getCustomer(OrderDetailActivity.this).getRefund().getSub() != null) {
                List<Sub> subList = Constants.getCustomer(OrderDetailActivity.this).getRefund().getSub();
                boolean found = false;
                Double coin = 0.0;
                for (Sub sub : subList) {
                    if (sub.getOrder().equals(orderId)) {
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
                    double priceOrder=order.getTotalPrice()+order.getDeliveryPrice();
                    if (order.getTotalPriceActual() > priceOrder - order.getRefundCostOrder()) {
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
                //If the edit Button Text is Edit, we will make the products quantity editable and change the text of button to save
                if(editOrderButton.getText().toString().equalsIgnoreCase(getString(R.string.edit))) {
                    OrderDetailAdapter.editClicked = true;
                    orderDetailAdapter.notifyDataSetChanged();
                    editOrderButton.setText(getString(R.string.save));
                }
                else
                {
                    //If edit Button is Save , we will change the text to Edit and  save the Order
                    saveOrder();
                    editOrderButton.setText(getString(R.string.edit));
                    OrderDetailAdapter.editClicked=false;
                    orderDetailAdapter.notifyDataSetChanged();

                }




            }
        });
       paymentStatusText.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               //If the payment status is unpaid,on Clicked we will display an alert which shows mode of payment
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
                final AlertDialog alertDialog= new AlertDialog.Builder(OrderDetailActivity.this).setTitle(getString(R.string.alert)).setMessage(getString(R.string.cancel_order_alert)).setPositiveButton(getString(R.string.confm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cancelButton.setEnabled(false);
                        cancelOrder();
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                }).create();
                alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.grey));
                    }
                });
                alertDialog.show();



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

  private void checkOrderPaymentStatus(String id) {
        String url= Constants.editOrderUrl+id;
        Log.d("checkurl",url);
       Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonFactory jsonFactory = new JsonFactory();
            try {
                JsonParser jsonParser = jsonFactory.createParser(response);
                SingleOrderResult resultOrder = (SingleOrderResult) objectMapper.readValue(jsonParser, SingleOrderResult.class);
                OrderPayload orderPayload=resultOrder.getPayload();
                //If Order's totalPriceActual value is greater than 0,it means user has paid else unpaid
                if(orderPayload.getTotalPriceActual()!=null&&orderPayload.getTotalPriceActual()>0)
                {
                    paymentStatusText.setText(getString(R.string.paid));
                    paymentStatusText.setTextColor(getResources().getColor(R.color.green));
                }
                else {
                    paymentStatusText.setText(getString(R.string.unpaid));
                    paymentStatusText.setTextColor(getResources().getColor(R.color.orange));

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


    private void saveOrder()  {
        // price should always be greater than or equal to deliveryCost
        if(changedtotal>=Constants.getCustomer(OrderDetailActivity.this).getDistrict().getDeliveryCost()) {
            String url = Constants.editOrderUrl + order.getId();
            JSONObject jsonObject = new JSONObject();
            JsonObjectRequest jsonObjectRequest = null;
            try {
                jsonObject.put("totalPrice", changedtotal);
                    jsonObject.put("refundCost", order.getRefundCostOrder());
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
                    jsonObject1.put("ifWeigh",productInfo.getIfWeigh());
                    jsonObject4.put("productInfo", jsonObject1);
                    jsonObject4.put("quantity", productOrder.getQuantity() + "");
                    jsonArray.put(jsonObject4);
                }
                jsonObject.put("productList", jsonArray);
                jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //status will be 0,if order is successfully placed and it will be 1 if some items are out of stock
                            if (response.getInt("status") == 0) {
                               Toast.makeText(OrderDetailActivity.this,getString(R.string.order_updated_success), Toast.LENGTH_LONG).show();
                                orderEdited=true;


                            }

                            else if(response.getInt("status")==1)
                            {
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
                                    // Display alert if any item is out of stock list
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
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderDetailActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_listalert, null, false);
        ListView lv = (ListView) convertView.findViewById(R.id.lvw);
        final CustomAlertAdapter adapter = new CustomAlertAdapter(productStockList, OrderDetailActivity.this);
        alertDialog.setTitle(getResources().getString(R.string.sorry_insufficient));
        alertDialog.setView(convertView);
        // setCancelable False makes the alert unignorable,you need to respond
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        alertDialog.setAdapter(adapter,null);
        alertDialog.show();
    }
    @Override
    public void onQuantityChanged(ProductOrderList product, int quantity,String operation) {
        //changedtotal is subtotal of all the products
        //totalDouble is the totalAmount to be paid by customer(subtotal+deliveryprrice-refundcost)

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
        subTotalText.setText("$"+total);
        //calculate deliveryCost,if subtotal(changedtotal)>= freedelivery thn delivery cost is 0,otherwise add the delivery price to totalprice(totalDouble)
        if(changedtotal>=Constants.getCustomer(getApplicationContext()).getDistrict().getFreeDeliveryPrice())
        {
            delivery_price.setText("$"+0.0);

        }
        else
        {
            deliveryPriceDouble=Constants.getCustomer(getApplicationContext()).getDistrict().getDeliveryPrice();
            totalDouble=totalDouble+deliveryPriceDouble;
            delivery_price.setText("$"+Constants.getCustomer(getApplicationContext()).getDistrict().getDeliveryPrice());

        }

        //subtract refundCost;In case user still have ecoin even after using some in order,,we will recalculate the refundvalue(order.getRefundCostOrder()+eCoin)
            double refundTotal=order.getRefundCostOrder()+eCoin;
            refundTotal=Double.parseDouble(new DecimalFormat("##.##").format(refundTotal));
            //if totalamt>=refundtotal means all ecoins are used,so we will set ecoin to be 0
            //if it is less than refund total,means ecoind are not fully used  we will recaluclte it(efundTotal-totalDouble),refundTotal cannot be more than thetotal Price
            //so It will be same as totalPrice(totalDouble).In this case total price is 0.0

            if(totalDouble>=refundTotal)
            {
                eCoin=0;
                order.setRefundCostOrder(refundTotal);
                totalDouble=totalDouble-order.getRefundCostOrder();
                netBalanceText.setText("-$"+order.getRefundCostOrder());
            }
            else
            {

                eCoin=refundTotal-totalDouble;
                refundTotal=totalDouble;
                order.setRefundCostOrder(refundTotal);
                totalDouble=totalDouble-order.getRefundCostOrder();
                netBalanceText.setText("-$"+order.getRefundCostOrder());
            }
        String totalamt=String.format("%.2f",totalDouble);
        totalPriceText.setText("$"+totalamt);
    }


    private void cancelOrder() {
        Map<String,String> params=new HashMap<>();
        params.put("state","canceled");
        String url= Constants.deleteOrderUrl+order.getId();
        CustomRequest customRequest=new CustomRequest(Request.Method.PATCH, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                ResultClass resultClass= gson.fromJson(response.toString() , ResultClass.class);
                if(resultClass.getStatus()==0)
                {
                    Toast.makeText(OrderDetailActivity.this,getString(R.string.order_cancelled_success),Toast.LENGTH_LONG).show();
                    cancelButton.setEnabled(true);
                    finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        globalProvider.addRequest(customRequest);


    }




}
