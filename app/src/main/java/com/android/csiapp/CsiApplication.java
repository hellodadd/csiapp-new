package com.android.csiapp;

import android.app.Application;

import com.droi.sdk.core.Core;
import com.droi.sdk.selfupdate.DroiUpdate;

/**
 * Created by liwei on 2017/4/5.
 */

public class CsiApplication extends Application {
    private boolean isReporting=false;
    public boolean getIsReporting(){
        return isReporting;
    }
    public void setIsReporting(boolean reporting){
        isReporting=reporting;
    }
    public void onCreate(){
        super.onCreate();

        //baas
        Core.initialize(this);
        DroiUpdate.initialize(this, "eXbZCAY0RbZUMuCx_YAvlXNwPPn-Fm83x_bxtxp5Jj7NKz9Wjr9z37iDEaYzU8CE");

    }
}
