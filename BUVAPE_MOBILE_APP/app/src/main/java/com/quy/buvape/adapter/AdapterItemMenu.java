package com.quy.buvape.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.quy.buvape.R;
import com.quy.buvape.model.ItemMenu;
import com.quy.buvape.view.CollasingActivity;
import com.quy.buvape.view.FriendsActivity;
import com.quy.buvape.view.Login;
import com.quy.buvape.view.ScanQRActivity;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdapterItemMenu extends RecyclerView.Adapter<AdapterItemMenu.ItemMenuHolder> {
    public ArrayList<ItemMenu> arr;
    private Context context;
    private ItemListener itemListener;
    private final static int CAMERA_PR = 1;

    public  AdapterItemMenu(Context context,ArrayList<ItemMenu> arr,ItemListener itemListener){
        this.context = context;
        this.arr = arr;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public ItemMenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemMenuHolder(LayoutInflater.from(context).inflate(R.layout.item_menu_profile,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemMenuHolder holder, int position) {
        ItemMenu itemMenu = arr.get(position);
        holder.imageView.setImageResource(itemMenu.getImageItem());
        holder.textView.setText(itemMenu.getNameItem());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemMenu.getId().equalsIgnoreCase("SCANS")){
                    itemListener.itemClickListener(holder,itemMenu);
                }else if (itemMenu.getId().equalsIgnoreCase("FRIENDS")){
                    context.startActivity(new Intent(context, CollasingActivity.class));
                }
                else if(itemMenu.getId().equalsIgnoreCase("LOGOUT")){
                   // removeShare(context);

                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Đăng xuất")
                            .setContentText("Bạn muốn thoát khỏi thiết bị này")
                            .setCancelText("Hủy bỏ")
                            .setConfirmText("Tiếp tục")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.setTitleText("Hủy bỏ thành công")
                                            .setContentText("Bạn vẫn duy trì đăng nhập")
                                            .setConfirmText("OK")
                                            .showCancelButton(false)
                                            .setCancelClickListener(null)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    removeShare(context);
                                    sDialog.setTitleText("Thành công")
                                            .setContentText("Quay lại với hệ thống sớm nhé <3.")
                                            .setConfirmText("OK")
                                            .showCancelButton(false)
                                            .setCancelClickListener(null)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                }
                            })
                            .show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ItemMenuHolder extends RecyclerView.ViewHolder{
        ConstraintLayout item;
        ImageView imageView;
        TextView textView;

        public ItemMenuHolder(@NonNull View view) {
            super(view);
            item = view.findViewById(R.id.item);
            imageView = view.findViewById(R.id.icon_menu);
            textView = view.findViewById(R.id.text_menu);
        }
    }

    private void removeShare(Context context){
        SharedPreferences settings = context.getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
        context.startActivity(new Intent(context, Login.class));
    }

    public interface ItemListener{
        void itemClickListener(ItemMenuHolder holder,ItemMenu itemMenu);
    }
}
