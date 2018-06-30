package com.android.csiapp.Crime.createscene;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.csiapp.Crime.utils.BackAlertDialog;
import com.android.csiapp.Crime.utils.ClearableEditText;
import com.android.csiapp.Crime.utils.CreateSceneUtils;
import com.android.csiapp.Crime.utils.DictionaryInfo;
import com.android.csiapp.Crime.utils.SaveAlertDialog;
import com.android.csiapp.Crime.utils.tree.TreeViewListActivity;
import com.android.csiapp.Databases.CrimeProvider;
import com.android.csiapp.Databases.CrimeToolItem;
import com.android.csiapp.R;

public class CreateScene_FP2_NewToolActivity extends AppCompatActivity {

    private Context context = null;
    private CrimeToolItem mCrimeToolItem;
    private int mEvent;
    private int mPosition;

    private ClearableEditText mName;

    private TextView mToolCategoryText, mToolSourceText;

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_click:
                    saveData();
                    Intent result = getIntent();
                    result.putExtra("com.android.csiapp.Databases.CrimeToolItem", mCrimeToolItem);
                    result.putExtra("Event", mEvent);
                    result.putExtra("Posiotion", mPosition);
                    if(mCrimeToolItem.checkInformation()){
                        setResult(Activity.RESULT_OK, result);
                        finish();
                    }else {
                        SaveAlertDialog dialog = new SaveAlertDialog(CreateScene_FP2_NewToolActivity.this);
                        dialog.onCreateDialog(result,false,null);
                        dialog.setOwnerActivity(CreateScene_FP2_NewToolActivity.this);
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
        setContentView(R.layout.create_scene_fp2_new_tool);

        context = this.getApplicationContext();
        mCrimeToolItem = (CrimeToolItem) getIntent().getSerializableExtra("com.android.csiapp.Databases.CrimeToolItem");
        mEvent = (int) getIntent().getIntExtra("Event", 1);
        mPosition = (int) getIntent().getIntExtra("Position", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(context.getResources().getString(R.string.title_activity_crimetool));
        toolbar.setTitleTextColor(context.getResources().getColor(R.color.titleBar));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back_mini);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                BackAlertDialog dialog = new BackAlertDialog(CreateScene_FP2_NewToolActivity.this);
                dialog.onCreateDialog(false,null);
                dialog.setOwnerActivity(CreateScene_FP2_NewToolActivity.this);
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
            BackAlertDialog dialog = new BackAlertDialog(CreateScene_FP2_NewToolActivity.this);
            dialog.onCreateDialog(false,null);
            dialog.setOwnerActivity(CreateScene_FP2_NewToolActivity.this);
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_fp2_subactivity, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = "";
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == CreateSceneUtils.EVENT_TOOL_CATEGORY_SELECT_ITEM){
                result = (String) data.getStringExtra("Select");
                mCrimeToolItem.setToolCategory(result);
                result = DictionaryInfo.getDictValue(DictionaryInfo.mToolCategoryKey, result);
                mToolCategoryText.setText(result);
            }else if(requestCode == CreateSceneUtils.EVENT_TOOL_SOURCE_SELECT_ITEM){
                result = (String) data.getStringExtra("Select");
                mCrimeToolItem.setToolSource(result);
                result = DictionaryInfo.getDictValue(DictionaryInfo.mToolSourceKey, result);
                mToolSourceText.setText(result);
            }
        }
    }

    private void initView(){
        mName = (ClearableEditText) findViewById(R.id.tool_name_editView);
        mToolCategoryText = (TextView) findViewById(R.id.toolCategory);
        mToolCategoryText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent it = new Intent(CreateScene_FP2_NewToolActivity.this, TreeViewListActivity.class);
                it.putExtra("Key",DictionaryInfo.mToolCategoryKey);
                it.putExtra("Selected", mCrimeToolItem.getToolCategory());
                startActivityForResult(it, CreateSceneUtils.EVENT_TOOL_CATEGORY_SELECT_ITEM);
            }
        });

        mToolSourceText = (TextView) findViewById(R.id.toolSource);
        mToolSourceText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent it = new Intent(CreateScene_FP2_NewToolActivity.this, TreeViewListActivity.class);
                it.putExtra("Key",DictionaryInfo.mToolSourceKey);
                it.putExtra("Selected", mCrimeToolItem.getToolSource());
                startActivityForResult(it, CreateSceneUtils.EVENT_TOOL_SOURCE_SELECT_ITEM);
            }
        });
    }

    private void initData(){
        mName.setText(mCrimeToolItem.getToolName());
        mToolCategoryText.setText(DictionaryInfo.getDictValue(DictionaryInfo.mToolCategoryKey, mCrimeToolItem.getToolCategory()));
        mToolSourceText.setText(DictionaryInfo.getDictValue(DictionaryInfo.mToolSourceKey, mCrimeToolItem.getToolSource()));
    }

    private void saveData(){
        mCrimeToolItem.setToolName(mName.getText());
        mCrimeToolItem.setUuid(CrimeProvider.getUUID());
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        releaseFocusEditText();
    }

    private void releaseFocusEditText(){
        mName.clearFocus();
    }
}
