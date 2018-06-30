package com.android.csiapp.Crime.createscene;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.csiapp.Crime.utils.BackAlertDialog;
import com.android.csiapp.Crime.utils.ClearableEditText;
import com.android.csiapp.Crime.utils.CreateSceneUtils;
import com.android.csiapp.Crime.utils.DateTimePicker;
import com.android.csiapp.Crime.utils.DictionaryInfo;
import com.android.csiapp.Crime.utils.HandWriteActivity;
import com.android.csiapp.Crime.utils.SaveAlertDialog;
import com.android.csiapp.Crime.utils.tree.TreeViewListActivity;
import com.android.csiapp.Databases.CrimeProvider;
import com.android.csiapp.Databases.WitnessItem;
import com.android.csiapp.Databases.WitnessProvider;
import com.android.csiapp.R;

public class CreateScene_FP8_AddWitnessActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context = null;
    private WitnessItem mWitnessItem;
    private int mEvent;
    private int mPosition;

    private ClearableEditText mName, mNumber, mAddress;

    private TextView mBirthday;
    private Button mBirthday_button;

    private ImageView mImage;
    private Button mBtn;
    private RadioGroup mRadioJzGroupSex;


    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_click:
                    saveData();
                    Intent result = getIntent();
                    result.putExtra("com.android.csiapp.Databases.WitnessItem", mWitnessItem);
                    result.putExtra("Event", mEvent);
                    result.putExtra("Posiotion", mPosition);
                    if(mWitnessItem.checkInformation()){
                        setResult(Activity.RESULT_OK, result);
                        finish();
                    }else {
                        SaveAlertDialog dialog = new SaveAlertDialog(CreateScene_FP8_AddWitnessActivity.this);
                        dialog.onCreateDialog(result,false,null);
                        dialog.setOwnerActivity(CreateScene_FP8_AddWitnessActivity.this);
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
        setContentView(R.layout.create_scene_fp8_add_witness);

        context = this.getApplicationContext();
        mWitnessItem = (WitnessItem) getIntent().getSerializableExtra("com.android.csiapp.Databases.WitnessItem");
        mEvent = (int) getIntent().getIntExtra("Event", 1);
        mPosition = (int) getIntent().getIntExtra("Position", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(context.getResources().getString(R.string.title_activity_witness_peopleinformation));
        toolbar.setTitleTextColor(context.getResources().getColor(R.color.titleBar));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back_mini);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                BackAlertDialog dialog = new BackAlertDialog(CreateScene_FP8_AddWitnessActivity.this);
                dialog.onCreateDialog(false,null);
                dialog.setOwnerActivity(CreateScene_FP8_AddWitnessActivity.this);
            }
        });
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        initView();
        initData();
        getLastData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            //What to do on back clicked
            BackAlertDialog dialog = new BackAlertDialog(CreateScene_FP8_AddWitnessActivity.this);
            dialog.onCreateDialog(false,null);
            dialog.setOwnerActivity(CreateScene_FP8_AddWitnessActivity.this);
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_fp8_subactivity, menu);
        return true;
    }

    private void initView(){
        mName = (ClearableEditText) findViewById(R.id.name_editView);
        mNumber = (ClearableEditText) findViewById(R.id.contact_number_editView);
        mNumber.setKeyListener(DigitsKeyListener.getInstance("()-0123456789"));
        mAddress = (ClearableEditText) findViewById(R.id.address_editView);

        mRadioJzGroupSex=(RadioGroup)findViewById(R.id.radioGroupJzPeopleSex);
        mRadioJzGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton=(RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                if(radioButton.getText().toString().equals("男")) {
                    mWitnessItem.setWitnessSex("1");
                }
                else{
                    mWitnessItem.setWitnessSex("2");
                }
            }
        });

        mBirthday = (TextView) findViewById(R.id.birthday_date);
        mBirthday_button = (Button) findViewById(R.id.birthday_date_button);
        mBirthday_button.setOnClickListener(this);
        mBirthday.setOnClickListener(this);

        mImage = (ImageView) findViewById(R.id.image);
        mBtn = (Button) findViewById(R.id.btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CreateScene_FP8_AddWitnessActivity.this, HandWriteActivity.class), CreateSceneUtils.EVENT_PHOTO_TYPE_SIGN);
            }
        });
    }

    private void initData(){
        mName.setText(mWitnessItem.getWitnessName());

        if(mWitnessItem.getWitnessSex().equals("")) mWitnessItem.setWitnessSex("1");
        RadioButton radioButton1=(RadioButton)findViewById(R.id.radioJzSexMan);
        RadioButton radioButton2=(RadioButton)findViewById(R.id.radioJzSexWomen);
        if((mWitnessItem.getWitnessSex().equals("1"))) {
            radioButton1.setChecked(true);
            radioButton2.setChecked(false);
        }
        else{
            radioButton1.setChecked(false);
            radioButton2.setChecked(true);
        }

        mBirthday.setText(DateTimePicker.getCurrentDate(mWitnessItem.getWitnessBirthday()));
        mNumber.setText(mWitnessItem.getWitnessNumber());
        mAddress.setText(mWitnessItem.getWitnessAddress());
        if(!mWitnessItem.getPhotoPath().isEmpty()){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bm = BitmapFactory.decodeFile(mWitnessItem.getPhotoPath(), options);
            mImage.setImageBitmap(bm);
            mImage.setVisibility(View.VISIBLE);
        }
    }

    private void getLastData(){
        if(mEvent == 1){
            WitnessProvider witnessProvider = new WitnessProvider(context);
            WitnessItem witnessItem = witnessProvider.getLastRecord();
            if(witnessItem!=null) {
                mName.setText(witnessItem.getWitnessName());

                if(witnessItem.getWitnessSex().equals("")) witnessItem.setWitnessSex("1");
                RadioButton radioButton1=(RadioButton)findViewById(R.id.radioJzSexMan);
                RadioButton radioButton2=(RadioButton)findViewById(R.id.radioJzSexWomen);
                if((witnessItem.getWitnessSex().equals("1"))) {
                    radioButton1.setChecked(true);
                    radioButton2.setChecked(false);
                }
                else{
                    radioButton1.setChecked(false);
                    radioButton2.setChecked(true);
                }

                mBirthday.setText(DateTimePicker.getCurrentDate(witnessItem.getWitnessBirthday()));
                mNumber.setText(witnessItem.getWitnessNumber());
                mAddress.setText(witnessItem.getWitnessAddress());
                if(!witnessItem.getPhotoPath().isEmpty()){
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap bm = BitmapFactory.decodeFile(witnessItem.getPhotoPath(), options);
                    mImage.setImageBitmap(bm);
                    mImage.setVisibility(View.VISIBLE);
                }
                mWitnessItem.setPhotoPath(witnessItem.getPhotoPath());
                mWitnessItem.setWitnessSex(witnessItem.getWitnessSex());
            }
        }
    }

    private void saveData(){
        mWitnessItem.setWitnessName(mName.getText());
        mWitnessItem.setWitnessNumber(mNumber.getText());
        mWitnessItem.setWitnessAddress(mAddress.getText());
        mWitnessItem.setUuid(CrimeProvider.getUUID());
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
        mNumber.clearFocus();
        mAddress.clearFocus();
    }

    @Override
    public void onClick(View v) {
        releaseFocusEditText();
        switch (v.getId()) {
            case R.id.birthday_date_button:
            case R.id.birthday_date:
                showDateTimeDialog(mBirthday);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CreateSceneUtils.EVENT_PHOTO_TYPE_SIGN)
            {
                String path = data.getStringExtra("SIGN");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bm = BitmapFactory.decodeFile(path, options);
                mImage.setImageBitmap(bm);
                mImage.setVisibility(View.VISIBLE);
                mWitnessItem.setPhotoPath(path);
            }
        }
    }

    private void showDateTimeDialog(final TextView textView) {
        // Create the dialog
        final Dialog mDateTimeDialog = new Dialog(this);
        // Inflate the root layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        final RelativeLayout mDateTimeDialogView = (RelativeLayout) inflater.inflate(R.layout.date_time_dialog, null);
        // Grab widget instance
        final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);

        // Update demo TextViews when the "OK" button is clicked
        ((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mDateTimePicker.clearFocus();
                // TODO Auto-generated method stub
                long time = mDateTimePicker.get().getTimeInMillis();
                mWitnessItem.setWitnessBirthday(time);
                textView.setText(DateTimePicker.getCurrentDate(time));
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
