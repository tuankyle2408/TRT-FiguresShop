package com.example.trt_figuresshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.trt_figuresshop.Adapter.GioHangAdapter;
import com.example.trt_figuresshop.Model.EventBus.EventTinhTong;
import com.example.trt_figuresshop.Model.GioHang;
import com.example.trt_figuresshop.R;
import com.example.trt_figuresshop.Retrofit.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangActivity extends AppCompatActivity {
    TextView giohang, tongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button buttonMuaHang;
    GioHangAdapter gioHangAdapter;
    List<GioHang> gioHangList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initControl();
        tinhtongtien();
    }

    private void tinhtongtien() {
        long tongtiensp = 0;
        for (int i = 0; i < Utils.arraygiohang.size();i++){
            tongtiensp = tongtiensp + (Utils.arraygiohang.get(i).getGiasp()* Utils.arraygiohang.get(i).getSoluong());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien.setText(decimalFormat.format(tongtiensp));
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (Utils.arraygiohang.size() == 0 ){
            giohang.setVisibility(View.VISIBLE);
        }else {
            gioHangAdapter = new GioHangAdapter(getApplicationContext(),Utils.arraygiohang);
            recyclerView.setAdapter(gioHangAdapter);
        }

    }

    private void initView() {
        giohang = findViewById(R.id.item_gio_hang_ten);
        toolbar = findViewById(R.id.toolbarGioHang);
        recyclerView = findViewById(R.id.recyclerView_GioHang);
        tongtien = findViewById(R.id.Tong_Tien);
        buttonMuaHang = findViewById(R.id.button_Mua_Hang);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventTinhTong(EventTinhTong eventTinhTong){
        if (eventTinhTong != null){
            tinhtongtien();
        }
    }
}