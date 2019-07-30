package com.easybuy.sg.grouponebuy.activities;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.helpers.CustomRequest;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.google.gson.JsonArray;

import org.json.JSONArray;
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
            Log.d("clickedhere","here");
            if(TextUtils.isEmpty(nameSecEditText.getText().toString())||TextUtils.isEmpty(nameTerEditText.getText().toString())||TextUtils.isEmpty(postCodeEditText.getText().toString()))
            {
                if(TextUtils.isEmpty(nameSecEditText.getText().toString()))
                {
                    nameSecEditText.setError("Please Street Name");
                }
                if(TextUtils.isEmpty(nameTerEditText.getText().toString()))
                {
                    nameTerEditText.setError("Please enter required field");
                }
                if(TextUtils.isEmpty(postCodeEditText.getText().toString()))
                {
                    postCodeEditText.setError("Please enter Postal Code");
                }

                return;
            }
           // String url= Constants.createDistrictUrl;
            JSONObject jsonObject=new JSONObject();
            try {

                JSONArray jsonArray=new JSONArray();
                jsonArray.put("5c40492fae387447c6691e87");
                jsonArray.put("5b5053980a0524071261602f");
                jsonArray.put("5c404935ae387447c6691e88");
                jsonArray.put("5b50539c0a05240712616030");
                jsonArray.put("5c404938ae387447c6691e89");
                jsonArray.put("5bbdf05bc1c1702631e4e455");
                jsonObject.put("cycle",jsonArray);
                jsonObject.put("deliveryCost",9.9);
                jsonObject.put("deliveryPrice",2);
                jsonObject.put("description_ch","");
                jsonObject.put("description_en","");
                jsonObject.put("freeDeliveryPrice",19.9);
                jsonObject.put("namePrimary_ch","客户自创");
                jsonObject.put("namePrimary_en","Customer Made");
                jsonObject.put("nameSecondary_ch",nameSecEditText.getText().toString());
                jsonObject.put("nameSecondary_en",nameSecEditText.getText().toString());
                jsonObject.put("nameTertiary_ch",nameTerEditText.getText().toString());
                jsonObject.put("nameTertiary_en",nameTerEditText.getText().toString());
                jsonObject.put("postcode",postCodeEditText.getText().toString());
                jsonObject.put("sequence",1000);
                jsonObject.put("state",1);
                jsonObject.put("type","其他");






                Log.d("checkurl",Constants.createDistrictUrl);

                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Constants.createDistrictUrl, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("checkresponse",response.toString());
                            int status=response.getInt("status");
                            Log.d("status1",status+"");
                            if(status==0)
                            {
                                String districtId=response.getString("payload");
                                Log.d("payloaddistrict",response.getString("payload"));

                                String userUrl=Constants.favouriteUrl+"/"+Constants.getCustomer(NewAddressActivity.this).customer_id;
                                Map<String,String> map=new HashMap<>();
                                if(TextUtils.isEmpty(unitNumEditText.getText().toString()))
                                {
                                    map.put("address","*");
                                }
                                else
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
                                                        Log.d("errorvolley",error.getMessage());

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
                                        String message= globalProvider.getErrorMessage(error);
                                        Toast.makeText(NewAddressActivity.this,message,Toast.LENGTH_LONG).show();

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
                        if(error!=null)
                        Log.d("checkerrorvoleey",error.toString()+"");
                        String message= globalProvider.getErrorMessage(error);
                        Toast.makeText(NewAddressActivity.this,message,Toast.LENGTH_LONG).show();

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
