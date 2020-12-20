package com.quy.buvape.model;

import android.content.Context;

import com.stfalcon.chatkit.commons.models.IUser;

import org.json.JSONObject;

public class Author implements IUser {
    private String id;
    private String name;
    private String avatar;


    public Author(String id,String name,String avatar){
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }

    public Author(String id) {
        this.id = id;
    }

    public Author(String id, String avatar) {
        this.id = id;
        this.avatar = avatar;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public void setName(String name) {
        this.name = name;
    }




}
