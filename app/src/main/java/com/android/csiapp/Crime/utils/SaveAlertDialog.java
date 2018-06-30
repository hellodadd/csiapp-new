package com.android.csiapp.Crime.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.android.csiapp.Databases.CrimeItem;

/**
 * Created by user on 2016/10/14.
 */
public class SaveAlertDialog extends AlertDialog {
    private Context mContext;
    private AlertDialog alertDialog;
    public SaveAlertDialog(Context context) {
        super(context);
        mContext = context;
    }

    public void onCreateDialog(final Intent result, boolean isMainActivity, CrimeItem crimeItem){
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if(isMainActivity){
            builder.setTitle("确认 : 请填写必填项信息");
            String message = "";
            if(crimeItem!=null) message = crimeItem.needToCheckInformation(message);
            builder.setMessage(message);
        }else{
            builder.setTitle("确认");
            builder.setMessage("请填写必填项信息");
        }
        builder.setPositiveButton("补全信息", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        })
        .setNegativeButton("储存", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getOwnerActivity().setResult(Activity.RESULT_OK, result);
                getOwnerActivity().finish();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
}
