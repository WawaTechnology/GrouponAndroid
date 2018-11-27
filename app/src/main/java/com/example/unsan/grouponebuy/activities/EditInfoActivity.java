package com.example.unsan.grouponebuy.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.unsan.grouponebuy.R;
import com.example.unsan.grouponebuy.helpers.CustomRequest;
import com.example.unsan.grouponebuy.helpers.GlobalProvider;
import com.example.unsan.grouponebuy.helpers.Utf8JsonRequest;
import com.example.unsan.grouponebuy.model.Customer;
import com.example.unsan.grouponebuy.model.Result;
import com.example.unsan.grouponebuy.network.Constants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditInfoActivity extends AppCompatActivity {
    private static final int PICK_PHOTO = 123;
    EditText nameEdit, emailEdit, phoneEdit, postcodeEdit, unitNoEdit, paynowEdit;
    //ImageView uploadImageIcon;
    ImageView backButton;
    GlobalProvider globalProvider;
    Button saveButton;
    Map<String, String> params;
    boolean postalChanged;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_info);
        params = new HashMap<>();
        nameEdit = (EditText) findViewById(R.id.name_edit);
        emailEdit = (EditText) findViewById(R.id.email_edit);
        phoneEdit = (EditText) findViewById(R.id.phone_edit);
        postcodeEdit = (EditText) findViewById(R.id.postcode_edit);
        unitNoEdit = (EditText) findViewById(R.id.unitno_edit);
        paynowEdit = (EditText) findViewById(R.id.paynow_edit);
        backButton=(ImageView)findViewById(R.id.back);
        saveButton = (Button) findViewById(R.id.save);
      //  uploadImageIcon = (ImageView) findViewById(R.id.upload_image);
        globalProvider = GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        Customer customer = globalProvider.getCustomer();
        nameEdit.setText(customer.userName);
        nameEdit.addTextChangedListener(new GenericTextWatcher(nameEdit));
        emailEdit.setText(customer.email);
        emailEdit.addTextChangedListener(new GenericTextWatcher(emailEdit));
        phoneEdit.setText(customer.phone);
        phoneEdit.addTextChangedListener(new GenericTextWatcher(phoneEdit));
        postcodeEdit.setText(customer.postcode);
        postcodeEdit.addTextChangedListener(new GenericTextWatcher(postcodeEdit));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (customer.getPayNowAccount() != null) {
            paynowEdit.setText(customer.getPayNowAccount());


        }
        paynowEdit.addTextChangedListener(new GenericTextWatcher(paynowEdit));
        if (customer.address != null) {
            unitNoEdit.setText(customer.address);
        }
        unitNoEdit.addTextChangedListener(new GenericTextWatcher(unitNoEdit));
     /*   uploadImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageIntent.setType("image/*");
                pickImageIntent.putExtra("aspectX", 1);
                pickImageIntent.putExtra("aspectY", 1);
                pickImageIntent.putExtra("scale", true);
                pickImageIntent.putExtra("outputFormat",
                        Bitmap.CompressFormat.JPEG.toString());
                startActivityForResult(pickImageIntent, PICK_PHOTO);
            }
        });
        */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Constants.favouriteUrl + "/" + globalProvider.getCustomer().customer_id;
                Log.d("curl", url);
                CustomRequest customRequest = new CustomRequest(Request.Method.PATCH, url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("checkresponse", response.toString());
                        updateCustomer();

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

    private void updateCustomer() {
        String url = Constants.favouriteUrl + "/" + globalProvider.getCustomer().customer_id;
        Utf8JsonRequest utf8JsonRequest = new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Log.d("responsevolley", response.getString("token"));
                    JsonFactory jsonFactory = new JsonFactory();
                    ObjectMapper objectMapper = new ObjectMapper();
                    String result = response.toString();
                    Log.d("checkuser", response.toString());


                    Log.d("contractresponse", result);

                    // String uname=response.getString("userName");
                    //Log.d("checkuname",uname);


                    JsonParser jsonParser = jsonFactory.createParser(result);
                    Result res = (Result) objectMapper.readValue(jsonParser, Result.class);
                    Log.d("check", res.getStatus() + "");
                    int status = res.getStatus();
                    if (status == 0) {
                      Constants.setCustomer(EditInfoActivity.this,null);
                      Constants.setCustomer(EditInfoActivity.this,res.getCustomer());
                      globalProvider.setCustomer(null);
                      globalProvider.setCustomer(res.getCustomer());
                      if(postalChanged)
                      {
                          new AlertDialog.Builder(EditInfoActivity.this).setTitle("Alert").setMessage("PostCode is changed!Please reset your district").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int i) {
                                  Intent intent=new Intent(EditInfoActivity.this,DistrictSettingActivity.class);
                                  intent.putExtra("postCodeChanged",true);

                                  startActivity(intent);
                                  finish();

                              }
                          }).create().show();

                      }
                      else {


                      Intent intent=new Intent(EditInfoActivity.this,MainActivity.class);
                      startActivity(intent);
                      }



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


        /*    @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == PICK_PHOTO && resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        //Display an error
                        return;
                    }

                    Uri uri = data.getData();
                    uploadImageIcon.setImageURI(uri);
                    Log.d("checkuri", uri.toString());
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        Bitmap lastBitmap = null;
                        lastBitmap = bitmap;

                        //encoding image to string
                        Log.d("btmap",lastBitmap.toString());


                        params.put("avatar", lastBitmap.toString());

                        //passing the image to volley

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
                }
            }
            */



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


                        case R.id.email_edit:

                        {
                            params.put("email", text);
                            break;
                        }
                        case R.id.postcode_edit:

                        {
                            params.put("postcode", text);
                            postalChanged=true;
                            break;
                        }
                        case R.id.unitno_edit:

                        {
                            params.put("address", text);
                            break;
                        }
                        case R.id.paynow_edit: {
                            params.put("payNowAccount", text);
                            break;
                        }


                    }
                }
            }

        }
