package com.easybuy.sg.grouponebuy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.easybuy.sg.grouponebuy.R;
import com.easybuy.sg.grouponebuy.adapter.CartListAdapter;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartListActivity extends AppCompatActivity {
    List<Product> productList;
    GlobalProvider globalProvider;
    RecyclerView recyclerView;
    CartListAdapter cartListAdapter;
    ImageView backButton;
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
        globalProvider=GlobalProvider.getGlobalProviderInstance(getApplicationContext());
        recyclerView=(RecyclerView) findViewById(R.id.recycler_pdlist);
        backButton=(ImageView) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        productList=new ArrayList<>();
        productList.addAll(globalProvider.cartList);
        cartListAdapter=new CartListAdapter(CartListActivity.this,productList);
        recyclerView.setAdapter(cartListAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(CartListActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        cartListAdapter.notifyDataSetChanged();




    }
}
