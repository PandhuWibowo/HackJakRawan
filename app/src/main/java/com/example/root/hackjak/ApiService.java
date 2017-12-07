package com.example.root.hackjak;

import com.example.root.hackjak.AllRestAPI.Response;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by root on 12/6/17.
 */

public interface ApiService {

    @GET("allcoor.php")
    //Call<CoordinatesItem> getCoordinate();
    Call<Response> getProperties();
}
