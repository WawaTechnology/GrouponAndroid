package com.easybuy.sg.grouponebuy.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easybuy.sg.grouponebuy.R;

import com.easybuy.sg.grouponebuy.activities.ProductDetailActivity;
import com.easybuy.sg.grouponebuy.activities.SearchActivity;
import com.easybuy.sg.grouponebuy.activities.SpecialSaleLayout;
import com.easybuy.sg.grouponebuy.adapter.CategoryAdapter;
import com.easybuy.sg.grouponebuy.adapter.ProductAdapter;
import com.easybuy.sg.grouponebuy.adapter.ProductDetailViewPagerAdapter;
import com.easybuy.sg.grouponebuy.adapter.SingleTopAdapter;
import com.easybuy.sg.grouponebuy.adapter.SpecialCategoryAdapter;
import com.easybuy.sg.grouponebuy.databinding.ActivityHomeBinding;
import com.easybuy.sg.grouponebuy.helpers.GlobalProvider;
import com.easybuy.sg.grouponebuy.model.CategorySpecial;
import com.easybuy.sg.grouponebuy.model.CategorySummary;
import com.easybuy.sg.grouponebuy.model.Product;
import com.easybuy.sg.grouponebuy.model.ProductImageId;
import com.easybuy.sg.grouponebuy.model.SpecialImage;
import com.easybuy.sg.grouponebuy.network.Constants;
import com.easybuy.sg.grouponebuy.utils.CategoryListener;
import com.easybuy.sg.grouponebuy.utils.ItemOffSetDecoration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;
import static java.lang.Boolean.FALSE;

/**
 * Created by Unsan on 5/6/18.
 */

public class FragmentHome extends Fragment implements CategoryAdapter.MyClickListener,ProductDetailViewPagerAdapter.OnItemClickListener {


    RecyclerView categoryRecyclerView;
    GlobalProvider globalProvider;
    CategoryAdapter categoryAdapter;
    SingleTopAdapter singleTopAdapter;
    SingleTopAdapter singleTopAdapter1;
    SingleTopAdapter singleTopAdapter2;
    List<ProductImageId> productImageIdList;
    List<ProductImageId> singleTopList;
    List<ProductImageId> doubleImageList;
    List<ProductImageId> doubleTopList;
    List<ProductImageId> tripleTopList;
    List<Product> saleProductList;
    Product oneImportantProduct;
    Product oneProduct;
    TimerTask timerTask;
   // boolean isScrolling;






    //SearchView searchView;
    ImageView mainImage;
   String[] imgArray;
    int[] arr;
    int i=0;

    LinearLayout indicatorLayout;

    ActivityHomeBinding activityHomeBinding;
    public CategoryListener categoryListener;
    RecyclerView singleImageRecycler;
    //private CategorySelectedListener fragmentCallback;
    List<CategorySummary> categoryList;
    List<CategorySpecial> categorySpecials;
    SpecialCategoryAdapter specialCategoryAdapter;
    List<ViewGroup> listViews;
    SingleTopAdapter singleTopAdapter0;
    String language;
    Date saleDate;
    private Handler handler = new Handler();
    private Runnable runnable;
    Timer timer;




    ProductDetailViewPagerAdapter productDetailViewPagerAdapter;




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // MyApplication.getRefWatcher(getActivity()).watch(this);
        globalProvider=GlobalProvider.getGlobalProviderInstance(getContext().getApplicationContext());

        singleTopList=new ArrayList();
        productImageIdList=new ArrayList<>();
        tripleTopList=new ArrayList<>();
        doubleImageList=new ArrayList<>();
        listViews=new ArrayList<>();
        doubleTopList=new ArrayList<>();
        saleProductList=new ArrayList<>();

        language=Constants.getLanguage(getContext().getApplicationContext());


        categoryList=new ArrayList();
        categorySpecials=new ArrayList<>();



            imgArray = new String[globalProvider.specialMImages.size()];

            for (int i = 0; i < imgArray.length; i++) {
                imgArray[i] = Constants.baseUrlStr + globalProvider.specialMImages.get(i).getImageEntryCh();
                Log.d("checkimae",imgArray[i]);

            }






    }

    public FragmentHome() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        activityHomeBinding = DataBindingUtil.inflate(inflater, R.layout.activity_home, null, false);
        View view = activityHomeBinding.getRoot();








        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
     //   ((Activity) getContext()). getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
       // int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        activityHomeBinding.relativeLayout.getLayoutParams().height= (int) (width/2.2);
        activityHomeBinding.relativeLayout.requestLayout();
        activityHomeBinding.singleOneimage.getLayoutParams().height = width / 2;
        activityHomeBinding.singleOneimage.requestLayout();
        activityHomeBinding.singleImpimage.getLayoutParams().height = width / 2;
        activityHomeBinding.singleImpimage.requestLayout();




        float searchHeight = 360 / displayMetrics.density;
        Log.d("getv", searchHeight + "");
        if(globalProvider.getFlashSale()!=null)
        {
            activityHomeBinding.flashsalelayout.setVisibility(View.VISIBLE);



                saleDate=globalProvider.saleDate;
                saleProductList.addAll(globalProvider.getFlashSale().getProductList());
            ProductAdapter productAdapter=new ProductAdapter(getContext(),saleProductList);
            activityHomeBinding.saleProductRecycler.setAdapter(productAdapter);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
            activityHomeBinding.saleProductRecycler.setLayoutManager(linearLayoutManager);


           // countDownStart();




        }
        for(ProductImageId productImageId:globalProvider.singleProductList) {
            if (productImageId.getCategory().equals("one-important")) {
                Log.d("oneimp","visible");

                String cover = Constants.baseUrlStr + productImageId.getProductCover();
                Glide.with(getContext()).load(cover).asBitmap().format(PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ebuylogo).fitCenter().into(activityHomeBinding.singleImpimage);
                activityHomeBinding.singleImpimage.setVisibility(View.VISIBLE);
                oneImportantProduct=productImageId.getProduct();



            }
            if (productImageId.getCategory().equals("one")) {
                Log.d("onesingle","visible");

                String cover = Constants.baseUrlStr + productImageId.getProductCover();
                Glide.with(getContext()).load(cover).asBitmap().format(PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ebuylogo).fitCenter().into(activityHomeBinding.singleOneimage);
                activityHomeBinding.singleOneimage.setVisibility(View.VISIBLE);
                oneProduct=productImageId.getProduct();



            }
        }

        if(oneImportantProduct==null)
        {
            activityHomeBinding.singleImpimage.setVisibility(View.GONE);
        }
        if(oneProduct==null)
        {
            activityHomeBinding.singleOneimage.setVisibility(View.GONE);
        }


    activityHomeBinding.singleImpimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                intent.putExtra("product", oneImportantProduct);
                getContext().startActivity(intent);
            }


        });
        activityHomeBinding.singleOneimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                intent.putExtra("product", oneProduct);
                getContext().startActivity(intent);
            }

        });





        //adding special images with indicator

    /* int heightViewPager= (int) (width/1.2);
        activityHomeBinding.viewPager.getLayoutParams().height=heightViewPager;
        activityHomeBinding.viewPager.requestLayout();
        */



        if(imgArray!=null&&imgArray.length>0)
        {
            for (int a = 0; a < imgArray.length; a++) {
                ViewGroup pager = (ViewGroup) getLayoutInflater().inflate(R.layout.viewpager_image, null);

                ImageView imageView = (ImageView) pager.findViewById(R.id.viewpagerimg);
                Log.d("checkimage",imgArray[a]);

                Glide.with(container.getContext()).load(imgArray[a]).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
                listViews.add(pager);


            }
            productDetailViewPagerAdapter = new ProductDetailViewPagerAdapter(listViews,this);
            activityHomeBinding.viewPager.setAdapter(productDetailViewPagerAdapter);

            if (imgArray.length > 1) {
                for (int a = 0; a < imgArray.length; a++) {
                    if (a == 0) {
                        ImageView imgView = new ImageView(getContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(18, 18);
                        imgView.setLayoutParams(lp);
                        imgView.setImageResource(R.drawable.selected_dot);
                        activityHomeBinding.indicator.addView(imgView);




                    } else {
                        ImageView imgView = new ImageView(getContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(18, 18);
                        lp.setMargins(20, 0, 0, 0);
                        imgView.setLayoutParams(lp);
                        imgView.setImageResource(R.drawable.default_dot);
                        // imgs.add(img);
                        activityHomeBinding.indicator.addView(imgView);


                    }

                }
            } else {

                ImageView img = new ImageView(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                        (18, 18);
                //lp.setMargins(5,0,0,0);
                img.setLayoutParams(lp);
                img.setImageResource(R.drawable.selected_dot);
                //imgs.add(img);
                activityHomeBinding.indicator.addView(img);

            }
        }
       // startViewPagerTimer();

   /*     if(language.equals("english"))
        {
           activityHomeBinding.catgoryRecycler.getLayoutParams().height=(width/2)+20;
           activityHomeBinding.catgoryRecycler.requestLayout();
        }
        else
        {
            activityHomeBinding.catgoryRecycler.getLayoutParams().height=(width/2);
            activityHomeBinding.catgoryRecycler.requestLayout();
        }
        */
        singleTopList.addAll(globalProvider.singleProductList);
        doubleImageList.addAll(globalProvider.doubleProductImageList);
        productImageIdList.addAll(globalProvider.threeImageLayout);
        doubleTopList.addAll(globalProvider.doubleProductList);
        tripleTopList.addAll(globalProvider.threeTopImageLayout);

        singleTopAdapter0=new SingleTopAdapter(getContext(),doubleTopList);
         singleTopAdapter=new SingleTopAdapter(getContext(),singleTopList);


      singleTopAdapter1=new SingleTopAdapter(getContext(),productImageIdList);
      singleTopAdapter2=new SingleTopAdapter(getContext(),doubleImageList);
      SingleTopAdapter singleTopAdapter3=new SingleTopAdapter(getContext(),tripleTopList);


 //setting adapter for 3 image layout
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        activityHomeBinding.recyclerThreelayout.setAdapter(singleTopAdapter1);
        activityHomeBinding.recyclerThreelayout.setLayoutManager(staggeredGridLayoutManager);
        //for three top

        StaggeredGridLayoutManager staggeredGridLayoutManager1 =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        activityHomeBinding.recyclerThreeTop.setAdapter(singleTopAdapter3);
        activityHomeBinding.recyclerThreeTop.setLayoutManager(staggeredGridLayoutManager1);
        //setting adapter for single images
      //  LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
       // activityHomeBinding.singleToprecycler.setLayoutManager(linearLayoutManager);
        //activityHomeBinding.singleToprecycler.setAdapter(singleTopAdapter);

        int bannerHeight= (int) (width/3.3);
        activityHomeBinding.specialBanner.getLayoutParams().height=bannerHeight;
        activityHomeBinding.specialBanner.requestLayout();

        Glide.with(getContext()).load(Constants.baseUrlStr+globalProvider.specialBanner.getProductCover()).asBitmap().format(PREFER_ARGB_8888).fitCenter().diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ebuylogo).into(activityHomeBinding.specialBanner);
        Log.d("checkimage",Constants.baseUrlStr+globalProvider.specialBanner.getProductCover());
        activityHomeBinding.specialBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ProductDetailActivity.class);
                intent.putExtra("product",globalProvider.specialBanner.getProduct());
                startActivity(intent);
            }
        });



  //setting adapter for double images
       // int hieght= (int) (width*0.6);
       // activityHomeBinding.recyclerDoubleTop.getLayoutParams().height=hieght;

        GridLayoutManager gridManager=new GridLayoutManager(getContext(),2);
        activityHomeBinding.recyclerDoubleTop.setLayoutManager(gridManager);
        activityHomeBinding.recyclerDoubleTop.setAdapter(singleTopAdapter0);



    GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);



     activityHomeBinding.doubleBottomrecycler.setLayoutManager(gridLayoutManager);


        activityHomeBinding.doubleBottomrecycler.setAdapter(singleTopAdapter2);



        activityHomeBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()

        {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < activityHomeBinding.indicator.getChildCount(); i++) {
                    if (i == position) {
                        ((ImageView) activityHomeBinding.indicator.getChildAt(i)).setImageResource(R.drawable.selected_dot);

                    } else {
                        ((ImageView) activityHomeBinding.indicator.getChildAt(i)).setImageResource(R.drawable.default_dot);
                    }
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state==SCROLL_STATE_IDLE)
                {

                /*  if(timer==null)

                        startViewPagerTimer();
                        */


                }

                if(state==SCROLL_STATE_DRAGGING)
                {

                  //  timer.cancel();
                   // timer.purge();
                }

            }
        });

        categoryAdapter=new CategoryAdapter(getContext(),categoryList,this);
        activityHomeBinding.catgoryRecycler.setAdapter(categoryAdapter);

        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getActivity(),4)
        {
            @Override
            public boolean canScrollVertically() {
                return false;
            }};
        activityHomeBinding.catgoryRecycler.setLayoutManager(layoutManager);

        ItemOffSetDecoration itemDecoration = new ItemOffSetDecoration(getContext(), R.dimen.item_offset);
        activityHomeBinding.catgoryRecycler.addItemDecoration(itemDecoration);


      //  ProductAdapter productAdapter = new ProductAdapter(getActivity(), basicProductList);
         specialCategoryAdapter=new SpecialCategoryAdapter(getActivity(),categorySpecials);
        activityHomeBinding.recyclerSpecial.setAdapter(specialCategoryAdapter);
        activityHomeBinding.searchbarHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });



        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        activityHomeBinding.recyclerSpecial.setLayoutManager(linearLayoutManager1);
        activityHomeBinding.recyclerSpecial.setNestedScrollingEnabled(FALSE);



        getCategories();
        getSpecialCategories();



        return view;
    }
    public void onResume()
    {
        if(saleDate!=null) {
            countDownStart();
        }
        if(timerTask==null)
        startViewPagerTimer();
        super.onResume();


    }
    private void startViewPagerTimer()
    {
         timerTask = new TimerTask() {
            @Override
            public void run() {
                activityHomeBinding.viewPager.post(new Runnable(){

                    @Override
                    public void run() {
                      //  isScrolling=true;
                        activityHomeBinding.viewPager.setCurrentItem((activityHomeBinding.viewPager.getCurrentItem()+1)%imgArray.length);
                    }
                });
            }
        };
        timer = new Timer();


        timer.schedule(timerTask, 5000, 5000);
    }
    private void countDownStart() {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    Date today=new Date();
                    long secs = (saleDate.getTime() - today.getTime()) / 1000;
                    int hours =(int) secs / 3600;
                    secs = secs % 3600;
                    int mins =(int) secs / 60;
                    secs = secs % 60;
                    String format="";
                    if(hours<10)
                    {
                        format+="0";
                    }
                    format+=hours+":";
                    if(mins<10)
                    {
                        format+="0";
                    }
                    format+=mins+":";
                    if(secs<10)
                    {
                        format+="0";
                    }
                    format+=secs;
                    activityHomeBinding.timeRemaining.setText(format);



                   // activityHomeBinding.hh.setText(hours+"");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
        if(timer!=null) {
            timer.cancel();
            timer.purge();
            timer=null;

        }
        if(timerTask!=null)
        {
            timerTask.cancel();
            timerTask=null;


        }
    }


    private void getSpecialCategories() {
        if(globalProvider.categorySpecialList.size()>0&&categorySpecials.size()<=0) {


                categorySpecials.addAll(globalProvider.categorySpecialList);




        }



        /*
        Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.GET, Constants.specialCategoryUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("checkresponse",response);
                JsonFactory jsonFactory = new JsonFactory();
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonParser jsonParser = jsonFactory.createParser(response);
                    SpecialCategoryList splcategories = (SpecialCategoryList) objectMapper.readValue(jsonParser, SpecialCategoryList.class);
                    Log.d("getstatus",splcategories.getStatus()+"");
                    int status=splcategories.getStatus();
                    if(status==0) {
                        List<CategorySpecial> categorySpecialList = splcategories.getCategorySpecialList();
                        for (CategorySpecial cs:categorySpecialList) {
                            Log.d("SPLNAME",cs.getNameEn());
                            categorySpecials.add(cs);
                        }
                        specialCategoryAdapter.notifyDataSetChanged();

                    }

                    else if(status==2)
                    {
                        Toast.makeText(getContext(),splcategories.getMsg(),Toast.LENGTH_LONG).show();
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
        */

    }

    private void getCategories() {

                     if(globalProvider.categoryList.size()>0) {
                         for(int i=0;i<8;i++)
                         {
                             categoryList.add(new CategorySummary(globalProvider.categoryList.get(i).getId(),globalProvider.categoryList.get(i).getNameCh(),globalProvider.categoryList.get(i).getNameEn(),globalProvider.categoryImageList.get(i).getImageEn()));
                            // categoryList.add(globalProvider.categoryList.get(i));
                         }



                     }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      /*  if(context instanceof CategorySelectedListener){
            fragmentCallback = (CategorySelectedListener)context;
        }
        */
        if (context instanceof CategoryListener) {
            categoryListener = (CategoryListener) context;
        }
    }

    @Override
    public void onCategorySelected() {
       // fragmentCallback.onSelectedCategory();
        categoryListener.onSelectedCategory();


    }


    @Override
    public void onItemClick(int position) {
        timer.cancel();
        Log.d("clicked at pos",position+"");
        SpecialImage specialImage=globalProvider.specialMImages.get(position);
        Intent intent=new Intent(getContext(),SpecialSaleLayout.class);
        intent.putExtra("specialImage",specialImage);
        startActivity(intent);


    }
}


