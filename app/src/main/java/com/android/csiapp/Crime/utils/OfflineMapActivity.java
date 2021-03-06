package com.android.csiapp.Crime.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.csiapp.R;
import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;

/**
 * Created by joanlin on 2016/10/23.
 */
public class OfflineMapActivity extends AppCompatActivity {
    protected static final String TAG = "OfflineMapActivity";
    /**
     * 离线地图功能
     */
    private MKOfflineMap mOfflineMap;
    private ListView mListView;
    /**
     * 离线地图的数据
     */
    private List<OfflineMapCityBean> mDatas = new ArrayList<OfflineMapCityBean>();
    /**
     * 适配器
     */
    private MyOfflineCityBeanAdapter mAdapter;
    private LayoutInflater mInflater;
    private Context context;
    /**
     * 目前加入下载队列的城市
     */
    private List<Integer> mCityCodes = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offlinemap);

        context = this;
        mInflater = LayoutInflater.from(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(context.getResources().getString(R.string.title_activity_offlinemap));
        toolbar.setTitleTextColor(context.getResources().getColor(R.color.titleBar));
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

        /**
         * 初始化离线地图
         */
        initOfflineMap();
        /**
         * 初始化ListView数据
         */
        initData();
        /**
         * 初始化ListView
         */
        initListView();

    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_click:
                    finish();
                    break;
                default:
                    break;
            }

            if(!msg.equals("")) {
                //Toast.makeText(CreateScene_FP3_PositionInformationActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_fp3_subactivity, menu);
        return true;
    }

    private void initListView()
    {
        mListView = (ListView) findViewById(R.id.id_offline_map_lv);
        mAdapter = new MyOfflineCityBeanAdapter();
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                int cityId = mDatas.get(position).getCityCode();
                if (mCityCodes.contains(cityId))
                {
                    removeTaskFromQueue(position, cityId);
                } else
                {
                    addToDownloadQueue(position, cityId);
                }

            }
        });
    }

    /**
     * 将任务移除下载队列
     *
     * @param pos
     * @param cityId
     */
    public void removeTaskFromQueue(int pos, int cityId)
    {
        mOfflineMap.pause(cityId);
        mDatas.get(pos).setFlag(OfflineMapCityBean.Flag.NO_STATUS);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 将下载任务添加至下载队列
     *
     * @param pos
     * @param cityId
     */
    public void addToDownloadQueue(int pos, int cityId)
    {
        mCityCodes.add(cityId);
        mOfflineMap.start(cityId);
        mDatas.get(pos).setFlag(OfflineMapCityBean.Flag.PAUSE);
        mAdapter.notifyDataSetChanged();
    }

    private void initData()
    {

        // 获得所有热门城市
        ArrayList<MKOLSearchRecord> offlineCityList = mOfflineMap
                .getHotCityList();
        // 手动添加了西安
        MKOLSearchRecord xian = new MKOLSearchRecord();
        xian.cityID = 9000;
        xian.cityName = "台灣省";
        offlineCityList.add(xian);
        // 获得所有已经下载的城市列表
        ArrayList<MKOLUpdateElement> allUpdateInfo = mOfflineMap
                .getAllUpdateInfo();
        // 设置所有数据的状态
        for (MKOLSearchRecord record : offlineCityList)
        {
            OfflineMapCityBean cityBean = new OfflineMapCityBean();
            cityBean.setCityName(record.cityName);
            cityBean.setCityCode(record.cityID);

            if (allUpdateInfo != null)//没有任何下载记录，返回null,为啥不返回空列表~~
            {
                for (MKOLUpdateElement ele : allUpdateInfo)
                {
                    if (ele.cityID == record.cityID)
                    {
                        cityBean.setProgress(ele.ratio);
                    }
                }

            }
            mDatas.add(cityBean);
        }

    }

    /**
     * 初始化离线地图
     */
    private void initOfflineMap()
    {
        mOfflineMap = new MKOfflineMap();
        // 设置监听
        mOfflineMap.init(new MKOfflineMapListener()
        {
            @Override
            public void onGetOfflineMapState(int type, int state)
            {
                switch (type)
                {
                    case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
                        // 离线地图下载更新事件类型
                        MKOLUpdateElement update = mOfflineMap.getUpdateInfo(state);
                        Log.e(TAG, update.cityName + " ," + update.ratio);
                        for (OfflineMapCityBean bean : mDatas)
                        {
                            if (bean.getCityCode() == state)
                            {
                                bean.setProgress(update.ratio);
                                bean.setFlag(OfflineMapCityBean.Flag.DOWNLOADING);
                                break;
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        Log.e(TAG, "TYPE_DOWNLOAD_UPDATE");
                        break;
                    case MKOfflineMap.TYPE_NEW_OFFLINE:
                        // 有新离线地图安装
                        Log.e(TAG, "TYPE_NEW_OFFLINE");
                        break;
                    case MKOfflineMap.TYPE_VER_UPDATE:
                        // 版本更新提示
                        break;
                }

            }
        });
    }

    @Override
    protected void onDestroy()
    {
        mOfflineMap.destroy();
        super.onDestroy();
    }

    /**
     * 热门城市地图列表的Adapter
     *
     * @author zhy
     *
     */
    class MyOfflineCityBeanAdapter extends BaseAdapter
    {

        @Override
        public boolean isEnabled(int position)
        {
            if (mDatas.get(position).getProgress() == 100)
            {
                return false;
            }
            return super.isEnabled(position);
        }

        @Override
        public int getCount()
        {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position)
        {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            OfflineMapCityBean bean = mDatas.get(position);
            ViewHolder holder = null;
            if (convertView == null)
            {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.offlinemap_item,
                        parent, false);
                holder.cityName = (TextView) convertView
                        .findViewById(R.id.id_cityname);
                holder.progress = (TextView) convertView
                        .findViewById(R.id.id_progress);
                convertView.setTag(holder);
            } else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.cityName.setText(bean.getCityName());
            int progress = bean.getProgress();
            String progressMsg = "";
            // 根据进度情况，设置显示
            if (progress == 0)
            {
                progressMsg = "未下载";
            } else if (progress == 100)
            {
                bean.setFlag(OfflineMapCityBean.Flag.NO_STATUS);
                progressMsg = "已下载";
            } else
            {
                progressMsg = progress + "%";
            }
            // 根据当前状态，设置显示
            switch (bean.getFlag())
            {
                case PAUSE:
                    progressMsg += "【等待下载】";
                    break;
                case DOWNLOADING:
                    progressMsg += "【正在下载】";
                    break;
                default:
                    break;
            }
            holder.progress.setText(progressMsg);
            return convertView;
        }

        private class ViewHolder
        {
            TextView cityName;
            TextView progress;

        }
    }

    public static  String getSdCacheDir(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            java.io.File fExternalStorageDirectory = Environment
                    .getExternalStorageDirectory();
            java.io.File autonaviDir = new java.io.File(
                    fExternalStorageDirectory, "amapsdk");
            boolean result = false;
            if (!autonaviDir.exists()) {
                result = autonaviDir.mkdir();
            }
            java.io.File minimapDir = new java.io.File(autonaviDir,
                    "offlineMap");
            if (!minimapDir.exists()) {
                result = minimapDir.mkdir();
            }
            return minimapDir.toString() + "/";
        } else {
            return "";
        }
    }
}
