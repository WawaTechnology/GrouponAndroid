package com.easybuy.sg.grouponebuy.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.activities.DistrictSettingActivity;
import com.easybuy.sg.grouponebuy.activities.EditInfoActivity;
import com.easybuy.sg.grouponebuy.activities.MainActivity;
import com.easybuy.sg.grouponebuy.helpers.CustomRequest;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.helpers.Utf8JsonRequest;
import com.easybuy.sg.grouponebuy.model.Address;
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
import java.util.List;
import java.util.Map;

public class DeliveryAddressAdapter extends RecyclerView.Adapter<DeliveryAddressAdapter.MyViewHolder> {
    Context context;
    List<Address> addressList;
    GlobalProvider globalProvider;
    UpdateAddressInterface updateAddressInterface;

    public DeliveryAddressAdapter(Context context,List<Address> addressList,UpdateAddressInterface updateAddressInterface)
    {
        this.context=context;
        this.addressList=addressList;
        this.updateAddressInterface=updateAddressInterface;
        globalProvider=GlobalProvider.getGlobalProviderInstance(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_address_item,null,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Address address=addressList.get(position);

           // Log.d("checkunit",address.getUnit());

        holder.primaryAddressText.setText(address.getUnit()+" , "+address.getDistrict().getNameTertiaryEn()+" "+address.getDistrict().getNameSecondaryEn());
        holder.districtText.setText(address.getDistrict().getNamePrimaryEn());
        holder.postCodeText.setText(context.getResources().getString(R.string.singapore)+" "+address.getDistrict().getPostcode());
        if(position==0)
        {
            holder.deleteButton.setVisibility(View.GONE);
            holder.defaultButton.setText(context.getResources().getString(R.string.default_address));
            holder.defaultButton.setEnabled(false);
            holder.defaultButton.setTextColor(context.getResources().getColor(R.color.red));
            holder.defaultButton.setBackground(context.getResources().getDrawable(R.drawable.button_rectangle));


          //  Drawable background=holder.defaultButton.getBackground();

        }
        else
        {

            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.defaultButton.setText(context.getResources().getString(R.string.set_as_default));
            holder.defaultButton.setTextColor(context.getResources().getColor(R.color.black));
            holder.defaultButton.setEnabled(true);

        }
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url=Constants.removeAddressUrl+globalProvider.getCustomerId();
                Map<String,String> map=new HashMap<>();
                map.put("districtID",address.getDistrict().getId());
                CustomRequest customRequest=new CustomRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getInt("status")==0)
                            {
                                addressList.remove(position);
                                notifyItemRemoved(position);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }}, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                globalProvider.addRequest(customRequest);
            }
        });
        holder.defaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Constants.changeDistrictUrl + globalProvider.getCustomerId();
               // Log.d("checkurl", url);
                final Map<String, String> params = new HashMap<>();
                String districtId=address.getDistrict().getId();
                String postalcode=address.getDistrict().getPostcode();

                params.put("district", districtId);

                params.put("postcode", postalcode);
                //PATCH REQUEST NOT SUPPORTED PRE LOLIPOP
                CustomRequest customRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       // Log.d("checkbinding", response.toString());
                        try {
                            int status = response.getInt("status");
                            if (status == 0) {
                                /*String url = Constants.favouriteUrl + "/" + globalProvider.getCustomerId();
                                Log.d("getu", url);
                                Map<String, String> param = new HashMap<>();

                                param.put("address", address.getUnit());
                                Log.d("address",address.getUnit());
                                CustomRequest unitChangeRequest = new CustomRequest(Request.Method.PATCH, url, param, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("checlres", response.toString());
                                        try {
                                            if (response.getInt("status") == 0) {
                                                updateAddressInterface.onFinish();
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
                                */
                                updateAddressInterface.onFinish();

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

        });



    }
   public interface UpdateAddressInterface
    {
        public void onFinish();
    }


    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView primaryAddressText,districtText,postCodeText;
        Button deleteButton,defaultButton;


        public MyViewHolder(View itemView) {
            super(itemView);
            deleteButton=(Button)itemView.findViewById(R.id.delete);
            defaultButton=(Button)itemView.findViewById(R.id.default_button);
            primaryAddressText=(TextView)itemView.findViewById(R.id.primary_address);
            districtText=(TextView)itemView.findViewById(R.id.district);
            postCodeText=(TextView)itemView.findViewById(R.id.postcode);

        }
    }
}
