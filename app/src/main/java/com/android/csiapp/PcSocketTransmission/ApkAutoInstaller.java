package com.android.csiapp.PcSocketTransmission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * Created by JOWE on 2016/10/14.
 */

public class ApkAutoInstaller {

    private static final String TAG = "ApkAutoInstaller";
    private static ApkAutoInstaller  mApkAutoInstaller;
    private Context mContext;

    private ApkAutoInstaller(Context context) {
        mContext = context;
    }

    public static ApkAutoInstaller getDefault(Context context) {
        if (mApkAutoInstaller == null) {
            synchronized (ApkAutoInstaller.class) {
                if (mApkAutoInstaller == null) {
                    mApkAutoInstaller = new ApkAutoInstaller(context);
                }
            }
        }
        return mApkAutoInstaller;
    }


    public boolean versionChecker(PackageManager pm, String pkgName, String apkPath){
        boolean checkFlag = false;
        int currentPkgVersionCode = -1;
        String currentPkgName ="";
        int installPkgVersionCode = -1;
        String installPkgName ="";
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(pkgName, 0);
            currentPkgVersionCode = pi.versionCode;
            currentPkgName = pi.packageName;
        }catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG,"currentPkgVersionCode="+ currentPkgVersionCode);

        File file = new File(apkPath);
        pi = pm.getPackageArchiveInfo(apkPath,PackageManager.GET_ACTIVITIES);
        if (pi!= null) {
            installPkgVersionCode = pi.versionCode;
            installPkgName = pi.packageName;
        }
        Log.d(TAG,"installPkgVersionCode="+installPkgVersionCode);


        if((currentPkgVersionCode < installPkgVersionCode) && (currentPkgName.equals(installPkgName))) {
            checkFlag = true;
        }
        return checkFlag;
    }

    public void installApk(String filePath) {
        if (TextUtils.isEmpty(filePath) || !filePath.endsWith(".apk")) {
            Log.d(TAG, "Apk path is incorrect");
            return;
        }
        Uri uri = Uri.fromFile(new File(filePath));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }
}
