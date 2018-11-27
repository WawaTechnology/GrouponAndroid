package com.example.unsan.grouponebuy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.example.unsan.grouponebuy.R;
import com.example.unsan.grouponebuy.activities.CustomerServiceActivity;
import com.example.unsan.grouponebuy.activities.MyApplication;
import com.example.unsan.grouponebuy.activities.SearchActivity;
import com.example.unsan.grouponebuy.adapter.CategoryDetailAdapter;
import com.example.unsan.grouponebuy.adapter.CategoryTitleAdapter;
import com.example.unsan.grouponebuy.helpers.GlobalProvider;
import com.example.unsan.grouponebuy.helpers.Utf8JsonRequest;
import com.example.unsan.grouponebuy.model.Category;
import com.example.unsan.grouponebuy.model.CategoryList;
import com.example.unsan.grouponebuy.model.CategoryPrimary;
import com.example.unsan.grouponebuy.model.CategoryPrimaryList;
import com.example.unsan.grouponebuy.model.CategorySummary;
import com.example.unsan.grouponebuy.network.Constants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragmentProductCategory extends Fragment implements CategoryTitleAdapter.TitleClickListener {
    GlobalProvider globalProvider;
   RecyclerView categoryTitleRecycler;
    RecyclerView categoryDetailRecycler;
  CategoryTitleAdapter categoryTitleAdapter;
  LinearLayout searchBarLayout;

    CategoryDetailAdapter categoryDetailAdapter;
    List<CategorySummary> categoryNameList;
    List<CategorySummary> categoryList;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        globalProvider=GlobalProvider.getGlobalProviderInstance(getContext().getApplicationContext());
        categoryNameList=new ArrayList<>();
        categoryList=new ArrayList<>();
       categoryTitleAdapter=new CategoryTitleAdapter(getContext(),categoryNameList,this);
        categoryDetailAdapter=new CategoryDetailAdapter(getContext(),categoryList);
       // MyApplication.getRefWatcher(getActivity()).watch(this);

    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.fragment_categoryproduct,container,false);
        categoryTitleRecycler=(RecyclerView)view.findViewById(R.id.categoryTitleRecycler);
        categoryDetailRecycler=(RecyclerView)view.findViewById(R.id.detail_recycler);
        searchBarLayout=(LinearLayout) view.findViewById(R.id.searchbar_ht);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayout.VERTICAL,false);

       categoryTitleRecycler.setLayoutManager(linearLayoutManager);
        categoryTitleRecycler.setAdapter(categoryTitleAdapter);
        searchBarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        GridLayoutManager LayoutManager=new GridLayoutManager(getActivity(),3);
        categoryDetailRecycler.setLayoutManager(LayoutManager);
        categoryDetailRecycler.setAdapter(categoryDetailAdapter);
        if(globalProvider.selectedCategory!=null)
        {
            Log.d("selctedcatid",globalProvider.selectedCategory);
            Log.d("selctedcatname",globalProvider.selectedCategoryName);

        }
        getCategoryPrimary();

        return view;
    }


    private void getCategoryPrimary() {

                    for(CategorySummary categorySummary:globalProvider.categoryList)
                    {

                        categoryNameList.add(categorySummary);









                    }


                  //  categoryList.addAll(categoryPList.getPayload().get(0).getCategories());

                    categoryTitleAdapter.notifyDataSetChanged();
                   // categoryDetailAdapter.notifyDataSetChanged();

                }




    @Override
    public void onClick(final String id) {
        Log.d("onclickcalled","here");
        Log.d("pid",id);
        String url= Constants.baseUrlStr+"categoryPrimarys/"+id;
        categoryList.clear();
        categoryDetailAdapter.notifyDataSetChanged();
        Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonFactory jsonFactory = new JsonFactory();
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                try
                {
                    JsonParser jsonParser = jsonFactory.createParser(response);
                    CategoryPrimaryList categoryrList=(CategoryPrimaryList)objectMapper.readValue(jsonParser, CategoryPrimaryList.class);

                   // Log.d("detailsize",categoryrList.getPayload().get(0).getCategories().size()+"");
                    categoryList.add(new CategorySummary(categoryrList.getPayload().get(0).getId(),"全部","All",categoryrList.getPayload().get(0).getImage()));
                    for(Category category:categoryrList.getPayload().get(0).getCategories())
                    {
                       // Log.d("categoryids",category.getId());
                        categoryList.add(new CategorySummary(category.getId(),category.getNameCh(),category.getNameEn(),category.getImage()));
                    }
                   // categoryList.addAll(categoryrList.getPayload().get(0).getCategories());


                    categoryDetailAdapter.notifyDataSetChanged();

                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message=null;
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
                Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();

            }
        });

        globalProvider.addRequest(utf8JsonRequest);





    }
}
