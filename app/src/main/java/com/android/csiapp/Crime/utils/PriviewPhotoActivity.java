package com.android.csiapp.Crime.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.csiapp.R;

public class PriviewPhotoActivity extends AppCompatActivity {

    private ImageView priview;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.priview_photo);

        String path = getIntent().getStringExtra("Path");

        priview = (ImageView) findViewById(R.id.priview);

        if(path!=null){
            Uri uri = Uri.parse(path);
            priview.setImageURI(uri);
        }

        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = getIntent();
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
    }
}
