package com.easybuy.sg.grouponebuy.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.helpers.CustomRequest;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.model.Customer;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.model.Result;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static com.easybuy.sg.grouponebuy.network.Constants.loginUrlStr;

public class SignInActivity extends AppCompatActivity {
    TextView signupText,userNameText;
    EditText phoneText,pwdText;
    Button submitButton;
    TextView forgotPasswordTextView;
    ImageView backButton;
    GlobalProvider globalProviderInstance;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signupText=(TextView)findViewById(R.id.signup);
        phoneText=(EditText)findViewById(R.id.phone_num) ;
        pwdText=(EditText) findViewById(R.id.pwd);
        userNameText=(TextView) findViewById(R.id.user_name);
        submitButton=(Button)findViewById(R.id.submit);
        forgotPasswordTextView=(TextView) findViewById(R.id.forgot);
        backButton=(ImageView) findViewById(R.id.back);
        globalProviderInstance=GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        try {
            findHistoryList();
        } catch (IOException e) {
            e.printStackTrace();
        }


        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);

                startActivity(intent);
            }
        });

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
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(phoneText.getText()))
                {
                    phoneText.setError("Please enter phone number");
                }
                else if(TextUtils.isEmpty(pwdText.getText()))
                {
                    pwdText.setError("Please enter password");
                }
                if(!TextUtils.isEmpty(phoneText.getText())&&!(TextUtils.isEmpty(pwdText.getText())))
                {
                    submitButton.setEnabled(false);
                    Map<String, String> params = new HashMap<>();

                    params.put("phone",phoneText.getText().toString() );
                    params.put("password", pwdText.getText().toString());

                    CustomRequest jsonObjectRequest = new CustomRequest(Request.Method.POST, loginUrlStr, params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                JsonFactory jsonFactory = new JsonFactory();
                                ObjectMapper objectMapper = new ObjectMapper();
                                String result=response.toString();

                               // String uname=response.getString("userName");
                                //Log.d("checkuname",uname);
                                try
                                {


                                JsonParser jsonParser = jsonFactory.createParser(result);
                                 Result customer =(Result) objectMapper.readValue(jsonParser, Result.class);

                                int status=customer.getStatus();
                                if(status==0) {
                                    globalProviderInstance.setCustomerId(customer.getCustomer().customer_id);
                                    String token;
                                    token = response.getString("token");

                                    token = token.replaceAll("\"", "");//把token中的"\""全部替换成""
                                    Constants.setToken(SignInActivity.this, token);
                                    globalProviderInstance.setLogin(true);

                                    Constants.setCustomer(SignInActivity.this,customer.getCustomer());
                                    Customer customers=Constants.getCustomer(SignInActivity.this);
                                   // Log.d("checkcknm",customers.getUserName());
                                    saveHistory(customer.getCustomer().userName);

                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


                                    startActivity(intent);
                                    //this.setResult(Activity.RESULT_OK);//为结果绑定Activity.RESULT_OK
                                    finish();
                                }
                                else if(status==1)
                                {
                                    Toast.makeText(SignInActivity.this,"User doesn't exist!",Toast.LENGTH_LONG).show();
                                    phoneText.setError("Please enter correct number");
                                    submitButton.setEnabled(true);
                                }
                                else if(status==2)
                                {
                                    Toast.makeText(SignInActivity.this,"Password error",Toast.LENGTH_LONG).show();

                                    pwdText.setError("Please enter correct password!");
                                    submitButton.setEnabled(true);
                                }


                               // globalProviderInstance.setUser(user);
                                }
                            catch(IOException e) {
                                e.printStackTrace();
                            }








                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {




                            new AlertDialog.Builder(SignInActivity.this)
                                    .setMessage("Connection Issue")
                                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            submitButton.setEnabled(true);
                                        }
                                    }).show();


                        }
                    });


                    globalProviderInstance.addRequest(jsonObjectRequest);



                }
            }
        });
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignInActivity.this,ChangePwdActivity.class);
                startActivity(intent);
            }
        });




    }
    private void findHistoryList() throws IOException {

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ebuygroupon.txt");

        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);

        String s = null;

       s=br.readLine();
        if (s!=null) {
            userNameText.setText(s);
            userNameText.setVisibility(View.VISIBLE);
        }
        else
            userNameText.setVisibility(GONE);

        br.close();
        isr.close();
        fis.close();
    }


    private void saveHistory(String userName) throws IOException {

        FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ebuygroupon.txt");
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(userName);
        bw.newLine();


        bw.close();
        osw.close();
        fos.close();
    }
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

}
