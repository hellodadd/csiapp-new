package com.android.csiapp.Crime.createscene;

/**
 * Created by liwei on 2017/10/17.
 */


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.MxDraw.MxDrawActivity;
import com.MxDraw.MxFunction;
import com.android.csiapp.Crime.utils.CreateSceneUtils;
import com.android.csiapp.Databases.CrimeProvider;
import com.android.csiapp.Databases.PhotoItem;

import java.io.File;

public class MxCADAppActivity_1 extends MxDrawActivity {
    //文件名
    String sFilename="";
    PhotoItem mPotoItem=null;
    public MxCADAppActivity_1() {

    }


    @Override
    public void mcrxEntryPoint(int iCode)
    {
        super.mcrxEntryPoint(iCode);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        String path=getIntent().getStringExtra("path");
        initWorkDir(path);
        super.onCreate(savedInstanceState);
        sFilename=getIntent().getStringExtra("file");
        //打开dwg文件，如果文件不存在自动新建
        MxFunction.openFile(sFilename+".mwg");
    }
    @Override
    public void doReturn(int iRetCode)
    {
        //最终导出文件代码
        Intent result = getIntent();
        result.putExtra("isSaved",iRetCode);//是否保存,1保存
        result.putExtra("Map_ScreenShot", sFilename + ".png");
        setResult(Activity.RESULT_OK, result);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    @Override
    public void doCommand(int iCommand)
    {

    }
    @Override
    public  int touchesEvent(int iType,double dX,double dY)
    {

        return 0;
    }

    @Override
    public void exportFileDwg(String sFile)
    {
        Log.e("exportFileDwg",sFile);
    }

    @Override
    public void exportFilePdf(String sFile)
    {
        Log.e("exportFilePdf",sFile);

    }
}
