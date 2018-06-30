package com.android.csiapp.Crime.createscene;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.android.csiapp.Crime.utils.CreateSceneUtils;
import com.android.csiapp.Crime.utils.DateTimePicker;
import com.android.csiapp.Crime.utils.DictionaryInfo;
import com.android.csiapp.Crime.utils.PriviewPhotoActivity;
import com.android.csiapp.Databases.CrimeItem;
import com.android.csiapp.Databases.CrimeProvider;
import com.android.csiapp.Databases.PhotoItem;
import com.android.csiapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateScene_FP3_PositionInformationActivity extends AppCompatActivity {

    private Context context = null;
    private CrimeItem mItem;
    private PhotoItem mPositionItem;
    private String mAdd;
    private ImageView mNew_Position;
    private TableLayout mTablePosition1, mTablePosition2, mTableFlat;
    private TextView mIncidentTime, mIncidentLocation, mCreateUnit, mCreatePeople, mCreateTime;

    private String gpsLat = "", gpsLon = "";

    private String path,mfile;
    private int mResultCode;
    private Intent mResultData;
    private static final int SCREENSHOTS_STOP_DELAY = 100;
    private static final int MAX_IMAGES = 5;
    private boolean mIsToastFirst = true;
    private int mCount = 0;
    private int mSaveCount = 0;
    private Object mLock = new Object();
    private int mIndex = 0;
    private int mDensityDpi;
    private int mWidth;
    private int mHeight;
    private int mRemoveTopHeight;
    private int mRemoveBottomHeight;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private ImageReader mImageReader;
    private Image mImage;
    private MediaProjectionManager mMediaProjectionManager;
    private static final int REQUEST_MEDIA_PROJECTION = 2;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //Log.d("Anita","mHandler");
            super.handleMessage(msg);
        }
    };

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_camera:
                    if(mAdd.equalsIgnoreCase("Position")) {
                        Intent it = new Intent(CreateScene_FP3_PositionInformationActivity.this, CreateScene_FP3_NewPositionActivity_Amap.class);
                        startActivityForResult(it, CreateSceneUtils.REQUEST_POSITION);
                    }else{
                        //修改cad程序=============================
                        //使用当前时间作为文件名
                        Intent myIntent = new Intent(CreateScene_FP3_PositionInformationActivity.this, MxCADAppActivity.class);
                        String p=GetFlatDir();
                        myIntent.putExtra("file",mfile);
                        myIntent.putExtra("path",p);
                        startActivityForResult(myIntent, CreateSceneUtils.REQUEST_FLAT);
                    }
                    break;
                case R.id.action_click:
                    createVirtualEnvironment();
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    private void onSave(){
        //String path = ScreenShot.shoot(CreateScene_FP3_PositionInformationActivity.this);
        mPositionItem.setPhotoPath(path);
        mPositionItem.setUuid(CrimeProvider.getUUID());
        Intent result = getIntent();
        result.putExtra("com.android.csiapp.Databases.PhotoItem", mPositionItem);
        result.putExtra("gpsLat", gpsLat);
        result.putExtra("gpsLon", gpsLon);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_scene_fp3_position_information);

        context = this.getApplicationContext();
        mItem = (CrimeItem) getIntent().getSerializableExtra("com.android.csiapp.Databases.CrimeItem");
        mAdd = (String) getIntent().getStringExtra("Add");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String title = (IsPositionInformation())
                ?context.getResources().getString(R.string.title_activity_position_information)
                :context.getResources().getString(R.string.title_activity_flat_information);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(context.getResources().getColor(R.color.titleBar));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back_mini);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        initView();

        mPositionItem = new PhotoItem();
        if(IsPositionInformation()) {
            Intent it = new Intent(CreateScene_FP3_PositionInformationActivity.this, CreateScene_FP3_NewPositionActivity_Amap.class);
            startActivityForResult(it, CreateSceneUtils.REQUEST_POSITION);
        }else if(!IsPositionInformation() && mAdd.equals("Flat")){
            //调用cad程序=============================
            //使用当前时间作为文件名
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String date = sDateFormat.format(new java.util.Date());
            Intent myIntent = new Intent(CreateScene_FP3_PositionInformationActivity.this, MxCADAppActivity.class);
            String p=GetFlatDir();
            mfile=p+File.separator+"Flat_"+ date;
            myIntent.putExtra("file",mfile);
            //定义最终保存的文件名
            path=p+File.separator+"Flat_"+ date+".jpg";
            myIntent.putExtra("path",p);
            startActivityForResult(myIntent, CreateSceneUtils.REQUEST_FLAT);
            //Intent it = new Intent(CreateScene_FP3_PositionInformationActivity.this, CreateScene_FP3_NewFlatActivity.class);
            //startActivityForResult(it, CreateSceneUtils.REQUEST_FLAT);
        }
        else if(!IsPositionInformation() && mAdd.equals("EditFlat")){
            //修改cad程序=============================
            //使用当前时间作为文件名
            Intent myIntent = new Intent(CreateScene_FP3_PositionInformationActivity.this, MxCADAppActivity.class);
            String p=GetFlatDir();
            mfile=getIntent().getStringExtra("file");
            myIntent.putExtra("file",mfile);
            path=getIntent().getStringExtra("file")+".jpg";
            myIntent.putExtra("path",p);
            startActivityForResult(myIntent, CreateSceneUtils.REQUEST_FLAT);
        }
    }
    private String GetFlatDir(){
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "FlatPic");
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String dirMxDraw = mediaStorageDir.getPath();
        return dirMxDraw;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_fp3_position_information, menu);
        return true;
    }

    private void initView(){
        mNew_Position = (ImageView) findViewById(R.id.new_position);
        mNew_Position.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(CreateScene_FP3_PositionInformationActivity.this, PriviewPhotoActivity.class);
                it.putExtra("Path",mPositionItem.getPhotoPath());
                startActivity(it);
            }
        });

        mTablePosition1 = (TableLayout) findViewById(R.id.table_position1);
        mTablePosition2 = (TableLayout) findViewById(R.id.table_position2);
        mTableFlat = (TableLayout) findViewById(R.id.table_flat);

        if(IsPositionInformation()) {
            mTablePosition1.setVisibility(View.VISIBLE);
            mTablePosition2.setVisibility(View.VISIBLE);
        }else if(!IsPositionInformation()) {
            mTableFlat.setVisibility(View.VISIBLE);
        }

        mIncidentTime = (TextView) findViewById(R.id.incident_time);
        mIncidentLocation = (TextView) findViewById(R.id.incident_location);
        mCreateUnit = (TextView) findViewById(R.id.create_unit);
        mCreatePeople = (TextView) findViewById(R.id.create_people);
        mCreateTime = (TextView) findViewById(R.id.create_time);
        getInformation();
    }

    private void setPhoto(String path){
        Bitmap Bitmap = BitmapFactory.decodeFile(path);
        mNew_Position.setImageBitmap(Bitmap);
        mNew_Position.setVisibility(View.VISIBLE);
    }

    private void getInformation(){
        long time = mItem.getOccurredStartTime();
        mIncidentTime.setText(DateTimePicker.getCurrentDate(time));
        mIncidentLocation.setText(mItem.getLocation());
        mCreateUnit.setText(DictionaryInfo.getDictValue(DictionaryInfo.mAreaKey,mItem.getArea()));
        SharedPreferences prefs = context.getSharedPreferences("UserName", 0);
        mCreatePeople.setText(prefs.getString("username", ""));
        mCreateTime.setText(DateTimePicker.getCurrentDate(Calendar.getInstance().getTimeInMillis()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == CreateSceneUtils.REQUEST_POSITION) {
                //Add lat and lon
                gpsLat = (String) data.getStringExtra("gpsLat");
                gpsLon = (String) data.getStringExtra("gpsLon");

                mPositionItem.setPhotoPath(data.getStringExtra("Map_ScreenShot"));
                setPhoto(mPositionItem.getPhotoPath());
            } else if(requestCode == CreateSceneUtils.REQUEST_FLAT) {
                int isSaved=data.getIntExtra("isSaved",2);
                if(isSaved==1) {
                    mPositionItem.setPhotoPath(data.getStringExtra("Map_ScreenShot"));
                    setPhoto(mPositionItem.getPhotoPath());
                }
                else{
                    setResult(Activity.RESULT_CANCELED, data);
                    finish();
                }
            } else if (requestCode == REQUEST_MEDIA_PROJECTION) {
                mResultCode = resultCode;
                mResultData = data;

                setUpMediaProjection();
                setUpImageReader();
                setImageReaderListener();
                setUpVirtualDisplay();
            }
        }
    }

    private boolean IsPositionInformation(){
        return mAdd.equalsIgnoreCase("Position");
    }

    private void createVirtualEnvironment() {
        File mediaStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Report");
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return;
            }
        }
        mMediaProjectionManager = (MediaProjectionManager) this.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if(path==null || "".equals(path)) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            path = new File(mediaStorageDir.getPath() + File.separator + "POSITION_" + timeStamp + ".jpg").toString();
        }
        mDensityDpi = getResources().getDisplayMetrics().densityDpi;
        Display display = getWindowManager().getDefaultDisplay();

        int toolsBarHeight = (int) getResources().getDimension(R.dimen.toolbar_size);
        int statusBarHeight = CreateSceneUtils.getStatusHeight(context);
        mRemoveTopHeight = toolsBarHeight+statusBarHeight;
        mRemoveBottomHeight = CreateSceneUtils.getBottomStatusHeight(context);

        //Todo : need to check Virtual height
        mRemoveBottomHeight = mRemoveBottomHeight==0?100:mRemoveBottomHeight;

        //Log.d("Anita","toolsBarHeight ="+toolsBarHeight+", statusBarHeight ="+statusBarHeight+", mRemoveTopHeight = "+mRemoveTopHeight);
        //Log.d("Anita","mRemoveBottomHeight ="+mRemoveBottomHeight);
        Point point = new Point();
        display.getSize(point);
        this.mWidth = point.x;
        this.mHeight = point.y;

        startScreenCapture();
    }

    private void startScreenCapture(){
        if (mMediaProjection != null) {
            setUpVirtualDisplay();
        } else if (mResultCode != 0 && mResultData != null) {
            setUpMediaProjection();
            setUpVirtualDisplay();
        } else {
            startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
        }
    }

    private void setUpVirtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture",
                mWidth, mHeight, mDensityDpi, 9, mImageReader.getSurface(), null, mHandler);
    }

    private void setUpMediaProjection() {
        mMediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, mResultData);
    }

    private void setUpImageReader(){
        mIsToastFirst = true;
        mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, MAX_IMAGES);
    }

    private void setImageReaderListener(){
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                try {
                    if(mCount >= MAX_IMAGES - 1){
                        return;
                    }

                    acquireImageCount();
                    mImage = reader.acquireLatestImage();
                    if(null != mImage){
                        savePicture(mIndex);
                    }else{
                        destoryImageCount();
                    }
                } catch (Exception e) {
                }

                // 結束整個界面，但是延遲一定時間停止截屏，以便截屏時去掉系統彈出框
                finishScreenShots();
            }
        }, mHandler);
    }

    private void finishScreenShots(){
        if(mIsToastFirst){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopScreenCapture();
                    tearDownMediaProjection();
                }
            }, SCREENSHOTS_STOP_DELAY);
            mIsToastFirst = false;
        }
        //finish();
    }

    private void acquireImageCount(){
        synchronized (mLock) {
            mCount ++;
            mIndex ++;
            mSaveCount ++;
        }
    }

    private void destoryImageCount(){
        synchronized (mLock) {
            mCount --;
        }
    }

    private void savePicture(int index){
        new Thread(new SavePictureTask(index,mImage, path)).start();
    }

    private class SavePictureTask implements Runnable {

        private int index;
        private Image image;
        private String path;

        public SavePictureTask(int index, Image image, String path){
            this.index = index;
            this.image = image;
            this.path = path;
        }

        @Override
        public void run() {
            if(image == null){
                return;
            }
            Bitmap bitmap = null;
            try {
                Image.Plane[] planes = image.getPlanes();
                if(planes == null || planes.length < 1){
                    return;
                }

                Image.Plane plane = planes[0];
                if(plane == null){
                    return;
                }

                ByteBuffer buffer = plane.getBuffer();
                int pixelStride = plane.getPixelStride();
                int rowStride = plane.getRowStride();
                int rowPadding = rowStride - pixelStride * mWidth;

                //Log.d("Anita","width = "+mWidth+rowPadding/pixelStride+", height = "+mHeight);
                //移除虛擬快捷鍵高度
                Bitmap b = Bitmap.createBitmap(mWidth+rowPadding/pixelStride, mHeight-mRemoveBottomHeight, Bitmap.Config.ARGB_8888);
                //Log.d("Anita","b width = "+b.getWidth()+", height = "+b.getHeight()+", mRemoveTopHeight = "+mRemoveTopHeight);
                b.copyPixelsFromBuffer(buffer);
                buffer.position(0);
                if(image != null){
                    image.close();
                    image = null;
                    destoryImageCount();
                }
                //移除狀態欄與title高度
                bitmap = Bitmap.createBitmap(b, 0, mRemoveTopHeight, b.getWidth(), b.getHeight()-mRemoveTopHeight);
                //Log.d("Anita","bitmap width = "+bitmap.getWidth()+", height = "+bitmap.getHeight());
            } catch (Exception e) {
            } catch (OutOfMemoryError e){
            }

            FileOutputStream out = null;
            try {
                if(bitmap == null || bitmap.isRecycled()){
                    return;
                }
                File file = new File(path);
                if(!file.exists() || file.delete()){
                    file.createNewFile();
                    out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                }
            } catch (Exception e) {
            } catch (OutOfMemoryError e){
            } finally {
                if(out != null){
                    try {
                        out.close();
                    } catch (IOException e) {
                    }
                }
                if(bitmap != null && !bitmap.isRecycled()){
                    bitmap.recycle();
                }
                mSaveCount--;
                if(mSaveCount==0) onSave();
            }
        }
    }

    private void stopScreenCapture(){
        if(mVirtualDisplay == null){
            return ;
        }
        mVirtualDisplay.release();
        mVirtualDisplay = null;
    }

    private void tearDownMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }
}
