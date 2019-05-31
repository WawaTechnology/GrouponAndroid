package com.easybuy.sg.grouponebuy.activities;

import android.content.Intent;
import android.databinding.generated.callback.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.helpers.CustomRequest;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewAddressActivity extends AppCompatActivity {
    Button submitButton;
    EditText nameTerEditText,nameSecEditText,postCodeEditText,unitNumEditText;
    GlobalProvider globalProvider;
    ImageView backButton;

public void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.new_address_activity);
    submitButton=(Button)findViewById(R.id.submit);
    nameTerEditText=(EditText)findViewById(R.id.building_name_edit);
    nameSecEditText=(EditText)findViewById(R.id.street_name_edit);
    postCodeEditText=(EditText)findViewById(R.id.postal_edittext);
    unitNumEditText=(EditText) findViewById(R.id.unit_num_edit);
    backButton= (ImageView) findViewById(R.id.back);
            globalProvider=GlobalProvider.getGlobalProviderInstance(NewAddressActivity.this);
    Intent intent=getIntent();
    postCodeEditText.setText(intent.getStringExtra("postal"));
    backButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    });
    submitButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String url= Constants.createDistrictUrl;
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("deliveryCost",9.9);
                jsonObject.put("deliveryPrice",2);
                jsonObject.put("freeDeliveryPrice",19.9);
                jsonObject.put("state",1);
                jsonObject.put("type","其他");
                jsonObject.put("sequence",1000);
                jsonObject.put("description_ch","");
                jsonObject.put("description_en","");
                jsonObject.put("namePrimary_ch","客户自创");
                jsonObject.put("namePrimary_en","Customer Made");
                jsonObject.put("postcode",Integer.parseInt(postCodeEditText.getText().toString()));
                jsonObject.put("nameSecondary_ch",nameSecEditText.getText().toString());
                jsonObject.put("nameSecondary_en",nameSecEditText.getText().toString());
                jsonObject.put("nameTertiary_ch",nameTerEditText.getText().toString());
                jsonObject.put("nameTertiary_en",nameTerEditText.getText().toString());
                JsonArray jsonArray=new JsonArray();
                jsonArray.add("5c40492fae387447c6691e87");
                jsonArray.add("5b5053980a0524071261602f");
                jsonArray.add("5c404935ae387447c6691e88");
                jsonArray.add("5b50539c0a05240712616030");
                jsonArray.add("5c404938ae387447c6691e89");
                jsonArray.add("5bbdf05bc1c1702631e4e455");
                jsonObject.put("cycle",jsonArray);
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Constants.createDistrictUrl, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status=response.getInt("status");
                            Log.d("status1",status+"");
                            if(status==0)
                            {
                                String districtId=response.getString("payload");
                                Log.d("payloaddistrict",response.getString("payload"));

                                String userUrl=Constants.favouriteUrl+"/"+Constants.getCustomer(NewAddressActivity.this).customer_id;
                                Map<String,String> map=new HashMap<>();
                                map.put("address",unitNumEditText.getText().toString());
                                CustomRequest customRequest=new CustomRequest(Request.Method.PATCH, userUrl, map, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            Log.d("status2",response.getInt("status")+"");
                                            if(response.getInt("status")==0)
                                            {
                                                String url=Constants.changeDistrictUrl+Constants.getCustomer(NewAddressActivity.this).customer_id;
                                                Map<String,String> map=new HashMap<>();
                                                map.put("district",districtId);
                                                map.put("postcode",postCodeEditText.getText().toString());
                                                CustomRequest customRequest1=new CustomRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        try {
                                                            Log.d("status3",response.getInt("status")+"");
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        Intent intent = new Intent(NewAddressActivity.this, MainActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);

                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {

                                                    }});
                                                globalProvider.addRequest(customRequest1);


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
                globalProvider.addRequest(jsonObjectRequest);






            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    });

}
}
