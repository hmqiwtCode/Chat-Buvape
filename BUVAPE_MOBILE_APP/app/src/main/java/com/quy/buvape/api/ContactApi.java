package com.quy.buvape.api;

import com.quy.buvape.model.Contact;
import com.quy.buvape.model.Friend;
import com.quy.buvape.model.User;
import com.quy.buvape.model.User_Token;

import java.util.HashMap;
import java.util.List;
import io.reactivex.Observable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ContactApi {
    @GET("user_custom/{phone}")
    Call<List<Contact>> addContacts(@Path("phone") String phone);

    @POST("contact")
    Call<ResponseBody> addContact(@Body HashMap map);

    @GET("friend/{userId}")
    Call<List<Friend>> getFriend(@Path("userId") String userId);


}

