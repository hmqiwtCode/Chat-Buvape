package com.quy.buvape.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.quy.buvape.R;

public class LoadingDialog {
    private Activity context;
    private AlertDialog alertDialog;

    public LoadingDialog(Activity context){
        this.context = context;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyAlertDialogTheme);
        LayoutInflater inflater = context.getLayoutInflater();
       // builder.setView(inflater.inflate(R.layout.custom_dialog,null));
        View view = inflater.inflate(R.layout.custom_dialog,null,false);
        builder.setView(view);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void alertDismiss(){
        alertDialog.dismiss();
    }
}
