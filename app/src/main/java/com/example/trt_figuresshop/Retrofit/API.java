package com.example.trt_figuresshop.Retrofit;

import com.example.trt_figuresshop.Model.NewProductModel;
import com.example.trt_figuresshop.Model.ProductTypeModel;
import com.example.trt_figuresshop.Model.UserModel;


import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API {
    @GET("getsanpham.php")
    Observable<ProductTypeModel> getProductType();

    @GET("getsanphammoi.php")
    Observable<NewProductModel> getNewProduct();

    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<NewProductModel> getProduct(
            @Field("page") int page,
            @Field("loai") int loai
    );

    @POST("dangki.php")
    @FormUrlEncoded
    Observable<UserModel> dangKi(
            @Field("email") String email,
            @Field("username") String username,
            @Field("pass") String pass,
            @Field("phone") String phone
    );
}
