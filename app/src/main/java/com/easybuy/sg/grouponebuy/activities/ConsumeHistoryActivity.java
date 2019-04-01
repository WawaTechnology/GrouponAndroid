package com.easybuy.sg.grouponebuy.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.adapter.ConsumeAdapter;

import com.easybuy.sg.grouponebuy.helpers.CustomRequest;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.helpers.WithdrawDialogListener;
import com.easybuy.sg.grouponebuy.model.Consume;
import com.easybuy.sg.grouponebuy.model.ConsumePayload;
import com.easybuy.sg.grouponebuy.model.ConsumeResult;
import com.easybuy.sg.grouponebuy.model.Customer;
import com.easybuy.sg.grouponebuy.model.Refund;
import com.easybuy.sg.grouponebuy.model.RefundCostOrder;
import com.easybuy.sg.grouponebuy.model.RefundOrder;
import com.easybuy.sg.grouponebuy.model.Result;
import com.easybuy.sg.grouponebuy.model.Withdraw;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ConsumeHistoryActivity extends AppCompatActivity implements WithdrawDialogListener  {
    TabLayout tablayout;
    List<Consume> consumeList;
    RecyclerView consumeRecyclerView;
    ConsumeAdapter consumeAdapter;
    GlobalProvider globalProvider;
    ImageView backButton;
    TextView withdrawAmount;
    Button withdrawButton;
    Customer customer;




    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consume_history);
        globalProvider=GlobalProvider.getGlobalProviderInstance(this);
        customer=Constants.getCustomer(getApplicationContext());
        consumeRecyclerView=(RecyclerView) findViewById(R.id.consumer_recycler);
        backButton=(ImageView) findViewById(R.id.back);
        withdrawAmount=(TextView) findViewById(R.id.withdraw_amount);
        withdrawButton=(Button) findViewById(R.id.withdraw_button);
        consumeList=new ArrayList<>();
        tablayout = (TabLayout) findViewById(R.id.tab_layout);
        tablayout.addTab(tablayout.newTab().setText(getText(R.string.all_tab)), 0);
        tablayout.addTab(tablayout.newTab().setText(getText(R.string.refund_tab)), 1);
        tablayout.addTab(tablayout.newTab().setText(getText(R.string.consume_tab)), 2);
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals(getString(R.string.all_tab))) {

                        getConsumeList();





                }
                else if(tab.getText().equals(getString(R.string.refund_tab)))
                {
                    consumeList.clear();
                   List<Consume> refundList= globalProvider.consumeList;
                   for(Consume consume:refundList)
                   {
                       if(consume.getType().equalsIgnoreCase("refundPending"))
                       {
                          consumeList.add(consume) ;
                       }
                   }
                   consumeAdapter.notifyDataSetChanged();

                }
                else
                {
                    consumeList.clear();
                    List<Consume> consumerList= globalProvider.consumeList;
                    for(Consume consume:consumerList)
                    {
                        if(consume.getType().equalsIgnoreCase("refundDone"))
                        {
                            consumeList.add(consume);
                        }
                    }
                    consumeAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

       String ecoins= String.format("%.2f", customer.getRefund().getECoins());

        withdrawAmount.setText("$ "+ecoins);
        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(customer.getRefund()==null||customer.getRefund().getECoins()<=0)
                {


                }
                else
                {
                  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment prev = getSupportFragmentManager().findFragmentByTag("withdraw");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);

                    android.support.v4.app.DialogFragment dialogFragment = new ConsumeHistoryActivity.MyDialogFragment();






                    dialogFragment.show(ft,"withdraw");

                }
            }
        });
        consumeAdapter=new ConsumeAdapter(this,consumeList);
        consumeRecyclerView.setAdapter(consumeAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayout.VERTICAL,false);
        consumeRecyclerView.setLayoutManager(linearLayoutManager);

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






    }
    public void onResume()
    {
        String ecoins=String.format("%.2f", customer.getRefund().getECoins());

        withdrawAmount.setText("$ "+ecoins);
            getConsumeList();
        super.onResume();
    }

    private void getConsumeList() {
        consumeList.clear();
        String url= Constants.orderRefundUrl;
        Map<String, String> params = new HashMap<>();

        params.put("userID",Constants.getCustomer(this).customer_id );
        CustomRequest customRequest=new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                    // Log.d("responsevolley", response.getString("token"));
                    JsonFactory jsonFactory = new JsonFactory();
                    ObjectMapper objectMapper = new ObjectMapper();

                    String result=response.toString();
                try {


                    JsonParser jsonParser = jsonFactory.createParser(result);
                    ConsumeResult consumeResult =(ConsumeResult) objectMapper.readValue(jsonParser, ConsumeResult.class);
                    if(consumeResult.getStatus()==0)
                    {
                      ConsumePayload consumePayload= consumeResult.getPayload();
                      List<RefundCostOrder> refundCostOrderList=consumePayload.getRefundCostOrders();
                      List<RefundOrder> refundOrderList=consumePayload.getRefundOrders();
                      List<Withdraw> withdrawList=consumePayload.getWithdraws();
                      for(RefundCostOrder refundCostOrder:refundCostOrderList) {
                          Consume consumer = new Consume(refundCostOrder.getOrder(), refundCostOrder.getTime(), "refundDone", refundCostOrder.getRefundCost());
                          consumeList.add(consumer);
                      }
                        for(RefundOrder refundOrder:refundOrderList) {
                            Consume consumer = new Consume(refundOrder.getOrder(), refundOrder.getTime(), "refundPending", refundOrder.getRefund());
                            consumeList.add(consumer);
                        }
                        for(Withdraw withdraw:withdrawList)
                        {
                            Consume consume=new Consume(null,withdraw.getTime(),"withdraw"+withdraw.getStatus(),withdraw.getWithdraw());
                            consumeList.add(consume);
                        }
                        Collections.sort(consumeList, new Comparator<Consume>() {
                            public int compare(Consume o1, Consume o2) {
                                if (o1.getDate() == null || o2.getDate() == null)
                                    return 0;
                                else
                                {
                                    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                    SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                                    Date d = null;
                                    Date d1=null;
                                    try {
                                        d = input.parse(o1.getDate());
                                        d1 = input.parse(o2.getDate());
                                        String formatted = output.format(d);
                                        String formatted1 = output.format(d1);
                                        d =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(formatted);
                                        d1=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(formatted1);
                                       return (d.getTime() > d1.getTime() ? -1 : 1);





                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                return 0;
                            }





                        });

                        consumeAdapter.notifyDataSetChanged();

                        globalProvider.consumeList.clear();
                        globalProvider.consumeList.addAll(consumeList);

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

            }
        });
        globalProvider.addRequest(customRequest);


    }

    @Override
    public void onFinishWithdrawDialog(double amount) {
        getConsumeList();
        Double amountN=customer.getRefund().getECoins()-amount;
        String price = String.format("%.2f", amountN);
        withdrawAmount.setText("$ "+price);


    }

    public static class MyDialogFragment extends DialogFragment {
        EditText amountText;
        EditText accountText;
        Customer customer;
        GlobalProvider globalProvider;
        TextView allButton;

        Button okButton;

        WithdrawDialogListener dialogListener;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            customer=Constants.getCustomer(getContext());
            globalProvider=GlobalProvider.getGlobalProviderInstance(getContext());
        }
        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            // Verify that the host activity implements the callback interface
            try {
                // Instantiate the EditNameDialogListener so we can send events to the host
                dialogListener = (WithdrawDialogListener) context;
            } catch (ClassCastException e) {
                // The activity doesn't implement the interface, throw exception
                throw new ClassCastException(context.toString()
                        + " must implement EditNameDialogListener");
            }
        }
        public MyDialogFragment()
        {

        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);

            // request a window without the title
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            return dialog;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.consume_dialog,container,false);
            amountText=view.findViewById(R.id.amt);
            accountText=view.findViewById(R.id.account);
            allButton=view.findViewById(R.id.all_button);
            okButton=view.findViewById(R.id.ok);
            allButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    amountText.setText(customer.getRefund().getECoins()+"");
                }
            });
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(amountText.getText().toString().isEmpty())
                    {
                        Toast.makeText(getContext(),getContext().getString(R.string.enter_amount),Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(accountText.getText().toString().isEmpty())
                    {
                        Toast.makeText(getContext(),getContext().getString(R.string.enter_account),Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String amount=amountText.getText().toString();
                    final Double amountDouble=Double.parseDouble(amount);


                    if(amountDouble>customer.getRefund().getECoins())
                    {
                        Toast.makeText(getContext(),getContext().getString(R.string.less_ecoin),Toast.LENGTH_SHORT).show();
                        dismiss();
                        return;
                    }

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("user",customer.customer_id);

                        jsonObject.put("withdraw",amountDouble);
                        jsonObject.put("account",accountText.getText().toString());
                        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Constants.withdrawUrl, jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("res",response.toString());
                                try {
                                    int status = response.getInt("status");
                                    if(status==0)
                                    {
                                        dialogListener.onFinishWithdrawDialog(amountDouble);
                                        Toast.makeText(getContext(),getContext().getString(R.string.withdraw_successful),Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                dismiss();


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dismiss();

                            }
                        });
                        globalProvider.addRequest(jsonObjectRequest);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                }
            });
            return view;

        }


    }



}
