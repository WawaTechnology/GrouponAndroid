package com.example.unsan.grouponebuy.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.example.unsan.grouponebuy.adapter.OrderAdapter;
import com.example.unsan.grouponebuy.helpers.CustomRequest;
import com.example.unsan.grouponebuy.helpers.GlobalProvider;
import com.example.unsan.grouponebuy.model.Order;
import com.example.unsan.grouponebuy.model.ResultOrder;
import com.example.unsan.grouponebuy.network.Constants;
import com.example.unsan.grouponebuy.utils.CategoryListener;
import com.example.unsan.grouponebuy.utils.CircleBadgeView;
import com.example.unsan.grouponebuy.utils.PaginationScrollListener;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentOrderTOne extends Fragment {
    List<Order> orderList;
  //  List<Order> allOrderList;
    RecyclerView orderRecyclerView;
    OrderAdapter orderAdapter;
    LinearLayout noOrderLayOut;
    CircleBadgeView circleBadgeView;
    CustomRequest customRequest;
    public static final String TAG = "OrderTag";
    int page = 1;
    int pageSize = 5;
    int TOTAL_PAGE = 2;
    boolean isLoading, isLastPage;
    GlobalProvider globalProvider;
    ImageView processingImage, paymentImage, allOrderImage;
    TextView processingText, paymenttext, allordertext;
    LinearLayout allOrderLayout, paymentLayout, processingLayout;
    ColorStateList oldColors;
    int TAB_PRESSED = 1;
    Button submitButton;
    TextView paymentBadge;
    final int Order_Code=113;
    public CategoryListener categoryListener;
  //  private CircleBadgeView numView;
   // private CircleBadgeView processingView;
    private  int DEFAULT_LR_PADDING_DIP = 5;
    TextView badgeText;
    



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MyApplication.getRefWatcher(getActivity()).watch(this);
        orderList = new ArrayList<>();
      //  allOrderList=new ArrayList<>();
        globalProvider = GlobalProvider.getGlobalProviderInstance(getContext().getApplicationContext());


       /* for (int i = 0; i < 2; i++) {
            int orderNumber = 123456 + i;


            Order order = new Order(orderNumber, "05-06-2018", "05:40", "06-06-2018", "12:30", "delivered");
            orderList.add(order);
        }
        */

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CategoryListener) {
            categoryListener = (CategoryListener) context;
        }
    }


    public FragmentOrderTOne() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ordertone, container, false);
        allOrderLayout = (LinearLayout) view.findViewById(R.id.alllayout);
        paymentLayout = (LinearLayout) view.findViewById(R.id.paymentlayout);
        paymentBadge=(TextView) view.findViewById(R.id.paymentbadge);
        badgeText=(TextView) view.findViewById(R.id.badge);
        processingLayout = (LinearLayout) view.findViewById(R.id.processinglayout);
        noOrderLayOut = (LinearLayout) view.findViewById(R.id.no_orderlayout);
        processingImage = (ImageView) view.findViewById(R.id.processingImage);
        allOrderImage = (ImageView) view.findViewById(R.id.allorderimage);
        paymentImage = (ImageView) view.findViewById(R.id.paymentimage);
        processingText = (TextView) view.findViewById(R.id.processingText);
        paymenttext = (TextView) view.findViewById(R.id.paymenttext);
        allordertext = (TextView) view.findViewById(R.id.allordertext);
        submitButton=(Button)view.findViewById(R.id.submit);
       // numView = new CircleBadgeView(getContext(), paymentImage);
      //  numView.setTextColor(Color.WHITE);
      //  numView.setBackgroundColor(Color.RED);
      //  processingView = new CircleBadgeView(getContext(), processingImage);
       // processingView.setTextColor(Color.WHITE);
       // processingView.setBackgroundColor(Color.RED);

        oldColors = paymenttext.getTextColors();


        orderRecyclerView = (RecyclerView) view.findViewById(R.id.status_recycler);
        orderAdapter = new OrderAdapter(getActivity(), orderList);
        orderRecyclerView.setAdapter(orderAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        orderRecyclerView.setLayoutManager(linearLayoutManager);
        orderRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                Log.d("loadmore","called");
                isLoading = true;
                page += 1;

                // mocking network delay for API call
            /*   new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
                */

               loadNextPage();

            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGE;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
   //     processingText.setTextColor(getResources().getColor(R.color.red));
      //  processingImage.setImageResource(R.drawable.shipping);


        if(!globalProvider.isLogin())
        {
            noOrderLayOut.setVisibility(View.VISIBLE);
            orderRecyclerView.setVisibility(View.GONE);
        }
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    categoryListener.onSelectedCategory();





            }
        });
        allordertext.setTextColor(getResources().getColor(R.color.red));
        allOrderImage.setImageResource(R.drawable.orders);
       // else
           // getOrders();



        processingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TAB_PRESSED = 0;
                isLoading = false;
                isLastPage = false;
                TOTAL_PAGE = 2;
                page = 1;
                orderList.clear();
                orderAdapter.notifyDataSetChanged();
               getOrders();


                paymenttext.setTextColor(oldColors);
               // paymentImage.setImageResource(R.drawable.card);
                processingText.setTextColor(getResources().getColor(R.color.red));
               // processingImage.setImageResource(R.drawable.shipping);
                allordertext.setTextColor(oldColors);
              //  allOrderImage.setImageResource(R.drawable.lst);


            }
        });
        allOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TAB_PRESSED = 1;
                TOTAL_PAGE = 2;
                page = 1;

                isLoading = false;
                isLastPage = false;
                orderList.clear();
                orderAdapter.notifyDataSetChanged();
                getOrders();
                paymenttext.setTextColor(oldColors);
               // paymentImage.setImageResource(R.drawable.card);
                processingText.setTextColor(oldColors);
              //  processingImage.setImageResource(R.drawable.truck);
                allordertext.setTextColor(getResources().getColor(R.color.red));
               // allOrderImage.setImageResource(R.drawable.orders);

                // getAllOrders();

            }
        });
        paymentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TAB_PRESSED = 2;
                isLoading = false;
                isLastPage = false;
                TOTAL_PAGE = 1;
                page = 1;
                orderList.clear();
                orderAdapter.notifyDataSetChanged();
                getOrders();
               /* orderList.clear();
                for(Order order:allOrderList)
                {
                    if(order.getState().equals("waiting"))
                    {
                        orderList.add(order);
                    }
                }
                */
                // getPendingPaymentOrders();
                paymenttext.setTextColor(getResources().getColor(R.color.red));
               // paymentImage.setImageResource(R.drawable.payment);
                processingText.setTextColor(oldColors);
               // processingImage.setImageResource(R.drawable.truck);
                allordertext.setTextColor(oldColors);
               // allOrderImage.setImageResource(R.drawable.lst);


            }
        });

        return view;
    }

    private void loadNextPage() {
        Log.d("called", "loadNextPage");
        if (globalProvider.isLogin()) {
            String url = Constants.getOrderUrl + page + "/" + pageSize;
            Map<String, String> params = new HashMap<>();

            params.put("state", "all");
            params.put("userID", globalProvider.getCustomer().customer_id);
            Log.d("url",url);
            Log.d("userId",globalProvider.getCustomer().customer_id);
            CustomRequest customRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonFactory jsonFactory = new JsonFactory();
                  //  Log.d("checkresponse", response.toString());
                    try {
                        JsonParser jsonParser = jsonFactory.createParser(response.toString());
                        ResultOrder resultOrder = (ResultOrder) objectMapper.readValue(jsonParser, ResultOrder.class);
                        Log.d("ostatus",resultOrder.getStatus()+"");
                        Log.d("resultorder",response.toString());
                        orderAdapter.removeLoadingFooter();
                        if (resultOrder.getStatus() == 0) {


                            orderList.addAll(resultOrder.getOrder());
                            orderAdapter.notifyDataSetChanged();
                            noOrderLayOut.setVisibility(View.GONE);
                            orderRecyclerView.setVisibility(View.VISIBLE);
                           Log.d("checkresultsize",resultOrder.getOrder().size()+"");
                            isLoading = false;

                            if (pageSize <= resultOrder.getOrder().size()) {
                                orderAdapter.addLoadingFooter();
                                TOTAL_PAGE+=1;
                            }
                            else
                                isLastPage = true;

                            Log.d("checkisLatP",isLastPage+"");
                            Log.d("isLoading",isLoading+"");
                        } else {
                            if (resultOrder.getStatus() == 2) {
                                isLastPage = true;
                                orderAdapter.isLoadingAdded=false;
                                //orderAdapter.removeLoadingFooter();
                                orderAdapter.notifyDataSetChanged();
                                return;


                            }
                        }
                    } catch (IOException e) {
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



    public void onResume()
    {
        super.onResume();
        Log.d("resumecalledhere","here");
      /*  if(globalProvider.getDeletedOrder()!=null)
        {
            Order order=globalProvider.getDeletedOrder();
            Log.d("checkdeleteorderid",order.getId());
           for(Order o:orderList)
           {
               if(o.getId().equals(order.getId()))
               {
                   orderList.remove(o);
                   orderAdapter.notifyDataSetChanged();
                   break;
               }
           }
            Log.d("orderemoved","here");

            globalProvider.setDeletedOrder(null);
        }
        */
      if(globalProvider.isLogin())
      {
          Log.d("onresumeorder","called");
          Activity activity = getActivity();
          if(isAdded()&&activity!=null) {

              getBadge();
              orderList.clear();
              orderAdapter.notifyDataSetChanged();
              getOrders();
          }
      }


    }
    public void getBadge()
    {


        String url=Constants.orderBadgeUrl;
        Log.d("checkurl",url);
        Map<String,String> params=new HashMap<>();
        params.put("userID", globalProvider.getCustomer().customer_id);
        Log.d("userid",globalProvider.getCustomer().customer_id);
        CustomRequest customRequest=new CustomRequest(Request.Method.POST, url,params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
              //  Log.d("checkbadgeres",response.toString());
                try {
                    if (response.getInt("status") == 0) {
                        int waitingPay = response.getJSONObject("payload").getInt("waitingPayNumber");
                        int waitingProcessing = response.getJSONObject("payload").getInt("waitingNumber");
                        if (waitingPay > 0) {
                            paymentBadge.setTextColor(getResources().getColor(R.color.white));
                            paymentBadge.setText(waitingPay + "");
                            paymentBadge.setVisibility(View.VISIBLE);

                           /* numView.setText(waitingPay + "");//


                                DEFAULT_LR_PADDING_DIP = 5;
                                int paddingPixels_ = dipToPixels(DEFAULT_LR_PADDING_DIP);
                                numView.setPadding(paddingPixels_, 0, paddingPixels_, 0);

                                // buyNumView.setPadding(paddingPixels_, 0, 0, 0);
                                numView.setTextSize(11);

                               // numView.setBadgeMargin();
                            numView.setBadgeMargin(8, 0);



                            //buyNumView.setTextSize(12);
                            numView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                            numView.setGravity(Gravity.RIGHT);
                            numView.show();
                            */

                            Log.d("getwaitingPay", waitingPay + "");
                        }
                        else {
                           // numView.hide();
                            paymentBadge.setVisibility(View.INVISIBLE);
                        }
                        if(waitingProcessing>0)
                        {
                            badgeText.setTextColor(getResources().getColor(R.color.white));
                            badgeText.setText(waitingProcessing + "");
                            badgeText.setVisibility(View.VISIBLE);
                            /*processingView.setText(waitingProcessing + "");//


                            DEFAULT_LR_PADDING_DIP = 5;
                            int paddingPixels_ = dipToPixels(DEFAULT_LR_PADDING_DIP);
                            processingView.setPadding(paddingPixels_, 0, paddingPixels_, 0);

                            processingView.setTextSize(11);



                            //buyNumView.setTextSize(12);
                           // processingView.setBadgeMargin(8,0);
                            processingView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                            processingView.setGravity(Gravity.CENTER);
                            processingView.show();
                            */
                        }
                        else {
                            badgeText.setVisibility(View.GONE);
                           // processingView.hide();
                        }
                    }
                }catch (JSONException e) {
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
    public void onStop()
    {
        if(globalProvider.getRequestQueue()!=null)
        {
            globalProvider.getRequestQueue().cancelAll(TAG);
        }
        super.onStop();
    }


    public void getOrders() {



        if (globalProvider.isLogin()) {
            String url = Constants.getOrderUrl + page + "/" + pageSize;

            Log.d("userId", globalProvider.getCustomer().customer_id);
            Map<String, String> params = new HashMap<>();
          if(TAB_PRESSED==1) {
                params.put("state", "all");
            }
            else if(TAB_PRESSED==2)
            {
                params.put("state","waitingPay");
            }
            else
            {
                params.put("state","waiting");
            }


            params.put("userID", globalProvider.getCustomer().customer_id);

             customRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonFactory jsonFactory = new JsonFactory();
                   // Log.d("checkorderresponse", response.toString());
                    try {
                        JsonParser jsonParser = jsonFactory.createParser(response.toString());
                        ResultOrder resultOrder = (ResultOrder) objectMapper.readValue(jsonParser, ResultOrder.class);
                        if (resultOrder.getStatus() == 0) {
                         /*   if (TAB_PRESSED == 1) {
                                orderList.addAll(resultOrder.getOrder());
                            } else if (TAB_PRESSED == 2) {
                                for (Order order : resultOrder.getOrder()) {
                                    if (order.getTotalPriceActual() == null) {
                                        Log.d("checkorderid", order.getId());
                                        orderList.add(order);
                                    }
                                }
                            } else {
                                for (Order order : resultOrder.getOrder()) {
                                    if (order.getState().equalsIgnoreCase("waiting")) {
                                        orderList.add(order);
                                    }
                                }
                            }
                            */

                            orderList.addAll(resultOrder.getOrder());
                           // Log.d("orderleistsize",orderList.size()+"");
                           // orderAdapter.notifyDataSetChanged();

                            orderAdapter.notifyDataSetChanged();


                            if (orderList.size() <= 0) {
                                noOrderLayOut.setVisibility(View.VISIBLE);
                                orderRecyclerView.setVisibility(View.GONE);
                            } else {
                                noOrderLayOut.setVisibility(View.GONE);

                                orderRecyclerView.setVisibility(View.VISIBLE);
                                Log.d("checkorders",orderList.size()+"");

                                if (pageSize<=orderList.size()) {
                                    orderAdapter.addLoadingFooter();
                                    Log.d("footeradded","here");
                                    Log.d("isLoading1",isLoading+"");
                                    Log.d("isLastpage",isLastPage+"");
                                }
                                else {
                                    isLastPage = true;
                                    orderAdapter.isLoadingAdded=false;
                                }

                            }
                           // Log.d("isLastPage",isLastPage+"");


                        }
                        else if(resultOrder.getStatus()==2)
                        {
                            noOrderLayOut.setVisibility(View.VISIBLE);
                            orderRecyclerView.setVisibility(View.GONE);
                            isLastPage = true;

                        }
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
            customRequest.setTag(TAG);
            globalProvider.addRequest(customRequest);


        }

    }



/*
    public void setCartNum() {
        Log.d("cartset","here");
        if(!globalProvider.cartList.isEmpty()) {

            numView.setText(globalProvider.cartList.size() + "");//

            if (globalProvider.cartList.size() >= 10) {

                DEFAULT_LR_PADDING_DIP = 3;
                int paddingPixels = dipToPixels(DEFAULT_LR_PADDING_DIP);
                numView.setPadding(paddingPixels, 0, paddingPixels, 0);
                numView.setTextSize(10);

            } else {
                DEFAULT_LR_PADDING_DIP = 5;
                int paddingPixels_ = dipToPixels(DEFAULT_LR_PADDING_DIP);
                numView.setPadding(paddingPixels_, 0, paddingPixels_, 0);
                numView.setTextSize(11);

            }

            //numView.setTextSize(12);
            numView.setBadgePosition(CircleBadgeView.POSITION_TOP_RIGHT);
            numView.setGravity(Gravity.CENTER);
            numView.show();
        }
        else
            HideCartNum();


    }

    public void HideCartNum(){
        numView.hide();

    }
    private int dipToPixels(int dip) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        return (int) px;
    }
    */


   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==Order_Code) {

                if (resultCode == Activity.RESULT_OK) {
                   Order order=(Order) data.getSerializableExtra("ordercancelled");
                   orderList.remove(order);
                   orderAdapter.notifyDataSetChanged();
                }



        }
    }
    */
   private int dipToPixels(int dip) {
       Resources r = getResources();
       float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
       return (int) px;
   }
}
