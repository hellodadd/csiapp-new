package com.android.csiapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.csiapp.Switch;

/**
 * Created by server on 17-8-23.
 */

public class LauncherPreference extends LinearLayout {


    private View mRootView;
    private ImageView mIcon;
    private EnhancedTextView mTitleText;
    private TextView mSummary;
    private ImageView mGuideIcon;
    private Switch mSwitch;
    private ImageView mRightArrow;
    private View mSpinner;
    private View mDivider;

    private static final int IMG_RED_DOT = 0;
    private static final int IMG_SWITCH = 1;
    private static final int IMG_RIGHT_ARROW = 2;
    private static final int IMG_SPINNER = 3;

    public LauncherPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LauncherPreference);
        inflate(context, R.layout.launcher_preference, this);

        mRootView = findViewById(R.id.root);
        mIcon = (ImageView) findViewById(R.id.icon);
        mTitleText = (EnhancedTextView) findViewById(R.id.title);
        mSummary = (TextView) findViewById(R.id.summary);
        mGuideIcon = (ImageView) findViewById(R.id.red_new_guide);
        mSwitch = (Switch) findViewById(R.id.switch1);
        mRightArrow = (ImageView) findViewById(R.id.right_arrow);
        mSpinner = findViewById(R.id.spinner);
        mDivider = findViewById(R.id.divider);


        int backgroundResId = array.getResourceId(R.styleable.LauncherPreference_preferenceBackground, 0);
        if (backgroundResId != 0) {
            mRootView.setBackgroundResource(backgroundResId);
        }

        int iconResId = array.getResourceId(R.styleable.LauncherPreference_preferenceIcon, 0);
        if (iconResId != 0) {
            mIcon.setVisibility(VISIBLE);
            mIcon.setImageResource(iconResId);
        }

        String title = array.getString(R.styleable.LauncherPreference_preferenceTitle);
        if (!TextUtils.isEmpty(title)) {
            mTitleText.setText(title);
        }

        String summary = array.getString(R.styleable.LauncherPreference_preferenceSummary);
        if (!TextUtils.isEmpty(summary)) {
            mSummary.setText(summary);
            mSummary.setVisibility(VISIBLE);
        }

        int titleColor = array.getColor(R.styleable.LauncherPreference_PreferencetitleColor, context.getResources().getColor(R.color.selector_preference_title));
        mTitleText.setTextColor(titleColor);

        int summaryColor = array.getColor(R.styleable.LauncherPreference_PreferenceSummaryColor, context.getResources().getColor(R.color.preference_summary));
        mSummary.setTextColor(summaryColor);


        int rightImg = array.getInteger(R.styleable.LauncherPreference_rightImage, -1);
        if (rightImg == IMG_RED_DOT) {
            mGuideIcon.setVisibility(VISIBLE);
        } else if (rightImg == IMG_SWITCH) {
            mSwitch.setVisibility(VISIBLE);
        } else if (rightImg == IMG_RIGHT_ARROW) {
            mRightArrow.setVisibility(VISIBLE);
        } else if (rightImg == IMG_SPINNER) {
            mSpinner.setVisibility(VISIBLE);
        }

        boolean hideDivider = array.getBoolean(R.styleable.LauncherPreference_hideDivider, false);
        if (hideDivider) {
            mDivider.setVisibility(GONE);
        }
    }



    public void setTitle(String title) {
        mTitleText.setText(title);
    }

    public void setSummary(String summary) {
        mSummary.setText(summary);
        mSummary.setVisibility(VISIBLE);
    }

    public void setGuideIconVisible(boolean visible) {
        mGuideIcon.setVisibility(visible ? VISIBLE : GONE);
    }


    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        mSwitch.setOnCheckedChangeListener(listener);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggole();
            }
        });
    }

    public boolean isChecked() {
        return mSwitch.isChecked();
    }

    public void setChecked(boolean checked) {
        mSwitch.setChecked(checked);
    }

    public void toggole() {
        mSwitch.toggle();
    }


}
