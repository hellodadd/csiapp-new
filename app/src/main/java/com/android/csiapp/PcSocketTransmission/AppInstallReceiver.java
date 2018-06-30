package com.android.csiapp.PcSocketTransmission;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by JOWE on 2016/10/14.
 */

public class AppInstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager manager = context.getPackageManager();
        String packageName = intent.getData().getSchemeSpecificPart();
        String currentPackageName = context.getPackageName();
        if (packageName.equals(currentPackageName)) {
            if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
                Toast.makeText(context, "安装成功"+packageName, Toast.LENGTH_LONG).show();
            } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
                Toast.makeText(context, "卸载成功"+packageName, Toast.LENGTH_LONG).show();
            } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
                Toast.makeText(context, "替换成功"+packageName, Toast.LENGTH_LONG).show();
            } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_CHANGED)) {
                Toast.makeText(context, "替换成功"+packageName, Toast.LENGTH_LONG).show();
            }
        }
    }
}
