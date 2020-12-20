package com.quy.buvape.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.quy.buvape.MainActivity;
import com.quy.buvape.R;
import com.quy.buvape.api.BaseLink;
import com.quy.buvape.api.UserApi;
import com.quy.buvape.model.Res;
import com.quy.buvape.model.User;
import com.quy.buvape.model.User_Token;


import org.json.JSONException;


import java.io.IOException;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Login extends AppCompatActivity {

    private Button btn_login;
    private TextView textSignup;
    private EditText txt_phone;
    private EditText txt_pass;
    private Retrofit retrofit;
    private UserApi userApi;
    final LoadingDialog loadingDialog = new LoadingDialog(this);
    private  SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sharedpreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);

        retrofit = new Retrofit.Builder()
                .baseUrl(BaseLink.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userApi = retrofit.create(UserApi.class);


        btn_login = findViewById(R.id.btn_login);
        textSignup = findViewById(R.id.textSignup);
        txt_phone = findViewById(R.id.txt_phone);
        txt_pass = findViewById(R.id.txt_pass);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
               // getUserLogin();
                getUserLogin1();
            }
        });

        textSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Login.this, SignUpPhoneActivity.class);
                Login.this.startActivity(myIntent);
            }
        });
    }

//    private void getUserLogin(){
//        String phone = txt_phone.getText().toString();
//        String pass = txt_pass.getText().toString();
//        User user = new User(phone,pass);
//
//        Call<User> call = userApi.getLogin(user);
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if(!response.isSuccessful()){
//                    Log.e("CODE_1",response.code()+"");
//                    loadingDialog.alertDismiss();
//                    return;
//                }
//             //   User userResponse = response.body();
//                Log.e("CODE_S",response.body()+"");
//                loadingDialog.alertDismiss();
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Log.e("CODE_F",t.getMessage()+"");
//                loadingDialog.alertDismiss();
//            }
//        });
//    }

    private void getUserLogin1(){
        String phone = txt_phone.getText().toString();
        String pass = txt_pass.getText().toString();
        User user = new User(phone,pass);
        HashMap<String,String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", pass);

        Call<User_Token> call = userApi.getLogin1(map);
        call.enqueue(new Callback<User_Token>() {
            @Override
            public void onResponse(Call<User_Token> call, Response<User_Token> response) {
                if(!response.isSuccessful()){
                    Log.e("CODE1",response.code()+"");
                    loadingDialog.alertDismiss();
                    Toasty.error(Login.this, "Login Failed.Please Try Again!!!", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                //   User userResponse = response.body();

                if (!response.body().getUser().getActive()){
                    loadingDialog.alertDismiss();
                    Toasty.error(Login.this, "Account has been locked", Toast.LENGTH_SHORT, true).show();
                    return;
                }


                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("Login",response.body().getToken());
                editor.putString("phone",response.body().getUser().getPhone());
                editor.putString("email",response.body().getUser().getEmail());
                editor.putString("name",response.body().getUser().getFirstname() + " " + response.body().getUser().getLastname() );
                editor.putString("firstname",response.body().getUser().getFirstname());
                editor.putString("lastname",response.body().getUser().getLastname());
                editor.putString("id",response.body().getUser().getId()+"");
                editor.commit();

                    Log.e("CODE_SUCCESS",response.body().getToken());
                    Intent myIntent = new Intent(Login.this, MainActivity.class);
                    myIntent.putExtra("token", response.body().getToken());
                    Login.this.startActivity(myIntent);
                    loadingDialog.alertDismiss();
                    finish();
            }

            @Override
            public void onFailure(Call<User_Token> call, Throwable t) {
                Log.e("CODE_FAILURE",t.getMessage()+"");
                loadingDialog.alertDismiss();
                Toasty.error(Login.this, "Login Failed.Please Try Again!!!", Toast.LENGTH_SHORT, true).show();
            }
        });
    }

}