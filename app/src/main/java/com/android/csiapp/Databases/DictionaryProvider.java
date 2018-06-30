package com.android.csiapp.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.csiapp.XmlHandler.Dictionary;
import com.android.csiapp.XmlHandler.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/11/7.
 */
public class DictionaryProvider {
    // 表格名稱
    public static final String TABLE_NAME = "dictionary";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String DICTKEY_COLUMN = "dictkey";
    public static final String PARENTKEY_COLUMN = "parentkey";
    public static final String ROOTKEY_COLUMN = "rootkey";
    public static final String DICTVALUE_COLUMN = "dictvalue";
    public static final String DICTREMARK_COLUMN = "dictremark";
    public static final String DICTSPELL_COLUMN = "dictspell";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String IDENTIFY_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DICTKEY_COLUMN + " TEXT NOT NULL, " +
                    PARENTKEY_COLUMN + " TEXT NOT NULL, " +
                    ROOTKEY_COLUMN + " TEXT NOT NULL, " +
                    DICTVALUE_COLUMN + " TEXT NOT NULL, " +
                    DICTREMARK_COLUMN + " TEXT NOT NULL, " +
                    DICTSPELL_COLUMN + " TEXT NOT NULL)";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public DictionaryProvider(Context context) {
        db = DatabasesHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    public void insert(Dictionary dictionary) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(DICTKEY_COLUMN, dictionary.getDictKey());
        cv.put(PARENTKEY_COLUMN, dictionary.getParentKey());
        cv.put(ROOTKEY_COLUMN, dictionary.getRootKey());
        cv.put(DICTVALUE_COLUMN, dictionary.getDictValue());
        cv.put(DICTREMARK_COLUMN, (dictionary.getDictRemark()==null)?"":dictionary.getDictRemark());
        cv.put(DICTSPELL_COLUMN, (dictionary.getDictSpell()==null)?"":dictionary.getDictSpell());

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);
    }

    // 刪除所有記事資料
    public void deleteAll(){
        // 刪除原有的表格
        //Login
        db.execSQL("DROP TABLE IF EXISTS " + DictionaryProvider.TABLE_NAME);
        // 建立新版的表格
        //Login
        db.execSQL(DictionaryProvider.IDENTIFY_TABLE);
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

    public List<String> queryToGetDictKey(String rootkey){
        List<String> list = new ArrayList<>();
        String[] projection = new String[] {DICTKEY_COLUMN, PARENTKEY_COLUMN, DICTVALUE_COLUMN};
        String where = ROOTKEY_COLUMN + " = '" + rootkey +"'";
        Cursor cursor = db.query(TABLE_NAME, projection, where, null, null, null, DICTKEY_COLUMN, null);
        if(cursor==null || cursor.getCount()==0) {
            return new ArrayList<String>();
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public HashMap<String,String> queryToGetParentHashMap(String rootkey){
        HashMap<String,String> list = new HashMap<String,String>();
        String[] projection = new String[] {DICTKEY_COLUMN, PARENTKEY_COLUMN, DICTVALUE_COLUMN};
        String where = ROOTKEY_COLUMN + " = '" + rootkey +"'";
        Cursor cursor = db.query(TABLE_NAME, projection, where, null, null, null, DICTKEY_COLUMN, null);
        if(cursor==null || cursor.getCount()==0) {
            return new HashMap<String, String>();
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.put(cursor.getString(0), cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> queryToGetList(String rootkey){
        List<String> list = new ArrayList<>();
        String[] projection = new String[] {DICTKEY_COLUMN, PARENTKEY_COLUMN, DICTVALUE_COLUMN};
        String where = ROOTKEY_COLUMN + " = '" + rootkey +"'";
        Cursor cursor = db.query(TABLE_NAME, projection, where, null, null, null, DICTKEY_COLUMN, null);
        if(cursor==null || cursor.getCount()==0) {
            return new ArrayList<String>();
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public HashMap<String,String> queryToGetHashMap(String rootkey){
        HashMap<String,String> list = new HashMap<String,String>();
        String[] projection = new String[] {DICTKEY_COLUMN, PARENTKEY_COLUMN, DICTVALUE_COLUMN};
        String where = ROOTKEY_COLUMN + " = '" + rootkey +"'";
        Cursor cursor = db.query(TABLE_NAME, projection, where, null, null, null, DICTKEY_COLUMN, null);
        if(cursor==null || cursor.getCount()==0) {
            return new HashMap<String, String>();
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.put(cursor.getString(0), cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public String queryUnitArea(String rootkey, String unitCode){
        String result = "";
        String[] projection = new String[] {DICTVALUE_COLUMN};
        String where = ROOTKEY_COLUMN + " = '" + rootkey +"' AND " + DICTKEY_COLUMN + " = '" + unitCode +"'";
        Cursor cursor = db.query(TABLE_NAME, projection, where, null, null, null, DICTVALUE_COLUMN, null);
        if(cursor==null || cursor.getCount()==0) {
            return "";
        }
        cursor.moveToFirst();
        result = cursor.getString(0);
        cursor.close();
        return result;
    }
}
