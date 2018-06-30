package com.android.csiapp.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/11/29.
 */
public class MonitoringPhotoProvider {
    // 表格名稱
    public static final String TABLE_NAME = "monitoring_photo";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String UUID_COLUMN = "uuid";
    public static final String PHOTO_PATH_COLUMN = "photo_path";
    public static final String PHOTO_INFO_COLUMN = "photo_info";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    UUID_COLUMN + " TEXT NOT NULL, " +
                    PHOTO_PATH_COLUMN + " TEXT NOT NULL, " +
                    PHOTO_INFO_COLUMN + " TEXY NOT NULL)";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public MonitoringPhotoProvider(Context context) {
        db = DatabasesHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定list的物件
    public String inserts(List<PhotoItem> items) {
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
    public long insert(PhotoItem item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(UUID_COLUMN, item.getUuid());
        cv.put(PHOTO_PATH_COLUMN, item.getPhotoPath());
        cv.put(PHOTO_INFO_COLUMN, item.getPhotoInfo());

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        return id;
    }

    // 修改參數指定list的物件
    public String updates(String ids, List<PhotoItem> items) {
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
    public boolean update(long id, PhotoItem item) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(UUID_COLUMN, item.getUuid());
        cv.put(PHOTO_PATH_COLUMN, item.getPhotoPath());
        cv.put(PHOTO_INFO_COLUMN, item.getPhotoInfo());

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
    public List<PhotoItem> querys(String ids){
        List<PhotoItem> items = new ArrayList<PhotoItem>();
        String[] id = ids.split(",");

        if(ids.length()==0) return items;

        for(String s:id){
            PhotoItem item = query(Integer.parseInt(s.trim()));
            items.add(item);
        }
        return items;
    }

    public PhotoItem query(long id){
        String where = KEY_ID + "=" + id;
        PhotoItem item = new PhotoItem();
        Cursor cursor = db.query(TABLE_NAME, null, where, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item.setId(id);
            item.setUuid(cursor.getString(1));
            item.setPhotoPath(cursor.getString(2));
            item.setPhotoInfo(cursor.getString(3));
        }
        cursor.close();

        return item;
    }
}
