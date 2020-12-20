package com.quy.buvape.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quy.buvape.MainActivity;
import com.quy.buvape.R;
import com.quy.buvape.api.BaseLink;
import com.quy.buvape.api.UserApi;
import com.quy.buvape.model.User;
import com.quy.buvape.model.User_Token;

import java.util.Calendar;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddInfoUserActivity extends AppCompatActivity {
    private final int TYPE_ACCOUNT = 0;
    private final boolean ISACTIVE = true;
    private EditText txt_email;
    private EditText txt_firstName;
    private EditText txt_lastName;
    private EditText txt_password;
    private EditText txt_dob;
    private EditText txt_phone;
    private Button btn_confirm;
    private UserApi userApi;
    private Retrofit retrofit;
    private String date;

    private SharedPreferences sharedpreferences;
    final LoadingDialog loadingDialog = new LoadingDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info_user);
        txt_dob = findViewById(R.id.txt_dob);
        txt_phone = findViewById(R.id.txt_phone);
        txt_email = findViewById(R.id.txt_email);
        txt_firstName = findViewById(R.id.txt_firstname);
        txt_lastName = findViewById(R.id.txt_lastname);
        txt_password = findViewById(R.id.txt_password);
        btn_confirm = findViewById(R.id.btn_done);

        sharedpreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);


        retrofit = new Retrofit.Builder()
                .baseUrl(BaseLink.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userApi = retrofit.create(UserApi.class);


        Intent intent = getIntent();
        String mobile = intent.getStringExtra("mobile");

       // String mobile1 = "84838405987";
        txt_phone.setText(mobile);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        txt_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddInfoUserActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        System.out.println(month);
                        month = month + 1;
                        date = year + "-" + month + "-" +  dayOfMonth;
                        String dateAdd = dayOfMonth + "-" + month + "-" + year;
                        txt_dob.setText(dateAdd);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txt_dob.getText().toString().trim().equals("") || txt_phone.getText().toString().trim().equals("")
                || txt_email.getText().toString().trim().equals("") || txt_firstName.getText().toString().trim().equals("")
                || txt_lastName.getText().toString().trim().equals("") || txt_password.getText().toString().trim().equals("") || !Patterns.EMAIL_ADDRESS.matcher(txt_email.getText().toString()).matches()){
                    Toasty.error(AddInfoUserActivity.this, "Field not valid!!!", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                loadingDialog.startLoadingDialog();
                User user = new User(txt_email.getText().toString().trim(),
                        txt_phone.getText().toString().trim(),txt_firstName.getText().toString().trim(),txt_lastName.getText().toString().trim(),
                        date,TYPE_ACCOUNT,ISACTIVE,txt_password.getText().toString().trim());
                Call<User_Token> call = userApi.addUser(user);
                call.enqueue(new Callback<User_Token>() {
                    @Override
                    public void onResponse(Call<User_Token> call, Response<User_Token> response) {

                        if(!response.isSuccessful()){
                            Log.e("CODE1",response.code()+"");
                            loadingDialog.alertDismiss();
                            Toasty.error(AddInfoUserActivity.this, "SignUp Failed.Please Try Again!!!", Toast.LENGTH_SHORT, true).show();
                            return;
                        }
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("Login",response.body().getToken());
                        editor.putString("phone",response.body().getUser().getPhone());
                        editor.putString("email",response.body().getUser().getEmail());
                        editor.putString("name",response.body().getUser().getFirstname() + " " + response.body().getUser().getLastname() );
                        editor.putString("id",response.body().getUser().getId()+"");
                        editor.commit();

                        Intent myIntent = new Intent(AddInfoUserActivity.this, MainActivity.class);
                        //myIntent.putExtra("token", response.body().getToken());
                        AddInfoUserActivity.this.startActivity(myIntent);
                        loadingDialog.alertDismiss();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<User_Token> call, Throwable t) {
                        Log.e("CODE_FAILURE",t.getMessage()+"");
                        loadingDialog.alertDismiss();
                        Toasty.error(AddInfoUserActivity.this, "SignUp Failed.Please Try Again!!!", Toast.LENGTH_SHORT, true).show();

                    }
                });


            }
        });
    }
}