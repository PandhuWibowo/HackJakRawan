package com.example.root.hackjak;

import com.example.root.hackjak.AllRestAPI.Response;
import com.example.root.hackjak.FullRestAPI.LoginPojo;
import com.example.root.hackjak.FullRestAPI.RegistPojo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by root on 12/6/17.
 */

public interface ApiService {

    @GET("allcoor.php")
    //Call<CoordinatesItem> getCoordinate();
    Call<Response> getProperties();

    @Multipart
    @POST("login.php")
    Call<LoginPojo> loginuser(@Part("email") RequestBody email,
                              @Part("password") RequestBody password);

    @FormUrlEncoded
    @POST("regist.php")
    Call<RegistPojo> regist(@Field("email") String email,
                            @Field("password") String password,
                            @Field("fullname") String fullname,
                            @Field("nik") String nik);

}
