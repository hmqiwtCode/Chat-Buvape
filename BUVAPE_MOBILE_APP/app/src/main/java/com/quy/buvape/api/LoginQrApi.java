package com.quy.buvape.api;

import com.quy.buvape.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LoginQrApi {

    @GET("loginQR")
    Call<String> testCall();
}
