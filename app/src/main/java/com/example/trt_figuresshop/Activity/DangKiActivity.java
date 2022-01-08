package com.example.trt_figuresshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trt_figuresshop.R;
import com.example.trt_figuresshop.Retrofit.API;
import com.example.trt_figuresshop.Retrofit.RetrofitClient;
import com.example.trt_figuresshop.Retrofit.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKiActivity extends AppCompatActivity {

    EditText email, pass, repass, username, phone;
    AppCompatButton button;
    API api;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
        initView();
        initControl();
    }

    private void initControl() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangki();
            }
        });
    }

    private void dangki() {
        String str_email = email.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_repass = repass.getText().toString().trim();
        String str_phone = phone.getText().toString().trim();
        String str_username = username.getText().toString().trim();
        if (TextUtils.isEmpty(str_email)){
            Toast.makeText(getApplicationContext(), "ban chua nhap Email", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(str_pass)){
            Toast.makeText(getApplicationContext(), "ban chua nhap pass", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(str_repass)){
            Toast.makeText(getApplicationContext(), "ban chua nhap lai pass", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(str_phone)){
            Toast.makeText(getApplicationContext(), "ban chua nhap phone", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(str_username)){
            Toast.makeText(getApplicationContext(), "ban chua nhap username", Toast.LENGTH_SHORT).show();
        }else {
            if (str_pass.equals(str_repass)){
                compositeDisposable.add(api.dangKi(str_email,str_username,str_pass,str_phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()){
                                Utils.user_current.setEmail(str_email);
                                Utils.user_current.setPassword(str_pass);
                                Intent intent = new Intent(getApplicationContext(),DangNhapActivity.class);
                                startActivity(intent);
                                finish();


                                Toast.makeText(getApplicationContext(), "Thanh Cong", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
            }else {
                Toast.makeText(getApplicationContext(), "ban nhap 2 pass ko giong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        api = RetrofitClient.getInstance(Utils.Base_URL).create(API.class);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        repass = findViewById(R.id.repass);
        button = findViewById(R.id.btndangki);
        username = findViewById(R.id.username);
        phone = findViewById(R.id.phone);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}