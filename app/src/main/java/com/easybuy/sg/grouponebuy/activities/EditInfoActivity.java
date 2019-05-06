package com.easybuy.sg.grouponebuy.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
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
import java.util.HashMap;
import java.util.Map;

public class EditInfoActivity extends AppCompatActivity {

    TextView nameText, phoneText, paynowText;
    TextView addressText,emailText;
    //ImageView uploadImageIcon;
    ImageView backButton;
    ImageView bottomImage;
    GlobalProvider globalProvider;
    private static final int Code=123;
    private static final int ADDRESS_CHANGE_CODE=120;
    LinearLayout addressChangeNext;
    TextView phoneLabel,emailLabel;
    Customer customer;


    Map<String, String> params;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_info);
        nameText = (TextView) findViewById(R.id.name_text);
        emailText = (TextView) findViewById(R.id.email_text);
        phoneText = (TextView) findViewById(R.id.phone_text);


        paynowText = (TextView) findViewById(R.id.paynow_text);
        addressText=(TextView) findViewById(R.id.address_text);
        backButton=(ImageView)findViewById(R.id.back);
        phoneLabel=(TextView) findViewById(R.id.phone_label);
        emailLabel=(TextView)findViewById(R.id.email_label) ;
        addressChangeNext=(LinearLayout) findViewById(R.id.address_next);


        bottomImage=(ImageView) findViewById(R.id.bottomimage);
        params = new HashMap<>();
        globalProvider = GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        customer = Constants.getCustomer(getApplicationContext());
        nameText.setText(customer.userName);
        String phoneTextLabel=getResources().getString(R.string.phone);
        phoneLabel.setText(phoneTextLabel.substring(0,phoneTextLabel.length()-1));
        String emailTextLabel=getResources().getString(R.string.email);
        emailLabel.setText(emailTextLabel.substring(0,emailTextLabel.length()-1));
        if (customer.address != null) {


            if(Constants.getLanguage(EditInfoActivity.this).equals("english"))
            {
                addressText.setText(customer.address+" "+customer.getDistrict().getNameTertiaryEn()+" "+customer.getDistrict().getNameSecondaryEn()+" "+customer.getDistrict().getNamePrimaryEn()+" "+customer.postcode);
            }
            else
            {
                addressText.setText(customer.address+" "+customer.getDistrict().getNameTertiaryCh()+" "+customer.getDistrict().getNameSecondaryCh()+" "+customer.getDistrict().getNamePrimaryCh()+" "+customer.postcode);
            }

        }
      /*  nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                params.put("userName", nameEdit.getText().toString());

            }
        });
        */
        emailText.setText(customer.email);

        phoneText.setText(customer.phone);


     /*   phoneText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                params.put("phone",paynowEdit.getText().toString());

            }
        });
        */



        Glide.with(this).load(R.drawable.ebuygrey).fitCenter().into(bottomImage);
        addressChangeNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent=new Intent(EditInfoActivity.this,DistrictSettingActivity.class);
                Intent intent=new Intent(EditInfoActivity.this,DeliveryAddressActivity.class);
                startActivityForResult(intent,ADDRESS_CHANGE_CODE);
            }
        });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (customer.getPayNowAccount() != null) {
            paynowText.setText(customer.getPayNowAccount());


        }
        /*paynowEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                params.put("PayNowAccount", paynowEdit.getText().toString());

            }
        });
        */


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
        /*saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Constants.favouriteUrl + "/" + globalProvider.getCustomerId();
                CustomRequest customRequest = new CustomRequest(Request.Method.PATCH, url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getInt("status")==0)
                            //updateing sharedpreference
                            updateCustomer();
                            else
                                Toast.makeText(EditInfoActivity.this,R.string.update_user_unsuccessful,Toast.LENGTH_SHORT).show();
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
        */

    }
    public void onResume()
    {

        super.onResume();

    }
    public void changeInformation(View view)
    {
        int id=view.getId();
        //Log.d("idis",id+"");
        Intent intent=new Intent(EditInfoActivity.this,ChangeInformationActivity.class);


        switch(id)
        {
            case R.id.name_next:
            {
               // Log.d("nameis",R.id.name_next+"");
                intent.putExtra("changeField","name");
                break;
            }
            case R.id.email_next:
            {
                intent.putExtra("changeField","email");
                break;
            }
            case R.id.phone_next:
            {
                intent.putExtra("changeField","phone");
                break;
            }
            case R.id.paynow_next:
            {
                intent.putExtra("changeField","paynow");
                break;
            }
        }

        startActivityForResult(intent,Code);
    }

    /*
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==Code&&resultCode== Activity.RESULT_OK)
        {
          Map<String,String> map= (Map<String, String>) data.getSerializableExtra("infoupdated");
            Map.Entry<String,String> entry=  map.entrySet().iterator().next();
            String key = entry.getKey();
           // Log.d("key",key);
          switch(key)
          {
              case "userName":
              {
                 nameText.setText(entry.getValue());
                  break;

              }
              case "phone":
              {
                  phoneText.setText(entry.getValue());
                  break;
              }
              case "email":
              {
                  emailText.setText(entry.getValue());
                  break;
              }
              case "PayNowAccount":
              {
                  paynowText.setText(entry.getValue());
                  break;
              }
          }
        }
        else if(requestCode==ADDRESS_CHANGE_CODE&&resultCode==Activity.RESULT_OK)
        {
            if (Constants.getCustomer(getApplicationContext()).postcode != null) {
                Customer customer=Constants.getCustomer(getApplicationContext());


                if(Constants.getLanguage(EditInfoActivity.this).equals("english"))
                {
                    addressText.setText(customer.address+" "+customer.getDistrict().getNameTertiaryEn()+" "+customer.getDistrict().getNameSecondaryEn()+" "+customer.getDistrict().getNamePrimaryEn()+" "+customer.postcode);
                }
                else
                {
                    addressText.setText(customer.address+" "+customer.getDistrict().getNameTertiaryCh()+" "+customer.getDistrict().getNameSecondaryCh()+" "+customer.getDistrict().getNamePrimaryCh()+" "+customer.postcode);
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                      Constants.setCustomer(EditInfoActivity.this,null);
                      Constants.setCustomer(EditInfoActivity.this,res.getCustomer());
                      globalProvider.setCustomerId(null);
                      globalProvider.setCustomerId(res.getCustomer().customer_id);
                      Toast.makeText(EditInfoActivity.this,R.string.update_user_successful,Toast.LENGTH_SHORT).show();
                      new Handler().postDelayed(new Runnable() {
                          @Override
                          public void run() {
                              finish();

                          }
                      },2000);





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



            private class GenericTextWatcher implements TextWatcher {

                private View view;

                private GenericTextWatcher(View view) {
                    this.view = view;
                }

                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                public void afterTextChanged(Editable editable) {
                    String text = editable.toString();
                    switch (view.getId()) {
                        case R.id.name_text: {
                            params.put("userName", text);
                            break;
                        }
                        case R.id.phone_text:
                        {
                            params.put("phone",text);
                            break;
                        }



                        case R.id.paynow_text: {
                            params.put("PayNowAccount", text);
                           // Log.d("paynow",text);
                            break;
                        }


                    }
                }
            }


        }
