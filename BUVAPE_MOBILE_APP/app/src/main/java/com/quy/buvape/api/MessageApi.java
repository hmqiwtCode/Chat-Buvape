package com.quy.buvape.api;

import com.quy.buvape.model.ItemMessage;
import com.quy.buvape.model.ItemMessageWrapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MessageApi {
    @GET("message/{senderId}")
    Call<ItemMessageWrapper> getMessages(@Path("senderId") String sendId, @Query("id") String receiveId);

    @GET("messages/{id}")
    Call<ItemMessageWrapper> getMessageGroup(@Path("id") String id);
}
