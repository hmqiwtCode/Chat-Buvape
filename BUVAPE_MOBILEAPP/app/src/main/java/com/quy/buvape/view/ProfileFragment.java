package com.quy.buvape.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quy.buvape.R;
import com.quy.buvape.adapter.AdapterItemMenu;
import com.quy.buvape.model.ItemMenu;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private Button button;
    private final static int CAMERA_PR = 1;
    private RecyclerView listmenu;
    private ArrayList<ItemMenu> arrayList;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(getActivity()).inflate(R.layout.profile_fragment,container,false);
        SharedPreferences preferences = getContext().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        button = view.findViewById(R.id.button2);
        listmenu = view.findViewById(R.id.listmenu);
        textView = view.findViewById(R.id.name_profile);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),5, GridLayoutManager.VERTICAL,false);
        arrayList = new ArrayList<>();
        arrayList.add(new ItemMenu("FRIENDS","Friends",R.drawable.ic_friends));
        arrayList.add(new ItemMenu("SCANS","Scan QR",R.drawable.ic_scan));
        arrayList.add(new ItemMenu("LOGOUT","Logout",R.drawable.ic_logout));

        String name = preferences.getString("name","Qúy");
        textView.setText(name.split(" ")[name.split(" ").length-1]);
        listmenu.setLayoutManager(layoutManager);
        AdapterItemMenu adapterItemMenu = new AdapterItemMenu(getContext(), arrayList, new AdapterItemMenu.ItemListener() {
            @Override
            public void itemClickListener(AdapterItemMenu.ItemMenuHolder holder, ItemMenu itemMenu) {
                requestCameraForScan();
            }
        });
        listmenu.setAdapter(adapterItemMenu);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                requestCameraForScan();
//            }
//        });

        return view;
    }

    public void requestCameraForScan(){
        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},CAMERA_PR);
        }else{
            startActivity(new Intent(getActivity(),ScanQRActivity.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PR && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startActivity(new Intent(getActivity(),ScanQRActivity.class));
            }else{
                Toast.makeText(getActivity(), "Access denied", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Access denied", Toast.LENGTH_SHORT).show();
        }
    }
}
