package com.quy.buvape.api;

import com.quy.buvape.model.Res;
import com.quy.buvape.model.User;
import com.quy.buvape.model.User_Token;

import org.json.JSONArray;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {
    @POST("users/login_phone")
    Call<User> getLogin(@Body User user);

    @POST("users/login_phone")
    Call<User_Token> getLogin1(@Body HashMap map);

    @GET("app/user/{token}")
    Call<User> validateToken(@Path("token") String token);

    @GET("us/{id}")
    Call<User> getUserById(@Path("id") String id);

    @POST("add/app/users")
    Call<User_Token> addUser(@Body User user);

}
