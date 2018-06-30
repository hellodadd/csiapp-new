package com.android.csiapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by server on 17-8-23.
 */

public class EnhancedTextView extends TextView {

    public EnhancedTextView(Context context) {
        this(context, null);
    }


    public EnhancedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EnhancedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
