package com.android.csiapp.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.csiapp.Crime.utils.DictionaryInfo;

/**
 * Created by user on 2016/9/30.
 */
public class AnalysisProvider {
    // 表格名稱
    public static final String TABLE_NAME = "analysis";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String PEOPLE_NUMBER_COLUMN = "people_number";
    public static final String CRIME_MEANS_COLUMN = "crime_means";
    public static final String CRIME_CHARACTER_COLUMN = "crime_character";
    public static final String CRIME_ENTRANCE_COLUMN = "crime_entrance";
    public static final String CRIME_TIMING_COLUMN = "crime_timing";
    public static final String SELECT_OBJECT_COLUMN = "select_object";
    public static final String CRIME_EXPORT_COLUMN = "crime_export";
    public static final String PEOPLE_FEATURE_COLUMN = "people_feature";
    public static final String CRIME_FEATURE_COLUMN = "crime_feature";
    public static final String INTRUSIVE_METHOD_COLUMN = "intrusive_method";
    public static final String SELECT_LOCATION_COLUMN = "select_location";
    public static final String CRIME_PURPOSE_COLUMN = "crime_purpose";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PEOPLE_NUMBER_COLUMN + " TEXT NOT NULL, " +
                    CRIME_MEANS_COLUMN + " TEXT NOT NULL, " +
                    CRIME_CHARACTER_COLUMN + " TEXT NOT NULL, " +
                    CRIME_ENTRANCE_COLUMN + " TEXT NOT NULL, " +
                    CRIME_TIMING_COLUMN + " TEXT NOT NULL, " +
                    SELECT_OBJECT_COLUMN + " TEXT NOT NULL, " +
                    CRIME_EXPORT_COLUMN + " TEXT NOT NULL, " +
                    PEOPLE_FEATURE_COLUMN + " TEXT NOT NULL, " +
                    CRIME_FEATURE_COLUMN + " TEXT NOT NULL, " +
                    INTRUSIVE_METHOD_COLUMN + " TEXT NOT NULL, " +
                    SELECT_LOCATION_COLUMN + " TEXT NOT NULL, " +
                    CRIME_PURPOSE_COLUMN + " TEXT NOT NULL)";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public AnalysisProvider(Context context) {
        db = DatabasesHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public long insert(CrimeItem item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(PEOPLE_NUMBER_COLUMN, item.getCrimePeopleNumber());
        cv.put(CRIME_MEANS_COLUMN, item.getCrimeMeans());
        cv.put(CRIME_CHARACTER_COLUMN, item.getCrimeCharacter());
        cv.put(CRIME_ENTRANCE_COLUMN, item.getCrimeEntrance());
        cv.put(CRIME_TIMING_COLUMN, item.getCrimeTiming());
        cv.put(SELECT_OBJECT_COLUMN,  item.getSelectObject());
        cv.put(CRIME_EXPORT_COLUMN, item.getCrimeExport());
        cv.put(PEOPLE_FEATURE_COLUMN, item.getCrimePeopleFeature());
        cv.put(CRIME_FEATURE_COLUMN, item.getCrimeFeature());
        cv.put(INTRUSIVE_METHOD_COLUMN, item.getIntrusiveMethod());
        cv.put(SELECT_LOCATION_COLUMN, item.getSelectLocation());
        cv.put(CRIME_PURPOSE_COLUMN, item.getCrimePurpose());

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        return id;
    }

    // 修改參數指定的物件
    public boolean update(long id, CrimeItem item) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(PEOPLE_NUMBER_COLUMN, item.getCrimePeopleNumber());
        cv.put(CRIME_MEANS_COLUMN, item.getCrimeMeans());
        cv.put(CRIME_CHARACTER_COLUMN, item.getCrimeCharacter());
        cv.put(CRIME_ENTRANCE_COLUMN, item.getCrimeEntrance());
        cv.put(CRIME_TIMING_COLUMN, item.getCrimeTiming());
        cv.put(SELECT_OBJECT_COLUMN,  item.getSelectObject());
        cv.put(CRIME_EXPORT_COLUMN, item.getCrimeExport());
        cv.put(PEOPLE_FEATURE_COLUMN, item.getCrimePeopleFeature());
        cv.put(CRIME_FEATURE_COLUMN, item.getCrimeFeature());
        cv.put(INTRUSIVE_METHOD_COLUMN, item.getIntrusiveMethod());
        cv.put(SELECT_LOCATION_COLUMN, item.getSelectLocation());
        cv.put(CRIME_PURPOSE_COLUMN, item.getCrimePurpose());

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "=" + id;

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    // 刪除參數指定編號的資料
    public boolean delete(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where , null) > 0;
    }
}
