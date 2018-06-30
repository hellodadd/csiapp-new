package com.android.csiapp.Crime.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.android.csiapp.R;

/**
 * Created by user on 2016/10/12.
 */
public class ScreenShot {
    // 得到指定Activity的截屏，儲存到png
    @SuppressWarnings("ResourceType")
    private static Bitmap takeScreenShot(Activity activity){
        //View是你需要截圖的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        //得到狀態列高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int toolsBarHeight = (int) activity.getResources().getDimension(R.dimen.toolbar_size);
        int statusBarHeight = frame.top;
        int removeHeight = toolsBarHeight+statusBarHeight;

        //得到螢幕長和高　
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        //去掉標題列
        //Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Log.d("Anita","b1 width = "+b1.getWidth()+", height = "+b1.getHeight());
        Bitmap b = Bitmap.createBitmap(b1, 0, removeHeight, b1.getWidth(), b1.getHeight()-removeHeight);
        view.destroyDrawingCache();
        return b;
    }

    //儲存到sdcard
    private static void savePic(Bitmap b,String strFileName){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos)
            {
                b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String shoot(Activity a){
        File mediaStorageDir = new File( a.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Report");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String path = new File(mediaStorageDir.getPath() + File.separator + "POSITION_"+ timeStamp + ".jpg").toString();
        ScreenShot.savePic(ScreenShot.takeScreenShot(a), path);
        return path;
    }
}
