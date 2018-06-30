package com.android.csiapp.Crime.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.csiapp.Crime.utils.BackupRestore;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 2017/1/4.
 */
public class CreateSceneUtils {
    public static final String ACTION_RECEIVE_RESULT = "com.kuaikan.send_result";
    public static final int EVENT_CELL_COLLECTION = 0;

    public static final int EVENT_CASETYPE_SELECT_ITEM = 1;
    public static final int EVENT_AREA_SELECT_ITEM = 2;
    public static final int EVENT_SCENE_CONDITION_SELECT_ITEM = 3;
    public static final int EVENT_WEATHER_SELECT_ITEM = 4;
    public static final int EVENT_WIND_SELECT_ITEM = 5;
    public static final int EVENT_ILLUMINATION_SELECT_ITEM = 6;
    public static final int EVENT_SCENE_CONDUCTOR_SLELECT_ITEM = 7;
    public static final int EVENT_ACCESS_INSPECTORS_SLELECT_ITEM = 8;
    public static final int EVENT_SEX_SELECT_ITEM = 9;
    public static final int EVENT_TOOL_CATEGORY_SELECT_ITEM = 10;
    public static final int EVENT_TOOL_SOURCE_SELECT_ITEM = 11;
    public static final int EVENT_EVIDENCE_SELECT_ITEM = 12;
    public static final int EVENT_INFER_SELECT_ITEM = 13;
    public static final int EVENT_METHOD_SELECT_ITEM = 14;
    public static final int EVENT_GET_PEOPLE_SLELECT_ITEM = 15;
    public static final int EVENT_PEOPLE_NUMBER_SELECT_ITEM = 16;
    public static final int EVENT_CRIME_MEANS_SELECT_ITEM = 17;
    public static final int EVENT_CRIME_CHARACTER_SELECT_ITEM = 18;
    public static final int EVENT_CRIME_ENTRANCE_SELECT_ITEM = 19;
    public static final int EVENT_CRIME_TIMING_SELECT_ITEM = 20;
    public static final int EVENT_SELECT_OBJECT_SELECT_ITEM = 21;
    public static final int EVENT_CRIME_EXPORT_SELECT_ITEM = 22;
    public static final int EVENT_CRIME_FEATURE_SELECT_ITEM = 23;
    public static final int EVENT_INTRUSIVE_METHOD_SELECT_ITEM = 24;
    public static final int EVENT_SELECT_LOCATION_SELECT_ITEM = 25;
    public static final int EVENT_CRIME_PURPOSE_SELECT_ITEM = 26;

    public static final int EVENT_NEW_PEOPLE = 101;
    public static final int EVENT_NEW_ITEM = 102;
    public static final int EVENT_NEW_TOOL = 103;
    public static final int EVENT_NEW_POSITION = 104;
    public static final int EVENT_NEW_FLAT = 105;
    public static final int EVENT_EDIT_FLAT = 1051;
    public static final int REQUEST_POSITION = 106;
    public static final int REQUEST_FLAT = 107;
    public static final int EVENT_PHOTO_TYPE_POSITION = 108;
    public static final int EVENT_PHOTO_TYPE_LIKE = 109;
    public static final int EVENT_PHOTO_TYPE_IMPORTANT = 110;
    public static final int EVENT_TYPE_EVIDENCE = 111;
    public static final int EVENT_PHOTO_TYPE_NEW_EVIDENCE = 112;
    public static final int EVENT_PHOTO_TYPE_MONITORING = 113;
    public static final int EVENT_PHOTO_TYPE_CAMERA= 114;
    public static final int EVENT_NEW_WITNESS = 115;
    public static final int EVENT_PHOTO_TYPE_SIGN = 116;

    public static final int EVENT_PEOPLE_DELETE = 201;
    public static final int EVENT_ITEM_DELETE = 202;
    public static final int EVENT_TOOL_DELETE = 203;
    public static final int EVENT_POSITION_DELETE = 204;
    public static final int EVENT_FLAT_DELETE = 205;
    public static final int EVENT_FLAT_EDIT = 2051;
    public static final int EVENT_POSITION_PHOTO_DELETE = 206;
    public static final int EVENT_LIKE_PHOTO_DELETE = 207;
    public static final int EVENT_IMPORTANT_PHOTO_DELETE = 208;
    public static final int EVENT_EVIDENCE_DELETE = 209;
    public static final int EVENT_MONITORING_DELETE = 210;
    public static final int EVENT_CAMERA_DELETE = 211;
    public static final int EVENT_WITNESS_DELETE = 212;

    public static String copyToInternalPath(Activity activity, String OldPath){
        String NewPath = "";
        File mediaStorageDir = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Report");
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return NewPath;
            }
        }
        String[] filename = OldPath.split("/");
        NewPath = new File(mediaStorageDir.getPath() + File.separator + filename[filename.length-1]).toString();
        Log.d("Anita", "new path = "+NewPath);
        BackupRestore.copyFile(OldPath, NewPath);
        BackupRestore.deleteFiles(new File(OldPath));
        return NewPath;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()获取ListView对应的Adapter
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    public static int getStatusHeight(Context context)
    {
        int statusHeight = -1;
        try
        {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public static  int getBottomStatusHeight(Context context){
        int totalHeight = getDpi(context);

        int contentHeight = getScreenHeight(context);

        return totalHeight  - contentHeight;
    }

    public static int getScreenHeight(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getDpi(Context context){
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi=displayMetrics.heightPixels;
        }catch(Exception e){
            e.printStackTrace();
        }
        return dpi;
    }

    public static File getOutputMediaFile(Context context, int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Report");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == EVENT_PHOTO_TYPE_POSITION){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "POSITION_PHOTO_"+ timeStamp + ".jpg");
        } else if(type == EVENT_PHOTO_TYPE_LIKE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "OVERVIEW_PHOTO_"+ timeStamp + ".jpg");
        } else if (type == EVENT_PHOTO_TYPE_IMPORTANT) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMPORTANT_PHOTO_"+ timeStamp + ".jpg");
        } else if (type == EVENT_PHOTO_TYPE_NEW_EVIDENCE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "NEW_EVIDENCE_"+ timeStamp + ".jpg");
        } else if (type == EVENT_PHOTO_TYPE_MONITORING){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "MONITORING_PHOTO_"+ timeStamp + ".jpg");
        } else if(type == EVENT_PHOTO_TYPE_CAMERA) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "CAMERA_PHOTO_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static Bitmap loadBitmapFromFile(File f) {
        Bitmap b = null;
        BitmapFactory.Options option = new BitmapFactory.Options();
        // Bitmap sampling factor, size = (Original Size)/(inSampleSize)
        option.inSampleSize = 4;

        try {
            FileInputStream fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, option);
            fis.close();

            Matrix matrix = new Matrix();
            matrix.setRotate(0, (float)b.getWidth()/2, (float)b.getHeight()/2);
            Bitmap resultImage = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);

            return resultImage;
        } catch(Exception e) {
            return null;
        }
    }
}
