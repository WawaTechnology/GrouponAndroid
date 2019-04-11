package com.easybuy.sg.grouponebuy.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.helpers.CustomRequest;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.helpers.Utf8JsonRequest;
import com.easybuy.sg.grouponebuy.model.Customer;
import com.easybuy.sg.grouponebuy.model.Result;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ChangeInformationActivity extends AppCompatActivity {
    EditText infoEdit;
    TextView infoText;
    Customer customer;
    TextView saveButton;
    TextView backButton;


    Map<String,String> map;
    GlobalProvider globalProvider;
    ImageView deleteText;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_info_activity);
        customer= Constants.getCustomer(getApplicationContext());

        map=new HashMap<>();
        deleteText=(ImageView) findViewById(R.id.delete_button);
        backButton=(TextView) findViewById(R.id.back);
        infoEdit=(EditText)findViewById(R.id.field_value);
        infoText=(TextView) findViewById(R.id.fieldChange);

        saveButton=(Button) findViewById(R.id.save);
        globalProvider=GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        saveButton.setEnabled(false);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Intent intent=getIntent();
       String value= intent.getStringExtra("changeField");
       checkValue(value);
       deleteText.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               infoEdit.setText(" ");
           }
       });

       infoEdit.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void afterTextChanged(Editable editable) {
               saveButton.setEnabled(true);



           }
       });
        infoEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(ChangeInformationActivity.this);
                } else {
                    showKeyboard(ChangeInformationActivity.this);
                }
            }
        });

       saveButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Map.Entry<String,String> entry=  map.entrySet().iterator().next();
               String key = entry.getKey();
               //String value = entry.getValue();
               map.put(key,infoEdit.getText().toString());
               String url = Constants.favouriteUrl + "/" + globalProvider.getCustomerId();
               CustomRequest customRequest = new CustomRequest(Request.Method.PATCH, url, map, new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {
                       try {
                           if(response.getInt("status")==0)
                               //updateing sharedpreference
                               updateCustomer();
                           else
                               Toast.makeText(ChangeInformationActivity.this,R.string.update_user_unsuccessful,Toast.LENGTH_SHORT).show();
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }

                   }
               }, new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       Log.d("checkerror", error.toString());

                   }
               });
               globalProvider.addRequest(customRequest);


           }
       });

    }
    private void updateCustomer() {
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
                        Constants.setCustomer(ChangeInformationActivity.this,null);
                        Constants.setCustomer(ChangeInformationActivity.this,res.getCustomer());
                        globalProvider.setCustomerId(null);
                        globalProvider.setCustomerId(res.getCustomer().customer_id);
                        Toast.makeText(ChangeInformationActivity.this,R.string.update_user_successful,Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent=new Intent();
                                intent.putExtra("infoupdated", (Serializable) map);


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


    private void checkValue(String value) {
        switch(value)
        {
            case "name":
            {
                infoText.setText("UserName");
                infoEdit.setText(customer.userName);
                map.put("userName",customer.userName);
                break;

            }
            case "phone":
            {
                infoText.setText("Phone");
                infoEdit.setText(customer.phone);
                map.put("phone",customer.phone);
                break;
            }
            case "email":
            {
                infoText.setText("Email");
                infoEdit.setText(customer.email);
                map.put("email",customer.email);
                break;
            }
            case "paynow":
            {
                infoText.setText("PayNow");
                infoEdit.setText(customer.payNowAccount);
                map.put("PayNowAccount",customer.payNowAccount);
                break;
            }


        }
        if(infoEdit.getText().length()>0)
        infoEdit.setSelection(infoEdit.getText().length());
    }
    public static void showKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            activity.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }
}
