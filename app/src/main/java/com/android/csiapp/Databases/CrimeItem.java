package com.android.csiapp.Databases;

import com.android.csiapp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by AnitaLin on 2016/9/9.
 */
public class CrimeItem implements Serializable {
    // 編號、日期時間、顏色、標題、內容、檔案名稱、經緯度、修改、已選擇
    //Page 1
    private long id;
    private String mSceneId;
    private String mSceneNo;
    private String mCaseId;
    private String mLoginName;
    private String mUnitCode;
    private long mCreateTime;
    private String mComplete;
    private String mDelete;
    private Boolean mIsCollecting;
    private Boolean mIsCollected;
    private ArrayList<String> mCellResult;
    private String mGpsLat;
    private String mGpsLon;
    private Boolean mGetLastData;
    private String mCasetype;
    private String mArea;
    private String mLocation;
    private long mOccurred_start_time;
    private long mOccurred_end_time;
    private long mGet_access_time;
    private String mUnitsAssigned;
    private String mAccessPolicemen;
    private long mAccess_start_time;
    private long mAccess_end_time;
    private String mAccessLocation;
    private String mCaseOccurProcess;
    private String mSceneCondition;
    private String mChangeOption;
    private String mChangeReason;
    private String mWeatherCondition;
    private String mWindDirection;
    private String mTemperature;
    private String mHumidity;
    private String mAccessReason;
    private String mIlluminationCondition;
    private String mProductPeopleName;
    private String mProductPeopleUnit;
    private String mProductPeopleDuties;
    private String mSafeguard;
    private String mSceneConductor;
    private String mAccessInspectors;

    //Page 1 (Cell result)
    private List<PhotoItem> mCellResultItem;

    //Page 2 (New people)
    private List<RelatedPeopleItem> mReleatedPeopleItem;

    //Page 2 (New Item)
    private List<LostItem> mLostItem;

    //Page 2 (New Tool)
    private List<CrimeToolItem> mCrimeToolItem;

    //Page 3 (Position)
    private List<PhotoItem> mPositionItem;

    //Page 3 (Flat)
    private List<PhotoItem> mFlatItem;

    //Page 4 (PositionPhoto)
    private List<PhotoItem> mPositionPhotoItem;

    //Page 4 (Overview)
    private List<PhotoItem> mOverviewPhotoItem;

    //Page 4 (Important)
    private List<PhotoItem> mImportantPhotoItem;

    //Page 5 (Evidence)
    private List<EvidenceItem> mEvidenceItem;

    //Page 5 (Monitoring)
    private List<PhotoItem> mMonitoringPhotoItem;

    //Page 5 (Camera)
    private List<PhotoItem> mCameraPhotoItem;

    //Page 6 (Overview)
    private String mOverview;

    //Page 7
    private String mCrimePeopleNumber;
    private String mCrimeMeans;
    private String mCrimeCharacter;
    private String mCrimeEntrance;
    private String mCrimeTiming;
    private String mSelectObject;
    private String mCrimeExport;
    private String mCrimePeopleFeature;
    private String mCrimeFeature;
    private String mIntrusiveMethod;
    private String mSelectLocation;
    private String mCrimePurpose;

    //Page 8 (New Witness)
    private List<WitnessItem> mWitnessItem;

    public CrimeItem() {
        Calendar c = Calendar.getInstance();
        long time = c.getTimeInMillis();
        this.id = 0;
        this.mSceneId = "";
        this.mSceneNo = "";
        this.mCaseId = "";
        this.mLoginName = "";
        this.mUnitCode = "";
        this.mCreateTime = time;
        this.mComplete = "0";
        this.mDelete = "0";
        this.mIsCollecting = false;
        this.mIsCollected = false;
        this.mCellResult = new ArrayList<String>();
        this.mGpsLat = "";
        this.mGpsLon = "";
        this.mGetLastData = false;
        this.mCasetype = "";
        this.mArea = "";
        this.mLocation = "";
        this.mOccurred_start_time = time-1000*60*30*3;
        this.mOccurred_end_time = time-1000*60*30;
        this.mGet_access_time = time;
        this.mUnitsAssigned = "";
        this.mAccessPolicemen = "";
        this.mAccess_start_time = time+1000*60*30;
        this.mAccess_end_time = time+1000*60*30*3;
        this.mAccessLocation = "";
        this.mCaseOccurProcess = "";
        this.mSceneCondition = "";
        this.mChangeOption = "";
        this.mChangeReason = "";
        this.mWeatherCondition = "2";
        this.mWindDirection = "09";
        this.mTemperature = "20";
        this.mHumidity = "35";
        this.mAccessReason = "";
        this.mIlluminationCondition = "";
        this.mProductPeopleName = "";
        this.mProductPeopleUnit = "";
        this.mProductPeopleDuties = "";
        this.mSafeguard = "";
        this.mSceneConductor = "";
        this.mAccessInspectors = "";

        this.mCellResultItem = new ArrayList<PhotoItem>();

        this.mReleatedPeopleItem = new ArrayList<RelatedPeopleItem>();
        this.mLostItem = new ArrayList<LostItem>();
        this.mCrimeToolItem = new ArrayList<CrimeToolItem>();

        this.mPositionItem = new ArrayList<PhotoItem>();
        this.mFlatItem = new ArrayList<PhotoItem>();

        this.mPositionPhotoItem = new ArrayList<PhotoItem>();
        this.mOverviewPhotoItem = new ArrayList<PhotoItem>();
        this.mImportantPhotoItem = new ArrayList<PhotoItem>();

        this.mEvidenceItem = new ArrayList<EvidenceItem>();
        this.mMonitoringPhotoItem = new ArrayList<PhotoItem>();
        this.mCameraPhotoItem = new ArrayList<PhotoItem>();

        this.mOverview = "";

        this.mCrimePeopleNumber = "";
        this.mCrimeMeans = "";
        this.mCrimeCharacter = "";
        this.mCrimeEntrance = "";
        this.mCrimeTiming = "";
        this.mSelectObject = "";
        this.mCrimeExport = "";
        this.mCrimePeopleFeature = "";
        this.mCrimeFeature = "";
        this.mIntrusiveMethod = "";
        this.mSelectLocation = "";
        this.mCrimePurpose = "";

        this.mWitnessItem = new ArrayList<WitnessItem>();
    }

    public CrimeItem(long id, String casetype, String area, long time) {
        this.id = id;
        this.mCasetype = casetype;
        this.mArea = area;
        this.mOccurred_start_time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSceneId() {
        return mSceneId;
    }

    public void setSceneId(String sceneCaseId) {
        this.mSceneId = sceneCaseId;
    }

    public String getSceneNo() {
        return mSceneNo;
    }

    public void setSceneNo(String sceneNo) {
        this.mSceneNo = sceneNo;
    }

    public String getCaseId() {
        return mCaseId;
    }

    public void setCaseId(String caseId) {
        this.mCaseId = caseId;
    }

    public String getLoginName() {
        return mLoginName;
    }

    public void setLoginName(String loginName) {
        this.mLoginName = loginName;
    }

    public String getUnitCode() {
        return mUnitCode;
    }

    public void setUnitCode(String unitCode) {
        this.mUnitCode = unitCode;
    }

    public long getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(long createTime) {
        this.mCreateTime = createTime;
    }

    public String getComplete() {
        return mComplete;
    }

    public void setComplete(String complete) {
        this.mComplete = complete;
    }

    public String getDelete() {
        return mDelete;
    }

    public void setDelete(String delete) {
        this.mDelete = delete;
    }

    //Page 1
    public ArrayList<String> getCellResult() {
        return mCellResult;
    }

    public void setCellResult(ArrayList<String> cellResult) {
        this.mCellResult = cellResult;
    }

    public Boolean IsCollecting() {
        return mIsCollecting;
    }

    public void setCollecting(Boolean isCollecting) {
        this.mIsCollecting = isCollecting;
    }

    public Boolean IsCollected() {
        return mIsCollected;
    }

    public void setCollected(Boolean isCollected) {
        this.mIsCollected = isCollected;
    }

    public String getGpsLat() {
        return mGpsLat;
    }

    public void setGpsLat(String gpsLat) {
        this.mGpsLat = gpsLat;
    }

    public String getGpsLon() {
        return mGpsLon;
    }

    public void setGpsLon(String gpsLon) {
        this.mGpsLon = gpsLon;
    }

    public Boolean IsGetLastData() {
        return mGetLastData;
    }

    public void setGetLastData(Boolean getLastData) {
        this.mGetLastData = getLastData;
    }

    public String getCasetype() {
        return mCasetype;
    }

    public void setCasetype(String casetype) {
        this.mCasetype = casetype;
    }

    public String getArea() {
        return mArea;
    }

    public void setArea(String area) {
        this.mArea = area;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocationa(String location) {
        this.mLocation = location;
    }

    public long getOccurredStartTime() {
        return mOccurred_start_time;
    }

    public void setOccurredStartTime(long time) {
        this.mOccurred_start_time = time;
    }

    public long getOccurredEndTime() {
        return mOccurred_end_time;
    }

    public void setOccurredEndTime(long time) {
        this.mOccurred_end_time = time;
    }

    public long getGetAccessTime() {
        return mGet_access_time;
    }

    public void setGetAccessTime(long time) {
        this.mGet_access_time = time;
    }

    public String getUnitsAssigned () {
        return mUnitsAssigned;
    }

    public void setUnitsAssigned (String unitsAssigned ) {
        this.mUnitsAssigned = unitsAssigned ;
    }

    public String getAccessPolicemen () {
        return mAccessPolicemen;
    }

    public void setAccessPolicemen (String accessPolicemen ) {
        this.mAccessPolicemen = accessPolicemen ;
    }

    public long getAccessStartTime () {
        return mAccess_start_time;
    }

    public void setAccessStartTime (long time ) {
        this.mAccess_start_time = time ;
    }

    public long getAccessEndTime () {
        return mAccess_end_time;
    }

    public void setAccessEndTime (long time ) {
        this.mAccess_end_time = time ;
    }

    public String getAccessLocation () {
        return mAccessLocation;
    }

    public void setAccessLocation (String accessLocation ) {
        this.mAccessLocation = accessLocation ;
    }

    public String getCaseOccurProcess () {
        return mCaseOccurProcess;
    }

    public void setCaseOccurProcess(String caseOccurProcess ) {
        this.mCaseOccurProcess = caseOccurProcess ;
    }

    public String getSceneCondition () {
        return mSceneCondition;
    }

    public void setSceneCondition (String sceneCondition ) {
        this.mSceneCondition = sceneCondition ;
    }

    public String getChangeOption() {
        return mChangeOption;
    }

    public void setChangeOption (String changeOption ) {
        this.mChangeOption = changeOption ;
    }

    public String getChangeReason () {
        return mChangeReason;
    }

    public void setChangeReason (String changeReason ) {
        this.mChangeReason = changeReason ;
    }

    public String getWeatherCondition () {
        return mWeatherCondition;
    }

    public void setWeatherCondition (String weatherCondition ) {
        this.mWeatherCondition = weatherCondition ;
    }

    public String getWindDirection () {
        return mWindDirection;
    }

    public void setWindDirection (String windDirection ) {
        this.mWindDirection = windDirection ;
    }

    public String getTemperature() {
        return mTemperature;
    }

    public void setTemperature (String temperature ) {
        this.mTemperature = temperature ;
    }

    public String getHumidity () {
        return mHumidity;
    }

    public void setHumidity (String humidity ) {
        this.mHumidity = humidity ;
    }

    public String getAccessReason () {
        return mAccessReason;
    }

    public void setAccessReason (String accessReason ) {
        this.mAccessReason = accessReason ;
    }

    public String getIlluminationCondition () {
        return mIlluminationCondition;
    }

    public void setIlluminationCondition (String illuminationCondition ) {
        this.mIlluminationCondition = illuminationCondition ;
    }

    public String getProductPeopleName () {
        return mProductPeopleName;
    }

    public void setProductPeopleName (String productPeopleName ) {
        this.mProductPeopleName = productPeopleName ;
    }

    public String getProductPeopleUnit () {
        return mProductPeopleUnit;
    }

    public void setProductPeopleUnit (String productPeopleUnit ) {
        this.mProductPeopleUnit = productPeopleUnit ;
    }

    public String getProductPeopleDuties () {
        return mProductPeopleDuties;
    }

    public void setProductPeopleDuties (String productPeopleDuties ) {
        this.mProductPeopleDuties = productPeopleDuties ;
    }

    public String getSafeguard() {
        return mSafeguard;
    }

    public void setSafeguard (String safeguard ) {
        this.mSafeguard = safeguard ;
    }

    public String getSceneConductor () {
        return mSceneConductor;
    }

    public void setSceneConductor (String sceneConductor ) {
        this.mSceneConductor = sceneConductor ;
    }

    public String getAccessInspectors  () {
        return mAccessInspectors ;
    }

    public void setAccessInspectors  (String accessInspectors  ) {
        this.mAccessInspectors  = accessInspectors ;
    }

    //Page 1 (Cell Result)
    public List<PhotoItem> getCellResultItem() {return mCellResultItem; }

    public void setCellResultItem(List<PhotoItem> cellResultItem) {this.mCellResultItem = cellResultItem; }

    //Page 2 (New People)
    public List<RelatedPeopleItem> getReleatedPeople() {return mReleatedPeopleItem; }

    public void setReleatedPeople(List<RelatedPeopleItem> relatedPeopleItem) {this.mReleatedPeopleItem = relatedPeopleItem; }

    //Page 2 (New Item)
    public List<LostItem> getLostItem() {return mLostItem; }

    public void setLostItem(List<LostItem> lostItem) {this.mLostItem = lostItem; }

    //Page 2  (New Tool)
    public List<CrimeToolItem>  getCrimeTool() {return mCrimeToolItem; }

    public void setCrimeTool(List<CrimeToolItem>  crimeTool) {this.mCrimeToolItem = crimeTool; }

    //Page 3 (Position)
    public List<PhotoItem>  getPosition() {return mPositionItem; }

    public void setPosition(List<PhotoItem>  positionItem) {this.mPositionItem = positionItem; }

    //Page 3 (Flat)
    public List<PhotoItem>  getFlat() {return mFlatItem; }

    public void setFlat(List<PhotoItem>  flatItem) {this.mFlatItem = flatItem; }

    //Page 4 (PositionPhoto)
    public List<PhotoItem>  getPositionPhoto() {return mPositionPhotoItem; }

    public void setPositionPhoto(List<PhotoItem>  positionPhotoItem) {this.mPositionPhotoItem = positionPhotoItem; }

    //Page 4 (OverviewPhoto)
    public List<PhotoItem>  getOverviewPhoto() {return mOverviewPhotoItem; }

    public void setOverviewPhoto(List<PhotoItem>  overviewPhotoItem) {this.mOverviewPhotoItem = overviewPhotoItem; }

    //Page 4 (ImportantPhoto)
    public List<PhotoItem>  getImportantPhoto() {return mImportantPhotoItem; }

    public void setImportantPhoto(List<PhotoItem>  importantPhotoItem) {this.mImportantPhotoItem = importantPhotoItem; }

    //Page 5 (Evidence)
    public List<EvidenceItem>  getEvidenceItem() {return mEvidenceItem; }

    public void setEvidenceItem(List<EvidenceItem>  evidenceItem) {this.mEvidenceItem = evidenceItem; }

    //Page 5 (Evidence)
    public List<PhotoItem>  getMonitoringPhoto() {return mMonitoringPhotoItem; }

    public void setMonitoringPhoto(List<PhotoItem>  monitoringPhotoItem) {this.mMonitoringPhotoItem = monitoringPhotoItem; }

    //Page 5 (Evidence)
    public List<PhotoItem>  getCameraPhoto() {return mCameraPhotoItem; }

    public void setCameraPhoto(List<PhotoItem>  cameraPhotoItem) {this.mCameraPhotoItem = cameraPhotoItem; }

    //Page 6
    public String getOverview() {return mOverview; }

    public void setOverview(String overview) {this.mOverview = overview; }

    //Page 7
    public String getCrimePeopleNumber() {return mCrimePeopleNumber; }

    public void setCrimePeopleNumber(String crimePeopleNumber) {this.mCrimePeopleNumber = crimePeopleNumber; }

    public String getCrimeMeans() {return mCrimeMeans; }

    public void setCrimeMeans(String crimeMeans) {this.mCrimeMeans = crimeMeans; }

    public String getCrimeCharacter() {return mCrimeCharacter; }

    public void setCrimeCharacter(String crimeCharacter) {this.mCrimeCharacter = crimeCharacter; }

    public String getCrimeEntrance() {return mCrimeEntrance; }

    public void setCrimeEntrance(String crimeEntrance) {this.mCrimeEntrance = crimeEntrance; }

    public String getCrimeTiming() {return mCrimeTiming; }

    public void setCrimeTiming(String crimeTiming) {this.mCrimeTiming = crimeTiming; }

    public String getSelectObject() {return mSelectObject; }

    public void setSelectObject(String selectObject) {this.mSelectObject = selectObject; }

    public String getCrimeExport() {return mCrimeExport; }

    public void setCrimeExport(String crimeExport) {this.mCrimeExport = crimeExport; }

    public String getCrimePeopleFeature() {return mCrimePeopleFeature; }

    public void setCrimePeopleFeature(String peopleFeature) {this.mCrimePeopleFeature = peopleFeature; }

    public String getCrimeFeature() {return mCrimeFeature; }

    public void setCrimeFeature(String crimeFeature) {this.mCrimeFeature = crimeFeature; }

    public String getIntrusiveMethod() {return mIntrusiveMethod; }

    public void setIntrusiveMethod(String intrusiveMethod) {this.mIntrusiveMethod = intrusiveMethod; }

    public String getSelectLocation() {return mSelectLocation; }

    public void setSelectLocation(String selectLocation) {this.mSelectLocation = selectLocation; }

    public String getCrimePurpose() {return mCrimePurpose; }

    public void setCrimePurpose(String crimePurpose) {this.mCrimePurpose = crimePurpose; }

    //Page 8 (New Witness)
    public List<WitnessItem> getWitness() {return mWitnessItem; }

    public void setWitness(List<WitnessItem> witness) {this.mWitnessItem = witness; }

    //Check Information
    public boolean checkInformation(){
        boolean result = false;
        if(!mCasetype.isEmpty() &&!mArea.isEmpty() &&!mLocation.isEmpty() &&!mUnitsAssigned.isEmpty()
                &&!mAccessPolicemen.isEmpty() &&!mAccessLocation.isEmpty()
                &&!mCaseOccurProcess.isEmpty() &&!mSceneCondition.isEmpty()
                &&!mWeatherCondition.isEmpty() &&!mWindDirection.isEmpty()
                &&!mTemperature.isEmpty() &&!mHumidity.isEmpty()
                &&!mAccessReason.isEmpty() &&!mIlluminationCondition.isEmpty()
                &&!mProductPeopleName.isEmpty() &&!mProductPeopleUnit.isEmpty() &&!mProductPeopleDuties.isEmpty()
                &&!mSafeguard.isEmpty() &&!mSceneConductor.isEmpty() &&!mAccessInspectors.isEmpty()
                &&!mCrimePeopleNumber.isEmpty() &&!mCrimePeopleFeature.isEmpty() && !mOverview.isEmpty()
                && !mPositionItem.isEmpty() && !mWitnessItem.isEmpty()
                && !mPositionPhotoItem.isEmpty() && !mOverviewPhotoItem.isEmpty() && !mImportantPhotoItem.isEmpty())
                //&& !mCellResultItem.isEmpty())去掉基站信息检查，列为非必填项，liwei=========================
            result = true;
        return result;
    }

    public String needToCheckInformation(String message){
        if(mCasetype.isEmpty()) message = message + "案件类别\n";
        if(mArea.isEmpty()) message = message + "发案区划\n";
        if(mLocation.isEmpty()) message = message + "发案地点\n";
        if(mUnitsAssigned.isEmpty()) message = message + "指派单位\n";
        if(mAccessPolicemen.isEmpty()) message = message + "接警人\n";
        if(mAccessLocation.isEmpty()) message = message + "勘验地点\n";
        if(mCaseOccurProcess.isEmpty()) message = message + "案件发现过程\n";
        if(mSceneCondition.isEmpty()) message = message + "现场条件\n";
        if(mWeatherCondition.isEmpty()) message = message + "天气状况\n";
        if(mWindDirection.isEmpty()) message = message + "风向\n";
        if(mTemperature.isEmpty()) message = message + "温度\n";
        if(mHumidity.isEmpty()) message = message + "湿度\n";
        if(mAccessReason.isEmpty()) message = message + "勘验事由\n";
        if(mIlluminationCondition.isEmpty()) message = message + "光照条件\n";
        if(mProductPeopleName.isEmpty()) message = message + "保护人姓名\n";
        if(mProductPeopleUnit.isEmpty()) message = message + "保护人单位\n";
        if(mProductPeopleDuties.isEmpty()) message = message + "保护人职务\n";
        if(mSafeguard.isEmpty()) message = message + "保护措施\n";
        if(mSceneConductor.isEmpty()) message = message + "现场指挥人员\n";
        if(mAccessInspectors.isEmpty()) message = message + "勘验检查人员\n";
        if(mCrimePeopleNumber.isEmpty()) message = message + "作案人数\n";
        if(mCrimePeopleFeature.isEmpty()) message = message + "作案人特点\n";
        if(mOverview.isEmpty()) message = message + "勘验情况\n";
        if(mPositionItem.isEmpty()) message = message + "方位示意图\n";
        if(mWitnessItem.isEmpty()) message = message + "见证人\n";
        if(mPositionPhotoItem.isEmpty()) message = message + "方位照片\n";
        if(mOverviewPhotoItem.isEmpty()) message = message + "概貌照片\n";
        if(mImportantPhotoItem.isEmpty()) message = message + "重点部位\n";
        if(mOccurred_end_time<mOccurred_start_time) message=message+"发案开始时间必须在发案结束时间之前\n";
        if(mGet_access_time<mOccurred_end_time) message=message+"接勘时间必须在发案结束时间之后\n";
        if(mAccess_start_time<mGet_access_time) message=message+"勘验开始时间必须在接勘时间之后\n";
        if(mAccess_end_time<mAccess_start_time) message=message+"勘验开始时间必须在勘验结束时间之前\n";

        //if(mCellResultItem.isEmpty()) message = message + "基站信息\n";liwei=========================
        return message;
    }
}
