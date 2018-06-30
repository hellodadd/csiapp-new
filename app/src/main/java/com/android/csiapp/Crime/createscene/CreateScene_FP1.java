package com.android.csiapp.Crime.createscene;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.method.DigitsKeyListener;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.csiapp.Crime.utils.ClearableEditText;
import com.android.csiapp.Crime.utils.CreateSceneUtils;
import com.android.csiapp.Crime.utils.DateTimePicker;
import com.android.csiapp.Crime.utils.DictionaryInfo;
import com.android.csiapp.Crime.utils.UserInfo;
import com.android.csiapp.Crime.utils.tree.TreeViewListActivity;
import com.android.csiapp.Databases.CellProvider;
import com.android.csiapp.Databases.CrimeItem;
import com.android.csiapp.Databases.CrimeProvider;
import com.android.csiapp.Databases.PhotoItem;
import com.android.csiapp.LoadingButton;
import com.android.csiapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateScene_FP1 extends Fragment implements View.OnClickListener {

    private Context context = null;
    private CrimeItem mItem;
    private int mEvent;

    private LoadingButton mCellCollection;
    private Button mCellDetail;
    private List<PhotoItem> mCellResultItems;
    private CellProvider mCellProvider;

    private TextView mCasetypeText, mAreaText, mWeatherConditionText, mWindDirectionText;
    private TextView mIlluminationConditionText, mSceneConductorText, mAccessInspectorsText;

    private ClearableEditText mLocation, mUnitsAssigned, mAccessPolicemen, mAccessLocation, mCaseOccurProcess;
    private ClearableEditText mChangeReason, mTemperature, mHumidity, mAccessReason;
    private ClearableEditText mProductPeopleName, mProductPeopleUnit, mProductPeopleDuties, mSafeguard;
    private Button mCaseOccurProcessBtn, mAccessReasonBtn;
    private LinearLayout mChangeReasonLinearLayout;
    private CheckBox mInformantCkBx, mVictimCkBx, mOtherCkBx;

    private TextView mOccurred_start_time, mOccurred_end_time, mGet_access_time, mAccess_start_time, mAccess_end_time;
    private Button mOccurred_start_button, mOccurred_end_button, mGet_access_button, mAccess_start_button, mAccess_end_button,mClose_layout_botton;
    private LinearLayout mClose_layout;

    private RadioGroup mRadioGroupSceneCondition;
    private RadioButton mRadioSceneCondition1,mRadioSceneCondition2;

    public CreateScene_FP1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_scene_fp1, container, false);
        CreateSceneActivity activity  = (CreateSceneActivity) getActivity();
        if(savedInstanceState == null){
            mItem = activity.getItem();
            mEvent = activity.getEvent();
        }else{
            mItem = (CrimeItem) savedInstanceState.getSerializable("CrimeItem");
            mEvent = (int) savedInstanceState.getSerializable("Event");
        }
        context = getActivity().getApplicationContext();

        IntentFilter filter = new IntentFilter();
        filter.addAction(CreateSceneUtils.ACTION_RECEIVE_RESULT);
        context.registerReceiver(mReceiver,filter);
        mCellProvider = new CellProvider(context);

        initView(view);
        initData();
        getLastData();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("CrimeItem",mItem);
        outState.putSerializable("Event",mEvent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            savedInstanceState.putSerializable("CrimeItem", mItem);
            savedInstanceState.putSerializable("Event", mEvent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = "";
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CreateSceneUtils.EVENT_CELL_COLLECTION) {
            }else if(requestCode == CreateSceneUtils.EVENT_CASETYPE_SELECT_ITEM){
                result = (String) data.getStringExtra("Select");
                mItem.setCasetype(result);
                result = DictionaryInfo.getDictValue(DictionaryInfo.mCaseTypeKey, result);
                mCasetypeText.setText(result);
            }else if(requestCode == CreateSceneUtils.EVENT_AREA_SELECT_ITEM){
                result = (String) data.getStringExtra("Select");
                mItem.setArea(result);
                result = DictionaryInfo.getDictValue(DictionaryInfo.mAreaKey, result);
                mAreaText.setText(result);
            }else if(requestCode == CreateSceneUtils.EVENT_SCENE_CONDITION_SELECT_ITEM){
                //变动原因改为radio button
                //result = (String) data.getStringExtra("Select");
                //mItem.setSceneCondition(result);
                //enableChangeReason(result);
                //result = DictionaryInfo.getDictValue(DictionaryInfo.mSceneConditionKey, result);
                //mSceneConditionText.setText(result);
            }else if(requestCode == CreateSceneUtils.EVENT_WEATHER_SELECT_ITEM){
                result = (String) data.getStringExtra("Select");
                mItem.setWeatherCondition(result);
                result = DictionaryInfo.getDictValue(DictionaryInfo.mWeatherConditionKey, result);
                mWeatherConditionText.setText(result);
            }else if(requestCode == CreateSceneUtils.EVENT_WIND_SELECT_ITEM){
                result = (String) data.getStringExtra("Select");
                mItem.setWindDirection(result);
                result = DictionaryInfo.getDictValue(DictionaryInfo.mWindDirectionKey, result);
                mWindDirectionText.setText(result);
            }else if(requestCode == CreateSceneUtils.EVENT_ILLUMINATION_SELECT_ITEM){
                result = (String) data.getStringExtra("Select");
                mItem.setIlluminationCondition(result);
                result = DictionaryInfo.getDictValue(DictionaryInfo.mIlluminationConditionKey, result);
                mIlluminationConditionText.setText(result);
            }else if(requestCode == CreateSceneUtils.EVENT_SCENE_CONDUCTOR_SLELECT_ITEM){
                result = (String) data.getStringExtra("Select");
                mItem.setSceneConductor(result);
                result = UserInfo.getUserName(result);
                mSceneConductorText.setText(result);
            }else if(requestCode == CreateSceneUtils.EVENT_ACCESS_INSPECTORS_SLELECT_ITEM) {
                result = (String) data.getStringExtra("Select");
                mItem.setAccessInspectors(result);
                result = UserInfo.getUserName(result);
                mAccessInspectorsText.setText(result);
            }
        }
    }

    private void initView(View view){
        //基站採集
        mCellCollection = (LoadingButton) view.findViewById(R.id.cell_collection);
        if(mItem.IsCollecting()) {
            mCellCollection.setText("采集中");
            mCellCollection.showLoading();
        }
        mCellCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCollection();
                ((LoadingButton)v).showLoading();
            }
        });

        //查看基站信息
        mCellDetail = (Button) view.findViewById(R.id.cell_detail);
        if((!mItem.IsCollecting() && mItem.IsCollected()) || !mItem.getCellResultItem().isEmpty())
            mCellDetail.setVisibility(View.VISIBLE);
        mCellDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Anita","result size = "+mItem.getCellResultItem().size());
                if(mItem.getCellResultItem().size()!=0){
                    Intent showRet = new Intent("android.intent.action.kuaikan.show_result");
                    showRet.putExtra("xml", mItem.getCellResultItem().get(mItem.getCellResultItem().size()-1).getPhotoPath());
                    showRet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(showRet);
                }
            }
        });

        //收起或打开
        this.mClose_layout=(LinearLayout)view.findViewById(R.id.colose_layout);
        this.mClose_layout_botton=(Button)view.findViewById(R.id.close_button);
        this.mClose_layout_botton.setOnClickListener(this);

        //案件類別
        mCasetypeText = (TextView) view.findViewById(R.id.casetype);
        mCasetypeText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), TreeViewListActivity.class);
                it.putExtra("Key",DictionaryInfo.mCaseTypeKey);
                it.putExtra("Selected", mItem.getCasetype());
                startActivityForResult(it, CreateSceneUtils.EVENT_CASETYPE_SELECT_ITEM);
            }
        });

        //發案區劃
        mAreaText = (TextView) view.findViewById(R.id.area);
        mAreaText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), TreeViewListActivity.class);
                it.putExtra("Key",DictionaryInfo.mAreaKey);
                it.putExtra("Selected", mItem.getArea());
                startActivityForResult(it, CreateSceneUtils.EVENT_AREA_SELECT_ITEM);
            }
        });

        //現場條件
        mRadioGroupSceneCondition=(RadioGroup)view.findViewById(R.id.radioGroupSceneCondition);
        mRadioSceneCondition1=(RadioButton)view.findViewById(R.id.radioSceneCondition1);
        mRadioSceneCondition2=(RadioButton)view.findViewById(R.id.radioSceneCondition2);
        mRadioGroupSceneCondition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //打开或者收起变动原因
                if(mRadioSceneCondition1.isChecked()){
                    //原始现场
                    mItem.setSceneCondition("1");
                    enableChangeReason("1");
                }
                else{
                    //变动现场
                    mItem.setSceneCondition("2");
                    enableChangeReason("2");
                }
            }
        });
        //天氣狀況
        mWeatherConditionText = (TextView) view.findViewById(R.id.weatherCondition);
        mWeatherConditionText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), TreeViewListActivity.class);
                it.putExtra("Key",DictionaryInfo.mWeatherConditionKey);
                it.putExtra("Selected", mItem.getWeatherCondition());
                startActivityForResult(it, CreateSceneUtils.EVENT_WEATHER_SELECT_ITEM);
            }
        });

        //風向
        mWindDirectionText = (TextView) view.findViewById(R.id.windDirection);
        mWindDirectionText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), TreeViewListActivity.class);
                it.putExtra("Key",DictionaryInfo.mWindDirectionKey);
                it.putExtra("Selected", mItem.getWindDirection());
                startActivityForResult(it, CreateSceneUtils.EVENT_WIND_SELECT_ITEM);
            }
        });

        //光照條件
        mIlluminationConditionText = (TextView) view.findViewById(R.id.illuminationCondition);
        mIlluminationConditionText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), TreeViewListActivity.class);
                it.putExtra("Key",DictionaryInfo.mIlluminationConditionKey);
                it.putExtra("Selected", mItem.getIlluminationCondition());
                startActivityForResult(it, CreateSceneUtils.EVENT_ILLUMINATION_SELECT_ITEM);
            }
        });

        //現場指揮人員
        mSceneConductorText = (TextView) view.findViewById(R.id.sceneConductor);
        mSceneConductorText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), TreeViewListActivity.class);
                SharedPreferences prefs4 = context.getSharedPreferences("UnitCode", 0);
                String UnitCode= prefs4.getString("unitcode", "");
                it.putExtra("unitcode",UnitCode);//登录用户单位代码UnitCode
                it.putExtra("user","uc");//显示上级单位及本单位用户
                it.putExtra("Key",UserInfo.mSceneConductor);
                it.putExtra("Selected", mItem.getSceneConductor());
                it.putExtra("DataInfo", "UserInfo");
                startActivityForResult(it, CreateSceneUtils.EVENT_SCENE_CONDUCTOR_SLELECT_ITEM);
            }
        });

        //勘驗檢查人員
        mAccessInspectorsText = (TextView) view.findViewById(R.id.accessInspectors);
        mAccessInspectorsText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), TreeViewListActivity.class);
                SharedPreferences prefs4 = context.getSharedPreferences("UnitCode", 0);
                String UnitCode= prefs4.getString("unitcode", "");
                it.putExtra("unitcode",UnitCode);//登录用户单位代码
                it.putExtra("user","c");//显示本单位用户
                it.putExtra("Key",UserInfo.mAccessInspectors);
                it.putExtra("Selected", mItem.getAccessInspectors());
                it.putExtra("DataInfo", "UserInfo");
                startActivityForResult(it, CreateSceneUtils.EVENT_ACCESS_INSPECTORS_SLELECT_ITEM);
            }
        });

        //案發區劃
        mLocation = (ClearableEditText) view.findViewById(R.id.location);
        mLocation.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count){
                mAccessLocation.setText(mLocation.getText());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void afterTextChanged(Editable s){}
        });

        //指派單位
        mUnitsAssigned = (ClearableEditText) view.findViewById(R.id.unitsAssigned);

        //接警人
        mAccessPolicemen= (ClearableEditText) view.findViewById(R.id.accessPolicemen);

        //勘驗地點
        mAccessLocation = (ClearableEditText) view.findViewById(R.id.accessLocation);

        //案件發現過程
        mCaseOccurProcess = (ClearableEditText) view.findViewById(R.id.caseOccurProcess);
        mCaseOccurProcess.setMaxLines(10);
        mCaseOccurProcessBtn = (Button) view.findViewById(R.id.caseOccurProcess_button);
        mCaseOccurProcessBtn.setOnClickListener(this);

        //現場變動原因
        mChangeReasonLinearLayout = (LinearLayout) view.findViewById(R.id.change_reason_linear);
        mChangeReason = (ClearableEditText) view.findViewById(R.id.change_reason);
        mChangeReason.setHint("输入其它变动原因");
        mInformantCkBx = (CheckBox) view.findViewById(R.id.InformantCkBx);
        mVictimCkBx = (CheckBox) view.findViewById(R.id.VictimCkBx);
        mOtherCkBx = (CheckBox) view.findViewById(R.id.OtherCkBx);
        ArrayList<String> ChangeOption = DictionaryInfo.getDictKeyList(DictionaryInfo.mChangeOptionKey);
        if(ChangeOption.size()>=3) {
            mInformantCkBx.setText(DictionaryInfo.getDictValue(DictionaryInfo.mChangeOptionKey, ChangeOption.get(0)));
            mVictimCkBx.setText(DictionaryInfo.getDictValue(DictionaryInfo.mChangeOptionKey, ChangeOption.get(1)));
            mOtherCkBx.setText(DictionaryInfo.getDictValue(DictionaryInfo.mChangeOptionKey, ChangeOption.get(2)));
        }
        mInformantCkBx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String changeOption = mItem.getChangeOption();
                if(mInformantCkBx.isChecked()) {
                    if(!changeOption.equalsIgnoreCase("")) changeOption = changeOption +",";
                    changeOption = changeOption + DictionaryInfo.getDictKey(DictionaryInfo.mChangeOptionKey, mInformantCkBx.getText().toString());
                }else {
                    String[] array = changeOption.split(",");
                    changeOption = "";
                    String remove = DictionaryInfo.getDictKey(DictionaryInfo.mChangeOptionKey, mInformantCkBx.getText().toString());
                    for(int i = 0;i<array.length;i++){
                        if(!array[i].equalsIgnoreCase(remove)){
                            if(!changeOption.equalsIgnoreCase("")) changeOption = changeOption +",";
                            changeOption = changeOption + array[i];
                        }
                    }
                }
                mItem.setChangeOption(changeOption);
            }
        });
        mVictimCkBx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String changeOption = mItem.getChangeOption();
                if(mVictimCkBx.isChecked()) {
                    if(!changeOption.equalsIgnoreCase("")) changeOption = changeOption +",";
                    changeOption = changeOption + DictionaryInfo.getDictKey(DictionaryInfo.mChangeOptionKey, mVictimCkBx.getText().toString());
                }else {
                    String[] array = changeOption.split(",");
                    changeOption = "";
                    String remove = DictionaryInfo.getDictKey(DictionaryInfo.mChangeOptionKey, mVictimCkBx.getText().toString());
                    for(int i = 0;i<array.length;i++){
                        if(!array[i].equalsIgnoreCase(remove)){
                            if(!changeOption.equalsIgnoreCase("")) changeOption = changeOption +",";
                            changeOption = changeOption + array[i];
                        }
                    }
                }
                mItem.setChangeOption(changeOption);
            }
        });
        mOtherCkBx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String changeOption = mItem.getChangeOption();
                if(mOtherCkBx.isChecked()) {
                    if(!changeOption.equalsIgnoreCase("")) changeOption = changeOption +",";
                    changeOption = changeOption + DictionaryInfo.getDictKey(DictionaryInfo.mChangeOptionKey, mOtherCkBx.getText().toString());
                }else {
                    String[] array = changeOption.split(",");
                    changeOption = "";
                    String remove = DictionaryInfo.getDictKey(DictionaryInfo.mChangeOptionKey, mOtherCkBx.getText().toString());
                    for(int i = 0;i<array.length;i++){
                        if(!array[i].equalsIgnoreCase(remove)){
                            if(!changeOption.equalsIgnoreCase("")) changeOption = changeOption +",";
                            changeOption = changeOption + array[i];
                        }
                    }
                }
                mItem.setChangeOption(changeOption);
            }
        });

        //溫度
        mTemperature = (ClearableEditText) view.findViewById(R.id.temperature);
        mTemperature.setKeyListener(DigitsKeyListener.getInstance("-0123456789"));

        //濕度
        mHumidity = (ClearableEditText) view.findViewById(R.id.humidity);
        mHumidity.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

        //勘驗事由
        mAccessReason = (ClearableEditText) view.findViewById(R.id.accessReason);
        mAccessReason.setMaxLines(10);
        mAccessReasonBtn = (Button) view.findViewById(R.id.accessReason_button);
        mAccessReasonBtn.setOnClickListener(this);

        //保護人姓名
        mProductPeopleName = (ClearableEditText) view.findViewById(R.id.productPeople_name);

        //保護人單位
        mProductPeopleUnit = (ClearableEditText) view.findViewById(R.id.productPeople_unit);

        //保護人職務
        mProductPeopleDuties = (ClearableEditText) view.findViewById(R.id.productPeople_duties);

        //保護措施
        mSafeguard = (ClearableEditText) view.findViewById(R.id.safeguard);

        //時間
        mOccurred_start_time = (TextView) view.findViewById(R.id.occurred_start_time);
        mOccurred_end_time = (TextView) view.findViewById(R.id.occurred_end_time);
        mGet_access_time = (TextView) view.findViewById(R.id.get_access_time);
        mAccess_start_time = (TextView) view.findViewById(R.id.access_start_time);
        mAccess_end_time = (TextView) view.findViewById(R.id.access_end_time);
        mOccurred_start_button = (Button) view.findViewById(R.id.occurred_start_time_button);
        mOccurred_end_button = (Button) view.findViewById(R.id.occurred_end_time_button);
        mGet_access_button = (Button) view.findViewById(R.id.get_access_time_button);
        mAccess_start_button = (Button) view.findViewById(R.id.access_start_time_button);
        mAccess_end_button= (Button) view.findViewById(R.id.access_end_time_button);
        mOccurred_start_button.setOnClickListener(this);
        mOccurred_end_button.setOnClickListener(this);
        mGet_access_button.setOnClickListener(this);
        mAccess_start_button.setOnClickListener(this);
        mAccess_end_button.setOnClickListener(this);

        mOccurred_start_time.setOnClickListener(this);
        mOccurred_end_time.setOnClickListener(this);
        mGet_access_time.setOnClickListener(this);
        mAccess_start_time.setOnClickListener(this);
        mAccess_end_time.setOnClickListener(this);
    }

    private void initData(){
        mCellResultItems = mItem.getCellResultItem();
        mCasetypeText.setText(DictionaryInfo.getDictValue(DictionaryInfo.mCaseTypeKey, mItem.getCasetype()));
        mAreaText.setText(DictionaryInfo.getDictValue(DictionaryInfo.mAreaKey, mItem.getArea()));
        //设置原始还是变动现场
        enableChangeReason(mItem.getSceneCondition());

        mWeatherConditionText.setText(DictionaryInfo.getDictValue(DictionaryInfo.mWeatherConditionKey, mItem.getWeatherCondition()));
        mWindDirectionText.setText(DictionaryInfo.getDictValue(DictionaryInfo.mWindDirectionKey, mItem.getWindDirection()));
        mIlluminationConditionText.setText(DictionaryInfo.getDictValue(DictionaryInfo.mIlluminationConditionKey, mItem.getIlluminationCondition()));

        mLocation.setText(mItem.getLocation());
        mUnitsAssigned.setText(mItem.getUnitsAssigned());
        mAccessPolicemen.setText(mItem.getAccessPolicemen());
        mAccessLocation.setText(mItem.getAccessLocation());
        mCaseOccurProcess.setText(mItem.getCaseOccurProcess());

        mInformantCkBx.setChecked(mItem.getChangeOption().contains(
                DictionaryInfo.getDictKey(DictionaryInfo.mChangeOptionKey, mInformantCkBx.getText().toString())));
        mVictimCkBx.setChecked(mItem.getChangeOption().contains(
                DictionaryInfo.getDictKey(DictionaryInfo.mChangeOptionKey, mVictimCkBx.getText().toString())));
        mOtherCkBx.setChecked(mItem.getChangeOption().contains(
                DictionaryInfo.getDictKey(DictionaryInfo.mChangeOptionKey, mOtherCkBx.getText().toString())));
        mChangeReason.setText(mItem.getChangeReason());

        mTemperature.setText(mItem.getTemperature());
        mHumidity.setText(mItem.getHumidity());
        mAccessReason.setText(mItem.getAccessReason());
        mProductPeopleName.setText(mItem.getProductPeopleName());
        mProductPeopleUnit.setText(mItem.getProductPeopleUnit());
        mProductPeopleDuties.setText(mItem.getProductPeopleDuties());
        mSafeguard.setText(mItem.getSafeguard());
        mSceneConductorText.setText(UserInfo.getUserName(mItem.getSceneConductor()));
        mAccessInspectorsText.setText(UserInfo.getUserName(mItem.getAccessInspectors()));

        mOccurred_start_time.setText(DateTimePicker.getCurrentTime(mItem.getOccurredStartTime()));
        mOccurred_end_time.setText(DateTimePicker.getCurrentTime(mItem.getOccurredEndTime()));
        mGet_access_time.setText(DateTimePicker.getCurrentTime(mItem.getGetAccessTime()));
        mAccess_start_time.setText(DateTimePicker.getCurrentTime(mItem.getAccessStartTime()));
        mAccess_end_time.setText(DateTimePicker.getCurrentTime(mItem.getAccessEndTime()));
    }

    private void getLastData(){
        if(mEvent == 1 && !mItem.IsGetLastData()){
            mItem.setGetLastData(true);
            SharedPreferences prefs1 = context.getSharedPreferences("LoginName", 0);
            String LoginName = prefs1.getString("loginname", "");
            SharedPreferences prefs2 = context.getSharedPreferences("UserName", 0);
            String UserName = prefs2.getString("username", "");
            SharedPreferences prefs3 = context.getSharedPreferences("UnitName", 0);
            String UnitName = prefs3.getString("unitname", "");
            SharedPreferences prefs4 = context.getSharedPreferences("UnitCode", 0);
            String UnitCode= prefs4.getString("unitcode", "");
            mProductPeopleName.setText(UserName);
            mAreaText.setText(DictionaryInfo.getDictValue(DictionaryInfo.mAreaKey, UnitCode));
            mProductPeopleUnit.setText(UnitName);
            mUnitsAssigned.setText("110指挥中心");
            mIlluminationConditionText.setText("自然光");
            mSafeguard.setText("专人看护现场，防止他人进入");
            mProductPeopleDuties.setText("民警");
            mSceneConductorText.setText(UserInfo.getUserName(LoginName));
            mAccessInspectorsText.setText(UserInfo.getUserName(LoginName));

            CrimeItem lastItem = getLastRecord();
            if(lastItem!=null){
                mAccessPolicemen.setText(lastItem.getAccessPolicemen());
                mWeatherConditionText.setText(DictionaryInfo.getDictValue(DictionaryInfo.mWeatherConditionKey, lastItem.getWeatherCondition()));
                mWindDirectionText.setText(DictionaryInfo.getDictValue(DictionaryInfo.mWindDirectionKey, lastItem.getWindDirection()));
                mTemperature.setText(lastItem.getTemperature());
                mHumidity.setText(lastItem.getHumidity());
                enableChangeReason(lastItem.getSceneCondition());
                mInformantCkBx.setChecked(lastItem.getChangeOption().contains(
                        DictionaryInfo.getDictKey(DictionaryInfo.mChangeOptionKey, mInformantCkBx.getText().toString())));
                mVictimCkBx.setChecked(lastItem.getChangeOption().contains(
                        DictionaryInfo.getDictKey(DictionaryInfo.mChangeOptionKey, mVictimCkBx.getText().toString())));
                mOtherCkBx.setChecked(lastItem.getChangeOption().contains(
                        DictionaryInfo.getDictKey(DictionaryInfo.mChangeOptionKey, mOtherCkBx.getText().toString())));
                mChangeReason.setText(mItem.getChangeReason());
                mChangeReason.setText(lastItem.getChangeReason());
                if(lastItem.getSceneConductor()!=null) mSceneConductorText.setText(UserInfo.getUserName(lastItem.getSceneConductor()));
                if(lastItem.getAccessInspectors()!=null)mAccessInspectorsText.setText(UserInfo.getUserName(lastItem.getAccessInspectors()));

                mItem.setWeatherCondition(lastItem.getWeatherCondition());
                mItem.setWindDirection(lastItem.getWindDirection());
                mItem.setSceneCondition(lastItem.getSceneCondition());
                mItem.setChangeOption(lastItem.getChangeOption());
            }
            mItem.setArea(UnitCode);
            mItem.setIlluminationCondition(DictionaryInfo.getDictKey(DictionaryInfo.mIlluminationConditionKey, (String) mIlluminationConditionText.getText()));
            mItem.setSceneConductor(UserInfo.getLoginName((String) mSceneConductorText.getText()));
            mItem.setAccessInspectors(UserInfo.getLoginName((String) mAccessInspectorsText.getText()));
            saveData();
        }
    }

    public void saveData(){
        mItem.setCellResultItem(mCellResultItems);
        //儲存文本內容
        mItem.setLocationa(mLocation.getText());
        mItem.setUnitsAssigned(mUnitsAssigned.getText());
        mItem.setAccessPolicemen(mAccessPolicemen.getText());
        mItem.setAccessLocation(mAccessLocation.getText());
        mItem.setCaseOccurProcess(mCaseOccurProcess.getText());
        mItem.setChangeReason(mChangeReason.getText());
        mItem.setTemperature(mTemperature.getText());
        mItem.setHumidity(mHumidity.getText());
        mItem.setAccessReason(mAccessReason.getText());
        mItem.setProductPeopleName(mProductPeopleName.getText());
        mItem.setProductPeopleUnit(mProductPeopleUnit.getText());
        mItem.setProductPeopleDuties(mProductPeopleDuties.getText());
        mItem.setSafeguard(mSafeguard.getText());
    }

    @Override
    public void onResume(){
        super.onResume();
        initData();
    }

    @Override
    public void onPause(){
        super.onPause();
        releaseFocusEditText();
        saveData();
    }

    @Override
    public void onClick(View v) {
        releaseFocusEditText();
        switch (v.getId()) {
            case R.id.occurred_start_time_button:
            case R.id.occurred_start_time:
                showDateTimeDialog(mOccurred_start_time,1);
                break;
            case R.id.occurred_end_time_button:
            case R.id.occurred_end_time:
                showDateTimeDialog(mOccurred_end_time,2);
                break;
            case R.id.get_access_time_button:
            case R.id.get_access_time:
                showDateTimeDialog(mGet_access_time,3);
                break;
            case R.id.access_start_time_button:
            case R.id.access_start_time:
                showDateTimeDialog(mAccess_start_time,4);
                break;
            case R.id.access_end_time_button:
            case R.id.access_end_time:
                showDateTimeDialog(mAccess_end_time,5);
                break;
            case R.id.caseOccurProcess_button:
                mCaseOccurProcess.setText(getCaseOccurProcess());
                break;
            case R.id.accessReason_button:
                mAccessReason.setText(getAccessReason());
                break;
            case R.id.close_button:
                this.onCloseLayoutBtnClick(v);
                break;
            default:
                break;
        }
    }

    private void releaseFocusEditText(){
        mLocation.clearFocus();
        mUnitsAssigned.clearFocus();
        mAccessPolicemen.clearFocus();
        mAccessLocation.clearFocus();
        mCaseOccurProcess.clearFocus();
        mChangeReason.clearFocus();
        mTemperature.clearFocus();
        mHumidity.clearFocus();
        mAccessReason.clearFocus();
        mProductPeopleName.clearFocus();
        mProductPeopleUnit.clearFocus();
        mProductPeopleDuties.clearFocus();
        mSafeguard.clearFocus();
    }

    private void enableChangeReason(String sceneCondition){
        if(sceneCondition.equals("")) {
            sceneCondition="1";
            mItem.setSceneCondition("1");
        }
        if(sceneCondition.equalsIgnoreCase("1")){
            mRadioSceneCondition1.setChecked(true);
            mChangeReasonLinearLayout.setVisibility(View.GONE);
            mItem.setChangeOption("");
            mItem.setChangeReason("");
        }else if(sceneCondition.equalsIgnoreCase("2")){
            mRadioSceneCondition2.setChecked(true);
            mChangeReasonLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    private String getCaseOccurProcess(){
        saveData();
        //Example : "据<被害人/报案人>报称:<发案开始时间> 在<发案地点>，该处发现一起<案件类别>，后拨打电话报警"
        String result = "据";
        if(mItem.getReleatedPeople().size()>0){
            for(int i = 0;i<mItem.getReleatedPeople().size();i++){
                result = result + mItem.getReleatedPeople().get(i).getPeopleName();
                if(i!=mItem.getReleatedPeople().size()-1) result = result + ",";
            }
        }else{
            result = result + "<被害人/报案人>";
        }

        result = result + "报称:"+ DateTimePicker.getCurrentTime(mItem.getOccurredStartTime())
                        + "，在" + mItem.getLocation()
                        + "，该处发现一起" + DictionaryInfo.getDictValue(DictionaryInfo.mCaseTypeKey, mItem.getCasetype())
                        + "，后拨打电话报警。";

        return result;
    }

    private String getAccessReason(){
        saveData();
        //Example : "<发案区划><接警人>接到<指派单位>的指派: 在该所管界内<发案地点>发生一起<案件类别>，请速派人员勘查现场"
        String result = DictionaryInfo.getDictValue(DictionaryInfo.mAreaKey,mItem.getArea()) + mItem.getAccessPolicemen() + "接到"
                        + mItem.getUnitsAssigned() + "的指派: 在该所管界内"
                        + mItem.getLocation() + "发生一起" + DictionaryInfo.getDictValue(DictionaryInfo.mCaseTypeKey, mItem.getCasetype())
                        + "，请速派人员勘查现场。";
        return result;
    }

    private void showDateTimeDialog(final TextView textView,final int type) {
        // Create the dialog
        final Dialog mDateTimeDialog = new Dialog(getContext());
        // Inflate the root layout
        final RelativeLayout mDateTimeDialogView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.date_time_dialog, null);
        // Grab widget instance
        final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);

        mDateTimePicker.clickTime();

        // Update demo TextViews when the "OK" button is clicked
        ((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mDateTimePicker.clearFocus();
                // TODO Auto-generated method stub
                long time = mDateTimePicker.get().getTimeInMillis();
                if(type == 1) mItem.setOccurredStartTime(time);
                else if(type == 2) mItem.setOccurredEndTime(time);
                else if(type == 3) mItem.setGetAccessTime(time);
                else if(type == 4) mItem.setAccessStartTime(time);
                else if(type == 5) mItem.setAccessEndTime(time);
                textView.setText(DateTimePicker.getCurrentTime(time));
                mDateTimeDialog.dismiss();
            }
        });

        // Cancel the dialog when the "Cancel" button is clicked
        ((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDateTimeDialog.cancel();
            }
        });

        // Reset Date and Time pickers when the "Reset" button is clicked
        ((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDateTimePicker.reset();
            }
        });

        // No title on the dialog window
        mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set the dialog content view
        mDateTimeDialog.setContentView(mDateTimeDialogView);
        // Display the dialog
        mDateTimeDialog.show();
    }

    private CrimeItem getLastRecord(){
        CrimeProvider crimeProvider = new CrimeProvider(context);
        List<CrimeItem> items = crimeProvider.getAll();
        CrimeItem item = null;
        /* << AnitaLin */
        //Bug [在采集过程中自动保存，避免程序崩溃数据丢失]
        //The record already insert to databases, so the last record need to get the second item.
        if(items.size()>=2) item = items.get(items.size()-2);
        /* >> */
        return item;
    }

    private void startCollection(){
        Log.d("Anita", "startCollection");
        mCellCollection.setClickable(false);
        mCellCollection.setText("采集中");
        mItem.setCollecting(true);
        mItem.setCollected(false);
        mItem.setCellResult(new ArrayList<String>());
        mCellDetail.setVisibility(View.GONE);

        Intent it=new Intent();
        it.setAction("com.kuaikan.one_key");
        it.setComponent(new ComponentName("com.kuaikan.app.scenecollection",
                "com.kuaikan.app.scenecollection.OneKeyService"));
        it.putExtra("show", false);
        //强制完整采集
        //it.putExtra("is_quick_serch",true);
        //it.putExtra("is_all_search", true);
        //强制快速采集
        //it.putExtra("is_quick_serch",true);
        //it.putExtra("is_all_search", false);
        //跟随系统设置
        it.putExtra("is_quick_serch",false);
        it.putExtra("is_all_search", false);
        context.startService(it);
    }

    private void stopCollection(){
        Log.d("Anita", "stopCollection");

        Intent it=new Intent();
        it.setAction("com.kuaikan.one_key");
        it.setComponent(new ComponentName("com.kuaikan.app.scenecollection",
                "com.kuaikan.app.scenecollection.OneKeyService"));
        it.putExtra("request_type", 0);
        context.stopService(it);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(CreateSceneUtils.ACTION_RECEIVE_RESULT.equals(action) && mItem.IsCollecting()){
                Log.d("Anita", "Received cell result");
                mCellCollection.setClickable(true);
                mCellCollection.setText("开始采集");
                mCellCollection.showButtonText();
                mItem.setCollecting(false);
                mItem.setCollected(true);
                ArrayList<String> result= (ArrayList<String>) intent.getStringArrayListExtra("result");

                /* << AnitaLin */
                //Bug [基站信息在采集过程中退出，再次新增或从列表进入采集时，程序崩溃退出]
                //Check if getActivity is null
                Activity activity = getActivity();
                if(activity == null) return;
                /* >> */

                String file_path = (String) intent.getStringExtra("file_path");
                String uuid = (String) intent.getStringExtra("uuid");
                String path = CreateSceneUtils.copyToInternalPath(activity, file_path);
                Log.d("Anita","uuid from another service = "+uuid);
                Log.d("Anita","file_path from another service = "+file_path);
                PhotoItem CellResult = new PhotoItem();
                CellResult.setPhotoPath(path);
                CellResult.setUuid(uuid);
                Log.d("Anita","uuid = "+uuid);
                Log.d("Anita","file_path = "+path);
                CellResult.setId(mCellProvider.insert(CellResult));
                mCellResultItems.add(CellResult);
                Log.d("Anita","received result size = "+result.size());
                mItem.setCellResult(result);
                //stopCollection();

                mCellDetail.setVisibility(View.VISIBLE);
            }
        }
    };

    //add by liwei 2017.2.25收起或打开更多选项
    public void onCloseLayoutBtnClick(View view) {
        if(this.mClose_layout_botton.getText().toString().equals("更多")){
            mClose_layout.setVisibility(View.VISIBLE);
            this.mClose_layout_botton.setText("收起");
        }
        else{
            mClose_layout.setVisibility(View.GONE);
            this.mClose_layout_botton.setText("更多");
        }
    }
}
