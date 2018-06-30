package com.android.csiapp.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by AnitaLin on 2016/9/9.
 */
public class DatabasesHelper extends SQLiteOpenHelper {

    // 資料庫名稱
    public static final String DATABASE_NAME = "csi_databases.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 3;
    // 資料庫物件，固定的欄位變數
    private static SQLiteDatabase database;

    private String TAG = "DatabasesHelper";

    public DatabasesHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new DatabasesHelper(context, DATABASE_NAME,
                    null, VERSION).getWritableDatabase();
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建立應用程式需要的表格
        //Login
        db.execSQL(IdentifyProvider.IDENTIFY_TABLE);
        //Whole key
        db.execSQL(CrimeProvider.CREATE_TABLE);
        //Page 1
        db.execSQL(CellProvider.CREATE_TABLE);
        db.execSQL(SceneProvider.CREATE_TABLE);
        //Page 2
        db.execSQL(RelatedPeopleProvider.CREATE_TABLE);
        db.execSQL(LostProvider.CREATE_TABLE);
        db.execSQL(CrimeToolProvider.CREATE_TABLE);
        //Page 3
        db.execSQL(PositionProvider.CREATE_TABLE);
        db.execSQL(FlatProvider.CREATE_TABLE);
        //Page 4
        db.execSQL(PositionPhotoProvider.CREATE_TABLE);
        db.execSQL(OverviewPhotoProvider.CREATE_TABLE);
        db.execSQL(ImportantPhotoProvider.CREATE_TABLE);
        //Page 5
        db.execSQL(EvidenceProvider.CREATE_TABLE);
        db.execSQL(MonitoringPhotoProvider.CREATE_TABLE);
        db.execSQL(CameraPhotoProvider.CREATE_TABLE);
        //Page 6
        db.execSQL(OverviewProvider.CREATE_TABLE);
        //Page 7
        db.execSQL(AnalysisProvider.CREATE_TABLE);
        //Page 8
        db.execSQL(WitnessProvider.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /*switch (oldVersion){
            case 1:
                if(newVersion<=1){
                    return;
                }
                try {
                    upgradeDatabaseToVersion2(db);
                    db.setTransactionSuccessful();
                } catch (Throwable ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    break;
                } finally {
                    db.endTransaction();
                }
            return;
        }*/
        Log.e(TAG, "Destroying all old data.");
        // 刪除原有的表格
        dropAll(db);
        // 呼叫onCreate建立新版的表格
        onCreate(db);
    }

    private void upgradeDatabaseToVersion2(SQLiteDatabase db){
    }

    private void dropAll(SQLiteDatabase db){
        //Login
        db.execSQL("DROP TABLE IF EXISTS " + IdentifyProvider.TABLE_NAME);
        //Whole key
        db.execSQL("DROP TABLE IF EXISTS " + CrimeProvider.TABLE_NAME);
        //Page 1
        db.execSQL("DROP TABLE IF EXISTS " + CellProvider.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SceneProvider.TABLE_NAME);
        //Page 2
        db.execSQL("DROP TABLE IF EXISTS " + RelatedPeopleProvider.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LostProvider.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CrimeToolProvider.TABLE_NAME);
        //Page 3
        db.execSQL("DROP TABLE IF EXISTS " + PositionProvider.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FlatProvider.TABLE_NAME);
        //Page 4
        db.execSQL("DROP TABLE IF EXISTS " + PositionPhotoProvider.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OverviewPhotoProvider.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ImportantPhotoProvider.TABLE_NAME);
        //Page 5
        db.execSQL("DROP TABLE IF EXISTS " + EvidenceProvider.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MonitoringPhotoProvider.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CameraPhotoProvider.TABLE_NAME);
        //Page 6
        db.execSQL("DROP TABLE IF EXISTS " + OverviewProvider.TABLE_NAME);
        //Page 7
        db.execSQL("DROP TABLE IF EXISTS " + AnalysisProvider.TABLE_NAME);
        //Page 8
        db.execSQL("DROP TABLE IF EXISTS " + WitnessProvider.TABLE_NAME);
    }
}
