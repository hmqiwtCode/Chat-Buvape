package com.quy.buvape.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.zxing.Result;
import com.quy.buvape.R;
import com.quy.buvape.api.BaseLink;

import java.net.URISyntaxException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;
    private Socket mSocket;
    private SharedPreferences preferences;
    private String tokenLogin;
    private String name;
    private String phone;
    {
        try {
            mSocket = IO.socket(BaseLink.BASE_URL_WEB);
        }catch (URISyntaxException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        mSocket.connect();

        preferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        tokenLogin = preferences.getString("Login",null);
        name = preferences.getString("name",null);
        phone = preferences.getString("phone",null);

    }

    @Override
    public void handleResult(Result result) {
        Log.e("result",result.getText());
        mSocket.emit("scanPhone",result.getText()+"-__-" + name + "-__-" + phone);
       // onBackPressed();
//        mSocket.on("getReload", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Log.e("dataa",args[0].toString());
//            }
//        });

        MediaPlayer mp = MediaPlayer.create(ScanQRActivity.this, R.raw.success);
        mp.start();

        Intent intent = new Intent(ScanQRActivity.this,AcceptActivity.class);
        intent.putExtra("token",tokenLogin + "-_-" +result.getText());
        startActivity(intent);

        //onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
        mSocket.disconnect();
        Log.e("dis","disconnect");
    }



    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}