package com.easybuy.sg.grouponebuy.activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.helpers.CustomRequest;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.network.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePwdActivity extends AppCompatActivity {
    EditText phoneEditText, emailEditText;
    String phoneNumber, emailAddress;
    Button submitButton;
    GlobalProvider globalProvider;
    Handler handler=new Handler();
    int n;

    ImageView backButton;
    private String valid_email;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pwd);
        phoneEditText = (EditText) findViewById(R.id.phone_text);
        emailEditText = (EditText) findViewById(R.id.email_text);
        backButton = (ImageView) findViewById(R.id.back);
        submitButton = (Button) findViewById(R.id.confirm);
        globalProvider = GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                is_Valid_Email(emailEditText);

            }
        });
        final View parent = (View) backButton.getParent();  // button: the view you want to enlarge hit area
        parent.post(new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                backButton.getHitRect(rect);
                rect.top -= 10;    // increase top hit area
                rect.left -= 10;   // increase left hit area
                rect.bottom += 10; // increase bottom hit area
                rect.right += 50;  // increase right hit area
                parent.setTouchDelegate(new TouchDelegate(rect, backButton));
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(phoneEditText.getText()) || phoneEditText.getText().length()<8) {

                    Toast.makeText(ChangePwdActivity.this,"Please enter valid Phone Number",Toast.LENGTH_SHORT).show();
                }
                else if(valid_email==null)
                {
                    Toast.makeText(ChangePwdActivity.this,"Please enter valid Email",Toast.LENGTH_SHORT).show();
                }
                else{
                    Map<String, String> params = new HashMap<>();

                    params.put("phone", phoneEditText.getText().toString());
                    params.put("email", emailEditText.getText().toString());

                    CustomRequest jsonObjectRequest = new CustomRequest(Request.Method.POST, Constants.updatePasswordUrl, params, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                           // Log.d("checkres",response.toString());
                            try {
                                if(response.getInt("status")==0)
                                {
                                    Toast.makeText(ChangePwdActivity.this,"We have sent an email to you",Toast.LENGTH_LONG).show();
                                    submitButton.setEnabled(false);
                                    globalProvider.setLogin(false);
                                    Constants.setCustomer(ChangePwdActivity.this,null);
                                     n=20;

                                    submitButton.postDelayed(runnable,1000);

                                }
                                else if(response.getInt("status")==404)
                                {
                                    Toast.makeText(ChangePwdActivity.this,"No Email Found!",Toast.LENGTH_SHORT).show();
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

                }


            }
        });
    }

    private void is_Valid_Email(EditText emailEditText) {
        if (emailEditText.getText().toString() == null) {
            emailEditText.setError("Invalid Email Address");
            valid_email = null;
        } else if (isEmailValid(emailEditText.getText().toString()) == false) {
            emailEditText.setError("Invalid Email Address");
            valid_email = null;
        }
        else
        {
            valid_email=emailEditText.getText().toString();
        }


    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }

    public void onDestroy()
    {
        handler.removeCallbacks(runnable);
        submitButton.removeCallbacks(runnable);
        handler=null;



        super.onDestroy();

    }
    final Runnable runnable=new Runnable() {
        @Override
        public void run() {
            submitButton.setText("Try again after "+n+" secs");
            n=n-1;
            if(n==0)
            {
                submitButton.setText("Submit");
                submitButton.setEnabled(true);
                return;
            }
            handler.postDelayed(runnable,1000);


        }
    };

}
