package com.android.csiapp.Crime.listscene;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemProperties;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.csiapp.CsiApplication;
import com.android.csiapp.Databases.CrimeItem;
import com.android.csiapp.Databases.CrimeProvider;
import com.android.csiapp.PcSocketTransmission.FileHelper;
import com.android.csiapp.R;
import com.android.csiapp.WebServiceTransmission.WebService;
import com.android.csiapp.WebServiceTransmission.WebServiceSocket;
import com.android.csiapp.XmlHandler.DataInitial;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class ListReportSceneActivity extends AppCompatActivity {

    private Context mContext = null;
    private CrimeProvider mCrimeProvider;
    private List<CrimeItem> items_list;
    private ListView mListV;
    private HashMap<Integer, Boolean> isSelected;
    private ListDeleteAdapter mAdapter;

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_select_all:
                    msg += "Select All";
                    for (int i = 0; i < items_list.size(); i++) {
                        mAdapter.getIsSelected().put(i, true);
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                case R.id.action_reportscene:
                    msg += "Report";
                    List<CrimeItem> crimeItemList=new ArrayList<>();
                    if(items_list.size()>0){
                        isSelected = mAdapter.getIsSelected();
                        for (int i = 0; i < isSelected.size(); i++) {
                            if (isSelected.get(i).equals(true)) {
                                crimeItemList.add(items_list.get(i));
                            }
                        }
                    }
                    if(crimeItemList.size()>0) {
                        //开始上报数据
                        Toast.makeText(ListReportSceneActivity.this, "后台数据上传，共:" + crimeItemList.size() + "条", Toast.LENGTH_SHORT).show();
                        UploadSceneTask uploadSceneTask=new UploadSceneTask(crimeItemList);
                        uploadSceneTask.execute();
                    }
                    Intent result = getIntent();
                    setResult(Activity.RESULT_OK, result);
                    finish();
                    break;
            }

            if(!msg.equals("")) {
                //Toast.makeText(ListDeleteActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_report_scene);
        mContext = this.getApplicationContext();

        mCrimeProvider = new CrimeProvider(mContext);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(mContext.getResources().getColor(R.color.titleBar));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back_mini);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //What to do on back clicked
            }
        });
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        SharedPreferences prefs1 = mContext.getSharedPreferences("LoginName", 0);
        String LoginName = prefs1.getString("loginname", "");

        items_list = mCrimeProvider.getAllCompleteItem(LoginName);

        Collections.sort(items_list,
                new Comparator<CrimeItem>() {
                    public int compare(CrimeItem o1, CrimeItem o2) {
                        return String.valueOf(o1.getOccurredStartTime()).compareTo(String.valueOf(o2.getOccurredStartTime()));
                    }
                });
        Collections.reverse(items_list);

        mListV=(ListView)findViewById(R.id.listView);
        isSelected = new HashMap<Integer,Boolean>();
        mAdapter = new ListDeleteAdapter(ListReportSceneActivity.this,items_list, isSelected);
        mListV.setAdapter(mAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_reportscene, menu);
        return true;
    }

    /**
     * 逐一读取已经完成的现场上报
     * */
    public class UploadSceneTask extends AsyncTask<Void, Void, Boolean> {
        List<CrimeItem> crimeItemList=null;
        UploadSceneTask(List<CrimeItem> upLoadList){
            crimeItemList=upLoadList;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //通过网络上报现场数据
            //设置上传
            CsiApplication csiApplication=(CsiApplication)getApplication();
            csiApplication.setIsReporting(true);

            WebService webService=new WebService(mContext);
            DataInitial dataInitial = new DataInitial(mContext);
            //文件存储位置
            String mSceneMsgPath = Environment.getExternalStorageDirectory()+"/SceneMsg.zip";
            String scencNo="";
            for(int i=0;i<crimeItemList.size();i++){
                //循环上报数据
                String sceneID=crimeItemList.get(i).getSceneId();
                String unitCode=crimeItemList.get(i).getUnitCode();
                String userName=crimeItemList.get(i).getLoginName();
                String userID=crimeItemList.get(i).getLoginName();
                String deviceID= SystemProperties.get("ro.serialno");
                //生成单一zip文件
                dataInitial.CreateBaseMsgIdZip(crimeItemList.get(i).getSceneId());
                File fileBaseMsg = new File(mSceneMsgPath);
                byte[] sceneData=null;
                try {
                    sceneData = FileHelper.readFile(fileBaseMsg);
                }catch (Exception e1){

                }
                if(sceneData!=null){
                    scencNo=webService.ReportSceneInfo(sceneID,unitCode,userName,userID,deviceID,sceneData);
                    //正确写入现勘编号
                    if(!"".equals(scencNo)) {
                        crimeItemList.get(i).setComplete("2");
                        crimeItemList.get(i).setSceneNo(scencNo);
                        mCrimeProvider.update(crimeItemList.get(i));
                    }
                    else{
                        //scencNo为"",失败一次则不进行后续操作
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //showProgress(false, mProcessView, mMainView);
            if(success) {
                String msg = mContext.getResources().getString(R.string.scene_upload_success);
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }else {
                String msg = mContext.getResources().getString(R.string.scene_upload_failed);
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
            //通过网络上报现场数据
            CsiApplication csiApplication=(CsiApplication)getApplication();
            csiApplication.setIsReporting(false);
        }

        @Override
        protected void onCancelled() {
            //showProgress(false, mProcessView, mMainView);
            String msg = mContext.getResources().getString(R.string.scene_upload_failed);
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            CsiApplication csiApplication=(CsiApplication)getApplication();
            csiApplication.setIsReporting(false);
        }
    }

    }
