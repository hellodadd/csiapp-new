package com.android.csiapp.Crime.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.csiapp.Crime.utils.paint.PaintView;
import com.android.csiapp.Crime.utils.paint.PaintViewCallBack;
import com.android.csiapp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HandWriteActivity extends AppCompatActivity {

    private Context context = null;
    @Bind(R.id.view)
    PaintView mPathView;
    @Bind(R.id.clear1)
    Button mClear;
    @Bind(R.id.ll)
    LinearLayout ll;

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_click:
                    if (mPathView.getTouched()) {
                        try {
                            File mediaStorageDir = new File( context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Report");
                            // This location works best if you want the created images to be shared
                            // between applications and persist after your app has been uninstalled.
                            // Create the storage directory if it does not exist
                            if (!mediaStorageDir.exists()){
                                if (!mediaStorageDir.mkdirs()){
                                    finish();
                                }
                            }
                            // Create a media file name
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                            mPathView.save(mediaStorageDir.getPath() + File.separator + "SIGN_"+ timeStamp + ".jpg", 10);
                            String path = mediaStorageDir.getPath() + File.separator + "SIGN_"+ timeStamp + ".jpg";
                            Intent result = getIntent().putExtra("SIGN", path);
                            setResult(Activity.RESULT_OK, result);
                            finish();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(HandWriteActivity.this, "您没有签名~", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hand_write);

        context = this.getApplicationContext();

        ButterKnife.bind(this);
        setResult(50);
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPathView.clearAll(true);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("签名");
        toolbar.setTitleTextColor(context.getResources().getColor(R.color.titleBar));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back_mini);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                BackAlertDialog dialog = new BackAlertDialog(HandWriteActivity.this);
                dialog.onCreateDialog(false,null);
                dialog.setOwnerActivity(HandWriteActivity.this);
            }
        });
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        initCallBack();
    }

    private void initCallBack() {
        mPathView.setCallBack(new PaintViewCallBack() {
            // 当画了之后对Button进行更新
            @Override
            public void onHasDraw() {
            }

            // 当点击之后让各个弹出的窗口都消失
            @Override
            public void onTouchDown() {
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_fp8_subactivity, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
