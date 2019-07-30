package com.easybuy.sg.grouponebuy.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.easybuy.sg.grouponebuy.presenter.ClickPresenter;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.databinding.ActivitySignupBinding;
import com.easybuy.sg.grouponebuy.helpers.CustomRequest;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.model.Customer;
import com.easybuy.sg.grouponebuy.model.Result;
import com.easybuy.sg.grouponebuy.model.User;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.easybuy.sg.grouponebuy.network.Constants.signUpUrl;

public class SignUpActivity extends AppCompatActivity {
    String valid_email;
ActivitySignupBinding activitySignupBinding;
GlobalProvider globalProvider;
ImageView backButton;
int responseCode;
//Handler handler=new Handler();
int n;
boolean open=false;
String invitationCode;


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activitySignupBinding= DataBindingUtil.setContentView(this,R.layout.activity_signup);
        backButton=activitySignupBinding.back;
        User user=new User();
        activitySignupBinding.setUsermodel(user);
        globalProvider=GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        activitySignupBinding.code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(activitySignupBinding.code.getText().toString().trim().length()>0)
                {
                    invitationCode=activitySignupBinding.code.getText().toString();
                }
                else
                    invitationCode=null;

            }
        });
        activitySignupBinding.postcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(activitySignupBinding.postcode.getText().toString().trim().length()<6)
                {
                    activitySignupBinding.postcode.setError("Invalid Postal Number");
                }
                else
                {
                    checkPostalStatus();
                }

            }
        });
        activitySignupBinding.phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(activitySignupBinding.phone.getText().toString().trim().length()<8)
                {
                    activitySignupBinding.phone.setError("Invalid Phone Number");
                }
                else if(activitySignupBinding.phone.getText().toString().trim().length()==8)
                {
                    String url=Constants.checkPhoneUrl;
                    Map<String,String> map=new HashMap<>();
                    map.put("phone",activitySignupBinding.phone.getText().toString().trim());
                    CustomRequest customRequest=new CustomRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getInt("status")==1)
                                {
                                    activitySignupBinding.phone.setError("Phone number already registered");
                                    activitySignupBinding.submit.setClickable(false);
                                }
                                else
                                {
                                    activitySignupBinding.submit.setClickable(true);
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


            }
        });

        /*

       activitySignupBinding.getCodeButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(activitySignupBinding.email.getText().toString().length()>0&&isEmailValid(activitySignupBinding.email.getText()))
               {
                   final Map<String,String> map=new HashMap<>();
                   map.put("email",activitySignupBinding.email.getText().toString().trim());
                   CustomRequest customRequest=new CustomRequest(Request.Method.POST, Constants.sendEmailCode, map, new Response.Listener<JSONObject>() {
                       @Override
                       public void onResponse(JSONObject response) {
                           Log.d("responsecode",response.toString());
                           try {
                               if(response.getInt("status")==0)
                               {
                                   Toast.makeText(SignUpActivity.this,"Verification Code sent to your email",Toast.LENGTH_SHORT).show();
                                  Log.d("emailse",map.get("email")) ;
                                  n=20;
                                 responseCode= response.getInt("payload");
                                 activitySignupBinding.getCodeButton.setEnabled(false);
                                   activitySignupBinding.getCodeButton.postDelayed(runnable,1000);

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
               else {
                   activitySignupBinding.email.setError("Please Enter Email Address");
               }
           }
       });
       activitySignupBinding.verificationCode.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void afterTextChanged(Editable editable) {
               if(activitySignupBinding.verificationCode.getText().toString().length()>0)
               {
                 int code=Integer.parseInt(activitySignupBinding.verificationCode.getText().toString());
                 if(code!=responseCode)
                 {
                     activitySignupBinding.verificationCode.setError("Please enter valid code");
                     activitySignupBinding.verificationStatus.setVisibility(View.GONE);


                 }
                 else
                 {

                     activitySignupBinding.verificationStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_box_green_24dp));
                     activitySignupBinding.verificationStatus.setVisibility(View.VISIBLE);

                 }

               }

           }
       });
       */
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







        activitySignupBinding.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Is_Valid_Email(activitySignupBinding.email);

            }
        });
        activitySignupBinding.setPresenter(new ClickPresenter() {
            @Override
            public void onButtonClick() {


                if(TextUtils.isEmpty(activitySignupBinding.name.getText().toString()))
                {
                    activitySignupBinding.name.setError("Please Enter name");


                }
                else if(TextUtils.isEmpty(activitySignupBinding.email.getText().toString()))
                {
                    activitySignupBinding.email.setError("Please Enter email");

                }
                else if(TextUtils.isEmpty(activitySignupBinding.phone.getText().toString()))
                {
                    activitySignupBinding.phone.setError("Please Enter Phone");

                }

                else if(TextUtils.isEmpty(activitySignupBinding.password.getText().toString()))
                {
                    activitySignupBinding.password.setError("Please Enter Password");

                }
                else if(TextUtils.isEmpty(activitySignupBinding.confirm.getText().toString()))
                {
                    activitySignupBinding.confirm.setError("Please Re Enter password");

                }

              else if(TextUtils.isEmpty(activitySignupBinding.postcode.getText().toString()))
                {
                    activitySignupBinding.postcode.setError("Please Enter Postal");

                }
              else if(activitySignupBinding.postcode.getText().toString().length()<6)
                {
                    activitySignupBinding.postcode.setError("Incorrect Postal");
                }





              else if(!activitySignupBinding.password.getText().toString().equals(activitySignupBinding.confirm.getText().toString()))
                {
                    activitySignupBinding.confirm.setError("Please Enter password correctly");

                }
                /*
                else if(TextUtils.isEmpty(activitySignupBinding.verificationCode.getText()))
                {
                    activitySignupBinding.verificationCode.setError("Please Enter Valid Code");
                }
                else if(Integer.parseInt(activitySignupBinding.verificationCode.getText().toString())!=responseCode)
                {
                    activitySignupBinding.verificationCode.setError("Please Enter Valid Code");
                }
                */
               else {
                    User user=activitySignupBinding.getUsermodel();




                            Map < String, String > map = new HashMap<>();
                    map.put("userName",activitySignupBinding.name.getText().toString());
                    map.put("email",activitySignupBinding.email.getText().toString());
                    map.put("phone",activitySignupBinding.phone.getText().toString());
                    map.put("password",activitySignupBinding.password.getText().toString());
                    map.put("postcode",activitySignupBinding.postcode.getText().toString());
                    if(invitationCode!=null)
                    {
                        map.put("code",invitationCode);
                    }

                    CustomRequest customRequest=new CustomRequest(Request.Method.POST, signUpUrl, map, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                        // Log.d("checksignupres",response.toString());
                         String result=response.toString();
                            JsonFactory jsonFactory = new JsonFactory();
                            ObjectMapper objectMapper = new ObjectMapper();


                            try {
                                if(response.getInt("status")==404)
                                {
                                    Toast.makeText(SignUpActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
                                    return;

                                }


                                JsonParser jsonParser = jsonFactory.createParser(result);
                                Result op = (Result) objectMapper.readValue(jsonParser, Result.class);
                                if(op.getStatus()==1)
                                {
                                    activitySignupBinding.phone.setError("Number already taken");
                                    return;
                                }
                                else {
                                    Customer customer = op.getCustomer();
                                   // Log.d("checkcus",customer.getUserName());
                                    Constants.setCustomer(SignUpActivity.this,customer);
                                    globalProvider.setCustomerId(customer.customer_id);
                                    globalProvider.setLogin(true);
                                    Log.d("open",open+"");
                                    if(open) {
                                        Intent intent = new Intent(SignUpActivity.this, DistrictSettingActivity.class);
                                        intent.putExtra("postal", activitySignupBinding.postcode.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        Intent intent = new Intent(SignUpActivity.this, NewAddressActivity.class);
                                        intent.putExtra("postal",activitySignupBinding.postcode.getText().toString());
                                       // intent.putExtra("postal", activitySignupBinding.postcode.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    }
                                }


                            }
                            catch(IOException e)
                            {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                           // Log.d("checkerrorresponse",error.toString());
                            String message = null;
                            if (error instanceof NetworkError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (error instanceof ServerError) {
                                message = "The server could not be found. Please try again after some time!!";
                            } else if (error instanceof AuthFailureError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (error instanceof ParseError) {
                                message = "Parsing error! Please try again after some time!!";
                            } else if (error instanceof NoConnectionError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (error instanceof TimeoutError) {
                                message = "Connection TimeOut! Please check your internet connection.";
                            }
                            Toast.makeText(SignUpActivity.this,message,Toast.LENGTH_LONG).show();

                        }
                    });
                    globalProvider.addRequest(customRequest);
                   // Customer customer=new Customer(null,null,null,user.getEmail(),null,user.getPhone(),user.getUserName(),null,user.getPostcode());







                }






            }
        });




    }
    public void Is_Valid_Email(EditText edt) {
        if (edt.getText().toString() == null) {
            edt.setError("Invalid Email Address");
            valid_email = null;
        } else if (isEmailValid(edt.getText().toString()) == false) {
            edt.setError("Invalid Email Address");
            valid_email = null;
        } else {
            valid_email = edt.getText().toString();
        }
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }
    /*
    public void onDestroy()
    {
        handler.removeCallbacks(runnable);
        activitySignupBinding.getCodeButton.removeCallbacks(runnable);
        handler=null;



        super.onDestroy();

    }
    */
    // end of TextWatcher (email)
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
    private void checkPostalStatus()
    {
        int postal=Integer.parseInt(activitySignupBinding.postcode.getText().toString());
        if((postal>=625000&&postal<629999)||(postal>=635000&&postal<639999)||(postal>=715000&&postal<719999)||(postal>=725000&&postal<=729999)||(postal>=98000&&postal<=98999)||(postal>=58000&&postal<=60000))
        {
            open=true;
            return;
        }
        Map<String,String> postCodeMap=new HashMap<>();
        postCodeMap.put("postcode",activitySignupBinding.postcode.getText().toString());
        CustomRequest customRequest=new CustomRequest(Request.Method.POST, Constants.districtUrl, postCodeMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int strState=response.getInt("status");
                    if(strState==2)
                    {
                        open=false;

                    }
                    else if(strState==0)
                    {
                        open=true;
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
    /*
    final Runnable runnable=new Runnable() {
        @Override
        public void run() {
            activitySignupBinding.getCodeButton.setText("Try again in "+n+" secs");
            n=n-1;
            if(n==0)
            {

                activitySignupBinding.getCodeButton.setText("Get Code");
                activitySignupBinding.getCodeButton.setEnabled(true);
                return;
            }
            handler.postDelayed(runnable,1000);


        }
    };
    */


}
