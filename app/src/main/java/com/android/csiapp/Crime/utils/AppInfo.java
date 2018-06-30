package com.android.csiapp.Crime.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by JOWE on 2016/11/4.
 */

public class AppInfo {
    private Context mContext;

    public void AppInfo(Context context) {
        mContext = context;
    }

    public static String getAppVersionName(Context context) {
        String appVersionName = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            appVersionName = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return appVersionName;
    }
}
