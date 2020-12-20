package com.quy.buvape.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.quy.buvape.MainActivity;
import com.quy.buvape.R;
import com.quy.buvape.api.BaseLink;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class AcceptActivity extends AppCompatActivity {

    private TextView txtTuChoi;
    private Button btn_accept;
    private String token;
    private Socket mSocket;
    private String firstName,lastName,email,id,phone;
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
        setContentView(R.layout.activity_accept);
        getInfo();
        mSocket.connect();
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        token = getIntent().getStringExtra("token");
        txtTuChoi = findViewById(R.id.tuchoi);
        btn_accept = findViewById(R.id.btn_accept);


        txtTuChoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSocket.emit("cancel_login",token);
                finish();
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("token",token);
                    jsonObject.put("firstname",firstName);
                    jsonObject.put("lastname",lastName);
                    jsonObject.put("email",email);
                    jsonObject.put("id",id);
                    jsonObject.put("phone",phone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSocket.emit("pass_login",jsonObject);
                startActivity(new Intent(AcceptActivity.this, MainActivity.class));
                finish();
            }
        });
    }


    void getInfo(){
        firstName = getSharedPreferences("LOGIN",MODE_PRIVATE).getString("firstname",null);
        lastName = getSharedPreferences("LOGIN",MODE_PRIVATE).getString("lastname",null);
        email = getSharedPreferences("LOGIN",MODE_PRIVATE).getString("email",null);
        id = getSharedPreferences("LOGIN",MODE_PRIVATE).getString("id",null);
        phone = getSharedPreferences("LOGIN",MODE_PRIVATE).getString("phone",null);
    }
}