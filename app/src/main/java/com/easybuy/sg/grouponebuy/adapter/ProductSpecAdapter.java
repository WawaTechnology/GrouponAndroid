package com.easybuy.sg.grouponebuy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.activities.ProductDetailActivity;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.helpers.Utf8JsonRequest;
import com.easybuy.sg.grouponebuy.model.CategoryProduct;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class ProductSpecAdapter extends RecyclerView.Adapter<ProductSpecAdapter.MyViewHolder> {

    public Context context;
    public List<CategoryProduct> productList;

    GlobalProvider globalProvider;
    SpecChangeListener specChangeListener;
    public interface SpecChangeListener
    {
        public void onSpecChange(Product product);
    }
    public ProductSpecAdapter(Context context,List<CategoryProduct> productList,SpecChangeListener specChangeListener)
    {
        this.context=context;
        this.productList=productList;

        globalProvider=GlobalProvider.getGlobalProviderInstance(context);
        this.specChangeListener=specChangeListener;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.product_spec,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
     final  CategoryProduct product= productList.get(position);
       holder.productSpecText.setText("$"+product.getPrice()+"/ "+product.getSpecificationEn());

            String productId=((ProductDetailActivity)context).productId;
            Log.d("detailpdid",productId);


        if(productId.equals(product.getId()))
        {
            holder.productSpecText.setBackground(context.getResources().getDrawable(R.drawable.orange_new));
            holder.productSpecText.setTextColor(Color.WHITE);
           // spec2TextView.setBackground(getResources().getDrawable(R.drawable.table_bkg));
        }
        else
        {



            holder.productSpecText.setBackground(context.getResources().getDrawable(R.drawable.table_bkg));
            holder.productSpecText.setPadding(8,6,8,6);
            holder.productSpecText.setTextColor(Color.BLACK);
        }
        holder.productSpecText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productId.equals(product.getId())) {

                } else {

                    String url = Constants.productUrl + "/" + product.getId();

                    Utf8JsonRequest utf8JsonRequest = new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonObj = null;
                            ObjectMapper objectMapper=new ObjectMapper();
                            JsonFactory jsonFactory=new JsonFactory();
                            try {
                                jsonObj = new JSONObject(response).getJSONObject("payload");
                              Product pd=  objectMapper.readValue(jsonObj.toString(),Product.class);
                              specChangeListener.onSpecChange(pd);
                              /*  ((Activity)context).finish();
                                Intent intent=((Activity)context).getIntent();


                                intent.putExtra("product",pd);
                                context.startActivity(intent);
                                */





                            } catch (JSONException e) {
                                e.printStackTrace();
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


            }});
        }

    @Override
    public int getItemCount() {

        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView productSpecText;

        public MyViewHolder(View itemView) {
            super(itemView);
            productSpecText=(TextView)itemView.findViewById(R.id.product_text);
        }
    }
}
