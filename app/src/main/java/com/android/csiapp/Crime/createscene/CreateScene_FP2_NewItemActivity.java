package com.android.csiapp.Crime.createscene;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.csiapp.Crime.utils.BackAlertDialog;
import com.android.csiapp.Crime.utils.ClearableEditText;
import com.android.csiapp.Crime.utils.SaveAlertDialog;
import com.android.csiapp.Databases.CrimeProvider;
import com.android.csiapp.Databases.LostItem;
import com.android.csiapp.R;

public class CreateScene_FP2_NewItemActivity extends AppCompatActivity {

    private Context context = null;
    private LostItem mLostItem;
    private int mEvent;
    private int mPosition;

    private ClearableEditText mName, mBrand, mAmount, mValue, mFeature;

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_click:
                    saveData();
                    Intent result = getIntent();
                    result.putExtra("com.android.csiapp.Databases.LostItem", mLostItem);
                    result.putExtra("Event", mEvent);
                    result.putExtra("Posiotion", mPosition);
                    if(mLostItem.checkInformation()){
                        setResult(Activity.RESULT_OK, result);
                        finish();
                    }else {
                        SaveAlertDialog dialog = new SaveAlertDialog(CreateScene_FP2_NewItemActivity.this);
                        dialog.onCreateDialog(result,false,null);
                        dialog.setOwnerActivity(CreateScene_FP2_NewItemActivity.this);
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
        setContentView(R.layout.create_scene_fp2_new_item);

        context = this.getApplicationContext();
        mLostItem = (LostItem) getIntent().getSerializableExtra("com.android.csiapp.Databases.LostItem");
        mEvent = (int) getIntent().getIntExtra("Event", 1);
        mPosition = (int) getIntent().getIntExtra("Position", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(context.getResources().getString(R.string.title_activity_lostitem));
        toolbar.setTitleTextColor(context.getResources().getColor(R.color.titleBar));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back_mini);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                BackAlertDialog dialog = new BackAlertDialog(CreateScene_FP2_NewItemActivity.this);
                dialog.onCreateDialog(false,null);
                dialog.setOwnerActivity(CreateScene_FP2_NewItemActivity.this);
            }
        });
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        initView();
        initData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            //What to do on back clicked
            BackAlertDialog dialog = new BackAlertDialog(CreateScene_FP2_NewItemActivity.this);
            dialog.onCreateDialog(false,null);
            dialog.setOwnerActivity(CreateScene_FP2_NewItemActivity.this);
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_fp2_subactivity, menu);
        return true;
    }

    private void initView(){
        mName = (ClearableEditText) findViewById(R.id.item_name_editView);
        mBrand = (ClearableEditText) findViewById(R.id.brand_model_editView);
        mAmount = (ClearableEditText) findViewById(R.id.amount_editView);
        mValue = (ClearableEditText) findViewById(R.id.value_editView);
        mFeature = (ClearableEditText) findViewById(R.id.feature_description_editView);
        mAmount.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        mValue.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
    }

    private void initData(){
        mName.setText(mLostItem.getItemName());
        mBrand.setText(mLostItem.getItemBrand());
        mAmount.setText(mLostItem.getItemAmount());
        mValue.setText(mLostItem.getItemValue());
        mFeature.setText(mLostItem.getItemFeature());
    }

    private void saveData(){
        mLostItem.setItemName(mName.getText());
        mLostItem.setItemBrand(mBrand.getText());
        mLostItem.setItemAmount(mAmount.getText());
        mLostItem.setItemValue(mValue.getText());
        mLostItem.setItemFeatue(mFeature.getText());
        mLostItem.setUuid(CrimeProvider.getUUID());
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }
}
