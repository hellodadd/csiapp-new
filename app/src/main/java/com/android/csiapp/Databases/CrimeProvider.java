package com.android.csiapp.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.csiapp.Crime.utils.DateTimePicker;
import com.android.csiapp.Crime.utils.DictionaryInfo;
import com.android.csiapp.Crime.utils.UserInfo;
import com.android.csiapp.XmlHandler.Dictionary;
import com.android.csiapp.XmlHandler.XmlHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by AnitaLin on 2016/9/9.
 */
public class CrimeProvider {
    // 表格名稱
    public static final String TABLE_NAME = "crime";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    public static final String SCENE_ID_COLUMN = "scene_id";
    public static final String SCENE_NO_COLUMN = "scene_no";
    public static final String CASE_ID_COLUMN = "case_id";
    public static final String CREATE_USER_COLUMN = "create_user";
    public static final String CREATE_UNIT_COLUMN = "create_unit";
    public static final String CREATE_TIME_COLUMN = "create_time";
    public static final String COMPLETE_COLUMN = "is_complete";
    public static final String DELETE_COLUMN = "is_delete";
    public static final String GPS_LAT_COLUMN = "gps_lat";
    public static final String GPS_LON_COLUMN = "gps_lon";

    public static final String CELL_ITEM_NUMBER_COLUMN = "cell_item_number";
    public static final String SCENE_ITEM_NUMBER_COLUMN = "scene_item_number";
    public static final String ANALYSIS_ITEM_NUMBER_COLUMN = "analysis_item_number";
    public static final String OVERVIEW_COLUMN = "overview_number";
    public static final String RELATEDPEOPLE_ITEM_NUMBER_COLUMN = "relatedpeople_item_number";
    public static final String LOST_ITEM_NUMBER_COLUMN = "lost_item_number";
    public static final String CRIMETOOL_ITEM_NUMBER_COLUMN = "crimetool_item_number";
    public static final String POSITION_ITEM_NUMBER_COLUMN = "position_item_number";
    public static final String FLAT_ITEM_COLUMN = "flat_item_number";
    public static final String POSITIONPHOTO_ITEM_NUMBER_COLUMN = "positionphoto_item_number";
    public static final String OVERVIEWPHOTO_ITEM_NUMBER_COLUMN = "overviewphoto_item_number";
    public static final String IMPORTANTPHOTO_ITEM_NUMBER_COLUMN = "importantphoto_item_number";
    public static final String EVIDENCE_ITEM_NUMBER_COLUMN = "evidence_item_number";
    public static final String MONITORINGPHOTO_ITEM_NUMBER_COLUMN = "monitoringphoto_item_number";
    public static final String CAMERAPHOTO_ITEM_COLUMN = "cameraphoto_item_number";
    public static final String WITNESS_ITEM_NUMBER_COLUMN = "witness_item_number";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SCENE_ID_COLUMN + " TEXT NOT NULL, " +
                    SCENE_NO_COLUMN + " TEXT NOT NULL, " +
                    CASE_ID_COLUMN + " TEXT NOT NULL, " +
                    CREATE_USER_COLUMN + " TEXT NOT NULL, " +
                    CREATE_UNIT_COLUMN + " TEXT NOT NULL, " +
                    CREATE_TIME_COLUMN + " TEXT NOT NULL, " +
                    COMPLETE_COLUMN + " TEXT NOT NULL, " +
                    DELETE_COLUMN + " TEXT NOT NULL, " +
                    GPS_LAT_COLUMN + " TEXT NOT NULL, " +
                    GPS_LON_COLUMN + " TEXT NOT NULL, " +
                    CELL_ITEM_NUMBER_COLUMN + " TEXT NOT NULL, " +
                    SCENE_ITEM_NUMBER_COLUMN + " TEXT NOT NULL, " +
                    ANALYSIS_ITEM_NUMBER_COLUMN + " TEXT NOT NULL, " +
                    OVERVIEW_COLUMN + " TEXT NOT NULL, " +
                    RELATEDPEOPLE_ITEM_NUMBER_COLUMN + " TEXT NOT NULL, " +
                    LOST_ITEM_NUMBER_COLUMN + " TEXT NOT NULL, " +
                    CRIMETOOL_ITEM_NUMBER_COLUMN + " TEXT NOT NULL, " +
                    POSITION_ITEM_NUMBER_COLUMN + " TEXT NOT NULL, " +
                    FLAT_ITEM_COLUMN + " TEXT NOT NULL, " +
                    POSITIONPHOTO_ITEM_NUMBER_COLUMN + " TEXT NOT NULL, " +
                    OVERVIEWPHOTO_ITEM_NUMBER_COLUMN + " TEXT NOT NULL, " +
                    IMPORTANTPHOTO_ITEM_NUMBER_COLUMN + " TEXT NOT NULL, " +
                    EVIDENCE_ITEM_NUMBER_COLUMN + " TEXT NOT NULL, " +
                    MONITORINGPHOTO_ITEM_NUMBER_COLUMN + " TEXT NOT NULL, " +
                    CAMERAPHOTO_ITEM_COLUMN + " TEXT NOT NULL, " +
                    WITNESS_ITEM_NUMBER_COLUMN + " TEXT NOT NULL)";

    // 資料庫物件
    private SQLiteDatabase db;
    private CellProvider mCellProvider;
    private SceneProvider mSceneProvider;
    private AnalysisProvider mAnalysisProvider;
    private OverviewProvider mOverviewProvider;
    private RelatedPeopleProvider mRelatedPeopleProvider;
    private LostProvider mLostProvider;
    private CrimeToolProvider mCrimeToolProvider;
    private PositionProvider mPositionProvider;
    private PositionPhotoProvider mPositionPhotoProvider;
    private OverviewPhotoProvider mOverviewPhotoProvider;
    private ImportantPhotoProvider mImportantPhotoProvider;
    private EvidenceProvider mEvidenceProvider;
    private MonitoringPhotoProvider mMonitoringPhotoProvider;
    private CameraPhotoProvider mCameraPhotoProvider;
    private FlatProvider mFlatProvider;
    private WitnessProvider mWitnessProvider;

    private Context mContext;

    // 建構子，一般的應用都不需要修改
    public CrimeProvider(Context context) {
        mContext = context;
        db = DatabasesHelper.getDatabase(context);
        mCellProvider = new CellProvider(context);
        mSceneProvider = new SceneProvider(context);
        mAnalysisProvider = new AnalysisProvider(context);
        mOverviewProvider = new OverviewProvider(context);
        mRelatedPeopleProvider = new RelatedPeopleProvider(context);
        mLostProvider = new LostProvider(context);
        mCrimeToolProvider = new CrimeToolProvider(context);
        mPositionProvider = new PositionProvider(context);
        mPositionPhotoProvider = new PositionPhotoProvider(context);
        mOverviewPhotoProvider = new OverviewPhotoProvider(context);
        mImportantPhotoProvider = new ImportantPhotoProvider(context);
        mEvidenceProvider = new EvidenceProvider(context);
        mMonitoringPhotoProvider = new MonitoringPhotoProvider(context);
        mCameraPhotoProvider = new CameraPhotoProvider(context);
        mFlatProvider = new FlatProvider(context);
        mWitnessProvider = new WitnessProvider(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public CrimeItem insert(CrimeItem item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();


        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        String uuid=CrimeProvider.getUUID();
        cv.put(SCENE_ID_COLUMN, uuid);
        //liwei 2017.3.1 scene_id===================
        item.setSceneId(uuid);
        cv.put(SCENE_NO_COLUMN, "");

        String case_id = item.getCaseId();
        cv.put(CASE_ID_COLUMN, CrimeProvider.getUUID());

        String login_name = item.getLoginName();
        cv.put(CREATE_USER_COLUMN, login_name);

        String unit_code = item.getUnitCode();
        cv.put(CREATE_UNIT_COLUMN, unit_code);

        cv.put(CREATE_TIME_COLUMN, Calendar.getInstance().getTimeInMillis());

        cv.put(COMPLETE_COLUMN, item.getComplete());

        cv.put(DELETE_COLUMN, item.getDelete());

        cv.put(GPS_LAT_COLUMN, item.getGpsLat());

        cv.put(GPS_LON_COLUMN, item.getGpsLon());

        String cell_id = mCellProvider.inserts(item.getCellResultItem());
        cv.put(CELL_ITEM_NUMBER_COLUMN, cell_id);

        long scene_id = mSceneProvider.insert(item);
        cv.put(SCENE_ITEM_NUMBER_COLUMN, scene_id);
        long analysis_id = mAnalysisProvider.insert(item);
        cv.put(ANALYSIS_ITEM_NUMBER_COLUMN, analysis_id);
        long overview_id = mOverviewProvider.insert(item);
        cv.put(OVERVIEW_COLUMN, overview_id);

        String related_id = mRelatedPeopleProvider.inserts(item.getReleatedPeople());
        cv.put(RELATEDPEOPLE_ITEM_NUMBER_COLUMN, related_id);

        String lost_id = mLostProvider.inserts(item.getLostItem());
        cv.put(LOST_ITEM_NUMBER_COLUMN, lost_id);

        String crimetool_id = mCrimeToolProvider.inserts(item.getCrimeTool());
        cv.put(CRIMETOOL_ITEM_NUMBER_COLUMN, crimetool_id);

        String position_id = mPositionProvider.inserts(item.getPosition());
        cv.put(POSITION_ITEM_NUMBER_COLUMN, position_id);

        String flat_id = mFlatProvider.inserts(item.getFlat());
        cv.put(FLAT_ITEM_COLUMN, flat_id);

        String positionPhoto_id = mPositionPhotoProvider.inserts(item.getPositionPhoto());
        cv.put(POSITIONPHOTO_ITEM_NUMBER_COLUMN, positionPhoto_id);

        String overviewPhoto_id = mOverviewPhotoProvider.inserts(item.getOverviewPhoto());
        cv.put(OVERVIEWPHOTO_ITEM_NUMBER_COLUMN, overviewPhoto_id);

        String importantPhoto_id = mImportantPhotoProvider.inserts(item.getImportantPhoto());
        cv.put(IMPORTANTPHOTO_ITEM_NUMBER_COLUMN, importantPhoto_id);

        String evidence_id = mEvidenceProvider.inserts(item.getEvidenceItem());
        cv.put(EVIDENCE_ITEM_NUMBER_COLUMN, evidence_id);

        String monitoring_id = mMonitoringPhotoProvider.inserts(item.getMonitoringPhoto());
        cv.put(MONITORINGPHOTO_ITEM_NUMBER_COLUMN, monitoring_id);

        String camera_id = mCameraPhotoProvider.inserts(item.getCameraPhoto());
        cv.put(CAMERAPHOTO_ITEM_COLUMN, camera_id);

        String witness_id = mWitnessProvider.inserts(item.getWitness());
        cv.put(WITNESS_ITEM_NUMBER_COLUMN, witness_id);

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        // 設定編號
        item.setId(id);
        // 回傳結果
        return item;
    }

    // 修改參數指定的物件
    public boolean update(CrimeItem item) {
        //liwei 2017.3.1，如果item没有sceneID，增加一个，为山东用户修改，完成后去掉
        if(item.getSceneId().equals("")){
            item.setSceneId(CrimeProvider.getUUID());
        }
        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        boolean result = false;
        String where = KEY_ID + "=" + item.getId();

        Cursor cursor = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        String Cell_id="", RelatedPeople_id="", Lost_id="", CrimeTool_id="", Position_id="", Flat_id="", PositionPhoto_id="", OverviewPhoto_id="", ImportantPhoto_id="", Evidence_id="", Monitoring_id="", Camera_id="", Witness_id="";

        if(cursor.moveToFirst()) {
            // 執行修改資料並回傳修改的資料數量是否成功
            Cell_id = mCellProvider.updates(cursor.getString(11), item.getCellResultItem());
            result = mSceneProvider.update(cursor.getLong(12),item);
            result = mAnalysisProvider.update(cursor.getLong(13),item);
            result = mOverviewProvider.update(cursor.getLong(14),item);
            RelatedPeople_id = mRelatedPeopleProvider.updates(cursor.getString(15),item.getReleatedPeople());
            Lost_id = mLostProvider.updates(cursor.getString(16),item.getLostItem());
            CrimeTool_id = mCrimeToolProvider.updates(cursor.getString(17),item.getCrimeTool());
            Position_id = mPositionProvider.updates(cursor.getString(18),item.getPosition());
            Flat_id = mFlatProvider.updates(cursor.getString(19),item.getFlat());
            PositionPhoto_id = mPositionPhotoProvider.updates(cursor.getString(20),item.getPositionPhoto());
            OverviewPhoto_id = mOverviewPhotoProvider.updates(cursor.getString(21),item.getOverviewPhoto());
            ImportantPhoto_id = mImportantPhotoProvider.updates(cursor.getString(22),item.getImportantPhoto());
            Evidence_id = mEvidenceProvider.updates(cursor.getString(23),item.getEvidenceItem());
            Monitoring_id = mMonitoringPhotoProvider.updates(cursor.getString(24),item.getMonitoringPhoto());
            Camera_id = mCameraPhotoProvider.updates(cursor.getString(25),item.getCameraPhoto());
            Witness_id = mWitnessProvider.updates(cursor.getString(26),item.getWitness());
            //return db.update(TABLE_NAME, cv, where, null) > 0;
        }
        cursor.close();

        ContentValues cv = new ContentValues();
        cv.put(SCENE_ID_COLUMN, item.getSceneId());
        cv.put(SCENE_NO_COLUMN, item.getSceneNo());
        cv.put(CASE_ID_COLUMN, item.getCaseId());
        cv.put(CREATE_USER_COLUMN, item.getLoginName());
        cv.put(CREATE_UNIT_COLUMN, item.getUnitCode());
        cv.put(CREATE_TIME_COLUMN, item.getCreateTime());
        cv.put(COMPLETE_COLUMN, item.getComplete());
        cv.put(DELETE_COLUMN, item.getDelete());
        cv.put(GPS_LAT_COLUMN, item.getGpsLat());
        cv.put(GPS_LON_COLUMN, item.getGpsLon());
        cv.put(CELL_ITEM_NUMBER_COLUMN, Cell_id);
        cv.put(SCENE_ITEM_NUMBER_COLUMN, item.getId());
        cv.put(ANALYSIS_ITEM_NUMBER_COLUMN, item.getId());
        cv.put(OVERVIEW_COLUMN, item.getId());
        cv.put(RELATEDPEOPLE_ITEM_NUMBER_COLUMN, RelatedPeople_id);
        cv.put(LOST_ITEM_NUMBER_COLUMN, Lost_id);
        cv.put(CRIMETOOL_ITEM_NUMBER_COLUMN, CrimeTool_id);
        cv.put(POSITION_ITEM_NUMBER_COLUMN, Position_id);
        cv.put(FLAT_ITEM_COLUMN, Flat_id);
        cv.put(POSITIONPHOTO_ITEM_NUMBER_COLUMN, PositionPhoto_id);
        cv.put(OVERVIEWPHOTO_ITEM_NUMBER_COLUMN, OverviewPhoto_id);
        cv.put(IMPORTANTPHOTO_ITEM_NUMBER_COLUMN, ImportantPhoto_id);
        cv.put(EVIDENCE_ITEM_NUMBER_COLUMN, Evidence_id);
        cv.put(MONITORINGPHOTO_ITEM_NUMBER_COLUMN, Monitoring_id);
        cv.put(CAMERAPHOTO_ITEM_COLUMN, Camera_id);
        cv.put(WITNESS_ITEM_NUMBER_COLUMN, Witness_id);

        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    // 刪除參數指定編號的資料
    public boolean delete(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        Cursor cursor = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            // 刪除指定編號資料並回傳刪除是否成功
            boolean result = false;
            result = mCellProvider.deletes(cursor.getString(11));
            result = mSceneProvider.delete(cursor.getLong(12));
            result = mAnalysisProvider.delete(cursor.getLong(13));
            result = mOverviewProvider.delete(cursor.getLong(14));
            result = mRelatedPeopleProvider.deletes(cursor.getString(15));
            result = mLostProvider.deletes(cursor.getString(16));
            result = mCrimeToolProvider.deletes(cursor.getString(17));
            result = mPositionProvider.deletes(cursor.getString(18));
            result = mFlatProvider.deletes(cursor.getString(19));
            result = mPositionPhotoProvider.deletes(cursor.getString(20));
            result = mOverviewPhotoProvider.deletes(cursor.getString(21));
            result = mImportantPhotoProvider.deletes(cursor.getString(22));
            result = mEvidenceProvider.deletes(cursor.getString(23));
            result = mMonitoringPhotoProvider.deletes(cursor.getString(24));
            result = mCameraPhotoProvider.deletes(cursor.getString(25));
            result = mWitnessProvider.deletes(cursor.getString(26));
            //return db.delete(TABLE_NAME, where, null) > 0;
            cursor.close();
            result = db.delete(TABLE_NAME, where , null) > 0;
            return result;
        }
        return false;
    }

    // 讀取所有記事資料
    public List<CrimeItem> getAll() {
        List<CrimeItem> result = new ArrayList<>();
        String where = DELETE_COLUMN + "= '0'";
        Cursor cursor = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor.getInt(0), null));
        }

        cursor.close();
        return result;
    }

    // 讀取指定人指定类型的数据
    public List<CrimeItem> getAllCompleteItem(String name) {
        List<CrimeItem> result = new ArrayList<>();
        String where = DELETE_COLUMN + "= '0' and "+COMPLETE_COLUMN +"='1'";
        Cursor cursor = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor.getInt(0), name));
        }
        cursor.close();
        return result;
    }

    public List<CrimeItem> getAll(String name) {
        if(name!=null) return getAll();

        List<CrimeItem> result = new ArrayList<>();
        String where = DELETE_COLUMN + "= '0'";
        Cursor cursor = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor.getInt(0), name));
        }

        cursor.close();
        return result;
    }

    public List<Integer> getAllWithCompleteOverDue() {
        long time = Calendar.getInstance().getTimeInMillis()-1000*60*60*24*90;//超過三個月的時間
        List<Integer> result = new ArrayList<Integer>();
        String where = CrimeProvider.COMPLETE_COLUMN + "= '2' AND " + CrimeProvider.CREATE_TIME_COLUMN + "<= " +time;
        Cursor cursor = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(cursor.getInt(0));
        }

        cursor.close();
        return result;
    }

    public List<Integer> getAllWithDeleteOverDue() {
        long time = Calendar.getInstance().getTimeInMillis()-1000*60*60*24*90;//超過三個月的時間
        List<Integer> result = new ArrayList<Integer>();
        String where = CrimeProvider.DELETE_COLUMN + "= '1' AND " + CrimeProvider.CREATE_TIME_COLUMN + "<= " +time;
        Cursor cursor = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(cursor.getInt(0));
        }

        cursor.close();
        return result;
    }

    // 刪除所有記事資料
    public void deleteAll(){
        // 刪除原有的表格
        //Login
        db.execSQL("DROP TABLE IF EXISTS " + IdentifyProvider.TABLE_NAME);
        //Dictionary
        db.execSQL("DROP TABLE IF EXISTS " + DictionaryProvider.TABLE_NAME);
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


        // 建立新版的表格
        //Login
        db.execSQL(IdentifyProvider.IDENTIFY_TABLE);
        //Dictionary
        db.execSQL(DictionaryProvider.IDENTIFY_TABLE);
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

    // 取得指定編號的資料物件
    public CrimeItem get(long id) {
        // 準備回傳結果用的物件
        CrimeItem item = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getRecord(id, null);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }

    // 把Cursor目前的資料包裝為物件
    private CrimeItem getRecord(long id, String name) {
        // 準備回傳結果用的物件
        CrimeItem result = new CrimeItem();
        result.setId(id);

        String where = KEY_ID + "=" + id;
        if(name!=null) where = where + " AND " + CREATE_USER_COLUMN + " = '" + name + "'";
        Cursor cursor = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        if(cursor.moveToFirst()) {
            result.setSceneId(cursor.getString(1));
            result.setSceneNo(cursor.getString(2));
            result.setCaseId(cursor.getString(3));
            result.setLoginName(cursor.getString(4));
            result.setUnitCode(cursor.getString(5));
            result.setCreateTime(cursor.getLong(6));
            result.setComplete(cursor.getString(7));
            result.setDelete(cursor.getString(8));
            result.setGpsLat(cursor.getString(9));
            result.setGpsLon(cursor.getString(10));

            List<PhotoItem> CellResult_items = mCellProvider.querys(cursor.getString(11));
            result.setCellResultItem(CellResult_items);

            String where1 = KEY_ID + "=" + cursor.getLong(12);
            Cursor cursor1 = db.query(
                    SceneProvider.TABLE_NAME, null, where1, null, null, null, null, null);
            if (cursor1.moveToFirst()) {
                // 讀取包裝一筆資料的物件
                result.setCasetype(cursor1.getString(1));
                result.setArea(cursor1.getString(2));
                result.setLocationa(cursor1.getString(3));
                result.setOccurredStartTime(cursor1.getLong(4));
                result.setOccurredEndTime(cursor1.getLong(5));
                result.setGetAccessTime(cursor1.getLong(6));
                result.setUnitsAssigned(cursor1.getString(7));
                result.setAccessPolicemen(cursor1.getString(8));
                result.setAccessStartTime(cursor1.getLong(9));
                result.setAccessEndTime(cursor1.getLong(10));
                result.setAccessLocation(cursor1.getString(11));
                result.setCaseOccurProcess(cursor1.getString(12));
                result.setSceneCondition(cursor1.getString(13));
                result.setChangeOption(cursor1.getString(14));
                result.setChangeReason(cursor1.getString(15));
                result.setWeatherCondition(cursor1.getString(16));
                result.setWindDirection(cursor1.getString(17));
                result.setTemperature(cursor1.getString(18));
                result.setHumidity(cursor1.getString(19));
                result.setAccessReason(cursor1.getString(20));
                result.setIlluminationCondition(cursor1.getString(21));
                result.setProductPeopleName(cursor1.getString(22));
                result.setProductPeopleUnit(cursor1.getString(23));
                result.setProductPeopleDuties(cursor1.getString(24));
                result.setSafeguard(cursor1.getString(25));
                result.setSceneConductor(cursor1.getString(26));
                result.setAccessInspectors(cursor1.getString(27));
            }
            cursor1.close();

            String where2 = KEY_ID + "=" + cursor.getLong(13);
            Cursor cursor2 = db.query(
                    AnalysisProvider.TABLE_NAME, null, where2, null, null, null, null, null);
            if (cursor2.moveToFirst()) {
                // 讀取包裝一筆資料的物件
                result.setCrimePeopleNumber(cursor2.getString(1));
                result.setCrimeMeans(cursor2.getString(2));
                result.setCrimeCharacter(cursor2.getString(3));
                result.setCrimeEntrance(cursor2.getString(4));
                result.setCrimeTiming(cursor2.getString(5));
                result.setSelectObject(cursor2.getString(6));
                result.setCrimeExport(cursor2.getString(7));
                result.setCrimePeopleFeature(cursor2.getString(8));
                result.setCrimeFeature(cursor2.getString(9));
                result.setIntrusiveMethod(cursor2.getString(10));
                result.setSelectLocation(cursor2.getString(11));
                result.setCrimePurpose(cursor2.getString(12));
            }
            cursor2.close();

            String where3 = KEY_ID + "=" + cursor.getLong(14);
            Cursor cursor3 = db.query(
                    OverviewProvider.TABLE_NAME, null, where3, null, null, null, null, null);
            if (cursor3.moveToFirst()) {
                // 讀取包裝一筆資料的物件
                result.setOverview(cursor3.getString(1));
            }
            cursor3.close();

            List<RelatedPeopleItem> RelatedPeople_items = mRelatedPeopleProvider.querys(cursor.getString(15));
            result.setReleatedPeople(RelatedPeople_items);

            List<LostItem> Lost_items = mLostProvider.querys(cursor.getString(16));
            result.setLostItem(Lost_items);

            List<CrimeToolItem> CrimeTool_items = mCrimeToolProvider.querys(cursor.getString(17));
            result.setCrimeTool(CrimeTool_items);

            List<PhotoItem> Position_items = mPositionProvider.querys(cursor.getString(18));
            result.setPosition(Position_items);

            List<PhotoItem> Flat_items = mFlatProvider.querys(cursor.getString(19));
            result.setFlat(Flat_items);

            List<PhotoItem> PositionPhoto_items = mPositionPhotoProvider.querys(cursor.getString(20));
            result.setPositionPhoto(PositionPhoto_items);

            List<PhotoItem> OverviewPhoto_items = mOverviewPhotoProvider.querys(cursor.getString(21));
            result.setOverviewPhoto(OverviewPhoto_items);

            List<PhotoItem> ImportantPhoto_items = mImportantPhotoProvider.querys(cursor.getString(22));
            result.setImportantPhoto(ImportantPhoto_items);

            List<EvidenceItem> Evidence_items = mEvidenceProvider.querys(cursor.getString(23));
            result.setEvidenceItem(Evidence_items);

            List<PhotoItem> Monitoring_items = mMonitoringPhotoProvider.querys(cursor.getString(24));
            result.setMonitoringPhoto(Monitoring_items);

            List<PhotoItem> Camera_items = mCameraPhotoProvider.querys(cursor.getString(25));
            result.setCameraPhoto(Camera_items);

            List<WitnessItem> Witness_items = mWitnessProvider.querys(cursor.getString(26));
            result.setWitness(Witness_items);
        }

        // 回傳結果
        return result;
    }

    // 把Cursor目前的資料包裝為物件
    public CrimeItem getRecordBySceneId(String id) {
        // 準備回傳結果用的物件
        CrimeItem result = new CrimeItem();

        String where = SCENE_ID_COLUMN + " = '" + id +"' AND " + DELETE_COLUMN + " = '0'";
        Cursor cursor = db.query(TABLE_NAME, null, where, null, null, null, null, null);
        if(cursor==null) {
            Log.d("Anita", "cursor is null");
            return null;
        }
        if(cursor.moveToNext() || cursor.getCount()!=0) {
            result.setId(cursor.getLong(0));
            result.setSceneId(cursor.getString(1));
            result.setSceneNo(cursor.getString(2));
            result.setCaseId(cursor.getString(3));
            result.setLoginName(cursor.getString(4));
            result.setUnitCode(cursor.getString(5));
            result.setCreateTime(cursor.getLong(6));
            result.setComplete(cursor.getString(7));
            result.setDelete(cursor.getString(8));
            result.setGpsLat(cursor.getString(9));
            result.setGpsLon(cursor.getString(10));

            List<PhotoItem> CellResult_items = mCellProvider.querys(cursor.getString(11));
            result.setCellResultItem(CellResult_items);

            String where1 = KEY_ID + "=" + cursor.getLong(12);
            Cursor cursor1 = db.query(
                    SceneProvider.TABLE_NAME, null, where1, null, null, null, null, null);
            if (cursor1.moveToFirst()) {
                // 讀取包裝一筆資料的物件
                result.setCasetype(cursor1.getString(1));
                result.setArea(cursor1.getString(2));
                result.setLocationa(cursor1.getString(3));
                result.setOccurredStartTime(cursor1.getLong(4));
                result.setOccurredEndTime(cursor1.getLong(5));
                result.setGetAccessTime(cursor1.getLong(6));
                result.setUnitsAssigned(cursor1.getString(7));
                result.setAccessPolicemen(cursor1.getString(8));
                result.setAccessStartTime(cursor1.getLong(9));
                result.setAccessEndTime(cursor1.getLong(10));
                result.setAccessLocation(cursor1.getString(11));
                result.setCaseOccurProcess(cursor1.getString(12));
                result.setSceneCondition(cursor1.getString(13));
                result.setChangeOption(cursor1.getString(14));
                result.setChangeReason(cursor1.getString(15));
                result.setWeatherCondition(cursor1.getString(16));
                result.setWindDirection(cursor1.getString(17));
                result.setTemperature(cursor1.getString(18));
                result.setHumidity(cursor1.getString(19));
                result.setAccessReason(cursor1.getString(20));
                result.setIlluminationCondition(cursor1.getString(21));
                result.setProductPeopleName(cursor1.getString(22));
                result.setProductPeopleUnit(cursor1.getString(23));
                result.setProductPeopleDuties(cursor1.getString(24));
                result.setSafeguard(cursor1.getString(25));
                result.setSceneConductor(cursor1.getString(26));
                result.setAccessInspectors(cursor1.getString(27));
            }
            cursor1.close();

            String where2 = KEY_ID + "=" + cursor.getLong(13);
            Cursor cursor2 = db.query(
                    AnalysisProvider.TABLE_NAME, null, where2, null, null, null, null, null);
            if (cursor2.moveToFirst()) {
                // 讀取包裝一筆資料的物件
                result.setCrimePeopleNumber(cursor2.getString(1));
                result.setCrimeMeans(cursor2.getString(2));
                result.setCrimeCharacter(cursor2.getString(3));
                result.setCrimeEntrance(cursor2.getString(4));
                result.setCrimeTiming(cursor2.getString(5));
                result.setSelectObject(cursor2.getString(6));
                result.setCrimeExport(cursor2.getString(7));
                result.setCrimePeopleFeature(cursor2.getString(8));
                result.setCrimeFeature(cursor2.getString(9));
                result.setIntrusiveMethod(cursor2.getString(10));
                result.setSelectLocation(cursor2.getString(11));
                result.setCrimePurpose(cursor2.getString(12));
            }
            cursor2.close();

            String where3 = KEY_ID + "=" + cursor.getLong(14);
            Cursor cursor3 = db.query(
                    OverviewProvider.TABLE_NAME, null, where3, null, null, null, null, null);
            if (cursor3.moveToFirst()) {
                // 讀取包裝一筆資料的物件
                result.setOverview(cursor3.getString(1));
            }
            cursor3.close();

            List<RelatedPeopleItem> RelatedPeople_items = mRelatedPeopleProvider.querys(cursor.getString(15));
            result.setReleatedPeople(RelatedPeople_items);

            List<LostItem> Lost_items = mLostProvider.querys(cursor.getString(16));
            result.setLostItem(Lost_items);

            List<CrimeToolItem> CrimeTool_items = mCrimeToolProvider.querys(cursor.getString(17));
            result.setCrimeTool(CrimeTool_items);

            List<PhotoItem> Position_items = mPositionProvider.querys(cursor.getString(18));
            result.setPosition(Position_items);

            List<PhotoItem> Flat_items = mFlatProvider.querys(cursor.getString(19));
            result.setFlat(Flat_items);

            List<PhotoItem> PositionPhoto_items = mPositionPhotoProvider.querys(cursor.getString(20));
            result.setPositionPhoto(PositionPhoto_items);

            List<PhotoItem> OverviewPhoto_items = mOverviewPhotoProvider.querys(cursor.getString(21));
            result.setOverviewPhoto(OverviewPhoto_items);

            List<PhotoItem> ImportantPhoto_items = mImportantPhotoProvider.querys(cursor.getString(22));
            result.setImportantPhoto(ImportantPhoto_items);

            List<EvidenceItem> Evidence_items = mEvidenceProvider.querys(cursor.getString(23));
            result.setEvidenceItem(Evidence_items);

            List<PhotoItem> Monitoring_items = mMonitoringPhotoProvider.querys(cursor.getString(24));
            result.setMonitoringPhoto(Monitoring_items);

            List<PhotoItem> Camera_items = mCameraPhotoProvider.querys(cursor.getString(25));
            result.setCameraPhoto(Camera_items);

            List<WitnessItem> Witness_items = mWitnessProvider.querys(cursor.getString(26));
            result.setWitness(Witness_items);

            return result;
        }

        // 回傳結果
        return null;
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

    // 建立範例資料
    public void sample() {
        long time = Calendar.getInstance().getTimeInMillis();
        CrimeItem item = new CrimeItem(0, "故意伤害", "北京",time);
        CrimeItem item2 = new CrimeItem(0, "诈骗", "南京",time);

        insert(item);
        insert(item2);
    }

    public void createScenesInfoXml(String name){

        List<CrimeItem> mItem = new ArrayList<>();
        List<HashMap<String, String>> mBaseInfoList = new ArrayList<>();
        List<HashMap<String, String>> mPeopleInfoList = new ArrayList<>();
        List<HashMap<String, String>> mGoodsInfoList = new ArrayList<>();
        List<HashMap<String, String>> mTraceInfoList = new ArrayList<>();
        List<HashMap<String, String>> mAttachInfoList = new ArrayList<>();

        mItem = getAll(name);

        Log.d("Anita","createScenesInfoXml size = "+mItem.size());
        if(mItem.size()>0){
            for(int i=0;i<mItem.size();i++){
                CrimeItem item = mItem.get(i);

                //scene
                HashMap<String, String> mBaseInfo = new LinkedHashMap<String, String>();
                mBaseInfo.put("id", item.getSceneId());
                mBaseInfo.put("inquestNo", item.getSceneNo());
                mBaseInfo.put("complete", item.getComplete());
                mBaseInfo.put("casetype", item.getCasetype());
                mBaseInfo.put("regionalism", item.getArea());
                mBaseInfo.put("sceneaddress", item.getLocation());
                mBaseInfo.put("starttime", DateTimePicker.getCurrentDashTime(item.getOccurredStartTime()));
                mBaseInfo.put("endtime", DateTimePicker.getCurrentDashTime(item.getOccurredEndTime()));
                mBaseInfo.put("receivertime", DateTimePicker.getCurrentDashTime(item.getGetAccessTime()));
                mBaseInfo.put("appointunit", item.getUnitsAssigned());
                mBaseInfo.put("receivedperson", item.getAccessPolicemen());
                mBaseInfo.put("inqueststarttime", DateTimePicker.getCurrentDashTime(item.getAccessStartTime()));
                mBaseInfo.put("inquestendtime", DateTimePicker.getCurrentDashTime(item.getAccessEndTime()));
                mBaseInfo.put("inquestaddress", item.getAccessLocation());
                mBaseInfo.put("discover", item.getCaseOccurProcess());
                mBaseInfo.put("operator", item.getLoginName());

                mBaseInfoList.add(mBaseInfo);
            }
        }

        final Object[] obj = new Object[5];
        obj[0] = mBaseInfoList;
        obj[1] = mPeopleInfoList;
        obj[2] = mGoodsInfoList;
        obj[3] = mTraceInfoList;
        obj[4] = mAttachInfoList;

        XmlHandler xmlhandler = new XmlHandler();
        xmlhandler.createSceneInfoXmlFile(obj);
    }

    public CrimeItem createBaseMsgXml(String id){

        List<CrimeItem> mItem = new ArrayList<>();
        List<HashMap<String, String>> mBaseInfoList = new ArrayList<>();
        List<HashMap<String, String>> mPeopleInfoList = new ArrayList<>();
        List<HashMap<String, String>> mGoodsInfoList = new ArrayList<>();
        List<HashMap<String, String>> mTraceInfoList = new ArrayList<>();
        List<HashMap<String, String>> mAttachInfoList = new ArrayList<>();

        CrimeItem record = getRecordBySceneId(id);
        if(record==null) return null;

        mItem.add(record);

        Log.d("Anita","createBaseMsgXml size = "+mItem.size());

        if(mItem.size()>0){
            for(int i=0;i<mItem.size();i++){
                CrimeItem item = mItem.get(i);

                //BaseInfo
                HashMap<String, String> mBaseInfo = new LinkedHashMap<String, String>();
                mBaseInfo.put("id", item.getSceneId());
                mBaseInfo.put("inquestNo", item.getSceneNo());
                mBaseInfo.put("caseid", item.getCaseId());
                mBaseInfo.put("createuser", item.getLoginName());
                mBaseInfo.put("createunit", item.getUnitCode());
                mBaseInfo.put("createtime", DateTimePicker.getCurrentDashTime(item.getCreateTime()));
                mBaseInfo.put("gpslat", item.getGpsLat());
                mBaseInfo.put("gpslon", item.getGpsLon());
                mBaseInfo.put("complete", item.getComplete());
                mBaseInfo.put("casetype", item.getCasetype());
                mBaseInfo.put("regionalism", item.getArea());
                mBaseInfo.put("address", item.getLocation());
                mBaseInfo.put("starttime", DateTimePicker.getCurrentDashTime(item.getOccurredStartTime()));
                mBaseInfo.put("endtime", DateTimePicker.getCurrentDashTime(item.getOccurredEndTime()));
                mBaseInfo.put("receivertime", DateTimePicker.getCurrentDashTime(item.getGetAccessTime()));
                mBaseInfo.put("appointunit", item.getUnitsAssigned());
                mBaseInfo.put("receivedperson", item.getAccessPolicemen());
                mBaseInfo.put("inqueststarttime", DateTimePicker.getCurrentDashTime(item.getAccessStartTime()));
                mBaseInfo.put("inquestendtime", DateTimePicker.getCurrentDashTime(item.getAccessEndTime()));
                mBaseInfo.put("inquestaddress", item.getAccessLocation());
                mBaseInfo.put("discover", item.getCaseOccurProcess());
                mBaseInfo.put("scenesituation", item.getSceneCondition());
                mBaseInfo.put("changeoption",item.getChangeOption());
                mBaseInfo.put("changeresason", item.getChangeReason());
                mBaseInfo.put("weather", item.getWeatherCondition());
                mBaseInfo.put("wind", item.getWindDirection());
                mBaseInfo.put("temperature", item.getTemperature());
                mBaseInfo.put("humidity", item.getHumidity());
                mBaseInfo.put("investigatecause", item.getAccessReason());
                mBaseInfo.put("beamsituation", item.getIlluminationCondition());
                mBaseInfo.put("protectionname", item.getProductPeopleName());
                mBaseInfo.put("protectionorg", item.getProductPeopleUnit());
                mBaseInfo.put("protectionpost", item.getProductPeopleDuties());
                mBaseInfo.put("protectionmeasure", item.getSafeguard());
                mBaseInfo.put("director", item.getSceneConductor());
                mBaseInfo.put("investigators", item.getAccessInspectors());

                mBaseInfo.put("inqueststate", item.getOverview());

                mBaseInfo.put("crimersnum", item.getCrimePeopleNumber());
                mBaseInfo.put("crimersmethod", item.getCrimeMeans());
                mBaseInfo.put("crimersnature", item.getCrimeCharacter());
                mBaseInfo.put("crimersenter", item.getCrimeEntrance());
                mBaseInfo.put("crimerstime", item.getCrimeTiming());
                mBaseInfo.put("crimersobject", item.getSelectObject());
                mBaseInfo.put("crimersexit", item.getCrimeExport());
                mBaseInfo.put("crimerfeature", item.getCrimePeopleFeature());
                mBaseInfo.put("crimerskeypoint", item.getCrimeFeature());
                mBaseInfo.put("crimersentermethod", item.getIntrusiveMethod());
                mBaseInfo.put("crimershouse", item.getSelectLocation());
                mBaseInfo.put("crimersmotive", item.getCrimePurpose());

                mBaseInfoList.add(mBaseInfo);

                //PeopleInfo
                List<RelatedPeopleItem> mRelatedPeopleItem = item.getReleatedPeople();
                List<WitnessItem> mWitnessItem = item.getWitness();
                for(int iP =0; iP<mRelatedPeopleItem.size();iP++){
                    HashMap<String, String> mPeopleInfo = new LinkedHashMap<String, String>();
                    mPeopleInfo.put("id",mRelatedPeopleItem.get(iP).getUuid());
                    mPeopleInfo.put("caseid",item.getSceneId());
                    mPeopleInfo.put("type",mRelatedPeopleItem.get(iP).getPeopleRelation());
                    mPeopleInfo.put("name",mRelatedPeopleItem.get(iP).getPeopleName());
                    mPeopleInfo.put("sex",mRelatedPeopleItem.get(iP).getPeopleSex());
                    mPeopleInfo.put("idnum",mRelatedPeopleItem.get(iP).getPeopleId());
                    mPeopleInfo.put("birthday","");
                    mPeopleInfo.put("mobile",mRelatedPeopleItem.get(iP).getPeopleNumber());
                    mPeopleInfo.put("address",mRelatedPeopleItem.get(iP).getPeopleAddress());
                    mPeopleInfo.put("sign","");
                    mPeopleInfoList.add(mPeopleInfo);
                }
                for(int iW =0; iW<mWitnessItem.size();iW++){
                    HashMap<String, String> mPeopleInfo = new LinkedHashMap<String, String>();
                    mPeopleInfo.put("id",mWitnessItem.get(iW).getUuid());
                    mPeopleInfo.put("caseid",item.getSceneId());
                    mPeopleInfo.put("type","见证人");
                    mPeopleInfo.put("name",mWitnessItem.get(iW).getWitnessName());
                    mPeopleInfo.put("sex",mWitnessItem.get(iW).getWitnessSex());
                    mPeopleInfo.put("idnum","");
                    mPeopleInfo.put("birthday",DateTimePicker.getCurrentDashDate(mWitnessItem.get(iW).getWitnessBirthday()));
                    mPeopleInfo.put("mobile",mWitnessItem.get(iW).getWitnessNumber());
                    mPeopleInfo.put("address",mWitnessItem.get(iW).getWitnessAddress());
                    if(mWitnessItem.get(iW).getPhotoPath().isEmpty()){
                        mPeopleInfo.put("sign", "");
                    }else {
                        mPeopleInfo.put("sign", mWitnessItem.get(iW).getPhotoPath().substring(mWitnessItem.get(iW).getPhotoPath().lastIndexOf("/")));
                    }
                    mPeopleInfoList.add(mPeopleInfo);
                }

                //GoodsInfo
                List<LostItem> mLostItem = item.getLostItem();
                List<CrimeToolItem> mCrimeToolItem = item.getCrimeTool();
                for(int iG =0; iG<mLostItem.size();iG++){
                    HashMap<String, String> mGoodsInfo = new LinkedHashMap<String, String>();
                    mGoodsInfo.put("id",mLostItem.get(iG).getUuid());
                    mGoodsInfo.put("caseid",item.getSceneId());
                    mGoodsInfo.put("type","损失物品");
                    mGoodsInfo.put("name",mLostItem.get(iG).getItemName());
                    mGoodsInfo.put("factory",mLostItem.get(iG).getItemBrand());
                    mGoodsInfo.put("number",mLostItem.get(iG).getItemAmount());
                    mGoodsInfo.put("price",mLostItem.get(iG).getItemValue());
                    mGoodsInfo.put("describe",mLostItem.get(iG).getItemFeature());
                    mGoodsInfoList.add(mGoodsInfo);
                }
                for(int iC =0; iC<mCrimeToolItem.size();iC++){
                    HashMap<String, String> mGoodsInfo = new LinkedHashMap<String, String>();
                    mGoodsInfo.put("id",mCrimeToolItem.get(iC).getUuid());
                    mGoodsInfo.put("caseid",item.getSceneId());
                    mGoodsInfo.put("type","作案工具");
                    mGoodsInfo.put("name",mCrimeToolItem.get(iC).getToolName());
                    mGoodsInfo.put("sort",mCrimeToolItem.get(iC).getToolCategory());
                    mGoodsInfo.put("source",mCrimeToolItem.get(iC).getToolSource());
                    mGoodsInfoList.add(mGoodsInfo);
                }

                //TraceInfo
                List<EvidenceItem> mEvidenceItem = item.getEvidenceItem();
                for(int iE =0; iE<mEvidenceItem.size();iE++) {
                    HashMap<String, String> mTraceInfo = new LinkedHashMap<String, String>();
                    mTraceInfo.put("id",mEvidenceItem.get(iE).getUuid());
                    mTraceInfo.put("caseid",item.getSceneId());
                    if(mEvidenceItem.get(iE).getPhotoPath().isEmpty()){
                        mTraceInfo.put("filename","");
                    }else {
                        mTraceInfo.put("filename",mEvidenceItem.get(iE).getPhotoPath().substring(mEvidenceItem.get(iE).getPhotoPath().lastIndexOf("/")));
                    }
                    String desc = mEvidenceItem.get(iE).getPhotoInfo();
                    if (desc == null || desc.isEmpty()) {
                        mTraceInfo.put("description", "");
                    }else {
                        mTraceInfo.put("description", desc);
                    }
                    mTraceInfo.put("type",mEvidenceItem.get(iE).getEvidenceCategory());
                    mTraceInfo.put("subtype",mEvidenceItem.get(iE).getEvidence());
                    mTraceInfo.put("material",mEvidenceItem.get(iE).getEvidenceName());
                    mTraceInfo.put("retain_port",mEvidenceItem.get(iE).getLegacySite());
                    mTraceInfo.put("basic_feature",mEvidenceItem.get(iE).getBasiceFeature());
                    mTraceInfo.put("tool_infer",mEvidenceItem.get(iE).getInfer());
                    mTraceInfo.put("extract_method",mEvidenceItem.get(iE).getMethod());
                    mTraceInfo.put("extract_time",DateTimePicker.getCurrentDashTime(mEvidenceItem.get(iE).getTime()));
                    mTraceInfo.put("extract_person",mEvidenceItem.get(iE).getPeople());
                    mTraceInfoList.add(mTraceInfo);
                }

                //AttachInfo
                List<PhotoItem> mCellResult = item.getCellResultItem();
                List<PhotoItem> mPosition = item.getPosition();
                List<PhotoItem> mFlat = item.getFlat();
                List<PhotoItem> mPositionPhoto = item.getPositionPhoto();
                List<PhotoItem> mOverviewPhoto = item.getOverviewPhoto();
                List<PhotoItem> mImportantPhoto = item.getImportantPhoto();
                List<PhotoItem> mMonitoringPhoto = item.getMonitoringPhoto();
                List<PhotoItem> mCameraPhoto = item.getCameraPhoto();

                for(int iC=0;iC<mCellResult.size();iC++){
                    HashMap<String, String> mAttachInfo = new LinkedHashMap<String, String>();
                    mAttachInfo.put("type","基站信息");
                    mAttachInfo.put("id",mCellResult.get(iC).getUuid());
                    mAttachInfo.put("caseid",item.getSceneId());
                    if(mCellResult.get(iC).getPhotoPath().isEmpty()){
                        mAttachInfo.put("filename","");
                    }else {
                        mAttachInfo.put("filename",mCellResult.get(iC).getPhotoPath().substring(mCellResult.get(iC).getPhotoPath().lastIndexOf("/")));
                    }
                    mAttachInfoList.add(mAttachInfo);
                }
                for(int iP=0;iP<mPosition.size();iP++){
                    HashMap<String, String> mAttachInfo = new LinkedHashMap<String, String>();
                    mAttachInfo.put("type","方位示意图");
                    mAttachInfo.put("id",mPosition.get(iP).getUuid());
                    mAttachInfo.put("caseid",item.getSceneId());
                    if(mPosition.get(iP).getPhotoPath().isEmpty()){
                        mAttachInfo.put("filename", "");
                    }else {
                        mAttachInfo.put("filename",mPosition.get(iP).getPhotoPath().substring(mPosition.get(iP).getPhotoPath().lastIndexOf("/")));
                    }
                    String desc = mPosition.get(iP).getPhotoInfo();
                    if (desc == null || desc.isEmpty()) {
                        mAttachInfo.put("description", "");
                    }else {
                        mAttachInfo.put("description", desc);
                    }
                    mAttachInfoList.add(mAttachInfo);
                }
                for(int iF=0;iF<mFlat.size();iF++){
                    HashMap<String, String> mAttachInfo = new LinkedHashMap<String, String>();
                    mAttachInfo.put("type","平面示意图");
                    mAttachInfo.put("id",mFlat.get(iF).getUuid());
                    mAttachInfo.put("caseid",item.getSceneId());
                    if(mFlat.get(iF).getPhotoPath().isEmpty()){
                        mAttachInfo.put("filename", "");
                    }else {
                        mAttachInfo.put("filename",mFlat.get(iF).getPhotoPath().substring(mFlat.get(iF).getPhotoPath().lastIndexOf("/")));
                    }
                    String desc = mFlat.get(iF).getPhotoInfo();
                    if (desc == null || desc.isEmpty()) {
                        mAttachInfo.put("description", "");
                    }else {
                        mAttachInfo.put("description", desc);
                    }
                    mAttachInfoList.add(mAttachInfo);
                }
                for(int iPP=0;iPP<mPositionPhoto.size();iPP++){
                    HashMap<String, String> mAttachInfo = new LinkedHashMap<String, String>();
                    mAttachInfo.put("type","方位照片");
                    mAttachInfo.put("id",mPositionPhoto.get(iPP).getUuid());
                    mAttachInfo.put("caseid",item.getSceneId());
                    if(mPositionPhoto.get(iPP).getPhotoPath().isEmpty()){
                        mAttachInfo.put("filename","");
                    }else {
                        mAttachInfo.put("filename",mPositionPhoto.get(iPP).getPhotoPath().substring(mPositionPhoto.get(iPP).getPhotoPath().lastIndexOf("/")));
                    }
                    String desc = mPositionPhoto.get(iPP).getPhotoInfo();
                    if (desc == null || desc.isEmpty()) {
                        mAttachInfo.put("description", "");
                    }else {
                        mAttachInfo.put("description", desc);
                    }
                    mAttachInfoList.add(mAttachInfo);
                }
                for(int iO=0;iO<mOverviewPhoto.size();iO++){
                    HashMap<String, String> mAttachInfo = new LinkedHashMap<String, String>();
                    mAttachInfo.put("type","概貌照片");
                    mAttachInfo.put("id",mOverviewPhoto.get(iO).getUuid());
                    mAttachInfo.put("caseid",item.getSceneId());
                    if(mOverviewPhoto.get(iO).getPhotoPath().isEmpty()){
                        mAttachInfo.put("filename","");
                    }else {
                        mAttachInfo.put("filename",mOverviewPhoto.get(iO).getPhotoPath().substring(mOverviewPhoto.get(iO).getPhotoPath().lastIndexOf("/")));
                    }
                    String desc = mOverviewPhoto.get(iO).getPhotoInfo();
                    if (desc == null || desc.isEmpty()) {
                        mAttachInfo.put("description", "");
                    }else {
                        mAttachInfo.put("description", desc);
                    }
                    mAttachInfoList.add(mAttachInfo);
                }
                for(int iI=0;iI<mImportantPhoto.size();iI++){
                    HashMap<String, String> mAttachInfo = new LinkedHashMap<String, String>();
                    mAttachInfo.put("type","重点部位");
                    mAttachInfo.put("id",mImportantPhoto.get(iI).getUuid());
                    mAttachInfo.put("caseid",item.getSceneId());
                    if(mImportantPhoto.get(iI).getPhotoPath().isEmpty()){
                        mAttachInfo.put("filename","");
                    }else {
                        mAttachInfo.put("filename",mImportantPhoto.get(iI).getPhotoPath().substring(mImportantPhoto.get(iI).getPhotoPath().lastIndexOf("/")));
                    }
                    String desc = mImportantPhoto.get(iI).getPhotoInfo();
                    if (desc == null || desc.isEmpty()) {
                        mAttachInfo.put("description", "");
                    }else {
                        mAttachInfo.put("description", desc);
                    }
                    mAttachInfoList.add(mAttachInfo);
                }
                for(int iM=0;iM<mMonitoringPhoto.size();iM++){
                    HashMap<String, String> mAttachInfo = new LinkedHashMap<String, String>();
                    mAttachInfo.put("type","监控画面");
                    mAttachInfo.put("id",mMonitoringPhoto.get(iM).getUuid());
                    mAttachInfo.put("caseid",item.getSceneId());
                    if(mMonitoringPhoto.get(iM).getPhotoPath().isEmpty()){
                        mAttachInfo.put("filename","");
                    }else {
                        mAttachInfo.put("filename",mMonitoringPhoto.get(iM).getPhotoPath().substring(mMonitoringPhoto.get(iM).getPhotoPath().lastIndexOf("/")));
                    }
                    String desc = mMonitoringPhoto.get(iM).getPhotoInfo();
                    if (desc == null || desc.isEmpty()) {
                        mAttachInfo.put("description", "");
                    }else {
                        mAttachInfo.put("description", desc);
                    }
                    mAttachInfoList.add(mAttachInfo);
                }
                for(int iI=0;iI<mCameraPhoto.size();iI++){
                    HashMap<String, String> mAttachInfo = new LinkedHashMap<String, String>();
                    mAttachInfo.put("type","摄像头位置");
                    mAttachInfo.put("id",mCameraPhoto.get(iI).getUuid());
                    mAttachInfo.put("caseid",item.getSceneId());
                    if(mCameraPhoto.get(iI).getPhotoPath().isEmpty()){
                        mAttachInfo.put("filename","");
                    }else {
                        mAttachInfo.put("filename",mCameraPhoto.get(iI).getPhotoPath().substring(mCameraPhoto.get(iI).getPhotoPath().lastIndexOf("/")));
                    }
                    String desc = mCameraPhoto.get(iI).getPhotoInfo();
                    if (desc == null || desc.isEmpty()) {
                        mAttachInfo.put("description", "");
                    }else {
                        mAttachInfo.put("description", desc);
                    }
                    mAttachInfoList.add(mAttachInfo);
                }
            }
        }

        final Object[] obj = new Object[5];
        obj[0] = mBaseInfoList;
        obj[1] = mPeopleInfoList;
        obj[2] = mGoodsInfoList;
        obj[3] = mTraceInfoList;
        obj[4] = mAttachInfoList;

        XmlHandler xmlhandler = new XmlHandler();
        xmlhandler.createSceneInfoXmlFile(obj);

        return record;
    }

    public static String getUUID(){
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        String replace = s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
        return replace;
    }

    public static String AddWholeUUID(String uuid){
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        String replace = s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
        return replace;
    }
}
