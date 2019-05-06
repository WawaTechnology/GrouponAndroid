package com.easybuy.sg.grouponebuy.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.adapter.DeliveryAddressAdapter;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.helpers.Utf8JsonRequest;
import com.easybuy.sg.grouponebuy.model.Address;
import com.easybuy.sg.grouponebuy.model.Customer;
import com.easybuy.sg.grouponebuy.model.Delivery;
import com.easybuy.sg.grouponebuy.model.Result;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class DeliveryAddressActivity extends AppCompatActivity implements DeliveryAddressAdapter.UpdateAddressInterface {

    RecyclerView recyclerView;
    Button addAddressButton;
    ImageView backButton;

    GlobalProvider globalProvider;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_address);
        globalProvider=GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        recyclerView=(RecyclerView)findViewById(R.id.recycler_address);
        addAddressButton=(Button) findViewById(R.id.add_delivery_button);
        backButton=(ImageView)findViewById(R.id.back) ;
        List<Address> addressList= Constants.getCustomer(getApplicationContext()).getAddressList();
        DeliveryAddressAdapter addressAdapter=new DeliveryAddressAdapter(DeliveryAddressActivity.this,addressList,DeliveryAddressActivity.this);
        recyclerView.setAdapter(addressAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(DeliveryAddressActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        addAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DeliveryAddressActivity.this,DistrictSettingActivity.class);
                startActivity(intent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onFinish() {

            String url = Constants.favouriteUrl + "/" + globalProvider.getCustomerId();
            Utf8JsonRequest utf8JsonRequest = new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JsonFactory jsonFactory = new JsonFactory();
                        ObjectMapper objectMapper = new ObjectMapper();
                        String result = response.toString();
                        JsonParser jsonParser = jsonFactory.createParser(result);
                        Result res = (Result) objectMapper.readValue(jsonParser, Result.class);

                        int status = res.getStatus();
                        if (status == 0) {
                            Constants.setCustomer(getApplicationContext(),null);
                            Constants.setCustomer(getApplicationContext(),res.getCustomer());
                           // Log.d("changinagmainaddress",Constants.getCustomer(getApplicationContext()).getDistrict().getNamePrimaryEn());
                           // Log.d("checkcustomeraddress",Constants.getCustomer(getApplicationContext()).getDistrict().getNameTertiaryEn());
                           // Log.d("unitnum",Constants.getCustomer(getApplicationContext()).address);
                          //  globalProvider.setCustomerId(null);
                           // globalProvider.setCustomerId(res.getCustomer().customer_id);
                            Toast.makeText(DeliveryAddressActivity.this,R.string.update_user_successful,Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Intent intent=new Intent();



                                    setResult(Activity.RESULT_OK, intent);
                                    finish();

                                }
                            },Toast.LENGTH_SHORT);







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
