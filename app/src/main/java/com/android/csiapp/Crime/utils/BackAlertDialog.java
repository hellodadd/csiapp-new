package com.android.csiapp.Crime.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.android.csiapp.Databases.CrimeItem;
import com.android.csiapp.Databases.CrimeProvider;

/**
 * Created by user on 2016/10/14.
 */
public class BackAlertDialog extends AlertDialog{
    private Context mContext;
    private AlertDialog alertDialog;
    private boolean mIsAddSceneQuit=false;
    public void setIsAddSceneQuit(boolean isAddSceneQuit){
        mIsAddSceneQuit=isAddSceneQuit;
    }
    public BackAlertDialog(Context context) {
        super(context);
        mContext = context;
    }

    public void onCreateDialog(boolean isMainActivity, final CrimeItem crimeItem){
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
        })
        .setNegativeButton("退出", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    if(mIsAddSceneQuit) {
                        //by liwei 2017.3.10
                        //新增现场时，会主动在数据库新增记录，如果选择不保存退出，删除新增的数据
                        CrimeProvider crimeProvider = new CrimeProvider(mContext);
                        crimeProvider.delete(crimeItem.getId());
                    }
                }
                catch (Exception e1){}
                getOwnerActivity().finish();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
}
