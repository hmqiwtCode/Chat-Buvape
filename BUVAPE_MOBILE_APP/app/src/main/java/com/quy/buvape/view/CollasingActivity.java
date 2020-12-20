package com.quy.buvape.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.quy.buvape.R;
import com.quy.buvape.adapter.AdapterFriends;
import com.quy.buvape.api.BaseLink;
import com.quy.buvape.api.ContactApi;
import com.quy.buvape.model.Contact;
import com.quy.buvape.model.Friend;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CollasingActivity extends AppCompatActivity {
    private RecyclerView refriends;
    private SwipeRefreshLayout refresh;
    private AdapterFriends adapterFriends;
    private Retrofit retrofit;
    private ContactApi contactApi;
    private SharedPreferences sharedpreferences;
    private ArrayList<Friend> arrayList;
    private TextView totalFriends;
    private TextView back_friend_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collasing);
        refresh = findViewById(R.id.refresh);

        totalFriends = findViewById(R.id.totalFriends);
        back_friend_btn = findViewById(R.id.back_friend_btn);



        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                adapterFriends.notifyDataSetChanged();
                refresh.setRefreshing(false);
            }
        });
        arrayList = new ArrayList<>();
        refriends = findViewById(R.id.refriends);

        retrofit = new Retrofit.Builder()
                .baseUrl(BaseLink.BASE_URL_CONTACT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        contactApi = retrofit.create(ContactApi.class);
        sharedpreferences = this.getSharedPreferences("LOGIN", Context.MODE_PRIVATE);

        Call<List<Friend>> call = contactApi.getFriend(sharedpreferences.getString("id","1"));
        Log.e("IDDDDD",sharedpreferences.getString("id","1"));
        call.enqueue(new Callback<List<Friend>>() {
            @Override
            public void onResponse(Call<List<Friend>> call, Response<List<Friend>> response) {
                Log.e("DDDDD",response.toString());
                arrayList = (ArrayList<Friend>) response.body();
                adapterFriends = new AdapterFriends(arrayList,CollasingActivity.this);
                refriends.setLayoutManager(new LinearLayoutManager(CollasingActivity.this));
                refriends.setAdapter(adapterFriends);
                totalFriends.setText(arrayList.size()+" Friends");
            }

            @Override
            public void onFailure(Call<List<Friend>> call, Throwable t) {

            }
        });

        back_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}