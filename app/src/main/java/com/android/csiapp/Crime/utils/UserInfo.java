package com.android.csiapp.Crime.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.csiapp.Databases.IdentifyProvider;
import com.android.csiapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by user on 2016/11/30.
 */
public class UserInfo {
    private Context mContext;

    public static final String mSceneConductor = "SceneConductor";
    public static final String mAccessInspectors = "AccessInspectors";
    public static final String mExtractionPeople = "ExtractionPeople";

    private static ArrayList<String> mUser = new ArrayList<String>();
    private static ArrayList<String> mLogin = new ArrayList<String>();
    private static HashMap<String,String> mUnitCode = new HashMap<String,String>();
    private static HashMap<String,String> mUnitName = new HashMap<String,String>();
    private static HashMap<String,String> mLoginUserHashMap  = new HashMap<String,String>();
    public static ArrayList<Integer> mUserNodes = new ArrayList<Integer>();

    public UserInfo(Context context){
        this.mContext = context;
    }

    public static void getInitialUser(Context context){
        SharedPreferences prefs = context.getSharedPreferences("InitialDevice", 0);
        String initstatus = prefs.getString("Initial", "0");
        IdentifyProvider identifyProvider = new IdentifyProvider(context);

        if(initstatus.equalsIgnoreCase("1")) {
            mUser = (ArrayList<String>) identifyProvider.queryToGetUser();
            mLogin = (ArrayList<String>) identifyProvider.queryToGetLogin();
            mLoginUserHashMap  = (HashMap<String,String>) identifyProvider.queryToGetHashMap();

            mUnitCode = (HashMap<String,String>) identifyProvider.queryToGetUnitCodeHashMap();
            mUnitName = (HashMap<String,String>) identifyProvider.queryToGetUnitNameHashMap();
            Object[] objects= sortWithTree(mUser, mLogin);
            if(objects.length==3) {
                mUser = (ArrayList<String>) objects[0];
                mLogin = (ArrayList<String>) objects[1];
                mUserNodes = (ArrayList<Integer>) objects[2];
            }else {
                mUserNodes = getTreeNodes(mLogin);
            }
        }
    }

    private static Object[] sortWithTree(ArrayList<String> mUser, ArrayList<String> mLogin){
        ArrayList<String> sortUserList = new ArrayList<String>();
        ArrayList<String> sortLoginList = new ArrayList<String>();
        ArrayList<Integer> sortNodeList = new ArrayList<Integer>();
        for(int i=0;i<mLogin.size();i++){
            String unitCode = mUnitCode.get(mLogin.get(i));
            String unitName = mUnitName.get(mLogin.get(i));
            if(!sortLoginList.contains(unitCode)){
                sortLoginList.add(unitCode);
                sortUserList.add(unitName);
                mLoginUserHashMap.put(unitCode, unitName);
                sortNodeList.add(0);
            }
        }
        Collections.reverse(mUser);
        Collections.reverse(mLogin);
        for(int j=0;j<mLogin.size();j++){
            for(int k = 0;k<sortLoginList.size();k++) {
                if (mUnitCode.get(mLogin.get(j)).equalsIgnoreCase(sortLoginList.get(k))) {
                    sortLoginList.add(k + 1, mLogin.get(j));
                    sortUserList.add(k + 1, mUser.get(j));
                    sortNodeList.add(k + 1,1);
                    break;
                }
            }
        }
        return new Object[]{sortUserList, sortLoginList, sortNodeList};
    }

    private static ArrayList<Integer> getTreeNodes(ArrayList<String> mLogin){
        ArrayList<Integer> DEMO_NODES = new ArrayList<Integer>();
        for(int z = 0; z<mLogin.size(); z++){
            int level=0;
            DEMO_NODES.add(level);
        }
        return DEMO_NODES;
    }

    public String getTitle(String rootkey) {
        String title = "";
        switch (rootkey) {
            case mSceneConductor:
                title = mContext.getResources().getString(R.string.scene_conductor);
                break;
            case mAccessInspectors:
                title = mContext.getResources().getString(R.string.access_inspectors);
                break;
            case mExtractionPeople:
                title = mContext.getResources().getString(R.string.extraction_people);
                break;
            default:
                break;
        }
        return title;
    }

    public String getMethod(String rootkey){
        String method = "Multiple";
        switch (rootkey) {
            case mSceneConductor:
                method = "Single";
                break;
            case mAccessInspectors:
                method = "Multiple";
                break;
            case mExtractionPeople:
                method = "Multiple";
                break;
            default:
                break;
        }
        return method;
    }

    public static ArrayList<Integer> getNodes(String rootkey) {

        return mUserNodes;
    }

    public static ArrayList<String> getLoginNameList(String rootkey) {
        return mLogin;
    }

    public static  HashMap<String,String> getUnitCodeMap(){
        return mUnitCode;
    }

    public ArrayList<String> getArray() {
        ArrayList<String> result = new ArrayList<String>();
        result = (mUser.size()!=0)?mUser:new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.sceneConductor)));
        return result;
    }

    public static String getLoginName(String UserName) {
        String result = "";

        if(UserName.equalsIgnoreCase("")) return result;

        String[] item =  UserName.split(",");
        boolean isFirst = true;
        for(int i = 0;i<item.length;i++){
            if(isFirst) {
                isFirst = false;
            }else {
                result = result+",";
            }
            if(mLoginUserHashMap.size()!=0) result = result+valueGetKey(mLoginUserHashMap, item[i].trim());
        }
        return result;
    }

    public static String getUserName(String LoginName) {
        String result = "";

        if(LoginName.equalsIgnoreCase("")) return result;

        String[] item =  LoginName.split(",");
        boolean isFirst = true;
        for(int i = 0;i<item.length;i++){
            if(isFirst) {
                isFirst = false;
            }else {
                result = result+",";
            }
            if(mLoginUserHashMap.size()!=0) result = result+mLoginUserHashMap.get(item[i].trim());
        }
        return result;
    }

    private static String valueGetKey(Map map, String value) {
        Set set = map.entrySet();
        ArrayList arr = new ArrayList<>();
        Iterator it = set.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            if(entry.getValue().equals(value)) {
                String s = (String) entry.getKey();
                arr.add(s);
            }
        }
        if(arr.size()!=0) return (String) arr.get(0);
        else return "";
    }
}
