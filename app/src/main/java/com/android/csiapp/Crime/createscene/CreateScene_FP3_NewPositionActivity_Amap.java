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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.api.maps.offlinemap.OfflineMapManager.OfflineMapDownloadListener;
import com.amap.api.maps.offlinemap.OfflineMapStatus;
import com.android.csiapp.Crime.utils.OfflineMapActivity;
import com.android.csiapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateScene_FP3_NewPositionActivity_Amap extends AppCompatActivity implements OfflineMapDownloadListener, LocationSource, AMapLocationListener {
    private Context context = null;

    private double gpsLat = 0, gpsLon = 0;

    private File mediaStorageDir;
    private String mFilepath;
    private MapView mMapView;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener;
    public AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mLocationClient = null;
    private LatLng mLocalLatlng;
    private OfflineMapManager amapManager = null;
    private boolean isFirstLoc = true;
    private boolean isFirstFail = true;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:
                    Log.d("aMap","filepath3: " + msg.obj.toString());
                    Intent result = getIntent().putExtra("Map_ScreenShot", msg.obj.toString());
                    result.putExtra("gpsLat", String.valueOf(gpsLat));
                    result.putExtra("gpsLon", String.valueOf(gpsLon));
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
                    aMap.getMapScreenShot(new AMap.OnMapScreenShotListener() {
                        @Override
                        public void onMapScreenShot(Bitmap bitmap) {

                        }

                        @Override
                        public void onMapScreenShot(Bitmap bitmap, int status) {
                            mediaStorageDir = new File( context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "aMap");
                            Log.d("aMap","onSnapshotReady");
                            if(!mediaStorageDir.exists()){
                                if(!mediaStorageDir.mkdirs()) {
                                    Log.d("aMap","Failed to create directory");
                                    return;
                                }
                            }
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                            BitmapFactory.Options option = new BitmapFactory.Options();
                            // Bitmap sampling factor, size = (Original Size)/(inSampleSize)
                            option.inSampleSize = 4;
                            if(null == bitmap){
                                return;
                            }
                            try {
                                String path = mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg";
                                mFilepath = path;
                                Log.d("aMap","filepath1: " + mFilepath);
                                File mediaFile = new File(path);
                                FileOutputStream out = new FileOutputStream(mediaFile);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); //100-best quality
                                out.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.d("aMap","filepath2: " + mFilepath);
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = mFilepath;
                            mHandler.sendMessage(msg);
                        }
                    });
                    break;
                /*case R.id.action_download_map:
                    break;*/
            }
            //finish();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_scene_fp3_new_position_amap);
        context = this.getApplicationContext();

        MapsInitializer.sdcardDir = OfflineMapActivity.getSdCacheDir(this);
        MapsInitializer.setNetWorkEnable(false);
        mMapView = (MapView) findViewById(R.id.amap);
        mMapView.onCreate(savedInstanceState);
        init();

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

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            MyLocationStyle myLocationStyle;
            myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location));
//            myLocationStyle.interval(3000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//            myLocationStyle.showMyLocation(false);//不显示定位蓝点
            aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
            aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

            UiSettings settings = aMap.getUiSettings();
            aMap.setLocationSource(this);
            settings.setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);
        }

        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        location();
    }

    private void location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(8000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

        amapManager = new OfflineMapManager(this, this);

        try {
            Log.d("ddd","download");
            //amapManager.downloadByCityName("北京市");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mLocationClient.stopLocation();//停止定位
        mLocationClient.onDestroy();//销毁定位客户端。
    }

    //激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        Toast.makeText(CreateScene_FP3_NewPositionActivity_Amap.this, "再次定位...", Toast.LENGTH_SHORT).show();
        isFirstFail = true;
        mListener = onLocationChangedListener;
    }

    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
    }

    /**
     * 地图视图改变监听
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                gpsLat = aMapLocation.getLatitude();//获取纬度
                gpsLon = aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //将地图移动到定位点
                    LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                    //设置缩放级别
                    //aMap.moveCamera(CameraUpdateFactory.zoomTo(19f));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(aMapLocation);
                    ///添加图钉
                    aMap.addMarker(new MarkerOptions()
                            .anchor(0.5f, 0.5f)//设置锚点
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.location)));
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(aMapLocation.getCountry() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getCity() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getDistrict() + ""
                            + aMapLocation.getStreet() + ""
                            + aMapLocation.getStreetNum());
                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    isFirstLoc = false;
                    Toast.makeText(CreateScene_FP3_NewPositionActivity_Amap.this,"定位成功", Toast.LENGTH_SHORT).show();
                    isFirstFail = true;
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                if(isFirstFail) {
                    //Toast.makeText(CreateScene_FP3_NewPositionActivity_Amap.this, "定位失败", Toast.LENGTH_SHORT).show();
                    isFirstFail = false;
                }
            }
        }
    }

    @Override
    public void onCheckUpdate(boolean hasNew, String name) {
        // TODO Auto-generated method stub
        Log.i("aMap", "onCheckUpdate " + name + " : " + hasNew);
        //Message message = new Message();
        //message.what = SHOW_MSG;
        //message.obj = "CheckUpdate " + name + " : " + hasNew;
        //handler.sendMessage(message);
    }

    @Override
    public void onRemove(boolean success, String name, String describe) {
        // TODO Auto-generated method stub
        Log.i("aMap", "onRemove " + name + " : " + success + " , "
                + describe);
        //handler.sendEmptyMessage(UPDATE_LIST);
        //Message message = new Message();
        //message.what = SHOW_MSG;
        //message.obj = "onRemove " + name + " : " + success + " , " + describe;
        //handler.sendMessage(message);
    }

    @Override
    public void onDownload(int status, int completeCode, String downName) {
        switch (status) {
            case OfflineMapStatus.SUCCESS:
                // changeOfflineMapTitle(OfflineMapStatus.SUCCESS, downName);
                break;
            case OfflineMapStatus.LOADING:
                Log.d("amap-download", "download: " + completeCode + "%" + ","
                        + downName);
                // changeOfflineMapTitle(OfflineMapStatus.LOADING, downName);
                break;
            case OfflineMapStatus.UNZIP:
                Log.d("amap-unzip", "unzip: " + completeCode + "%" + "," + downName);
                // changeOfflineMapTitle(OfflineMapStatus.UNZIP);
                // changeOfflineMapTitle(OfflineMapStatus.UNZIP, downName);
                break;
            case OfflineMapStatus.WAITING:
                Log.d("amap-waiting", "WAITING: " + completeCode + "%" + ","
                        + downName);
                break;
            case OfflineMapStatus.PAUSE:
                Log.d("amap-pause", "pause: " + completeCode + "%" + "," + downName);
                break;
            case OfflineMapStatus.STOP:
                break;
            case OfflineMapStatus.ERROR:
                Log.e("amap-download", "download: " + " ERROR " + downName);
                break;
            case OfflineMapStatus.EXCEPTION_AMAP:
                Log.e("amap-download", "download: " + " EXCEPTION_AMAP " + downName);
                break;
            case OfflineMapStatus.EXCEPTION_NETWORK_LOADING:
                Log.e("amap-download", "download: " + " EXCEPTION_NETWORK_LOADING "
                        + downName);
                //Toast.makeText(OfflineMapActivity.this, "网络异常", Toast.LENGTH_SHORT)
                //        .show();
                //amapManager.pause();
                break;
            case OfflineMapStatus.EXCEPTION_SDCARD:
                Log.e("amap-download", "download: " + " EXCEPTION_SDCARD "
                        + downName);
                break;
            default:
                break;
        }
        // changeOfflineMapTitle(status, downName);
        //handler.sendEmptyMessage(UPDATE_LIST);
    }
}