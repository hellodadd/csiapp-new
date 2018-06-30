package com.android.csiapp.Crime.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by joanlin on 16/9/21.
 */
public class BackupRestore extends AsyncTask<String, Void, String> {

    private static final String COMMAND_BACKUP = "backupDatabase";
    public static final String COMMAND_RESTORE = "restroeDatabase";
    private Context mContext;
    private String mSendResult = "";
    String mBackupPath = Environment.getExternalStorageDirectory()+"/Csibackup/";
    String mCachePath = Environment.getExternalStorageDirectory()+"/Csibackup/cache";

    public BackupRestore(Context context) {
        this.mContext = context;
    }

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub

        // 默认路径是 /data/data/(包名)/databases/*.db
        File dbFile = mContext.getDatabasePath("csi_databases.db");
        File exportDir = new File(mBackupPath);
        File cacheDir = new File(mCachePath);
        String cmd = params[0];

        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        if(!cacheDir.exists()) {
            cacheDir.mkdir();
        }

        boolean dbresult = false;
        boolean picresult = false;
        boolean baidumapresult = false;

        //Get Picture path (Report/BaiduMap)
        String picpath = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/Report";
        String baidumappath = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/BaiduMap";
        String exportpicpath = mCachePath+ "/pictures/Report";
        String exportbaidumappath = mCachePath+ "/pictures/BaiduMap";

        if (cmd.equals(COMMAND_BACKUP) ) {
            dbresult = DbBackupRestore(cmd, cacheDir, dbFile);
            picresult = picBackupRestore(cmd, picpath, exportpicpath);
            baidumapresult = picBackupRestore(cmd, baidumappath, exportbaidumappath);
            if (dbresult == true && picresult ==true && baidumapresult==true) {
                mSendResult  = "备份成功";
            } else {
                mSendResult  = "备份失败";
            }
            try {
                LinkedList<File> files = DirTraversal.listLinkedFiles(mCachePath);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String backupFileName = "backup_"+timeStamp+".zip";
                File file = DirTraversal.getFilePath(mBackupPath, backupFileName);
                ZipUtils.zipFiles(files, file);
                deleteFiles(cacheDir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (cmd.equals(COMMAND_RESTORE)) {
            String filename = params[1];
            File file = DirTraversal.getFilePath(mBackupPath, filename);
            try {
                ZipUtils.upZipFile(file, mCachePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String origfilepath = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            File origfile = new File(origfilepath);
            deleteFiles(origfile);

            dbresult = DbBackupRestore(cmd, cacheDir, dbFile);
            picresult = picBackupRestore(cmd, picpath, exportpicpath);
            baidumapresult = picBackupRestore(cmd, baidumappath, exportbaidumappath);
            if (dbresult == true && picresult ==true && baidumapresult == true) {
                mSendResult = "恢复成功";
                deleteFiles(cacheDir);
            } else {
                mSendResult  ="恢复失败";
            }
        }
        return mSendResult;
    }

    private boolean DbBackupRestore (String cmd, File exportdir, File dbfile) {

        File backup = new File(exportdir, dbfile.getName());
        boolean result = false;
        String command = cmd;

        if (command.equals(COMMAND_BACKUP)) {
            try {
                backup.createNewFile();
                DbCopy(dbfile, backup);
                result = true;
                Log.d("backup", "backup success");
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("backup", "backup fail");
                result = false;
                return result;
            }
        } else if (command.equals(COMMAND_RESTORE)) {
            try {
                DbCopy(backup, dbfile);
                Log.d("restore", "restore success");
                result = true;
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("restore", "restore fail");
                e.printStackTrace();
                result = false;
                return result;
            }
        } else {
            return result;
        }
    }

    private boolean picBackupRestore (String cmd, String picpath, String backuppath) {
        boolean result = false;
        if (cmd.equals(COMMAND_BACKUP)) {
            try {
                copyFolder(picpath, backuppath);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
            return result;
        } else if (cmd.equals(COMMAND_RESTORE)) {
            try {
                copyFolder(backuppath, picpath);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
            return result;
        } else {
            return result;
        }

    }

    private void DbCopy(File dbFile, File backup) throws IOException {
        FileChannel inChannel = new FileInputStream(dbFile).getChannel();
        FileChannel outChannel = new FileOutputStream(backup).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    /**
     * Copy file
     * @param oldPath String Original path
     * @param newPath String Copy path
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Copy folder
     * @param oldPath String Original path
     * @param newPath String Copy path
     * @return boolean
     */
    public static void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs();// If the folder is not exist, we need to create it.
            File a=new File(oldPath);
            String[] file=a.list();
            File temp=null;
            for (int i = 0; i < file.length; i++) {
                if(oldPath.endsWith(File.separator)){
                    temp=new File(oldPath+file[i]);
                }
                else{
                    temp=new File(oldPath+File.separator+file[i]);
                }

                if(temp.isFile()){
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ( (len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if(temp.isDirectory()){//If it is child folder
                    copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        if (result.equals("备份成功")) {
            Toast.makeText(mContext, "备份路径:" + mBackupPath, Toast.LENGTH_SHORT).show();
        }
    }

    public static void deleteFiles(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if(file.isDirectory()){
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
    }

            for (int i = 0; i < childFiles.length; i++) {
                deleteFiles(childFiles[i]);
            }
            file.delete();
        }
    }
}
