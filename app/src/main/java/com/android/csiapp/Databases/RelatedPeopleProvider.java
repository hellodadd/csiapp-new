package com.android.csiapp.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.csiapp.Crime.utils.DictionaryInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by user on 2016/10/12.
 */
public class RelatedPeopleProvider {
    // 表格名稱
    public static final String TABLE_NAME = "relatedpeople";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String UUID_COLUMN = "uuid";
    public static final String PEOPLE_RELATION_COLUMN = "people_relation";
    public static final String PEOPLE_NAME_COLUMN = "people_name";
    public static final String PEOPLE_SEX_COLUMN = "people_sex";
    public static final String PEOPLE_ID_COLUMN = "people_id";
    public static final String PEOPLE_NUMBER_COLUMN = "people_number";
    public static final String PEOPLE_ADDRESS_COLUMN = "people_address";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    UUID_COLUMN + " TEXT NOT NULL, " +
                    PEOPLE_RELATION_COLUMN + " TEXT NOT NULL, " +
                    PEOPLE_NAME_COLUMN + " TEXT NOT NULL, " +
                    PEOPLE_SEX_COLUMN + " TEXT NOT NULL, " +
                    PEOPLE_ID_COLUMN + " TEXT NOT NULL, " +
                    PEOPLE_NUMBER_COLUMN + " TEXT NOT NULL, " +
                    PEOPLE_ADDRESS_COLUMN + " TEXT NOT NULL)";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public RelatedPeopleProvider(Context context) {
        db = DatabasesHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定list的物件
    public String inserts(List<RelatedPeopleItem> items) {
        int length = items.size();
        String ids = "";

        if(length == 0) return ids;

        for(int i=0;i<length;i++) {
            long id = insert(items.get(i));
            if(i!=0) ids = ids+",";
            ids = ids+id;
        }
        return ids;
    }

    // 新增參數指定的物件
    public long insert(RelatedPeopleItem item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(UUID_COLUMN, item.getUuid());
        cv.put(PEOPLE_RELATION_COLUMN, item.getPeopleRelation());
        cv.put(PEOPLE_NAME_COLUMN, item.getPeopleName());
        cv.put(PEOPLE_SEX_COLUMN, item.getPeopleSex());
        cv.put(PEOPLE_ID_COLUMN, item.getPeopleId());
        cv.put(PEOPLE_NUMBER_COLUMN, item.getPeopleNumber());
        cv.put(PEOPLE_ADDRESS_COLUMN, item.getPeopleAddress());

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        return id;
    }

    // 修改參數指定list的物件
    public String updates(String ids, List<RelatedPeopleItem> items) {
        int length = items.size();
        String newIds = "";

        if(length == 0) return newIds;

        boolean result = false;
        for(int i=0;i<items.size();i++){
            long id = items.get(i).getId();
            result = update(id, items.get(i));
            if(i!=0) newIds=newIds+",";
            newIds=newIds+String.valueOf(id);
        }

        if(result) return newIds;
        return "";
    }

    // 修改參數指定的物件
    public boolean update(long id, RelatedPeopleItem item) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(UUID_COLUMN, item.getUuid());
        cv.put(PEOPLE_RELATION_COLUMN, item.getPeopleRelation());
        cv.put(PEOPLE_NAME_COLUMN, item.getPeopleName());
        cv.put(PEOPLE_SEX_COLUMN, item.getPeopleSex());
        cv.put(PEOPLE_ID_COLUMN, item.getPeopleId());
        cv.put(PEOPLE_NUMBER_COLUMN, item.getPeopleNumber());
        cv.put(PEOPLE_ADDRESS_COLUMN, item.getPeopleAddress());

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "=" + id;

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    // 刪除參數指定list的資料
    public boolean deletes(String ids) {
        boolean result = false;

        if(ids.length() == 0) return result;

        String[] id = ids.split(",");
        for(String s:id){
            long tag = Long.parseLong(s.trim());
            result = delete(tag);
        }

        return result;
    }

    // 刪除參數指定編號的資料
    public boolean delete(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where , null) > 0;
    }

    //搜尋指定編號的資料
    public List<RelatedPeopleItem> querys(String ids){
        List<RelatedPeopleItem> items = new ArrayList<RelatedPeopleItem>();
        String[] id = ids.split(",");

        if(ids.length()==0) return items;

        for(String s:id){
            RelatedPeopleItem item = query(Integer.parseInt(s.trim()));
            items.add(item);
        }
        return items;
    }

    public RelatedPeopleItem query(long id){
        String where = KEY_ID + "=" + id;
        RelatedPeopleItem item = new RelatedPeopleItem();
        Cursor cursor = db.query(TABLE_NAME, null, where, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item.setId(id);
            item.setUuid(cursor.getString(1));
            item.setPeopleRelation(cursor.getString(2));
            item.setPeopleName(cursor.getString(3));
            item.setPeopleSex(cursor.getString(4));
            item.setPeopleId(cursor.getString(5));
            item.setPeopleNumber(cursor.getString(6));
            item.setPeopleAddress(cursor.getString(7));
        }
        cursor.close();

        return item;
    }
}
