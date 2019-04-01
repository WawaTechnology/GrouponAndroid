package com.easybuy.sg.grouponebuy.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private static final int PICK_PHOTO = 123;
    EditText nameEdit, phoneEdit, paynowEdit;
    TextView postcodeText, unitNoText,addressText,emailText;
    //ImageView uploadImageIcon;
    ImageView backButton;
    ImageView bottomImage;
    GlobalProvider globalProvider;
    Button saveButton,updateAddressButton;
    Map<String, String> params;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_info);
        params = new HashMap<>();
        nameEdit = (EditText) findViewById(R.id.name_edit);
        emailText = (TextView) findViewById(R.id.email_text);
        phoneEdit = (EditText) findViewById(R.id.phone_edit);
        postcodeText = (TextView) findViewById(R.id.postcode_text);
        unitNoText = (TextView) findViewById(R.id.unitno_text);
        paynowEdit = (EditText) findViewById(R.id.paynow_edit);
        addressText=(TextView) findViewById(R.id.address_text);
        backButton=(ImageView)findViewById(R.id.back);
        saveButton = (Button) findViewById(R.id.save);
        updateAddressButton=(Button) findViewById(R.id.update_address);
        bottomImage=(ImageView) findViewById(R.id.bottomimage);
        globalProvider = GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        Customer customer = Constants.getCustomer(getApplicationContext());
        nameEdit.setText(customer.userName);
        nameEdit.addTextChangedListener(new TextWatcher() {
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
        emailText.setText(customer.email);

        phoneEdit.setText(customer.phone);
        //plcing the edit cursor at the end of phoneNumber
       /* phoneEdit.post(new Runnable() {
            @Override
            public void run() {
                phoneEdit.setSelection(phoneEdit.getText().length());

            }
        });
        */

        phoneEdit.addTextChangedListener(new TextWatcher() {
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
        postcodeText.setText(customer.postcode);
        if (customer.address != null) {
            unitNoText.setText(customer.address);
            if(Constants.getLanguage(EditInfoActivity.this).equals("english"))
            {
                addressText.setText(customer.getDistrict().getNameTertiaryEn()+" "+customer.getDistrict().getNameSecondaryEn()+" "+customer.getDistrict().getNamePrimaryEn());
            }
            else
            {
                addressText.setText(customer.getDistrict().getNameTertiaryCh()+" "+customer.getDistrict().getNameSecondaryCh()+" "+customer.getDistrict().getNamePrimaryCh());
            }

        }

        Glide.with(this).load(R.drawable.ebuygrey).fitCenter().into(bottomImage);

        updateAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditInfoActivity.this,DistrictSettingActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (customer.getPayNowAccount() != null) {
            paynowEdit.setText(customer.getPayNowAccount());


        }
        paynowEdit.addTextChangedListener(new TextWatcher() {
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
        saveButton.setOnClickListener(new View.OnClickListener() {
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
                        case R.id.name_edit: {
                            params.put("userName", text);
                            break;
                        }
                        case R.id.phone_edit:
                        {
                            params.put("phone",text);
                            break;
                        }



                        case R.id.paynow_edit: {
                            params.put("PayNowAccount", text);
                            Log.d("paynow",text);
                            break;
                        }


                    }
                }
            }

        }
