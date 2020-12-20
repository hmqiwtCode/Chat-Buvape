package com.quy.buvape.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;




import com.quy.buvape.R;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class SignUpPhoneActivity extends AppCompatActivity {

    private Button btnNext;
    private TextView txtPhoneNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_phone);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));

        btnNext = findViewById(R.id.btnNext);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtPhoneNumber.getText().toString().trim().length() < 11){
                    Toasty.error(SignUpPhoneActivity.this, "Phone number not valid", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                Intent myIntent = new Intent(SignUpPhoneActivity.this, CodeOtpActivity.class);
                myIntent.putExtra("mobile", txtPhoneNumber.getText().toString());
                SignUpPhoneActivity.this.startActivity(myIntent);
            }
        });

    }




}