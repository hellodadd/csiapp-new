package com.android.csiapp.XmlHandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.SystemProperties;
import android.util.Log;

import com.android.csiapp.Crime.utils.BackupRestore;
import com.android.csiapp.Crime.utils.DirTraversal;
import com.android.csiapp.Crime.utils.ZipUtils;
import com.android.csiapp.Databases.CrimeItem;
import com.android.csiapp.Databases.CrimeProvider;
import com.android.csiapp.Databases.DictionaryProvider;
import com.android.csiapp.Databases.IdentifyProvider;
import com.android.csiapp.PcSocketTransmission.FileHelper;
import com.amap.api.maps.MapsInitializer;

import org.dom4j.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by user on 2016/11/1.
 */
public class DataInitial {
    private Context mContext = null;

    //Page 1
    private HashMap<String,String> vParentData = new HashMap<>();
    private HashMap<String, HashMap<String,List<String>>> mCasetypeData = new HashMap<>();
    private HashMap<String,String> mAreaParentData = new HashMap<>();
    private HashMap<String, HashMap<String,List<String>>> mAreaData = new HashMap<>();

    public DataInitial(Context context){
        this.mContext = context;
    }

    //Command 1
    public void createDeviceMsgXml() {
        XmlHandler xmlhandler = new XmlHandler();
        String deviceid = (SystemProperties.get("ro.serialno"));
        String initstatus = "0";
        String swversion = "";
        String mapversion= MapsInitializer.getVersion();

        SharedPreferences prefs = mContext.getSharedPreferences("InitialDevice", 0);
        initstatus = prefs.getString("Initial", "0");

        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            swversion = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        xmlhandler.createDeviceMsg(deviceid, initstatus, swversion, mapversion);
    }

    //Command 2
    public boolean InitialDevice(){
        XmlHandler xmlhandler = new XmlHandler();
        Object[] object = xmlhandler.getInitialDeviceCmd();

        if(object == null || object.length!=2) return false;

        List<Dictionary> dictionarys = (List<Dictionary>) object[0];
        List<User> users = (List<User>) object[1];

        //Prase Dictionary
        DictionaryProvider mDictionary = new DictionaryProvider(mContext);
        mDictionary.deleteAll();
        for(int i=0;i<dictionarys.size();i++){
            mDictionary.insert(dictionarys.get(i));
        }

        //Prase User
        IdentifyProvider mIdentify = new IdentifyProvider(mContext);
        mIdentify.deleteAll();
        for(int j=0;j<users.size();j++){
            mIdentify.insert(users.get(j));
        }

        SharedPreferences.Editor editor = mContext.getSharedPreferences("InitialDevice", 0).edit();
        editor.putString("Initial", "1");
        editor.commit();

        return true;
    }

    public boolean InitialDevice(Document doc){
        XmlHandler xmlhandler = new XmlHandler();
        Object[] object = xmlhandler.getInitialDeviceCmd(doc);

        if(object == null || object.length!=2) return false;

        List<Dictionary> dictionarys = (List<Dictionary>) object[0];
        List<User> users = (List<User>) object[1];

        //Prase Dictionary
        DictionaryProvider mDictionary = new DictionaryProvider(mContext);
        mDictionary.deleteAll();
        for(int i=0;i<dictionarys.size();i++){
            mDictionary.insert(dictionarys.get(i));
        }

        //Prase User
        IdentifyProvider mIdentify = new IdentifyProvider(mContext);
        mIdentify.deleteAll();
        for(int j=0;j<users.size();j++){
            mIdentify.insert(users.get(j));
        }

        SharedPreferences.Editor editor = mContext.getSharedPreferences("InitialDevice", 0).edit();
        editor.putString("Initial", "1");
        editor.commit();

        return true;
    }

    //Command 11
    public boolean CreateBaseMsg(){
        XmlHandler xmlhandler = new XmlHandler();
        String[] object = xmlhandler.getSceneListCmd();

        if(object==null || object.length!=2) return false;

        String name = object[0];
        String unit = object[1];

        CrimeProvider mCrimeProvider = new CrimeProvider(mContext);
        mCrimeProvider.createScenesInfoXml(name);

        return true;
    }

    public void CreateBaseMsg(String name){
        CrimeProvider mCrimeProvider = new CrimeProvider(mContext);
        mCrimeProvider.createScenesInfoXml(name);
    }

    //Command 12
    public boolean CreateBaseMsgIdZip(String id){
        CrimeProvider mCrimeProvider = new CrimeProvider(mContext);
        CrimeItem crimeItem = mCrimeProvider.createBaseMsgXml(id);

        if(crimeItem==null) return false;
        if(!crimeItem.getComplete().equalsIgnoreCase("1")) return false;

        String catchPath = Environment.getExternalStorageDirectory()+"/BaseMsg/";
        File cacheDir = new File(catchPath);
        if(!cacheDir.exists()) {
            cacheDir.mkdir();
        }

        try {
            BackupRestore.copyFile(FileHelper.newFile("ScenesMsg.xml").toString(), catchPath+"ScenesMsg.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(crimeItem.getCellResultItem().size()!=0){
            for(int i=0;i<crimeItem.getCellResultItem().size();i++){
                String path = crimeItem.getCellResultItem().get(i).getPhotoPath();
                String[] filename = path.split("/");
                BackupRestore.copyFile(path, catchPath+filename[filename.length-1]);
            }
        }
        if(crimeItem.getPosition().size()!=0){
            for(int i=0;i<crimeItem.getPosition().size();i++){
                String path = crimeItem.getPosition().get(i).getPhotoPath();
                String[] filename = path.split("/");
                BackupRestore.copyFile(path, catchPath+filename[filename.length-1]);
            }
        }
        if(crimeItem.getFlat().size()!=0){
            for(int i=0;i<crimeItem.getFlat().size();i++){
                String path = crimeItem.getFlat().get(i).getPhotoPath();
                String[] filename = path.split("/");
                BackupRestore.copyFile(path, catchPath+filename[filename.length-1]);
                //拷贝dwg文件到指定目录
                BackupRestore.copyFile(path.replaceAll(".png",".dwg"), catchPath+filename[filename.length-1].replaceAll(".png",".dwg"));
                BackupRestore.copyFile(path.replaceAll(".png",".pdf"), catchPath+filename[filename.length-1].replaceAll(".png",".pdf"));
            }
        }
        if(crimeItem.getPositionPhoto().size()!=0){
            for(int i=0;i<crimeItem.getPositionPhoto().size();i++){
                String path = crimeItem.getPositionPhoto().get(i).getPhotoPath();
                String[] filename = path.split("/");
                BackupRestore.copyFile(path, catchPath+filename[filename.length-1]);
            }
        }
        if(crimeItem.getOverviewPhoto().size()!=0){
            for(int i=0;i<crimeItem.getOverviewPhoto().size();i++){
                String path = crimeItem.getOverviewPhoto().get(i).getPhotoPath();
                String[] filename = path.split("/");
                BackupRestore.copyFile(path, catchPath+filename[filename.length-1]);
            }
        }
        if(crimeItem.getImportantPhoto().size()!=0){
            for(int i=0;i<crimeItem.getImportantPhoto().size();i++){
                String path = crimeItem.getImportantPhoto().get(i).getPhotoPath();
                String[] filename = path.split("/");
                BackupRestore.copyFile(path, catchPath+filename[filename.length-1]);
            }
        }
        if(crimeItem.getMonitoringPhoto().size()!=0){
            for(int i=0;i<crimeItem.getMonitoringPhoto().size();i++){
                String path = crimeItem.getMonitoringPhoto().get(i).getPhotoPath();
                String[] filename = path.split("/");
                BackupRestore.copyFile(path, catchPath+filename[filename.length-1]);
            }
        }
        if(crimeItem.getCameraPhoto().size()!=0){
            for(int i=0;i<crimeItem.getCameraPhoto().size();i++){
                String path = crimeItem.getCameraPhoto().get(i).getPhotoPath();
                String[] filename = path.split("/");
                BackupRestore.copyFile(path, catchPath+filename[filename.length-1]);
            }
        }
        if(crimeItem.getEvidenceItem().size()!=0){
            for(int i=0;i<crimeItem.getEvidenceItem().size();i++){
                String path = crimeItem.getEvidenceItem().get(i).getPhotoPath();
                String[] filename = path.split("/");
                BackupRestore.copyFile(path, catchPath+filename[filename.length-1]);
            }
        }
        if(crimeItem.getWitness().size()!=0){
            for(int i=0;i<crimeItem.getWitness().size();i++){
                String path = crimeItem.getWitness().get(i).getPhotoPath();
                String[] filename = path.split("/");
                BackupRestore.copyFile(path, catchPath+filename[filename.length-1]);
            }
        }

        try{
            LinkedList<File> files = DirTraversal.listLinkedFiles(catchPath);
            String backupFileName = "/SceneMsg.zip";
            File file = DirTraversal.getFilePath(Environment.getExternalStorageDirectory().getAbsolutePath(), backupFileName);
            ZipUtils.zipFiles(files, file);
            BackupRestore.deleteFiles(cacheDir);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    //Command 13
    public boolean WriteSceneNo(){
        XmlHandler xmlhandler = new XmlHandler();
        String[] object = xmlhandler.writeSceneIdCmd();

        if(object == null || object.length==0) return false;

        String id = object[0];
        Log.d("Anita","Scene Id = "+id);
        String SceneNo = object[1];
        Log.d("Anita","Scene No = "+SceneNo);
        CrimeProvider mCrime = new CrimeProvider(mContext);
        CrimeItem mCrimeItem = mCrime.getRecordBySceneId(id);

        if(mCrimeItem == null || SceneNo.length() ==0) return false;
        if(mCrimeItem.getComplete().equalsIgnoreCase("0")) return false;

        mCrimeItem.setComplete("2");
        mCrimeItem.setCreateTime(Calendar.getInstance().getTimeInMillis());
        mCrimeItem.setSceneNo(SceneNo);
        mCrime.update(mCrimeItem);

        return true;
    }

    //Command 14
    public boolean deleteSceneInfo(){
        XmlHandler xmlhandler = new XmlHandler();
        List<String> object = xmlhandler.deleteSceneInfoCmd();
        List result = new ArrayList<String>();

        if(object == null || object.size()==0) return false;

        CrimeProvider mCrime = new CrimeProvider(mContext);
        for(int i=0;i<object.size();i++){
            String id = object.get(i);
            CrimeItem mCrimeItem = mCrime.getRecordBySceneId(id);
            if(mCrimeItem != null && !mCrimeItem.getComplete().equalsIgnoreCase("2")) {
                mCrimeItem.setDelete("1");
                mCrime.update(mCrimeItem);
                result.add(id);
            }else{
                Log.d("Anita","Cannot get the scene id from databases");
            }
        }

        if(result.size()!=0){
            xmlhandler.createSuccessDeleteMsgFile(result);
            return true;
        }else{
            return false;
        }
    }
}
