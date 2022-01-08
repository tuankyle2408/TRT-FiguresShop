package com.example.trt_figuresshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.trt_figuresshop.Adapter.NewProductAdapter;
import com.example.trt_figuresshop.Adapter.ProductTypeAdapter;
import com.example.trt_figuresshop.Model.NewProduct;
import com.example.trt_figuresshop.Model.NewProductModel;
import com.example.trt_figuresshop.Model.ProductType;
import com.example.trt_figuresshop.Model.ProductTypeModel;
import com.example.trt_figuresshop.R;
import com.example.trt_figuresshop.Retrofit.API;
import com.example.trt_figuresshop.Retrofit.RetrofitClient;
import com.example.trt_figuresshop.Retrofit.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listViewmain;
    DrawerLayout drawerLayout;
    ProductTypeAdapter productTypeAdapter;
    List<ProductType> arrayProductType;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    API api;
    List<NewProduct> arrayNewProduct;
    NewProductAdapter newProductAdapter;
    NotificationBadge notificationBadge;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api = RetrofitClient.getInstance(Utils.Base_URL).create(API.class);
        TRT();
        ActionBar();
        ActionViewFlipper();
        if (isConnected(this)){
            ActionViewFlipper();
            getProductType();
            getNewProduct();
            getEventClick();
        }else {
            Toast.makeText(getApplicationContext(), "Không có kết nối mạng", Toast.LENGTH_LONG).show();
        }
    }

    private void getEventClick() {
        listViewmain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent TrangChu = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(TrangChu);
                        break;
                    case 1:
                        Intent OP = new Intent(getApplicationContext(),GundamActivity.class);
                        OP.putExtra("loai",1 );
                        startActivity(OP);
                        break;
                    case 2:
                        Intent Gundam = new Intent(getApplicationContext(),GundamActivity.class);
                        Gundam.putExtra("loai",2 );
                        startActivity(Gundam);
                        break;
                    case 3:
                        Intent other = new Intent(getApplicationContext(),GundamActivity.class);
                        other.putExtra("loai",3);
                        startActivity(other);
                        break;
                }
            }
        });
    }

    private void getNewProduct() {
        compositeDisposable.add(api.getNewProduct()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                newProductModel -> {
                    if (newProductModel.isSuccess()){
                        arrayNewProduct = newProductModel.getResult();
                        newProductAdapter = new NewProductAdapter(getApplicationContext(),arrayNewProduct);
                        recyclerView.setAdapter(newProductAdapter);
                    }
                },
                throwable -> {
                    Toast.makeText(getApplicationContext(), "Không thể kết nối đến server"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                }
        ));
    }

    private void getProductType() {
        compositeDisposable.add(api.getProductType()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                productTypeModel -> {
                    if (productTypeModel.isSuccess()){
                        arrayProductType = productTypeModel.getResult();
                        productTypeAdapter = new ProductTypeAdapter(getApplicationContext(),arrayProductType);
                        listViewmain.setAdapter(productTypeAdapter);
                    }
                }
        ));
    }

    private void ActionViewFlipper() {
        List<String> carousel = new ArrayList<>();
        carousel.add("https://www.bluefinbrands.com/media/magefan_blog/Blog_Banner_GundamUniverse6_01_1.jpg");
        carousel.add("https://www.bluefinbrands.com/media/magefan_blog/Blog_Banner_GundamArtifact_01.jpg");
        carousel.add("http://3.bp.blogspot.com/_VuGTEXpGe3o/R9lgZWPemFI/AAAAAAAAAQM/5tPN04yJTWk/S1600-R/Bannernew3.png");
        for (int i = 0; i < carousel.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(carousel.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void TRT() {
        toolbar = findViewById(R.id.toolbarmain);
        viewFlipper = findViewById(R.id.viewflippermain);
        recyclerView = findViewById(R.id.recyview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        navigationView = findViewById(R.id.navigationviewmain);
        listViewmain = findViewById(R.id.listviewmain);
        drawerLayout = findViewById(R.id.drawerlayoutmain);
        arrayProductType = new ArrayList<>();
        arrayNewProduct = new ArrayList<>();
        notificationBadge = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.framechitietgiohang);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });
        if (Utils.arraygiohang == null){
            Utils.arraygiohang = new ArrayList<>();
        }else {
            int totalItem = 0;
            for (int i=0; i<Utils.arraygiohang.size();i++){
                totalItem = totalItem + Utils.arraygiohang.get(i).getSoluong();
            }
            notificationBadge.setText(String.valueOf(totalItem));
        }

    }

    @Override
    protected void onResume() {
        int totalItem = 0;
        for (int i=0; i<Utils.arraygiohang.size();i++){
            totalItem = totalItem + Utils.arraygiohang.get(i).getSoluong();
        }
        notificationBadge.setText(String.valueOf(totalItem));
        super.onResume();
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi != null && wifi.isConnected() || (mobile != null && mobile.isConnected())){
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}