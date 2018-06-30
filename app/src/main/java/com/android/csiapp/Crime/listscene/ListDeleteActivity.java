package com.android.csiapp.Crime.listscene;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.csiapp.Databases.CrimeItem;
import com.android.csiapp.Databases.CrimeProvider;
import com.android.csiapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/10/14.
 */
public class ListDeleteActivity extends AppCompatActivity {

    private Context context = null;
    private CrimeProvider mCrimeProvider;
    private List<CrimeItem> items_list;
    private ListView mListV;
    private HashMap<Integer, Boolean> isSelected;
    private ListDeleteAdapter mAdapter;

    final int LIST_DELETE = 0;

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_select_all:
                    msg += "Select All";
                    for (int i = 0; i < items_list.size(); i++) {
                        mAdapter.getIsSelected().put(i, true);
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                case R.id.action_delete:
                    msg += "Delete";
                    if(items_list.size()>0){
                        isSelected = mAdapter.getIsSelected();
                        for (int i = 0; i < isSelected.size(); i++) {
                            if (isSelected.get(i).equals(true)) {
                                if(!items_list.get(i).getComplete().equalsIgnoreCase("2")) {
                                    mCrimeProvider.delete(items_list.get(i).getId());
                                }else{
                                    Toast.makeText(ListDeleteActivity.this, "已上报数据无法删除", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                    Intent result = getIntent();
                    setResult(Activity.RESULT_OK, result);
                    finish();
                    break;
            }

            if(!msg.equals("")) {
                //Toast.makeText(ListDeleteActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_delete);

        context = this.getApplicationContext();

        mCrimeProvider = new CrimeProvider(context);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(context.getResources().getColor(R.color.titleBar));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.btn_back_mini);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //What to do on back clicked
            }
        });
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        items_list = new ArrayList<CrimeItem>();
        items_list = mCrimeProvider.getAll();

        Collections.sort(items_list,
                new Comparator<CrimeItem>() {
                    public int compare(CrimeItem o1, CrimeItem o2) {
                        return String.valueOf(o1.getOccurredStartTime()).compareTo(String.valueOf(o2.getOccurredStartTime()));
                    }
                });
        Collections.reverse(items_list);

        mListV=(ListView)findViewById(R.id.listView);
        isSelected = new HashMap<Integer,Boolean>();
        mAdapter = new ListDeleteAdapter(ListDeleteActivity.this,items_list, isSelected);
        mListV.setAdapter(mAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_delete, menu);
        return true;
    }
}
