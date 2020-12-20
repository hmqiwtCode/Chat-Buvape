package com.quy.buvape;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quy.buvape.adapter.ListContactAdapter;
import com.quy.buvape.api.BaseLink;
import com.quy.buvape.api.ContactApi;
import com.quy.buvape.api.UserApi;
import com.quy.buvape.model.Contact;
import com.quy.buvape.model.User;
import com.quy.buvape.model.User_Token;
import com.quy.buvape.view.Login;

import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactFragment extends Fragment {
    ArrayList<Contact> arr;
    private final static int READ_CONTACTS = 1;
    private TextView tv_getContact;
    private EditText search_contacts;
    ListContactAdapter adapter;
    View global = null;

    private Retrofit retrofit;
    private ContactApi contactApi;
    private  SharedPreferences sharedpreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.from(getContext()).inflate(R.layout.contact_fragment,container,false);
        global = view;
        tv_getContact = view.findViewById(R.id.tv_getAllContacts);
        search_contacts = view.findViewById(R.id.search_contacts);


        retrofit = new Retrofit.Builder()
                .baseUrl(BaseLink.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        contactApi = retrofit.create(ContactApi.class);

        tv_getContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                resquestContactsList(view);
            }
        });

        sharedpreferences = getActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);

        search_contacts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        resquestContactsList(view);

        return view;
    }

    private void resquestContactsList(View view){
        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CONTACTS},1);
        }else{
            logNumber(view);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == READ_CONTACTS && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                logNumber(global);
            }else{
                Toast.makeText(getActivity(), "Access denied", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Access denied", Toast.LENGTH_SHORT).show();
        }
    }


    private void logNumber(View view){
        arr = new ArrayList<>();
        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Contact contact = new Contact(name, phoneNumber);
            arr.add(contact);
        }
        phones.close();
        String arrPhone = "";
        for (int i = 0; i < arr.size(); i++){
            arrPhone += arr.get(i).getName() + "+_+" + arr.get(i).getPhone() ;
            if (i != arr.size() - 1){
                arrPhone += ";";
            }
        }
        String name = sharedpreferences.getString("name",null);
        String phone = sharedpreferences.getString("phone",null);
        arrPhone = name + "+_+" + phone + ";" + arrPhone;
        Log.e("STE",arrPhone);

        if (!arrPhone.equalsIgnoreCase("")) {
            Call<List<Contact>> call = contactApi.addContacts(arrPhone);
            call.enqueue(new Callback<List<Contact>>() {
                @Override
                public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                    if (!response.isSuccessful()) {
                        Log.e("CODE1", response.code() + "");

                        Toasty.error(getActivity(), "Failed", Toast.LENGTH_SHORT, true).show();
                        return;
                    }

                    arr = (ArrayList<Contact>) response.body();
                    Log.e("list", response.body().toString());
                    final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.lv_contacts);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ListContactAdapter(getContext(), arr);
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.setAdapter(adapter);
                                    }
                                });
                            }
                        }
                    }).start();

                }

                @Override
                public void onFailure(Call<List<Contact>> call, Throwable t) {
                    Log.e("CODE_FAILURE", t.getMessage() + "");

//                    Toasty.error(getActivity(), "Failed", Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }
}
