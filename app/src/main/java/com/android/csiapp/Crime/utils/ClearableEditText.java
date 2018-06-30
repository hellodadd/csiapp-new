package com.android.csiapp.Crime.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.csiapp.R;

/**
 * Created by user on 2016/9/26.
 */
public class ClearableEditText extends RelativeLayout
{
    private LayoutInflater inflater = null;
    private EditText edit_text;
    private Button btn_clear;

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initViews();
    }

    public ClearableEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initViews();

    }

    public ClearableEditText(Context context)
    {
        super(context);
        initViews();
    }

    public void setMaxLines(int lines){
        if(lines==1) {
            edit_text.setSingleLine(true);
        }else {
            edit_text.setSingleLine(false);
            edit_text.setMaxLines(lines);
        }
    }

    public void setHint(String hint){
        edit_text.setHint(hint);
    }

    void initViews()
    {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.clearable_edit_text, this, true);
        edit_text = (EditText) findViewById(R.id.clearable_edit);
        edit_text.setSingleLine(true);//默认单行
        btn_clear = (Button) findViewById(R.id.clearable_button_clear);
        btn_clear.setVisibility(RelativeLayout.INVISIBLE);
        clearText();
        showHideClearButton();
    }

    void clearText()
    {
        btn_clear.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                edit_text.setText("");
            }
        });
    }

    void showHideClearButton()
    {
        edit_text.addTextChangedListener(new TextWatcher()
        {

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() > 0)
                    btn_clear.setVisibility(RelativeLayout.VISIBLE);
                else
                    btn_clear.setVisibility(RelativeLayout.INVISIBLE);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    public String getText() {
        Editable text = edit_text.getText();
        return text.toString();
    }

    public void setText(String text) {
        edit_text.setText(text);
    }

    public void setKeyListener(KeyListener input) {
        edit_text.setKeyListener(input);
    }

    public void addTextChangedListener(TextWatcher input){edit_text.addTextChangedListener(input);}

    public void setTextColor(int color) {
        edit_text.setTextColor(color);
    }

    public void setTextHint(CharSequence hint) {
        edit_text.setHint(hint);
    }

    public void setTextHintColor(int color) {
        edit_text.setHintTextColor(color);
    }

    public void setEditTextFocusable(boolean focusable) {
        edit_text.setFocusable(focusable);
    }

    public void setEditTextAc(boolean ac) {}


}
