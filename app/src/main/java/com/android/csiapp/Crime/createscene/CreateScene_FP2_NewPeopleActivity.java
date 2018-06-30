package com.android.csiapp.Crime.createscene;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.csiapp.Crime.utils.BackAlertDialog;
import com.android.csiapp.Crime.utils.ClearableEditText;
import com.android.csiapp.Crime.utils.CreateSceneUtils;
import com.android.csiapp.Crime.utils.DictionaryInfo;
import com.android.csiapp.Crime.utils.IdCardVerify;
import com.android.csiapp.Crime.utils.SaveAlertDialog;
import com.android.csiapp.Crime.utils.tree.TreeViewListActivity;
import com.android.csiapp.Databases.CrimeProvider;
import com.android.csiapp.Databases.RelatedPeopleItem;
import com.android.csiapp.R;

import java.util.ArrayList;

public class CreateScene_FP2_NewPeopleActivity extends AppCompatActivity {

    private Context context = null;
    private RelatedPeopleItem mRelatedPeopleItem;
    private int mEvent;
    private int mPosition;

    private RadioGroup mRadioGroupRelation;
    private ArrayList<String> mReleationPeople = new ArrayList<String>();
    private RadioGroup mRadioGroupSex;
    private ClearableEditText mName, mId, mNumber, mAddress;

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_click:
                    saveData();
                    Intent result = getIntent();
                    result.putExtra("com.android.csiapp.Databases.RelatedPeopleItem", mRelatedPeopleItem);
                    result.putExtra("Event", mEvent);
                    result.putExtra("Posiotion", mPosition);

                    if(!mRelatedPeopleItem.getPeopleId().isEmpty() && !IdCardVerify.validateIdCardWithoutAddress(mRelatedPeopleItem.getPeopleId())){
                        String msg = "身份证号码错误";
                        Toast.makeText(CreateScene_FP2_NewPeopleActivity.this, msg, Toast.LENGTH_SHORT).show();

                        /* << AnitaLin */
                        //Bug [身份证号码校验有问题，一些正确的身份证号校验通不过]
                        //Clear id if the number verify failed
                        mRelatedPeopleItem.setPeopleId("");
                        mId.setText("");
                        /* >> */
                        break;
                    }

                    if(mRelatedPeopleItem.checkInformation()){
                        setResult(Activity.RESULT_OK, result);
                        finish();
                    }else{
                        SaveAlertDialog dialog = new SaveAlertDialog(CreateScene_FP2_NewPeopleActivity.this);
                        dialog.onCreateDialog(result,false,null);
                        dialog.setOwnerActivity(CreateScene_FP2_NewPeopleActivity.this);
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
        setContentView(R.layout.create_scene_fp2_new_people);

        context = this.getApplicationContext();
        mRelatedPeopleItem = (RelatedPeopleItem) getIntent().getSerializableExtra("com.android.csiapp.Databases.RelatedPeopleItem");
        mEvent = (int) getIntent().getIntExtra("Event", 1);
        mPosition = (int) getIntent().getIntExtra("Position", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(context.getResources().getString(R.string.title_activity_peopleinformation));
        toolbar.setTitleTextColor(context.getResources().getColor(R.color.titleBar));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back_mini);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                BackAlertDialog dialog = new BackAlertDialog(CreateScene_FP2_NewPeopleActivity.this);
                dialog.onCreateDialog(false,null);
                dialog.setOwnerActivity(CreateScene_FP2_NewPeopleActivity.this);
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
            BackAlertDialog dialog = new BackAlertDialog(CreateScene_FP2_NewPeopleActivity.this);
            dialog.onCreateDialog(false,null);
            dialog.setOwnerActivity(CreateScene_FP2_NewPeopleActivity.this);
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
             if(requestCode == CreateSceneUtils.EVENT_SEX_SELECT_ITEM){
                result = (String) data.getStringExtra("Select");
                mRelatedPeopleItem.setPeopleSex(result);
                result = DictionaryInfo.getDictValue(DictionaryInfo.mSexKey, result);
                //mSexText.setText(result);
            }
        }
    }

    private void initView(){
        mRadioGroupRelation=(RadioGroup)findViewById(R.id.radioGroupPeopleRelation);
        mRadioGroupRelation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton=(RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                mRelatedPeopleItem.setPeopleRelation(radioButton.getText().toString());
            }
        });

        mRadioGroupSex=(RadioGroup)findViewById(R.id.radioGroupPeopleSex);
        mRadioGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton=(RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                if(radioButton.getText().toString().equals("男")) {
                    mRelatedPeopleItem.setPeopleSex("1");
                }
                else{
                    mRelatedPeopleItem.setPeopleSex("2");
                }
            }
        });

        mName = (ClearableEditText) findViewById(R.id.name_editView);
        mId = (ClearableEditText) findViewById(R.id.identity_number_editView);
        mNumber = (ClearableEditText) findViewById(R.id.contact_number_editView);
        mAddress = (ClearableEditText) findViewById(R.id.address_editView);
        mId.setKeyListener(DigitsKeyListener.getInstance("abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"));
        mNumber.setKeyListener(DigitsKeyListener.getInstance("()-0123456789"));

        mId.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("Anita","s.length = "+s.length()+", s ="+s.toString());
                if(s.length()==17) mId.setKeyListener(new DigitsKeyListener() {
                    @Override
                    public int getInputType() {
                        return InputType.TYPE_TEXT_VARIATION_PASSWORD;
                    }
                    @Override
                    protected char[] getAcceptedChars() {
                        char[] data = getResources().getString(R.string.login_only_can_input).toCharArray();
                        return data;
                    }
                });
                if(s.length()!=17) mId.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                if(s.length()>=18) mId.setKeyListener(DigitsKeyListener.getInstance(""));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initData(){
        //如果没有选择人员类型，默认报案人
        if(mRelatedPeopleItem.getPeopleRelation().equals("")) mRelatedPeopleItem.setPeopleRelation("报案人");
        RadioButton radioButton1=(RadioButton)findViewById(R.id.radioBaoanren);
        RadioButton radioButton2=(RadioButton)findViewById(R.id.radioShouhairen);
        if((mRelatedPeopleItem.getPeopleRelation().equals("报案人"))) {
            radioButton1.setChecked(true);
            radioButton2.setChecked(false);
        }
        else{
            radioButton1.setChecked(false);
            radioButton2.setChecked(true);
        }

        mName.setText(mRelatedPeopleItem.getPeopleName());
        //如果没有选择人员性别，默认男

        if(mRelatedPeopleItem.getPeopleSex().equals("")) mRelatedPeopleItem.setPeopleSex("1");
        radioButton1=(RadioButton)findViewById(R.id.radioSexMan);
        radioButton2=(RadioButton)findViewById(R.id.radioSexWomen);
        if((mRelatedPeopleItem.getPeopleSex().equals("1"))) {
            radioButton1.setChecked(true);
            radioButton2.setChecked(false);
        }
        else{
            radioButton1.setChecked(false);
            radioButton2.setChecked(true);
        }
        mId.setText(mRelatedPeopleItem.getPeopleId());
        mNumber.setText(mRelatedPeopleItem.getPeopleNumber());
        mAddress.setText(mRelatedPeopleItem.getPeopleAddress());
    }

    private void saveData(){
        mRelatedPeopleItem.setPeopleName(mName.getText());
        mRelatedPeopleItem.setPeopleId(mId.getText());
        mRelatedPeopleItem.setPeopleNumber(mNumber.getText());
        mRelatedPeopleItem.setPeopleAddress(mAddress.getText());
        mRelatedPeopleItem.setUuid(CrimeProvider.getUUID());
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
        mId.clearFocus();
        mNumber.clearFocus();
        mAddress.clearFocus();
    }

    private int getPeople(String category){
        for(int i=0; i<mReleationPeople.size(); i++){
            if(category.equalsIgnoreCase(mReleationPeople.get(i))) return i;
        }
        return 0;
    }
}
