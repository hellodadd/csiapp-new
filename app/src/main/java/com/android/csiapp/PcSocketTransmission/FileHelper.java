package com.android.csiapp.PcSocketTransmission;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by JOWE on 2016/9/29.
 */

public class FileHelper {
    private static String FILEPATH = "";

    public static File newFile(String filename) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardDir = Environment.getExternalStorageDirectory();
            // SDCard目录：/mnt/sdcard
            FILEPATH = sdCardDir.getAbsolutePath();
            System.out.println("Sdcard dir=" + FILEPATH);
        }

        File file = null;

        try {
            file = new File(FILEPATH, filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void writeFile(File file, byte[] data, int offset, int count) throws IOException {
        FileOutputStream fos = new FileOutputStream(file, true);
        fos.write(data, offset, count);
        fos.flush();
        fos.close();
    }

    public static byte[] readFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        int leng = bis.available();
        byte[] b = new byte[leng];
        bis.read(b, 0, leng);
        bis.close();
        return b;
    }

    public static void writeStringToFile(String textstring, String filepath, String filename) {
        try {
            File file = new File(filepath, filename);
            FileWriter writer = new FileWriter(file);
            writer.write(textstring);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
