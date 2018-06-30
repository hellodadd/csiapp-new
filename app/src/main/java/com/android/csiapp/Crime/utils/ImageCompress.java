package com.android.csiapp.Crime.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by liwei on 2017/3/13.
 */

public class ImageCompress {
    //按照指定的长高获取一个正确的压缩比
    private static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        final int height=options.outHeight;
        final int width=options.outWidth;
        int inSmapleSize=1;
        if(height>reqHeight || width>reqWidth){
            final int heightRatio=Math.round((float)height/(float)reqHeight);
            final int widthRatio=Math.round((float)width/(float)reqWidth);
            inSmapleSize=heightRatio<widthRatio?heightRatio:widthRatio;
        }
        return inSmapleSize;
    }
    //压缩指定的文件到指定大小
    public static String getSmallBitmap(String filePath,int reqWidth,int reqHeight) {
        String desFile = filePath;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        //计算压缩比
        options.inSampleSize = ImageCompress.calculateInSampleSize(options, reqWidth, reqHeight);
        //压缩文件
        if (options.inSampleSize != 1) {
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            FileOutputStream fos = null;
            //设置新的文件名,获取文件路径的最后一个.位置
            int dotWei = filePath.lastIndexOf(".");
            desFile = filePath.substring(0, dotWei - 1) + "p" + filePath.substring(dotWei);
            try {
                fos = new FileOutputStream(desFile);
                if (bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                }
                //删除原有的文件
                File file=new File(filePath);
                file.delete();

            } catch (Exception e) {
                e.printStackTrace();
                desFile = "";
            }
        }
        return desFile;
    }
}
