package com.android.csiapp.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by user on 2016/10/12.
 */
public class EvidenceProvider {
    // 表格名稱
    public static final String TABLE_NAME = "evidence";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String UUID_COLUMN = "uuid";
    public static final String PHOTO_PATH_COLUMN = "photo_path";
    public static final String EVIDENCE_CATEGORY_COLUMN = "evidence_category";
    public static final String EVIDENCE_COLUMN = "evidence";
    public static final String EVIDENCE_NAME_COLUMN = "evidence_name";
    public static final String LEGACY_SITE_COLUMN = "legacy_site";
    public static final String BASIC_FEATURE_COLUMN = "basice_feature";
    public static final String INFER_COLUMN = "infer";
    public static final String METHOD_COLUMN = "method";
    public static final String TIME_COLUMN = "time";
    public static final String PEOPLE_COLUMN = "people";
    public static final String PHOTO_INFO_COLUM = "photo_info";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    UUID_COLUMN + " TEXT NOT NULL, " +
                    PHOTO_PATH_COLUMN + " TEXT NOT NULL, " +
                    EVIDENCE_CATEGORY_COLUMN + " TEXT NOT NULL, " +
                    EVIDENCE_COLUMN + " TEXT NOT NULL, " +
                    EVIDENCE_NAME_COLUMN + " TEXT NOT NULL, " +
                    LEGACY_SITE_COLUMN + " TEXT NOT NULL, " +
                    BASIC_FEATURE_COLUMN + " TEXT NOT NULL, " +
                    INFER_COLUMN + " TEXT NOT NULL, " +
                    METHOD_COLUMN + " TEXT NOT NULL, " +
                    TIME_COLUMN + " TEXT NOT NULL, " +
                    PEOPLE_COLUMN + " TEXT NOT NULL, " +
                    PHOTO_INFO_COLUM + " TEXT NOT NULL)";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public EvidenceProvider(Context context) {
        db = DatabasesHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定list的物件
    public String inserts(List<EvidenceItem> items) {
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
    public long insert(EvidenceItem item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(UUID_COLUMN, item.getUuid());
        cv.put(PHOTO_PATH_COLUMN, item.getPhotoPath());
        cv.put(EVIDENCE_CATEGORY_COLUMN, item.getEvidenceCategory());
        cv.put(EVIDENCE_COLUMN, item.getEvidence());
        cv.put(EVIDENCE_NAME_COLUMN, item.getEvidenceName());
        cv.put(LEGACY_SITE_COLUMN, item.getLegacySite());
        cv.put(BASIC_FEATURE_COLUMN, item.getBasiceFeature());
        cv.put(INFER_COLUMN, item.getInfer());
        cv.put(METHOD_COLUMN, item.getMethod());
        cv.put(TIME_COLUMN, item.getTime());
        cv.put(PEOPLE_COLUMN, item.getPeople());
        cv.put(PHOTO_INFO_COLUM, item.getPhotoInfo());

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        return id;
    }

    // 修改參數指定list的物件
    public String updates(String ids, List<EvidenceItem> items) {
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
    public boolean update(long id, EvidenceItem item) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(UUID_COLUMN, item.getUuid());
        cv.put(PHOTO_PATH_COLUMN, item.getPhotoPath());
        cv.put(EVIDENCE_CATEGORY_COLUMN, item.getEvidenceCategory());
        cv.put(EVIDENCE_COLUMN, item.getEvidence());
        cv.put(EVIDENCE_NAME_COLUMN, item.getEvidenceName());
        cv.put(LEGACY_SITE_COLUMN, item.getLegacySite());
        cv.put(BASIC_FEATURE_COLUMN, item.getBasiceFeature());
        cv.put(INFER_COLUMN, item.getInfer());
        cv.put(METHOD_COLUMN, item.getMethod());
        cv.put(TIME_COLUMN, item.getTime());
        cv.put(PEOPLE_COLUMN, item.getPeople());
        cv.put(PHOTO_INFO_COLUM, item.getPhotoInfo());

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
    public List<EvidenceItem> querys(String ids){
        List<EvidenceItem> items = new ArrayList<EvidenceItem>();
        String[] id = ids.split(",");

        if(ids.length()==0) return items;

        for(String s:id){
            EvidenceItem item = query(Integer.parseInt(s.trim()));
            items.add(item);
        }
        return items;
    }

    public EvidenceItem query(long id){
        String where = KEY_ID + "=" + id;
        EvidenceItem item = new EvidenceItem();
        Cursor cursor = db.query(TABLE_NAME, null, where, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item.setId(id);
            item.setUuid(cursor.getString(1));
            item.setPhotoPath(cursor.getString(2));
            item.setEvidenceCategory(cursor.getString(3));
            item.setEvidence(cursor.getString(4));
            item.setEvidenceName(cursor.getString(5));
            item.setLegacySite(cursor.getString(6));
            item.setBasiceFeature(cursor.getString(7));
            item.setInfer(cursor.getString(8));
            item.setMethod(cursor.getString(9));
            item.setTime(cursor.getLong(10));
            item.setPeople(cursor.getString(11));
            item.setPhotoInfo(cursor.getString(12));
        }
        cursor.close();

        return item;
    }
}
