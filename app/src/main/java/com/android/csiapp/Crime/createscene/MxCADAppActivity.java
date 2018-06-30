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

public class MxCADAppActivity extends MxDrawActivity {
    //文件名
    String sFilename="";
    PhotoItem mPotoItem=null;
    public MxCADAppActivity() {

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
        if(iRetCode==1) {
            mPotoItem = new PhotoItem();
            mPotoItem.setPhotoPath(sFilename + ".png");
            mPotoItem.setUuid(CrimeProvider.getUUID());
            result.putExtra("com.android.csiapp.Databases.PhotoItem", mPotoItem);
        }
        setResult( Activity.RESULT_OK, result);
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
    public void mcrxEntryPoint(int iCode) {
        if (iCode == kInitAppMsg)
        {
            MxFunction.initMxDraw(true,"重庆楚岳科技有限公司",
                    "重庆楚岳科技有限公司",
                    "25878",
                    "010ADE5E0DA2A305784A0000141A95A9398DF7883DEC3E93BCDF656327D0DCDE3DECF60325710000050AD3F6EBA981B3121C0000242A387B3023A0F460A49961DD90C2278829B6A453E0FCB119C568CBFC422612449CC8B908D0988AC3E20000262A5D3A5A6219F536F01D355390513E55A8D533DB0C42E23F5F481F8BCF6BC918D58AE6E5C1E023EE590000141A95A9398DF7883DEC3E93BCDF656327D0DCDE3DECF60325710000050AED4A30DA1E664AB30000080AAAA802780188A38A0000"
            );
        }
        super.mcrxEntryPoint(iCode);

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
