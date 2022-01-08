package com.example.trt_figuresshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.trt_figuresshop.Adapter.GundamAdapter;
import com.example.trt_figuresshop.Adapter.NewProductAdapter;
import com.example.trt_figuresshop.Model.NewProduct;
import com.example.trt_figuresshop.R;
import com.example.trt_figuresshop.Retrofit.API;
import com.example.trt_figuresshop.Retrofit.RetrofitClient;
import com.example.trt_figuresshop.Retrofit.Utils;

import java.util.List;
import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;


public class GundamActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    API api;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int loai;
    GundamAdapter gundamAdapter;
    List<NewProduct> newProductList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gundam);
        api = RetrofitClient.getInstance(Utils.Base_URL).create(API.class);
        loai = getIntent().getIntExtra("loai",2);
        TRT();
        ActionToolBar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading == false) {
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == newProductList.size()-1);
                    isLoading = true;
                    loadMore();
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                newProductList.add(null);
                gundamAdapter.notifyItemInserted(newProductList.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                newProductList.remove(newProductList.size()-1);
                gundamAdapter.notifyItemRemoved(newProductList.size());
                page = page + 1;
                getData(page);
                gundamAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        },2000);
    }

    private void getData(int page) {
        compositeDisposable.add(api.getProduct(page,loai)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                newProductModel -> {
                    if (newProductModel.isSuccess()){
                        if (gundamAdapter == null){
                            newProductList = newProductModel.getResult();
                            gundamAdapter = new GundamAdapter(getApplicationContext(), newProductList);
                            recyclerView.setAdapter(gundamAdapter);
                        }else {
                            int vitri = newProductList.size()-1;
                            int soluongadd = newProductModel.getResult().size();
                            for (int i = 0; i<soluongadd; i++)
                            {
                                newProductList.add(newProductModel.getResult().get(i));
                            }
                            gundamAdapter.notifyItemRangeInserted(vitri,soluongadd);
                        }

                    }else {
                        Toast.makeText(getApplicationContext(), "Đã load hết dữ liêu", Toast.LENGTH_LONG).show();
                        isLoading = true ;
                    }
                },
                throwable -> {
                    Toast.makeText(getApplicationContext(), "Không thể kết nối đến server", Toast.LENGTH_LONG).show();
                }
        )
        );
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void TRT() {
        toolbar = findViewById(R.id.toolbarGundam);
        recyclerView = findViewById(R.id.recyclerView_Gundam);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        newProductList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}