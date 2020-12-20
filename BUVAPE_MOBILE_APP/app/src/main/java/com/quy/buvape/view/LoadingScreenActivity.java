package com.quy.buvape.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.quy.buvape.MainActivity;
import com.quy.buvape.R;
import com.quy.buvape.api.BaseLink;
import com.quy.buvape.api.UserApi;
import com.quy.buvape.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadingScreenActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private UserApi userApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.barLoadingScreen));

        SharedPreferences preferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        String tokenLogin = preferences.getString("Login",null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BaseLink.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                userApi = retrofit.create(UserApi.class);


                if(tokenLogin != null){
                    Call<User> call = userApi.validateToken(tokenLogin);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(!response.isSuccessful()){
                                moveToLogin();
                                return;
                            }
                            Intent myIntent = new Intent(LoadingScreenActivity.this, MainActivity.class);
                            myIntent.putExtra("token", tokenLogin);
                            LoadingScreenActivity.this.startActivity(myIntent);
                            finish();
                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            moveToLogin();
                            return;
                        }
                    });
                }else {
                    moveToLogin();
                }

            }
        },4000);




    }

    private void moveToLogin(){
        Intent myIntent = new Intent(LoadingScreenActivity.this, Login.class);
        LoadingScreenActivity.this.startActivity(myIntent);
        finish();
    }
}