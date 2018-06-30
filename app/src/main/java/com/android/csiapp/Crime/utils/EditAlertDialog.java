package com.android.csiapp.Crime.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.android.csiapp.Databases.CrimeItem;

/**
 * Created by user on 2016/12/22.
 */
public class EditAlertDialog extends AlertDialog{
    private Context mContext;
    private AlertDialog alertDialog;
    public EditAlertDialog(Context context) {
        super(context);
        mContext = context;
    }

    public void onCreateDialog(boolean isMainActivity, CrimeItem crimeItem){
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if(isMainActivity){
            builder.setTitle("警告 : 请填写必填项信息");
            String message = "";
            if(crimeItem!=null) message = crimeItem.needToCheckInformation(message);
            builder.setMessage(message);
        }else{
            builder.setTitle("警告");
            builder.setMessage("请填写必填项信息");
        }
        builder.setPositiveButton("补全信息", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
}
