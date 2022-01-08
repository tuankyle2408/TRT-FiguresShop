package com.example.trt_figuresshop.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.trt_figuresshop.Model.GioHang;
import com.example.trt_figuresshop.Model.NewProduct;
import com.example.trt_figuresshop.R;
import com.example.trt_figuresshop.Retrofit.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.lang.reflect.Array;
import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {

    TextView tenfigures, gia, mota;
    Button button;
    ImageView imagechitiet;
    Spinner spinner;
    Toolbar toolbar;
    NewProduct newProduct;
    NotificationBadge notificationBadge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        initData();
        ActionToolBar();
        initControl();
    }

    private void initControl() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themgiohang();
            }
        });
    }

    private void themgiohang() {
        if (Utils.arraygiohang.size()>0){
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i = 0; i < Utils.arraygiohang.size();i++){
                if (Utils.arraygiohang.get(i).getIdsp()== newProduct.getId()){
                    Utils.arraygiohang.get(i).setSoluong(soluong + Utils.arraygiohang.get(i).getSoluong());
                    long gia = Long.parseLong(newProduct.getGiafigure()) * Utils.arraygiohang.get(i).getSoluong();
                    Utils.arraygiohang.get(i).getGiasp();
                    flag = true;
                }
            }
            if (flag == false){
                long gia = Long.parseLong(newProduct.getGiafigure()) * soluong;
                GioHang gioHang = new GioHang();
                gioHang.setGiasp(gia);
                gioHang.setSoluong(soluong);
                gioHang.setIdsp(newProduct.getId());
                gioHang.setTensp(newProduct.getTenfigures());
                gioHang.setHinh(newProduct.getHinh());
                Utils.arraygiohang.add(gioHang);
            }
        }else {
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(newProduct.getGiafigure()) * soluong;
            GioHang gioHang = new GioHang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setIdsp(newProduct.getId());
            gioHang.setTensp(newProduct.getTenfigures());
            gioHang.setHinh(newProduct.getHinh());
            Utils.arraygiohang.add(gioHang);
        }
        int totalItem = 0;
        for (int i=0; i<Utils.arraygiohang.size();i++){
            totalItem = totalItem + Utils.arraygiohang.get(i).getSoluong();
        }
        notificationBadge.setText(String.valueOf(totalItem));
    }

    private void initData() {
        newProduct = (NewProduct) getIntent().getSerializableExtra("chitiet");
        tenfigures.setText(newProduct.getTenfigures());
        mota.setText(newProduct.getMota());
        Glide.with(getApplicationContext()).load(newProduct.getHinh()).into(imagechitiet);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        gia.setText("Giá:"+decimalFormat.format(Double.parseDouble(newProduct.getGiafigure()))+"Đ");
        Integer[] integers = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,11,12,13,14,15,16,17,18,19,20};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,integers);
        spinner.setAdapter(adapterspin);
    }

    private void initView() {
        tenfigures = findViewById(R.id.item_chi_tiet_ten);
        gia = findViewById(R.id.item_chi_tiet_gia);
        mota = findViewById(R.id.item_chi_tiet_mota);
        button = findViewById(R.id.item_chi_tiet_button);
        spinner = findViewById(R.id.spinner);
        imagechitiet = findViewById(R.id.item_chi_tiet_hinh);
        toolbar = findViewById(R.id.toolbarDetail);
        notificationBadge = findViewById(R.id.menu_sl);
        FrameLayout frameLayout = findViewById(R.id.framechitietgiohang);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });
        if (Utils.arraygiohang != null){
            int totalItem = 0;
            for (int i=0; i<Utils.arraygiohang.size();i++){
                totalItem = totalItem + Utils.arraygiohang.get(i).getSoluong();
            }
            notificationBadge.setText(String.valueOf(totalItem));
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.arraygiohang != null){
            int totalItem = 0;
            for (int i=0; i<Utils.arraygiohang.size();i++){
                totalItem = totalItem + Utils.arraygiohang.get(i).getSoluong();
            }
            notificationBadge.setText(String.valueOf(totalItem));
        }
    }
}