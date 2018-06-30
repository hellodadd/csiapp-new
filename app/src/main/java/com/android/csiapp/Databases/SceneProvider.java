package com.android.csiapp.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.csiapp.Crime.utils.DictionaryInfo;
import com.android.csiapp.XmlHandler.Dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/9/30.
 */
public class SceneProvider {
    // 表格名稱
    public static final String TABLE_NAME = "scene";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String CASETYPE_COLUMN = "casetype";
    public static final String AREA_COLUMN = "area";
    public static final String LOCATION_COLUMN = "location";
    public static final String OCCURRED_START_TIME_COLUMN = "occurred_start_time";
    public static final String OCCURRED_END_TIME_COLUMN = "occurred_end_time";
    public static final String GET_ACCESS_TIME_COLUMN = "get_access_time";
    public static final String UNIT_COLUMN = "unit";
    public static final String POLICEMAN_COLUMN = "policeman";
    public static final String ACCESS_START_TIME_COLUMN = "access_start_time";
    public static final String ACCESS_END_TIME_COLUMN = "access_end_time";
    public static final String ACCESS_LOCATION_COLUMN = "access_location";
    public static final String CASE_OCCUR_PROCESS_COLUMN = "case_occur_process";
    public static final String SCENE_CONDITION_COLUMN = "scene_condition";
    public static final String CHANGE_OPTION_COLUMN = "change_option";
    public static final String CHANGE_REASON_COLUMN = "change_reason";
    public static final String WEATHER_COLUMN = "weather";
    public static final String WIND_COLUMN = "wind";
    public static final String TEMPERATURE_COLUMN = "temperature";
    public static final String HUMIDITY_COLUMN = "humidity";
    public static final String ACCESS_REASON_COLUMN = "access_reason";
    public static final String ILLUMINATION_CONDITION_COLUMN = "illumination_condition";
    public static final String PRODUCT_PEOPLE_NAME_COLUMN = "product_people_name";
    public static final String PRODUCT_PEOPLE_UNIT_COLUMN = "product_people_unit";
    public static final String PRODUCT_PEOPLE_DUTIES_COLUMN = "product_people_duties";
    public static final String SAFEGUARD_COLUMN = "safeguard";
    public static final String SCENE_CONDUCTOR_COLUMN = "scene_conductor";
    public static final String ACCESS_INSPECTOR_COLUMN = "access_inspector";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CASETYPE_COLUMN + " TEXT NOT NULL, " +
                    AREA_COLUMN + " TEXT NOT NULL, " +
                    LOCATION_COLUMN + " TEXT NOT NULL, " +
                    OCCURRED_START_TIME_COLUMN + " TEXT NOT NULL, " +
                    OCCURRED_END_TIME_COLUMN + " TEXT NOT NULL, " +
                    GET_ACCESS_TIME_COLUMN + " TEXT NOT NULL, " +
                    UNIT_COLUMN + " TEXT NOT NULL, " +
                    POLICEMAN_COLUMN + " TEXT NOT NULL, " +
                    ACCESS_START_TIME_COLUMN + " TEXT NOT NULL, " +
                    ACCESS_END_TIME_COLUMN + " TEXT NOT NULL, " +
                    ACCESS_LOCATION_COLUMN + " TEXT NOT NULL, " +
                    CASE_OCCUR_PROCESS_COLUMN + " TEXT NOT NULL, " +
                    SCENE_CONDITION_COLUMN + " TEXT NOT NULL, " +
                    CHANGE_OPTION_COLUMN + " TEXT NOT NULL, " +
                    CHANGE_REASON_COLUMN + " TEXT NOT NULL, " +
                    WEATHER_COLUMN + " TEXT NOT NULL, " +
                    WIND_COLUMN + " TEXT NOT NULL, " +
                    TEMPERATURE_COLUMN + " TEXT NOT NULL, " +
                    HUMIDITY_COLUMN + " TEXT NOT NULL, " +
                    ACCESS_REASON_COLUMN + " TEXT NOT NULL, " +
                    ILLUMINATION_CONDITION_COLUMN + " TEXT NOT NULL, " +
                    PRODUCT_PEOPLE_NAME_COLUMN + " TEXT NOT NULL, " +
                    PRODUCT_PEOPLE_UNIT_COLUMN + " TEXT NOT NULL, " +
                    PRODUCT_PEOPLE_DUTIES_COLUMN + " TEXT NOT NULL, " +
                    SAFEGUARD_COLUMN + " TEXT NOT NULL, " +
                    SCENE_CONDUCTOR_COLUMN + " TEXT NOT NULL, " +
                    ACCESS_INSPECTOR_COLUMN + " TEXT NOT NULL)";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public SceneProvider(Context context) {
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
        cv.put(CASETYPE_COLUMN, item.getCasetype());
        cv.put(AREA_COLUMN, item.getArea());
        cv.put(LOCATION_COLUMN, item.getLocation());
        cv.put(OCCURRED_START_TIME_COLUMN, item.getOccurredStartTime());
        cv.put(OCCURRED_END_TIME_COLUMN, item.getOccurredEndTime());
        cv.put(GET_ACCESS_TIME_COLUMN, item.getGetAccessTime());
        cv.put(UNIT_COLUMN, item.getUnitsAssigned());
        cv.put(POLICEMAN_COLUMN, item.getAccessPolicemen());
        cv.put(ACCESS_START_TIME_COLUMN, item.getAccessStartTime());
        cv.put(ACCESS_END_TIME_COLUMN, item.getAccessEndTime());
        cv.put(ACCESS_LOCATION_COLUMN, item.getAccessLocation());
        cv.put(CASE_OCCUR_PROCESS_COLUMN, item.getCaseOccurProcess());
        cv.put(SCENE_CONDITION_COLUMN, item.getSceneCondition());
        cv.put(CHANGE_OPTION_COLUMN, item.getChangeOption());
        cv.put(CHANGE_REASON_COLUMN, item.getChangeReason());
        cv.put(WEATHER_COLUMN, item.getWeatherCondition());
        cv.put(WIND_COLUMN, item.getWindDirection());
        cv.put(TEMPERATURE_COLUMN, item.getTemperature());
        cv.put(HUMIDITY_COLUMN, item.getHumidity());
        cv.put(ACCESS_REASON_COLUMN, item.getAccessReason());
        cv.put(ILLUMINATION_CONDITION_COLUMN, item.getIlluminationCondition());
        cv.put(PRODUCT_PEOPLE_NAME_COLUMN, item.getProductPeopleName());
        cv.put(PRODUCT_PEOPLE_UNIT_COLUMN, item.getProductPeopleUnit());
        cv.put(PRODUCT_PEOPLE_DUTIES_COLUMN, item.getProductPeopleDuties());
        cv.put(SAFEGUARD_COLUMN, item.getSafeguard());
        cv.put(SCENE_CONDUCTOR_COLUMN, item.getSceneConductor());
        cv.put(ACCESS_INSPECTOR_COLUMN, item.getAccessInspectors());
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
        cv.put(CASETYPE_COLUMN,item.getCasetype());
        cv.put(AREA_COLUMN, item.getArea());
        cv.put(LOCATION_COLUMN, item.getLocation());
        cv.put(OCCURRED_START_TIME_COLUMN, item.getOccurredStartTime());
        cv.put(OCCURRED_END_TIME_COLUMN, item.getOccurredEndTime());
        cv.put(GET_ACCESS_TIME_COLUMN, item.getGetAccessTime());
        cv.put(UNIT_COLUMN, item.getUnitsAssigned());
        cv.put(POLICEMAN_COLUMN, item.getAccessPolicemen());
        cv.put(ACCESS_START_TIME_COLUMN, item.getAccessStartTime());
        cv.put(ACCESS_END_TIME_COLUMN, item.getAccessEndTime());
        cv.put(ACCESS_LOCATION_COLUMN, item.getAccessLocation());
        cv.put(CASE_OCCUR_PROCESS_COLUMN, item.getCaseOccurProcess());
        cv.put(SCENE_CONDITION_COLUMN, item.getSceneCondition());
        cv.put(CHANGE_OPTION_COLUMN, item.getChangeOption());
        cv.put(CHANGE_REASON_COLUMN, item.getChangeReason());
        cv.put(WEATHER_COLUMN, item.getWeatherCondition());
        cv.put(WIND_COLUMN, item.getWindDirection());
        cv.put(TEMPERATURE_COLUMN, item.getTemperature());
        cv.put(HUMIDITY_COLUMN, item.getHumidity());
        cv.put(ACCESS_REASON_COLUMN, item.getAccessReason());
        cv.put(ILLUMINATION_CONDITION_COLUMN, item.getIlluminationCondition());
        cv.put(PRODUCT_PEOPLE_NAME_COLUMN, item.getProductPeopleName());
        cv.put(PRODUCT_PEOPLE_UNIT_COLUMN, item.getProductPeopleUnit());
        cv.put(PRODUCT_PEOPLE_DUTIES_COLUMN, item.getProductPeopleDuties());
        cv.put(SAFEGUARD_COLUMN, item.getSafeguard());
        cv.put(SCENE_CONDUCTOR_COLUMN, item.getSceneConductor());
        cv.put(ACCESS_INSPECTOR_COLUMN, item.getAccessInspectors());
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
