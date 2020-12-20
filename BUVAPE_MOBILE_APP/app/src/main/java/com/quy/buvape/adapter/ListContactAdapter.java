package com.quy.buvape.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.quy.buvape.R;
import com.quy.buvape.api.BaseLink;
import com.quy.buvape.api.ContactApi;
import com.quy.buvape.api.UserApi;
import com.quy.buvape.filter.SearchContact;
import com.quy.buvape.model.Contact;
import com.quy.buvape.model.User_Token;
import com.quy.buvape.view.LoadingDialog;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListContactAdapter extends RecyclerView.Adapter<ListContactAdapter.MyHolder> implements Filterable {
    public ArrayList<Contact> arr;
    private Context context;
    private SearchContact searchContact;

    private Retrofit retrofit;
    private ContactApi contactApi;
    private LoadingDialog loadingDialog;
    private SharedPreferences sharedpreferences;




    private String[] avatar = new String[]{"https://randomuser.me/api/portraits/men/32.jpg","https://randomuser.me/api/portraits/women/44.jpg",
    "https://randomuser.me/api/portraits/men/46.jpg","https://randomuser.me/api/portraits/men/97.jpg","https://uifaces.co/our-content/donated/gPZwCbdS.jpg",
    "https://images.unsplash.com/photo-1493666438817-866a91353ca9?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&s=b616b2c5b373a80ffc9636ba24f7a4a9",
    "https://randomuser.me/api/portraits/women/63.jpg","https://randomuser.me/api/portraits/men/86.jpg","https://uifaces.co/our-content/donated/rSuiu_Hr.jpg",
    "https://randomuser.me/api/portraits/men/1.jpg","https://randomuser.me/api/portraits/women/95.jpg","https://randomuser.me/api/portraits/women/79.jpg",
    "https://randomuser.me/api/portraits/men/36.jpg","https://randomuser.me/api/portraits/men/29.jpg"};
    Random rd = new Random();

    public  ListContactAdapter(Context context,ArrayList<Contact> arr ){
        this.context = context;
        this.arr = arr;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.item_contact_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        Contact ct = arr.get(position);
        loadingDialog = new LoadingDialog((Activity) context);
        sharedpreferences = context.getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        int intR = rd.nextInt(avatar.length-1);
       // holder.img.setImageResource(ct.getImage());
//        GlideToVectorYou
//                .init()
//                .with(this.context)
//                .setPlaceHolder(R.drawable.avatar, R.drawable.avatar)
//                .load(Uri.parse("https://ui-avatars.com/api/?name=John+Doe"), holder.img);

        Glide
                .with(holder.itemView.getContext())
                .load(avatar[intR])
                .centerCrop()
                .placeholder(R.drawable.avatar)
                .into(holder.img);
        holder.textName.setText(ct.getName());
        holder.textPhone.setText("Số điện thoại: " + ct.getPhone());
        holder.txt_name_on_phone.setText(ct.getN_phone());

        Log.e("friend",ct.isFriend()+"");
        if (ct.isFriend()){
            holder.btn_save_contact.setVisibility(View.GONE);
        }else {
            holder.btn_save_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadingDialog.startLoadingDialog();
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BaseLink.BASE_URL_CONTACT)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    contactApi = retrofit.create(ContactApi.class);
                    String uid = sharedpreferences.getString("id",null);
                    HashMap<String,String> map = new HashMap<>();
                    map.put("from",uid);
                    map.put("to",ct.getId());
                    Call<ResponseBody> call = contactApi.addContact(map);
                    call.enqueue(new Callback<ResponseBody>(){

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            loadingDialog.alertDismiss();
                            Log.e("res", response.toString());
                            if (response.isSuccessful()){
                                holder.btn_save_contact.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            loadingDialog.alertDismiss();
                        }
                    });


//                    Toast.makeText(context, ct.getPhone(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    @Override
    public Filter getFilter() {
        if (searchContact == null){
            searchContact = new SearchContact(arr,this);
        }
        return searchContact;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView textName;
        TextView textPhone;
        TextView txt_name_on_phone;
        ImageButton btn_open_call;
        ImageButton btn_open_message;
        ImageButton btn_save_contact;

        public MyHolder(@NonNull View view) {
            super(view);
            img = view.findViewById(R.id.img_contact_person);
            textName = view.findViewById(R.id.txt_name_contact);
            textPhone = view.findViewById(R.id.txt_phone_contact);
            txt_name_on_phone = view.findViewById(R.id.txt_name_on_phone);
            btn_open_call = view.findViewById(R.id.btn_open_call);
            btn_open_message = view.findViewById(R.id.btn_open_message);
            btn_save_contact = view.findViewById(R.id.btn_save_contact);


        }
    }
}
