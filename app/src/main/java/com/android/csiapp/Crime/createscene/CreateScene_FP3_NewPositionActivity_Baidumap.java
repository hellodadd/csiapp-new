package com.android.csiapp.Crime.createscene;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.csiapp.R;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateScene_FP3_NewPositionActivity_Baidumap extends AppCompatActivity {
    private Context context = null;

    private File mediaStorageDir;
    private String mFilepath;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private MyLocationData mLocData;
    private LocationClient mLocationClient;
    private MyLocationConfiguration mConfig;
    private BitmapDescriptor mCurrentMarker;
    private boolean mFirstLocation;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:
                    Log.d("BaiduMap","filepath3: " + msg.obj.toString());
                    Intent result = getIntent().putExtra("Map_ScreenShot", msg.obj.toString());
                    setResult(Activity.RESULT_OK, result);
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_camera:
                    mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                        @Override
                        public void onSnapshotReady(Bitmap bitmap) {
                            mediaStorageDir = new File( context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "BaiduMap");
                            Log.d("baidumapdemo","onSnapshotReady");
                            if(!mediaStorageDir.exists()){
                                if(!mediaStorageDir.mkdirs()) {
                                    Log.d("baidumapdemo","Failed to create directory");
                                    return;
                                }
                            }
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                            BitmapFactory.Options option = new BitmapFactory.Options();
                            // Bitmap sampling factor, size = (Original Size)/(inSampleSize)
                            option.inSampleSize = 4;

                            try {
                                String path = mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg";
                                mFilepath = path;
                                Log.d("BaiduMap","filepath1: " + mFilepath);
                                File mediaFile = new File(path);
                                FileOutputStream out = new FileOutputStream(mediaFile);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); //100-best quality
                                out.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.d("BaiduMap","filepath2: " + mFilepath);
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = mFilepath;
                            mHandler.sendMessage(msg);
                            //Intent result = new Intent(Create_FP3_NewPosition_Activity.this,Create_FragmentPage3.class);
                            //result.putExtra("BaiduMap_ScreenShot", filepath);
                            //setResult(Activity.RESULT_OK, result);
                        }
                    });
                    //Log.d("BaiduMap","filepath2: " + filepath);
                    //Intent result = getIntent().putExtra("BaiduMap_ScreenShot", filepath);
                    //setResult(Activity.RESULT_OK, result);
                    Toast.makeText(CreateScene_FP3_NewPositionActivity_Baidumap.this, "Screen Shot", Toast.LENGTH_SHORT).show();
                    //finish();
                    break;
                /*case R.id.action_download_map:
                    Intent intent = new Intent(CreateScene_FP3_NewPositionActivity_Baidumap.this,
                            OfflineMapActivity.class);
                    startActivity(intent);
                    break;*/
            }
            //finish();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.create_scene_fp3_new_position_baidumap);
        context = this.getApplicationContext();

        // Baidu Map Initialization
        mMapView =(MapView)findViewById(R.id.baiDuMv);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18f);
        mBaiduMap.setMapStatus(msu);

        // 定位初始化
        mLocationClient = new LocationClient(this);
        mFirstLocation =true;

        // 设置定位的相关配置
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setCoorType("bd09ll"); // 设置?��?类�?
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);

        // 设置自定义图标
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.location);

        mConfig = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true, mCurrentMarker);
        mBaiduMap.setMyLocationConfigeration(mConfig);

        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                Log.d("baidumapdemo","onReceiveLocation1");
                // map view 销毁后不在处理新接收的位置
                Log.d("baidumapdemo","onReceiveLocation2");
                if (location == null || mMapView == null) {
                    return;
                }

                // 构造定位数据
                mLocData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(0).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();

                // 设置定位数据
                mBaiduMap.setMyLocationData(mLocData);

                // 第一次定位时，将地图位置移动到当前位置
                if (mFirstLocation) {
                    mFirstLocation = false;
                    LatLng xy = new LatLng(location.getLatitude(), location.getLongitude());;

/*
                    // Add Marker to Baidu Map
                    OverlayOptions options = new MarkerOptions().position(xy).icon(mCurrentMarker);
                    mBaiduMap.addOverlay(options);
*/

                    MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(xy);
                    mBaiduMap.animateMapStatus(status);
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(context.getResources().getString(R.string.title_activity_new_position));
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

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_fp3_new_position, menu);
        return true;
    }

    @Override
    protected void onStart() {
        // 如果要显示位置图标,必须先开启图层定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
        {
            Log.d("BaiduMap","onReceiveLocation");
            mLocationClient.start();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        // 关闭图层定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()
        mMapView.onDestroy();
        mMapView = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()
        mMapView.onPause();
    }

    public void onClick_myLocation(View view) {
        mLocationClient.requestLocation();
    }
}
