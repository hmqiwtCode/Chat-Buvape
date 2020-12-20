package com.quy.buvape.model;

import android.app.Activity;
import android.app.Application;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.quy.buvape.api.BaseLink;

import java.net.URISyntaxException;

public class SocketIOClient extends Application {

    private static Socket mSocket;

    private static void initSocket() {
        try {
            mSocket = IO.socket(BaseLink.BASE_URL_CHAT);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static Socket getInstance(Activity activity) {
        if (mSocket != null)
            return mSocket;
         else {
            initSocket();
            return mSocket;
        }
    } }