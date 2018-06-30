package com.android.csiapp.Crime.createscene;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.csiapp.Crime.utils.ClearableEditText;
import com.android.csiapp.Crime.utils.DateTimePicker;
import com.android.csiapp.Crime.utils.DictionaryInfo;
import com.android.csiapp.Databases.CrimeItem;
import com.android.csiapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateScene_FP6 extends Fragment {

    private Context context = null;
    private CrimeItem mItem;
    private int mEvent;

    private LinearLayout mAutoBtn;
    private ClearableEditText mOverview;

    public CreateScene_FP6() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_scene_fp6, container, false);
        CreateSceneActivity activity  = (CreateSceneActivity) getActivity();
        if(savedInstanceState == null){
            mItem = activity.getItem();
            mEvent = activity.getEvent();
        }else{
            mItem = (CrimeItem) savedInstanceState.getSerializable("CrimeItem");
            mEvent = (int) savedInstanceState.getSerializable("Event");
        }
        context = getActivity().getApplicationContext();

        initView(view);
        initData();

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

    private void initView(View view){
        mAutoBtn = (LinearLayout) view.findViewById(R.id.automatic_generated_button);
        mAutoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auto_generated();
            }
        });
        mOverview = (ClearableEditText) view.findViewById(R.id.overview);
        mOverview.setMaxLines(30);
    }

    private void initData(){
        mOverview.setText(mItem.getOverview());
    }

    public void saveData(){
        mItem.setOverview(mOverview.getText().toString());
    }

    @Override
    public void onResume(){
        super.onResume();
        initData();
    }

    @Override
    public void onPause(){
        super.onPause();
        saveData();
    }

    private void auto_generated(){
        String type = mItem.getCasetype();
        if(type.isEmpty()){
            Toast.makeText(getActivity(), "请先选择案件类别", Toast.LENGTH_SHORT).show();
            return;
        }

        String text = "";

        String text1 = "";
        text1 = text1 + DateTimePicker.getCurrentTime(mItem.getGetAccessTime());
        text1 = text1 + mItem.getUnitsAssigned() + "接到一报警电话。\n";

        String text2 = "";
        text2 = text2 + "接报后，" + mItem.getAccessStartTime() + "，";
        text2 = text2 + "到达现场并对现场进行保护。\n";

        String text3 = "";
        text3 = text3 + "经勘查，现场位于" + mItem.getLocation() + "，";
        text3 = text3 + "现场东侧为，西侧为，南侧为，北侧为。\n";

        String text4 = "";
        if(mItem.getReleatedPeople().size()>0) {
            text4 = text4 + "事主身份信息：";
            for(int i=0;i<mItem.getReleatedPeople().size();i++){
                text4 = text4 + "姓名为" + mItem.getReleatedPeople().get(i).getPeopleName()
                        + "，性别为" + DictionaryInfo.getDictValue(DictionaryInfo.mSexKey, mItem.getReleatedPeople().get(i).getPeopleSex())
                        + "，身份证号为" + mItem.getReleatedPeople().get(i).getPeopleId()
                        + "，联系电话为" + mItem.getReleatedPeople().get(i).getPeopleNumber()
                        + "，现居住地为，" + mItem.getReleatedPeople().get(i).getPeopleAddress();
                text4 = text4 + "\n";
            }
        }

        String text5 = "";
        if(mItem.getLostItem().size()>0) {
            text5 = text5 + "损失物品";
            for(int i=0;i<mItem.getLostItem().size();i++){
                text5 = text5 + "品名为" + mItem.getLostItem().get(i).getItemName()
                        + "，厂牌型号为" + mItem.getLostItem().get(i).getItemBrand()
                        + "，数量为" + mItem.getLostItem().get(i).getItemAmount()
                        + "，价值为" + mItem.getLostItem().get(i).getItemValue()
                        + "，特征为，" + mItem.getLostItem().get(i).getItemFeature();
                text5 = text5 + "\n";
            }
        }

        String text6 = "";
        if(mItem.getEvidenceItem().size()>0) {
            text6 = text6 + "对现场内进行痕迹显现，";
            for(int i=0;i<mItem.getEvidenceItem().size();i++){
                text6 = text6 + "发现1枚可疑" + mItem.getEvidenceItem().get(i).getEvidenceCategory();
            }
            text6 = text6 + "。对现场及现场周围进行搜索，未发现其他物证。\n";
        }

        switch (type){
            case "050237":
                //盗窃牲畜
                text = text + "据报案人称，" + DateTimePicker.getCurrentTime(mItem.getOccurredStartTime());
                text = text + "在" + mItem.getLocation() + "，";
                text = text + DateTimePicker.getCurrentTime(mItem.getOccurredEndTime());
                text = text + "发现牲畜被盗，随即报案。\n";
                mOverview.setText(text1 + text + text2 + text3 + text4 + text5 + text6);
                break;
            case "050102":
                //挡路抢劫
                text = text + "据报案人称，" + DateTimePicker.getCurrentTime(mItem.getOccurredStartTime());
                text = text + "在" + mItem.getLocation() + "，";
                text = text + DateTimePicker.getCurrentTime(mItem.getOccurredEndTime());
                text = text + "遭遇抢劫，随即报案。\n";
                mOverview.setText(text1 + text + text2 + text3 + text4 + text5 + text6);
                break;
            case "050300":
                //诈骗
                text = text + "据报案人称，" + DateTimePicker.getCurrentTime(mItem.getOccurredStartTime());
                text = text + "在" + mItem.getLocation() + "，";
                text = text + DateTimePicker.getCurrentTime(mItem.getOccurredEndTime());
                text = text + "发现被骗，随即报案。\n";
                mOverview.setText(text1 + text + text2 + text3 + text4 + text5 + text6);
                break;
            case "050228":
                //竊盜電動自行車
                text = text + "据报案人称，" + DateTimePicker.getCurrentTime(mItem.getOccurredStartTime());
                text = text + "将电动车锁好后，停放在" + mItem.getLocation() + "，";
                text = text + DateTimePicker.getCurrentTime(mItem.getOccurredEndTime());
                text = text + "发现车辆被盗，随即报案。\n";
                mOverview.setText(text1 + text + text2 + text3 + text4 + text5 + text6);
                break;
            case "050240":
                //扒窃
                text = text + "据报案人称，" + DateTimePicker.getCurrentTime(mItem.getOccurredStartTime());
                text = text + "其在" + mItem.getLocation() + "发现被盗，随即报案。\n";
                mOverview.setText(text1 + text + text3 + text4 + text5 + text6);
                break;
            case "050224":
                //盗窃摩托车
                text = text + "据报案人称，" + DateTimePicker.getCurrentTime(mItem.getOccurredStartTime());
                text = text + "将摩托车锁好后，停放在" + mItem.getLocation() + "，";
                text = text + DateTimePicker.getCurrentTime(mItem.getOccurredEndTime());
                text = text + "发现车辆被盗，随即报案。\n";
                mOverview.setText(text1 + text + text2 + text3 + text4 + text5 + text6);
                break;
            case "050227":
                //盗窃自行车
                text = text + "据报案人称，" + DateTimePicker.getCurrentTime(mItem.getOccurredStartTime());
                text = text + "将自行车锁好后，停放在" + mItem.getLocation() + "，";
                text = text + DateTimePicker.getCurrentTime(mItem.getOccurredEndTime());
                text = text + "发现车辆被盗，随即报案。\n";
                mOverview.setText(text1 + text + text2 + text3 + text4 + text5 + text6);
                break;
            case "040103":
                //故意伤害
                text = text + "据报案人称，" + DateTimePicker.getCurrentTime(mItem.getOccurredStartTime());
                text = text + "其在" + mItem.getLocation() + "发生一起故意伤害案，随即报案。\n";
                mOverview.setText(text1 + text + text2 + text3 + text4 + text6);
                break;
            case "050400":
                //抢夺
                text = text + "据报案人称，" + DateTimePicker.getCurrentTime(mItem.getOccurredStartTime());
                text = text + "其在" + mItem.getLocation() + "行走时被抢，随即报案。\n";
                mOverview.setText(text1 + text + text3 + text4 + text5 + text6);
                break;
            default:
                break;
        }
        mItem.setOverview(mOverview.getText().toString());
    }
}
