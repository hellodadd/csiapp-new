package com.android.csiapp.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.csiapp.XmlHandler.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/9/29.
 */
public class IdentifyProvider {

    // 表格名稱
    public static final String TABLE_NAME = "identify";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String LOGINNAME_COLUMN = "loginname";
    public static final String PASSWORD_COLUMN = "password";
    public static final String USERNAME_COLUMN = "username";
    public static final String UNITCODE_COLUMN = "unitcode";
    public static final String UNITNAME_COLUMN = "unitname";
    public static final String IDCARDNO_COLUMN = "idcardno";
    public static final String CONTACT_COLUMN = "contact";
    public static final String DUTY_COLUMN = "duty";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String IDENTIFY_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LOGINNAME_COLUMN + " TEXT NOT NULL, " +
                    PASSWORD_COLUMN + " TEXT NOT NULL, " +
                    USERNAME_COLUMN + " TEXT NOT NULL, " +
                    UNITCODE_COLUMN + " TEXT NOT NULL, " +
                    UNITNAME_COLUMN + " TEXT NOT NULL, " +
                    IDCARDNO_COLUMN + " TEXT NOT NULL, " +
                    CONTACT_COLUMN + " TEXT NOT NULL, " +
                    DUTY_COLUMN + " TEXT NOT NULL)";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public IdentifyProvider(Context context) {
        db = DatabasesHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public void insert(String User, String Password) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(LOGINNAME_COLUMN, User);
        cv.put(PASSWORD_COLUMN, Password);
        cv.put(USERNAME_COLUMN, "");
        cv.put(UNITCODE_COLUMN, "");
        cv.put(UNITNAME_COLUMN, "");
        cv.put(IDCARDNO_COLUMN, "");
        cv.put(CONTACT_COLUMN, "");
        cv.put(DUTY_COLUMN, "");

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);
    }

    public void insert(User user) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(LOGINNAME_COLUMN, user.getLoginName());
        cv.put(PASSWORD_COLUMN, user.getPassword());
        cv.put(USERNAME_COLUMN, user.getUserName());
        cv.put(UNITCODE_COLUMN, user.getUnitCode());
        cv.put(UNITNAME_COLUMN, user.getUnitName());
        cv.put(IDCARDNO_COLUMN, user.getIdCardNo());
        cv.put(CONTACT_COLUMN, (user.getContact()==null)?"":user.getContact());
        cv.put(DUTY_COLUMN, (user.getDuty()==null)?"":user.getDuty());

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);
    }

    // 讀取User name
    public String getUser() {
        String result ="";
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor!=null && cursor.moveToNext()) {
            result = cursor.getString(1);
        }

        cursor.close();
        return result;
    }

    // 讀取Password
    public String getPassword() {
        String result ="";
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor!=null && cursor.moveToNext()) {
            result = cursor.getString(2);
        }

        cursor.close();
        return result;
    }

    public String getUnitCode(String name) {
        String result ="";
        String[] projection = new String[] {LOGINNAME_COLUMN, UNITCODE_COLUMN};
        String where = LOGINNAME_COLUMN + " = '" + name + "'";
        Cursor cursor = db.query(TABLE_NAME, projection, where, null, null, null, null, null);

        while (cursor!=null && cursor.moveToNext()) {
            result = cursor.getString(1);
        }

        cursor.close();
        return result;
    }

    public String getUnitName(String name) {
        String result ="";
        String[] projection = new String[] {LOGINNAME_COLUMN, UNITNAME_COLUMN};
        String where = LOGINNAME_COLUMN + " = '" + name + "'";
        Cursor cursor = db.query(TABLE_NAME, projection, where, null, null, null, null, null);

        while (cursor!=null && cursor.moveToNext()) {
            result = cursor.getString(1);
        }

        cursor.close();
        return result;
    }

    public String getUserName(String name) {
        String result ="";
        String[] projection = new String[] {LOGINNAME_COLUMN, USERNAME_COLUMN};
        String where = LOGINNAME_COLUMN + " = '" + name + "'";
        Cursor cursor = db.query(TABLE_NAME, projection, where, null, null, null, null, null);

        while (cursor!=null && cursor.moveToNext()) {
            result = cursor.getString(1);
        }

        cursor.close();
        return result;
    }

    // 刪除所有記事資料
    public void deleteAll(){
        // 刪除原有的表格
        //Login
        db.execSQL("DROP TABLE IF EXISTS " + IdentifyProvider.TABLE_NAME);
        // 建立新版的表格
        //Login
        db.execSQL(IdentifyProvider.IDENTIFY_TABLE);
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

    public boolean checkPasswordFromName(String name,String password){
        String[] projection = new String[] {LOGINNAME_COLUMN, PASSWORD_COLUMN};
        String where = LOGINNAME_COLUMN + " = '" + name +"'";
        Cursor cursor = db.query(TABLE_NAME, projection, where, null, null, null, null, null);
        if(cursor==null) {
            return false;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if(cursor.getString(1).equalsIgnoreCase(password)) return true;
            cursor.moveToNext();
        }

        cursor.close();
        return false;
    }

    public List<String> queryToGetUser(){
        List<String> list = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        if(cursor==null) {
            return null;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(3));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> queryToGetLogin(){
        List<String> list = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        if(cursor==null) {
            return null;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public HashMap<String,String> queryToGetUnitCodeHashMap(){
        HashMap<String,String> list = new HashMap<String,String>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        if(cursor==null) {
            return null;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.put(cursor.getString(1), cursor.getString(4));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public HashMap<String,String> queryToGetUnitNameHashMap(){
        HashMap<String,String> list = new HashMap<String,String>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        if(cursor==null) {
            return null;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.put(cursor.getString(1), cursor.getString(5));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public HashMap<String,String> queryToGetHashMap(){
        HashMap<String,String> list = new HashMap<String,String>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        if(cursor==null) {
            return null;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.put(cursor.getString(1), cursor.getString(3));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    // 建立範例資料
    public void sample() {
        insert("admin","e10adc3949ba59abbe56e057f20f883e");
    }
}
