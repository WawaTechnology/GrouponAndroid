package com.example.unsan.grouponebuy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.unsan.grouponebuy.R;
import com.example.unsan.grouponebuy.helpers.CustomRequest;
import com.example.unsan.grouponebuy.helpers.GlobalProvider;
import com.example.unsan.grouponebuy.network.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.unsan.grouponebuy.network.Constants.getOrderUrl;
import static com.example.unsan.grouponebuy.network.Constants.loginUrlStr;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText phoneEditText, newPassEditText, rePassEditText,oldPassEditText;
    String phoneNumber, newPssword, rePassword;
    Button submitButton;
    GlobalProvider globalProvider;
    LinearLayout loginlayout,nologinlayout;
    ImageView backButton;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        phoneEditText = (EditText) findViewById(R.id.phone_edit);
        newPassEditText = (EditText) findViewById(R.id.new_password);
        rePassEditText = (EditText) findViewById(R.id.confirm_password);
        loginlayout=(LinearLayout) findViewById(R.id.loginlayout);
        backButton=(ImageView) findViewById(R.id.back);
        nologinlayout=(LinearLayout) findViewById(R.id.no_loginlayout);
        oldPassEditText=(EditText) findViewById(R.id.old_edit);
        phoneNumber = phoneEditText.getText().toString();
        newPssword = newPassEditText.getText().toString();
        rePassword = rePassEditText.getText().toString();
        submitButton = (Button) findViewById(R.id.confirm);
        globalProvider = GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        if(globalProvider.isLogin())
        {
            loginlayout.setVisibility(View.VISIBLE);
            nologinlayout.setVisibility(View.GONE);
        }
        else
        {
            loginlayout.setVisibility(View.GONE);
            nologinlayout.setVisibility(View.VISIBLE);
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (globalProvider.isLogin())
                {
                    if(TextUtils.isEmpty(oldPassEditText.getText()))
                    {
                        oldPassEditText.setError("Please enter old password");
                    }
                }
                else
                {
                    if(TextUtils.isEmpty(phoneEditText.getText()))
                    {
                        phoneEditText.setError("Please enter phone number");
                    }
                }

              if (TextUtils.isEmpty(newPassEditText.getText()) || TextUtils.isEmpty(rePassEditText.getText())) {

                    Toast.makeText(ChangePasswordActivity.this, "Please Complete the form!", Toast.LENGTH_LONG).show();
                } else if (!newPssword.equals(rePassword)) {
                    Toast.makeText(ChangePasswordActivity.this, "Password should match!", Toast.LENGTH_LONG).show();
                } else {


                    Map<String, String> params = new HashMap<>();
                    String url="";
                  if(!globalProvider.isLogin()) {

                      params.put("phone", phoneEditText.getText().toString());
                       url=Constants.changePasswordUrl;

                  }
                  else
                  {
                      params.put("oldPassword", oldPassEditText.getText().toString());
                      url=Constants.ChangePasswordUrlA+globalProvider.getCustomer().customer_id;
                  }
                    params.put("newPassword", newPassEditText.getText().toString());
                  Log.d("checkurl",url);

                    CustomRequest jsonObjectRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                      Log.d("respo",response.toString());
                            try {
                                if(response.getInt("status")==0)
                                {
                                    Toast.makeText(ChangePasswordActivity.this,"Password updated Successfully",Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(ChangePasswordActivity.this,SignInActivity.class);
                                    startActivity(intent);

                                }
                                else if(response.getInt("status")==1)
                                {
                                    Toast.makeText(ChangePasswordActivity.this,"Old Password is incorrect!",Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ChangePasswordActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    });
                    globalProvider.addRequest(jsonObjectRequest);
                }


            }
        });
    }
}
