package com.android.csiapp.Crime.createscene;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.csiapp.Crime.utils.BackAlertDialog;
import com.android.csiapp.Crime.utils.ClearableEditText;
import com.android.csiapp.Crime.utils.CreateSceneUtils;
import com.android.csiapp.Crime.utils.DateTimePicker;
import com.android.csiapp.Crime.utils.DictionaryInfo;
import com.android.csiapp.Crime.utils.ImageCompress;
import com.android.csiapp.Crime.utils.PriviewPhotoActivity;
import com.android.csiapp.Crime.utils.SaveAlertDialog;
import com.android.csiapp.Crime.utils.UserInfo;
import com.android.csiapp.Crime.utils.tree.TreeViewListActivity;
import com.android.csiapp.Databases.CrimeItem;
import com.android.csiapp.Databases.CrimeProvider;
import com.android.csiapp.Databases.EvidenceItem;
import com.android.csiapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class CreateScene_FP5_NewEvidenceActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context = null;
    private CrimeItem mItem;
    private EvidenceItem mEvidenceItem;
    private int mEvent;
    private int mPosition;
    private Uri LocalFileUri = null;


    private ImageView mNew_evidence;
    private RadioGroup mRadioFfGroup;
    private ArrayList<String> mEvidence_category = new ArrayList<String>();
    private ArrayAdapter<String> mEvidence_category_adapter;
    private TextView mEvidenceTextLabel;
    private ClearableEditText mOtherEvidence;

    private Boolean isDoubleRefresh = false;

    private TextView mEvidenceText;

    private ClearableEditText mEvidenceName, mLegacySite, mBasiceFeature;

    private ImageView mBasiceFeatureLabel;
    private LinearLayout mInferLL;
    private TextView mInferText;

    private TextView mMethodText;

    private ImageView mMethodLabel;
    private ClearableEditText mOtherMethod;

    private TextView mTime;
    private Button mTime_button;

    private TextView mGetPeopleText;

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_camera:
                    LocalFileUri = Uri.fromFile(CreateSceneUtils.getOutputMediaFile(context, CreateSceneUtils.EVENT_PHOTO_TYPE_NEW_EVIDENCE));
                    takePhoto(LocalFileUri, CreateSceneUtils.EVENT_PHOTO_TYPE_NEW_EVIDENCE);
                    break;
                case R.id.action_click:
                    saveData();
                    Intent result = getIntent();
                    if(mEvidenceItem.getPhotoPath().isEmpty()) result.putExtra("photo_uri", mEvidenceItem.getPhotoPath());
                    mEvidenceItem.setUuid(CrimeProvider.getUUID());
                    result.putExtra("com.android.csiapp.Databases.EvidenceItem", mEvidenceItem);
                    result.putExtra("Event", mEvent);
                    result.putExtra("Posiotion", mPosition);
                    if(mEvidenceItem.checkInformation()){
                        setResult(Activity.RESULT_OK, result);
                        finish();
                    }else {
                        SaveAlertDialog dialog = new SaveAlertDialog(CreateScene_FP5_NewEvidenceActivity.this);
                        dialog.onCreateDialog(result,false,null);
                        dialog.setOwnerActivity(CreateScene_FP5_NewEvidenceActivity.this);
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
        setContentView(R.layout.create_scene_fp5_new_evidence);

        context = this.getApplicationContext();
        mEvent = (int) getIntent().getIntExtra("Event", 1);
        mPosition = (int) getIntent().getIntExtra("Position", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(context.getResources().getString(R.string.title_activity_new_evidence));
        toolbar.setTitleTextColor(context.getResources().getColor(R.color.titleBar));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back_mini);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                BackAlertDialog dialog = new BackAlertDialog(CreateScene_FP5_NewEvidenceActivity.this);
                dialog.onCreateDialog(false,null);
                dialog.setOwnerActivity(CreateScene_FP5_NewEvidenceActivity.this);
            }
        });
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        initView();
        initData();

        if(mEvent == 1) {
            LocalFileUri = Uri.fromFile(CreateSceneUtils.getOutputMediaFile(context, CreateSceneUtils.EVENT_PHOTO_TYPE_NEW_EVIDENCE));
            takePhoto(LocalFileUri, CreateSceneUtils.EVENT_PHOTO_TYPE_NEW_EVIDENCE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            //What to do on back clicked
            BackAlertDialog dialog = new BackAlertDialog(CreateScene_FP5_NewEvidenceActivity.this);
            dialog.onCreateDialog(false,null);
            dialog.setOwnerActivity(CreateScene_FP5_NewEvidenceActivity.this);
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_fp5_subactivity, menu);
        return true;
    }

    private void initView(){
        mNew_evidence = (ImageView) findViewById(R.id.new_evidence);
        mNew_evidence.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(CreateScene_FP5_NewEvidenceActivity.this, PriviewPhotoActivity.class);
                it.putExtra("Path",mEvidenceItem.getPhotoPath());
                startActivity(it);
            }
        });

        mEvidence_category = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.evidence_category)));

        mRadioFfGroup=(RadioGroup)findViewById(R.id.radioGroupFfPeopleSex);
        mRadioFfGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(isDoubleRefresh) return;
                RadioButton radioButton=(RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                int position =0;
                if(radioButton.getText().toString().equals("手印")) {
                    position=0;
                }
                else if(radioButton.getText().toString().equals("足迹")){
                    position=1;
                }
                else if(radioButton.getText().toString().equals("工痕")){
                    position=2;
                }
                else{
                    position=3;
                }
                refreshEvidenceItem(position);
                mEvidenceItem.setEvidenceCategory(mEvidence_category.get(position));

            }
        });

        mEvidenceText = (TextView) findViewById(R.id.evidence);
        mEvidenceText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent it = new Intent(CreateScene_FP5_NewEvidenceActivity.this, TreeViewListActivity.class);
                it.putExtra("Key",DictionaryInfo.mEvidenceHandKey);
                it.putExtra("Selected", mEvidenceItem.getEvidence());
                startActivityForResult(it, CreateSceneUtils.EVENT_EVIDENCE_SELECT_ITEM);
            }
        });
        mEvidenceTextLabel = (TextView) findViewById(R.id.evidenceTextLabel);
        mOtherEvidence = (ClearableEditText) findViewById(R.id.other_evidence);

        mEvidenceName = (ClearableEditText) findViewById(R.id.evidence_name);
        mLegacySite = (ClearableEditText) findViewById(R.id.legacy_site);
        mBasiceFeature = (ClearableEditText) findViewById(R.id.basice_feature);

        mBasiceFeatureLabel = (ImageView) findViewById(R.id.basicFeatureLabel);
        mInferLL = (LinearLayout) findViewById(R.id.inferLL);
        mInferText = (TextView) findViewById(R.id.infer);
        mInferText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent it = new Intent(CreateScene_FP5_NewEvidenceActivity.this, TreeViewListActivity.class);
                it.putExtra("Key",DictionaryInfo.mToolInferKey);
                it.putExtra("Selected", mEvidenceItem.getInfer());
                startActivityForResult(it, CreateSceneUtils.EVENT_INFER_SELECT_ITEM);
            }
        });

        mMethodText = (TextView) findViewById(R.id.method);
        mMethodText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent it = new Intent(CreateScene_FP5_NewEvidenceActivity.this, TreeViewListActivity.class);
                it.putExtra("Key",DictionaryInfo.mMethodHandKey);
                it.putExtra("Selected", mEvidenceItem.getMethod());
                startActivityForResult(it, CreateSceneUtils.EVENT_METHOD_SELECT_ITEM);
            }
        });
        mMethodLabel = (ImageView) findViewById(R.id.methodLabel);
        mOtherMethod = (ClearableEditText) findViewById(R.id.other_method);

        mTime = (TextView) findViewById(R.id.time);
        mTime_button = (Button) findViewById(R.id.time_button);
        mTime_button.setOnClickListener(this);
        mTime.setOnClickListener(this);

        mGetPeopleText = (TextView) findViewById(R.id.getPeople);
        mGetPeopleText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent it = new Intent(CreateScene_FP5_NewEvidenceActivity.this, TreeViewListActivity.class);
                it.putExtra("Key",UserInfo.mExtractionPeople);
                it.putExtra("Selected", mEvidenceItem.getPeople());
                it.putExtra("DataInfo", "UserInfo");
                startActivityForResult(it, CreateSceneUtils.EVENT_GET_PEOPLE_SLELECT_ITEM);
            }
        });
    }

    private void initData(){
        if(mEvent == 1) {
            mEvidenceItem = new EvidenceItem();
            mItem = (CrimeItem) getIntent().getSerializableExtra("com.android.csiapp.Databases.Item");
            mEvidenceItem.setPeople(mItem.getAccessInspectors());
            mEvidenceItem.setMethod(DictionaryInfo.getDictKey(DictionaryInfo.mMethodHandKey, "直接照相"));
        }else{
            mEvidenceItem = (EvidenceItem) getIntent().getSerializableExtra("com.android.csiapp.Databases.EvidenceItem");
        }
        if(!mEvidenceItem.getPhotoPath().isEmpty()) setPhoto(mEvidenceItem.getPhotoPath());

        isDoubleRefresh = true;//不要刷新数据
        int category = getCategory(mEvidenceItem.getEvidenceCategory());
        RadioButton r0=(RadioButton)findViewById(R.id.radioFfShouyin);
        RadioButton r1=(RadioButton)findViewById(R.id.radioFfFoot);
        RadioButton r2=(RadioButton)findViewById(R.id.radioFfGongHen);
        RadioButton r3=(RadioButton)findViewById(R.id.radioFfElse);
        if(category==0) {
            r0.setChecked(true);r1.setChecked(false);r2.setChecked(false);r3.setChecked(false);
        }
        else if(category==1){
            r0.setChecked(false);r1.setChecked(true);r2.setChecked(false);r3.setChecked(false);
        }
        else if(category==2){
            r0.setChecked(false);r1.setChecked(false);r2.setChecked(true);r3.setChecked(false);
        }
        else{
            r0.setChecked(false);r1.setChecked(false);r2.setChecked(false);r3.setChecked(true);
        }
//        mEvidenceItem.setEvidenceCategory(mEvidence_category.get(position));
        isDoubleRefresh=false;

        if(category == 0 ) {
            mEvidenceText.setText(DictionaryInfo.getDictValue(DictionaryInfo.mEvidenceHandKey, mEvidenceItem.getEvidence()));
            mMethodText.setText(DictionaryInfo.getDictValue(DictionaryInfo.mMethodHandKey, mEvidenceItem.getMethod()));
        }else if(category == 1 ){
            mEvidenceText.setText(DictionaryInfo.getDictValue(DictionaryInfo.mEvidenceFootKey, mEvidenceItem.getEvidence()));
            mMethodText.setText(DictionaryInfo.getDictValue(DictionaryInfo.mMethodFootKey, mEvidenceItem.getMethod()));
        }else if(category == 2 ){
            mEvidenceText.setText(DictionaryInfo.getDictValue(DictionaryInfo.mEvidenceToolKey, mEvidenceItem.getEvidence()));
            mMethodText.setText(DictionaryInfo.getDictValue(DictionaryInfo.mMethodToolKey, mEvidenceItem.getMethod()));
        }else if(category == 3 ){
            mEvidenceText.setVisibility(View.GONE);
            mMethodText.setVisibility(View.GONE);
            mOtherEvidence.setVisibility(View.VISIBLE);
            mOtherMethod.setVisibility(View.VISIBLE);
            mOtherEvidence.setText(mEvidenceItem.getEvidence());
            mOtherMethod.setText(mEvidenceItem.getMethod());
        }
        mEvidenceName.setText(mEvidenceItem.getEvidenceName());

        mLegacySite.setText(mEvidenceItem.getLegacySite());
        mBasiceFeature.setText(mEvidenceItem.getBasiceFeature());
        mInferText.setText(mEvidenceItem.getInfer());
        mTime.setText(DateTimePicker.getCurrentTime(mEvidenceItem.getTime()));
        mGetPeopleText.setText(UserInfo.getUserName(mEvidenceItem.getPeople()));
    }

    private void saveData(){
        mEvidenceItem.setEvidenceName(mEvidenceName.getText());
        mEvidenceItem.setLegacySite(mLegacySite.getText());
        mEvidenceItem.setBasiceFeature(mBasiceFeature.getText());

        if(getCategory(mEvidenceItem.getEvidenceCategory())==3){
            mEvidenceItem.setEvidence(mOtherEvidence.getText());
            mEvidenceItem.setMethod(mOtherMethod.getText());
        }
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
        mEvidenceName.clearFocus();
        mLegacySite.clearFocus();
        mBasiceFeature.clearFocus();
    }

    private int getCategory(String category){
        for(int i=0; i<mEvidence_category.size(); i++){
            if(category.equalsIgnoreCase(mEvidence_category.get(i))) return i;
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        releaseFocusEditText();
        switch (v.getId()) {
            case R.id.time_button:
            case R.id.time:
                showDateTimeDialog(mTime);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String result = "";
        int category = getCategory(mEvidenceItem.getEvidenceCategory());
        if(requestCode == CreateSceneUtils.EVENT_PHOTO_TYPE_NEW_EVIDENCE) {
            String path = LocalFileUri.getPath();
            File file = new File(path);
            if (path != null && file.exists()) {
                //进行图片压缩，压缩比例为1600*1600
                int width= Integer.parseInt(context.getResources().getString(R.string.image_reqWidth));
                int height= Integer.parseInt(context.getResources().getString(R.string.image_reqHeight));
                path= ImageCompress.getSmallBitmap(path,width,height);

                mEvidenceItem.setPhotoPath(path);
                setPhoto(path);
            } else {
                finish();
            }
        }else if(requestCode == CreateSceneUtils.EVENT_EVIDENCE_SELECT_ITEM){
            if(data!=null) result = (String) data.getStringExtra("Select");
            else result = mEvidenceItem.getEvidence();
            mEvidenceItem.setEvidence(result);
            if(category==0) result = DictionaryInfo.getDictValue(DictionaryInfo.mEvidenceHandKey, result);
            else if(category==1) result = DictionaryInfo.getDictValue(DictionaryInfo.mEvidenceFootKey, result);
            else if(category==2) result = DictionaryInfo.getDictValue(DictionaryInfo.mEvidenceToolKey, result);
            mEvidenceText.setText(result);
            mEvidenceName.setText(result);
        }else if(requestCode == CreateSceneUtils.EVENT_INFER_SELECT_ITEM){
            if(data!=null) result = (String) data.getStringExtra("Select");
            else result = mEvidenceItem.getInfer();
            mEvidenceItem.setInfer(result);
            result = DictionaryInfo.getDictValue(DictionaryInfo.mToolInferKey, result);
            mInferText.setText(result);
        }else if(requestCode == CreateSceneUtils.EVENT_METHOD_SELECT_ITEM){
            if(data!=null) result = (String) data.getStringExtra("Select");
            else result = mEvidenceItem.getMethod();
            mEvidenceItem.setMethod(result);
            if(category==0) result = DictionaryInfo.getDictValue(DictionaryInfo.mMethodHandKey, result);
            else if(category==1) result = DictionaryInfo.getDictValue(DictionaryInfo.mMethodFootKey, result);
            else if(category==2) result = DictionaryInfo.getDictValue(DictionaryInfo.mMethodToolKey, result);
            mMethodText.setText(result);
        }else if(requestCode == CreateSceneUtils.EVENT_GET_PEOPLE_SLELECT_ITEM){
            if(data!=null) result = (String) data.getStringExtra("Select");
            else result = mEvidenceItem.getPeople();
            mEvidenceItem.setPeople(result);
            result = UserInfo.getUserName(result);
            mGetPeopleText.setText(result);
        }
    }

    private void refreshEvidenceItem(int category){
        mEvidenceText.setText("");
        mMethodText.setText("");
        mEvidenceName.setText("");
        mEvidenceItem.setEvidence("");
        mEvidenceItem.setMethod("");

        if(category==0){
            //手印
            mEvidenceTextLabel.setText(getResources().getString(R.string.evidence_hand));
            mBasiceFeatureLabel.setBackground(getResources().getDrawable(R.drawable.green_60dp));
            mMethodLabel.setBackground(getResources().getDrawable(R.drawable.green_60dp));
            mInferLL.setVisibility(View.GONE);
            mEvidenceItem.setInfer("");

            mEvidenceText.setVisibility(View.VISIBLE);
            mMethodText.setVisibility(View.VISIBLE);
            mOtherEvidence.setVisibility(View.GONE);
            mOtherMethod.setVisibility(View.GONE);

            mEvidenceText = (TextView) findViewById(R.id.evidence);
            mEvidenceText.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    Intent it = new Intent(CreateScene_FP5_NewEvidenceActivity.this, TreeViewListActivity.class);
                    it.putExtra("Key",DictionaryInfo.mEvidenceHandKey);
                    it.putExtra("Selected", mEvidenceItem.getEvidence());
                    startActivityForResult(it, CreateSceneUtils.EVENT_EVIDENCE_SELECT_ITEM);
                }
            });

            mMethodText = (TextView) findViewById(R.id.method);
            mMethodText.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    Intent it = new Intent(CreateScene_FP5_NewEvidenceActivity.this, TreeViewListActivity.class);
                    it.putExtra("Key",DictionaryInfo.mMethodHandKey);
                    it.putExtra("Selected", mEvidenceItem.getMethod());
                    startActivityForResult(it, CreateSceneUtils.EVENT_METHOD_SELECT_ITEM);
                }
            });

            mEvidenceItem.setMethod(DictionaryInfo.getDictKey(DictionaryInfo.mMethodHandKey, "直接照相"));
            mMethodText.setText("直接照相");
        }else if(category==1){
            //足跡
            mEvidenceTextLabel.setText(getResources().getString(R.string.evidence_foot));
            mBasiceFeatureLabel.setBackground(getResources().getDrawable(R.drawable.green_60dp));
            mMethodLabel.setBackground(getResources().getDrawable(R.drawable.green_60dp));
            mInferLL.setVisibility(View.GONE);
            mEvidenceItem.setInfer("");

            mEvidenceText.setVisibility(View.VISIBLE);
            mMethodText.setVisibility(View.VISIBLE);
            mOtherEvidence.setVisibility(View.GONE);
            mOtherMethod.setVisibility(View.GONE);

            mEvidenceText = (TextView) findViewById(R.id.evidence);
            mEvidenceText.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    Intent it = new Intent(CreateScene_FP5_NewEvidenceActivity.this, TreeViewListActivity.class);
                    it.putExtra("Key",DictionaryInfo.mEvidenceFootKey);
                    it.putExtra("Selected", mEvidenceItem.getEvidence());
                    startActivityForResult(it, CreateSceneUtils.EVENT_EVIDENCE_SELECT_ITEM);
                }
            });

            mMethodText = (TextView) findViewById(R.id.method);
            mMethodText.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    Intent it = new Intent(CreateScene_FP5_NewEvidenceActivity.this, TreeViewListActivity.class);
                    it.putExtra("Key",DictionaryInfo.mMethodFootKey);
                    it.putExtra("Selected", mEvidenceItem.getMethod());
                    startActivityForResult(it, CreateSceneUtils.EVENT_METHOD_SELECT_ITEM);
                }
            });

            mEvidenceItem.setMethod(DictionaryInfo.getDictKey(DictionaryInfo.mMethodFootKey, "直接照相"));
            mMethodText.setText("直接照相");
        }else if(category==2){
            //工痕
            mEvidenceTextLabel.setText(getResources().getString(R.string.evidence_tool));
            mBasiceFeatureLabel.setBackground(getResources().getDrawable(R.drawable.red_60dp));
            mMethodLabel.setBackground(getResources().getDrawable(R.drawable.green_60dp));
            mInferLL.setVisibility(View.VISIBLE);

            mEvidenceText.setVisibility(View.VISIBLE);
            mMethodText.setVisibility(View.VISIBLE);
            mOtherEvidence.setVisibility(View.GONE);
            mOtherMethod.setVisibility(View.GONE);

            mEvidenceText = (TextView) findViewById(R.id.evidence);
            mEvidenceText.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    Intent it = new Intent(CreateScene_FP5_NewEvidenceActivity.this, TreeViewListActivity.class);
                    it.putExtra("Key",DictionaryInfo.mEvidenceToolKey);
                    it.putExtra("Selected", mEvidenceItem.getEvidence());
                    startActivityForResult(it, CreateSceneUtils.EVENT_EVIDENCE_SELECT_ITEM);
                }
            });

            mMethodText = (TextView) findViewById(R.id.method);
            mMethodText.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    Intent it = new Intent(CreateScene_FP5_NewEvidenceActivity.this, TreeViewListActivity.class);
                    it.putExtra("Key",DictionaryInfo.mMethodToolKey);
                    it.putExtra("Selected", mEvidenceItem.getMethod());
                    startActivityForResult(it, CreateSceneUtils.EVENT_METHOD_SELECT_ITEM);
                }
            });

            mEvidenceItem.setMethod(DictionaryInfo.getDictKey(DictionaryInfo.mMethodToolKey, "直接照相"));
            mMethodText.setText("直接照相");
        }else if(category==3){
            //其他
            mEvidenceTextLabel.setText(getResources().getString(R.string.evidence_other));
            mBasiceFeatureLabel.setBackground(getResources().getDrawable(R.drawable.red_60dp));
            mMethodLabel.setBackground(getResources().getDrawable(R.drawable.red_60dp));
            mInferLL.setVisibility(View.GONE);

            mEvidenceText.setVisibility(View.GONE);
            mMethodText.setVisibility(View.GONE);
            mOtherEvidence.setVisibility(View.VISIBLE);
            mOtherMethod.setVisibility(View.VISIBLE);

            mEvidenceName.setText(getResources().getString(R.string.evidence_name_other));
            mOtherMethod.setText("直接照相");
        }
    }

    private void takePhoto(Uri LocalFileUri, int PHOTO_TYPE) {
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, LocalFileUri);
        startActivityForResult(it, PHOTO_TYPE);
    }

    private void setPhoto(String path){
        Bitmap Bitmap = CreateSceneUtils.loadBitmapFromFile(new File(path));
        BitmapDrawable bDrawable = new BitmapDrawable(context.getResources(), Bitmap);
        mNew_evidence.setBackground(bDrawable);
        mNew_evidence.setVisibility(View.VISIBLE);
    }

    private void showDateTimeDialog(final TextView textView) {
        // Create the dialog
        final Dialog mDateTimeDialog = new Dialog(this);
        // Inflate the root layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        final RelativeLayout mDateTimeDialogView = (RelativeLayout) inflater.inflate(R.layout.date_time_dialog, null);
        // Grab widget instance
        final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);

        mDateTimePicker.clickTime();

        // Update demo TextViews when the "OK" button is clicked
        ((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mDateTimePicker.clearFocus();
                // TODO Auto-generated method stub
                long time = mDateTimePicker.get().getTimeInMillis();
                mEvidenceItem.setTime(time);
                textView.setText(DateTimePicker.getCurrentTime(mDateTimePicker.get().getTimeInMillis()));
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
}
