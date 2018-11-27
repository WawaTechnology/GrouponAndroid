package com.example.unsan.grouponebuy.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.unsan.grouponebuy.R;
import com.example.unsan.grouponebuy.helpers.CustomRequest;
import com.example.unsan.grouponebuy.helpers.GlobalProvider;
import com.example.unsan.grouponebuy.model.Customer;
import com.example.unsan.grouponebuy.model.Cycle;
import com.example.unsan.grouponebuy.model.District;
import com.example.unsan.grouponebuy.model.ProductList;
import com.example.unsan.grouponebuy.model.ResultDistrict;
import com.example.unsan.grouponebuy.network.Constants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistrictSettingActivity extends AppCompatActivity {
    TextView resultPostalText, pscode, openResultTextView;
    Button applyButton;
    EditText blkEditText, districtEditText,postalTextView;
    GlobalProvider globalProvider;

    android.support.v7.widget.SearchView searchView;
    String postalcode;
    TextView addressText,cycleText,deliveryText;
    LinearLayout deliveryTimeLayout;
    LinearLayout notOpenLayout,openLayOut;

    TextView unitNumberText;
    Button bindingButton;
    private String districtId;
    EditText unitNumEdit;
    boolean hasUnit;
    boolean postCodeChanged;

    ImageView backButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_districtsetting);
        globalProvider=GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        backButton=(ImageView)findViewById(R.id.back);
        if(globalProvider.getCustomer()!=null)
        {
            if(globalProvider.getCustomer().getAddress()!=null&&globalProvider.getCustomer().getAddress().length()>0)
            {
                hasUnit=true;
            }
        }
        Log.d("hasUnitcheck",hasUnit+"");

        postalTextView = (EditText) findViewById(R.id.postal);
        resultPostalText = (TextView) findViewById(R.id.result_postal);


        pscode = (TextView) findViewById(R.id.pscode);
        openResultTextView = (TextView) findViewById(R.id.open_result);
        unitNumberText=(TextView) findViewById(R.id.unit_num);
        bindingButton=(Button) findViewById(R.id.bindingbutton);
        unitNumEdit=(EditText) findViewById(R.id.unit_num_edit);

        applyButton = (Button) findViewById(R.id.apply);
        blkEditText = (EditText) findViewById(R.id.blkres);
        districtEditText = (EditText) findViewById(R.id.dsres);

        addressText=(TextView) findViewById(R.id.address);
        cycleText=(TextView) findViewById(R.id.cycle);
        deliveryText=(TextView) findViewById(R.id.deliverytext);
        notOpenLayout=(LinearLayout) findViewById(R.id.notopen_fragment);
        openLayOut=(LinearLayout) findViewById(R.id.open_fragment);


        deliveryTimeLayout=(LinearLayout) findViewById(R.id.deliverytimelayout);
        searchView=(android.support.v7.widget.SearchView) findViewById(R.id.searchview);




        Intent intent = getIntent();
        postalcode = intent.getStringExtra("postal");
        postCodeChanged=intent.getBooleanExtra("postCodeChanged",false);
        if(postalcode!=null||globalProvider.getCustomer()!=null) {
            if(postalcode==null) {
                postalcode = globalProvider.getCustomer().postcode;
            }

            postalTextView.setText(postalcode);
            checkPostalStatus(postalcode);
            String resultString = "The result of searching of :" + postalcode;
            resultPostalText.setText(resultString);

        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
     



        postalTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(postalTextView.getText()))
                {
                    postalTextView.setError("Please enter district!");
                    resultPostalText.setVisibility(View.GONE);
                    notOpenLayout.setVisibility(View.GONE);
                    openLayOut.setVisibility(View.GONE);
                }
                else {
                    resultPostalText.setVisibility(View.VISIBLE);
                    postalcode = postalTextView.getText().toString();
                    checkPostalStatus(postalcode);

                    String resultString = "The result of searching of :" + postalcode;
                    resultPostalText.setText(resultString);
                    pscode.setText(postalcode);
                }



            }
        });
        postalTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        bindingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (globalProvider.isLogin()) {
                    if (TextUtils.isEmpty(unitNumEdit.getText())) {
                        unitNumEdit.setError("Please enter unitNumber");
                        return;

                    }

                    // String url="http://192.168.1.70:3000/users/binding/"+globalProvider.getCustomer().customer_id;
                    String url = Constants.changeDistrictUrl + globalProvider.getCustomer().customer_id;
                    Log.d("checkurl", url);
                    final Map<String, String> params = new HashMap<>();

                    params.put("district", districtId);
                    Log.d("checldistid", districtId);
                    params.put("postcode", postalcode);
                    //PATCH REQUEST NOT SUPPORTED PRE LOLIPOP
                    CustomRequest customRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("checkbinding", response.toString());
                            try {
                                int status = response.getInt("status");
                                if (status == 0) {
                                    String url = Constants.favouriteUrl + "/" + globalProvider.getCustomer().customer_id;
                                    Log.d("getu", url);
                                    Map<String, String> param = new HashMap<>();
                                    Log.d("unitnum", unitNumEdit.getText().toString());
                                    param.put("address", unitNumEdit.getText().toString());
                                    CustomRequest unitChangeRequest = new CustomRequest(Request.Method.PATCH, url, param, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d("checlres", response.toString());
                                            try {
                                                if (response.getInt("status") == 0) {
                                                    Intent intent = new Intent(DistrictSettingActivity.this, MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("bindingerror", error.toString());

                                        }
                                    });

                                    globalProvider.addRequest(unitChangeRequest);

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

            else
            {
                Toast.makeText(DistrictSettingActivity.this,"You need to login first!",Toast.LENGTH_LONG).show();
            }
        }});




        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("searchviewclicked","yes");
                if(TextUtils.isEmpty(postalTextView.getText()))
                {
                    postalTextView.setError("Please enter district!");
                    resultPostalText.setVisibility(View.GONE);
                    notOpenLayout.setVisibility(View.GONE);
                    openLayOut.setVisibility(View.GONE);
                }
                else {
                    postalcode = postalTextView.getText().toString();
                    resultPostalText.setVisibility(View.VISIBLE);
                    checkPostalStatus(postalcode);

                    String resultString = "The result of searching of :" + postalcode;
                    resultPostalText.setText(resultString);
                    pscode.setText(postalcode);
                }


            }
        });
        if(TextUtils.isEmpty(postalTextView.getText()))
        {
            resultPostalText.setVisibility(View.GONE);
            notOpenLayout.setVisibility(View.GONE);
            openLayOut.setVisibility(View.GONE);

        }




        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (blkEditText.getText().toString().length() > 0 && districtEditText.getText().toString().length() > 0) {
                    String url=Constants.applyDistrictUrl;
                    String address=districtEditText.getText().toString()+" "+blkEditText.getText().toString();
                    Map<String,String> params=new HashMap<>();
                    params.put("postcode",postalcode);
                    params.put("name",address);
                    params.put("applicant",globalProvider.getCustomer().customer_id);
                    CustomRequest customRequest=new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("applyresponse",response.toString());
                            new AlertDialog.Builder(DistrictSettingActivity.this).setView(R.layout.custom_dialoglayout).setPositiveButton(new String("OK"), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {


                                    Intent intent = new Intent(DistrictSettingActivity.this, MainActivity.class);

                                    startActivity(intent);
                                }
                            }).create().show();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    globalProvider.addRequest(customRequest);



                    //Todo add dialog
                } else
                {
                    if(TextUtils.isEmpty(blkEditText.getText()))
                    {
                        blkEditText.setError("Enter block");
                    }
                    if(TextUtils.isEmpty(districtEditText.getText()))
                    {
                        districtEditText.setError("Enter district");
                    }
                }


            }
        });
    }

    private void checkPostalStatus(final String postalcode) {
        Map<String,String> params=new HashMap<>();
        params.put("postcode",postalcode);
        CustomRequest customRequest=new CustomRequest(Request.Method.POST, Constants.districtUrl, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("getre",response.toString());

                    int strState=response.getInt("status");
                    Log.d("mystate",strState+"");
                    if(strState==2)
                    {
                        openLayOut.setVisibility(View.GONE);
                        notOpenLayout.setVisibility(View.VISIBLE);


                        openResultTextView.setText("District not open! Please Apply!");
                        openResultTextView.setTextColor(getResources().getColor(R.color.red));

                        pscode.setText(postalcode);
                    }
                    else if(strState==0)
                    {

                        ObjectMapper objectMapper=new ObjectMapper();
                        JsonFactory jsonFactory = new JsonFactory();
                        try {
                            JsonParser jsonParser = jsonFactory.createParser(response.toString());
                           ResultDistrict resultDistrict = (ResultDistrict) objectMapper.readValue(jsonParser, ResultDistrict.class);
                           Log.d("checkdis",resultDistrict.getPayload().getNamePrimaryEn());

                          District district= resultDistrict.getPayload();

                         //todo customer add district
                          districtId=district.getId();
                            openLayOut.setVisibility(View.VISIBLE);
                            notOpenLayout.setVisibility(View.GONE);
                            List<String> timingList=new ArrayList();
                            for(Cycle cycle:district.getCycle())
                            {
                               int week= cycle.getWeek();
                               Log.d("checkwe",week+"");
                               String weekDay=globalProvider.deliveryTiming.get(week);
                               String duration=cycle.getDuration();
                               timingList.add(weekDay+" "+duration);

                            }

                            openResultTextView.setTextColor(getResources().getColor(R.color.green));
                            String result="State:   Open";
                                    openResultTextView.setText(result);
                                    String address="Address:   "+district.getNamePrimaryEn()+" - "+district.getNameSecondaryEn()+" - "+district.getNameTertiaryEn();
                                    addressText.setText(address);
                                    String cycle="";
                                    for(String timing:timingList)
                                    {
                                        cycle+=timing+"\n";
                                    }
                                    cycleText.setText(cycle);




                                    if(globalProvider.getCustomer()!=null&&hasUnit&&(globalProvider.getCustomer().postcode.equals(postalcode))&&!postCodeChanged) {
                                       Log.d("buttonno","no");
                                        unitNumberText.setText(globalProvider.getCustomer().address);
                                        bindingButton.setVisibility(View.GONE);
                                        unitNumEdit.setVisibility(View.GONE);
                                        unitNumberText.setVisibility(View.VISIBLE);
                                    }
                                    else
                                    {
                                        Log.d("buttonno","yes");
                                        unitNumEdit.setVisibility(View.VISIBLE);
                                        bindingButton.setVisibility(View.VISIBLE);
                                        unitNumberText.setVisibility(View.GONE);
                                    }




                        }
                         catch (JsonParseException e) {
                            e.printStackTrace();
                        } catch (JsonMappingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                    else
                    {
                        if(strState==1)
                        {
                            openResultTextView.setText("District will be opened shortly");


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
        globalProvider.addRequest(customRequest);
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
