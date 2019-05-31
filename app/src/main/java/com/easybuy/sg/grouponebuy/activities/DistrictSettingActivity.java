package com.easybuy.sg.grouponebuy.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.TouchDelegate;
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
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.helpers.CustomRequest;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.model.Customer;
import com.easybuy.sg.grouponebuy.model.Cycle;
import com.easybuy.sg.grouponebuy.model.District;
import com.easybuy.sg.grouponebuy.model.ProductList;
import com.easybuy.sg.grouponebuy.model.ResultDistrict;
import com.easybuy.sg.grouponebuy.network.Constants;
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

public class DistrictSettingActivity extends AppCompatActivity  {
    TextView resultPostalText,  openResultTextView;
    EditText postalTextView;
    GlobalProvider globalProvider;
    Button emailButton,telephoneButton,noNeedButton;
    ImageView searchView;
    String postalcode;
    TextView addressText,cycleText,deliveryText;
    LinearLayout deliveryTimeLayout;
    LinearLayout notOpenLayout,openLayOut;
    ImageView whatsAppCopyImageView,weChatCopyImageView;
    TextView weChatText,whatsAppText;

    Button bindingButton;
    private String districtId;
    EditText unitNumEdit;
    boolean hasUnit;
    boolean postCodeChanged;
    ImageView backButton;
    boolean isEdited;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_districtsetting);
        globalProvider=GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        backButton=(ImageView)findViewById(R.id.back);
        emailButton=(Button)findViewById(R.id.email_Clicked);
        telephoneButton=(Button)findViewById(R.id.telephone_clicked);
        noNeedButton=(Button)findViewById(R.id.no_needClicked);
        weChatCopyImageView=(ImageView)findViewById(R.id.wechat_copy);
        whatsAppCopyImageView=(ImageView)findViewById(R.id.whatsapp_copy);
        weChatText=(TextView)findViewById(R.id.wechat);
        whatsAppText=(TextView)findViewById(R.id.watsapp_label);
        if(globalProvider.getCustomerId()!=null)
        {
            if(Constants.getCustomer(getApplicationContext()).address!=null&&Constants.getCustomer(getApplicationContext()).address.length()>0)
            {
                hasUnit=true;
            }
        }
       // Log.d("hasUnitcheck",hasUnit+"");


        postalTextView = (EditText) findViewById(R.id.postal);
        resultPostalText = (TextView) findViewById(R.id.result_postal);


        //pscode = (TextView) findViewById(R.id.pscode);
        openResultTextView = (TextView) findViewById(R.id.open_result);

        bindingButton=(Button) findViewById(R.id.bindingbutton);
        unitNumEdit=(EditText) findViewById(R.id.unit_num_edit);

      //  applyButton = (Button) findViewById(R.id.apply);
       // blkEditText = (EditText) findViewById(R.id.blkres);
       // districtEditText = (EditText) findViewById(R.id.dsres);

        addressText=(TextView) findViewById(R.id.address);
        cycleText=(TextView) findViewById(R.id.cycle);
        deliveryText=(TextView) findViewById(R.id.deliverytext);
        notOpenLayout=(LinearLayout) findViewById(R.id.notopen_fragment);
        openLayOut=(LinearLayout) findViewById(R.id.open_fragment);


        deliveryTimeLayout=(LinearLayout) findViewById(R.id.deliverytimelayout);
        searchView=(ImageView) findViewById(R.id.searchview);





        Intent intent = getIntent();
        postalcode = intent.getStringExtra("postal");
        postCodeChanged=intent.getBooleanExtra("postCodeChanged",false);
        if(postalcode!=null||globalProvider.getCustomerId()!=null) {
            if(postalcode==null) {
               // postalcode = globalProvider.getCustomer().postcode;
                postalcode = Constants.getCustomer(getApplicationContext()).postcode;
            }

            postalTextView.setText(postalcode);
            checkPostalStatus(postalcode);
            String resultString = getString(R.string.result_postcode) + postalcode;
            resultPostalText.setText(resultString);

        }
        String watsAPPContact=getString(R.string.whatsapp_us)+" "+"<font color='red'>85189139</font>";
        whatsAppText.setText(Html.fromHtml(watsAPPContact), TextView.BufferType.SPANNABLE);
        String weChatContact=getString(R.string.wechat_us)+" "+"<font color='red'>EbuyAssistant</font>";
        weChatText.setText(Html.fromHtml(weChatContact),TextView.BufferType.SPANNABLE);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdited) {
                    new AlertDialog.Builder(DistrictSettingActivity.this).setTitle(R.string.alert).setMessage(R.string.cancel_address_update).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           finish();
                        }
                    }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    }).create().show();

                }
                else
                    finish();
            }
        });
        weChatCopyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                copyToClipBoard("Copied WeChatId","EbuyAssistant");


            }
        });
        whatsAppCopyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyToClipBoard("Copied Watsapp Number","85189139");

            }
        });
    emailButton.getLayoutParams().width=500;
        emailButton.getLayoutParams().height=70;
        emailButton.requestLayout();
        telephoneButton.getLayoutParams().width=500;
        telephoneButton.getLayoutParams().height=70;
        telephoneButton.requestLayout();
        noNeedButton.getLayoutParams().width=500;
        noNeedButton.getLayoutParams().height=70;
        noNeedButton.requestLayout();

     



        postalTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(postalTextView.getText())||postalTextView.getText().length()<6)
                {

                    resultPostalText.setVisibility(View.GONE);
                    notOpenLayout.setVisibility(View.GONE);
                    openLayOut.setVisibility(View.GONE);
                }
                else if(postalTextView.getText().toString().length()==6) {
                    isEdited=true;
                    resultPostalText.setVisibility(View.VISIBLE);
                    postalcode = postalTextView.getText().toString();
                    checkPostalStatus(postalcode);

                    String resultString = "The result of searching of :" + postalcode;
                    resultPostalText.setText(resultString);
                   // pscode.setText(postalcode);
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
                if(globalProvider.isLogin())
                {
                    if (TextUtils.isEmpty(unitNumEdit.getText())) {
                        unitNumEdit.setError("Please enter unitNumber");
                        return;

                    }
                    String url = Constants.favouriteUrl + "/" + globalProvider.getCustomerId();
                    Map<String, String> param = new HashMap<>();
                    Log.d("checkaddres",unitNumEdit.getText().toString());
                    param.put("address", unitNumEdit.getText().toString());
                    CustomRequest unitChangeRequest = new CustomRequest(Request.Method.PATCH, url, param, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                           // Log.d("checlres", response.toString());
                            try {
                                if (response.getInt("status") == 0) {
                                    String url = Constants.changeDistrictUrl + globalProvider.getCustomerId();
                                    final Map<String, String> params = new HashMap<>();

                                    params.put("district", districtId);

                                    params.put("postcode", postalcode);
                                    //PATCH REQUEST NOT SUPPORTED PRE LOLIPOP

                                    CustomRequest customRequest=new CustomRequest(Request.Method.POST, url,params, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                if(response.getInt("status")==0)
                                                {
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
                           // Log.d("bindingerror", error.toString());

                        }
                    });

                    globalProvider.addRequest(unitChangeRequest);

                }
                else
                {
                    Toast.makeText(DistrictSettingActivity.this,"You need to login first!",Toast.LENGTH_LONG).show();
                }

        }});




        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Log.d("searchviewclicked","yes");
                if(TextUtils.isEmpty(postalTextView.getText()))
                {
                    postalTextView.setError("Please enter district!");
                    resultPostalText.setVisibility(View.GONE);
                    notOpenLayout.setVisibility(View.GONE);
                    openLayOut.setVisibility(View.GONE);
                }
                else if(postalTextView.getText().toString().length()==6) {
                    postalcode = postalTextView.getText().toString();
                    resultPostalText.setVisibility(View.VISIBLE);
                    checkPostalStatus(postalcode);

                    String resultString = "The result of searching of :" + postalcode;
                    resultPostalText.setText(resultString);
                    //pscode.setText(postalcode);
                }


            }
        });
        if(TextUtils.isEmpty(postalTextView.getText()))
        {
            resultPostalText.setVisibility(View.GONE);
            notOpenLayout.setVisibility(View.GONE);
            openLayOut.setVisibility(View.GONE);

        }




        /*applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (blkEditText.getText().toString().length() > 0 && districtEditText.getText().toString().length() > 0) {
                    String url=Constants.applyDistrictUrl;
                    String address=districtEditText.getText().toString()+" "+blkEditText.getText().toString();
                    Map<String,String> params=new HashMap<>();
                    params.put("postcode",postalcode);
                    params.put("name",address);
                    params.put("applicant",globalProvider.getCustomerId());
                    String language=Constants.getLanguage(getApplicationContext());
                    if(language.equals("english"))
                    {
                        params.put("lang","english");
                    }
                    else
                        params.put("lang","chinese");
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
        */
    }

    private void copyToClipBoard(String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(DistrictSettingActivity.this, "Saved to clip board", Toast.LENGTH_SHORT).show();
    }

    private void checkPostalStatus(final String postalcode) {
        Map<String,String> params=new HashMap<>();
        params.put("postcode",postalcode);
        CustomRequest customRequest=new CustomRequest(Request.Method.POST, Constants.districtUrl, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                   // Log.d("getre",response.toString());

                    int strState=response.getInt("status");
                   // Log.d("mystate",strState+"");
                    if(strState==2)
                    {
                        openLayOut.setVisibility(View.GONE);
                        notOpenLayout.setVisibility(View.VISIBLE);


                        openResultTextView.setText(getString(R.string.district_not_open));
                        openResultTextView.setTextColor(getResources().getColor(R.color.red));

                       // pscode.setText(postalcode);
                    }
                    else if(strState==0)
                    {
                        String lang=Constants.getLanguage(DistrictSettingActivity.this.getApplicationContext());

                        ObjectMapper objectMapper=new ObjectMapper();
                        JsonFactory jsonFactory = new JsonFactory();
                        try {
                            JsonParser jsonParser = jsonFactory.createParser(response.toString());
                           ResultDistrict resultDistrict = (ResultDistrict) objectMapper.readValue(jsonParser, ResultDistrict.class);
                          // Log.d("checkdis",resultDistrict.getPayload().getNamePrimaryEn());

                          District district= resultDistrict.getPayload();

                         //todo customer add district
                          districtId=district.getId();
                            openLayOut.setVisibility(View.VISIBLE);
                            notOpenLayout.setVisibility(View.GONE);
                            List<String> timingList=new ArrayList();
                            for(Cycle cycle:district.getCycle())
                            {
                               int week= cycle.getWeek();
                              // Log.d("checkwe",week+"");
                               String weekDay=globalProvider.deliveryTiming.get(week);
                               if(!lang.equalsIgnoreCase("english"))
                               {
                                   weekDay=globalProvider.deliveryTimingChinese.get(weekDay);
                               }
                               String duration=cycle.getDuration();
                               timingList.add(weekDay+" "+duration);

                            }

                            openResultTextView.setTextColor(getResources().getColor(R.color.green));
                            String result=getString(R.string.state)+"           "+getString(R.string.open);

                            openResultTextView.setText(result);
                            String address=getString(R.string.address)+"     "+district.getNameSecondaryEn()+" - "+district.getNameTertiaryEn();
                            addressText.setText(address);
                            String cycle="";
                            for(String timing:timingList)
                            {
                                cycle+=" "+" "+" "+" "+" "+timing+"\n";
                            }
                            cycleText.setText(cycle);




                                    if(globalProvider.getCustomerId()!=null&&hasUnit&&(Constants.getCustomer(getApplicationContext()).postcode.equals(postalcode))&&!postCodeChanged) {
                                      // Log.d("buttonno","no");
                                       // unitNumberText.setText(Constants.getCustomer(getApplicationContext()).address);
                                        unitNumEdit.setText(Constants.getCustomer(getApplicationContext()).address);
                                       // bindingButton.setVisibility(View.GONE);
                                        //unitNumEdit.setVisibility(View.GONE);
                                       // unitNumberText.setVisibility(View.VISIBLE);
                                    }
                            unitNumEdit.setVisibility(View.VISIBLE);
                            bindingButton.setVisibility(View.VISIBLE);








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
                            openResultTextView.setText(getString(R.string.district_open_shortly));


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
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void onButtonClicked(View view)
    {

        String choice=null;

        switch (view.getId())
        {
            case R.id.telephone_clicked:
            {
                choice="phone inform";
                break;
            }
            case R.id.email_Clicked:
            {
                choice="email inform";
                break;
            }
            case R.id.no_needClicked:
            {
                choice="no need";
                break;

            }

        }
        Log.d("choice",choice);
       // Log.d("choice",choice);
        //Log.d("postcodee",postalcode);
        if(globalProvider.isLogin()) {


            String url = Constants.applyDistrictUrl;

            Map<String, String> params = new HashMap<>();
            params.put("postcode", postalcode);
            params.put("name", choice);
            params.put("applicant", globalProvider.getCustomerId());
            String language = Constants.getLanguage(getApplicationContext());
            if (language.equals("english")) {
                params.put("lang", "en");
            } else
                params.put("lang", "ch");

            CustomRequest customRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Log.d("applyresponse",response.toString());
                    try {
                        if (response.getInt("status") == 0 || response.getInt("status") == 2) {

                            Toast.makeText(DistrictSettingActivity.this, "Message sent successfully!", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(DistrictSettingActivity.this, MainActivity.class);

                                    startActivity(intent);

                                }
                            }, 2000);
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
            Intent intent=new Intent(DistrictSettingActivity.this,SignInActivity.class);
            startActivity(intent);
            finish();
        }



        //Todo add dialog
    }
    @Override
    public void onBackPressed()
    {
        if(isEdited) {

            new AlertDialog.Builder(DistrictSettingActivity.this).setTitle(R.string.alert).setMessage(R.string.cancel_address_update).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                }
            }).create().show();
        }
        else
            super.onBackPressed();

    }

    }

