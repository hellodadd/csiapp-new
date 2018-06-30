package com.android.csiapp.Crime.listscene;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.csiapp.CsiApplication;
import com.android.csiapp.Databases.CrimeProvider;
import com.android.csiapp.Databases.CrimeItem;
import com.android.csiapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private Context context = null;
    private CrimeProvider mCrimeProvider;
    private List<CrimeItem> items_list;
    private ListView mListV;
    private ListAdapter mAdapter;

    final int LIST_DELETE = 0;

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_search:
                    msg += "Search";
                    Intent it1 = new Intent(ListActivity.this, ListSearchActivity.class);
                    startActivityForResult(it1, 1);
                    break;
                case R.id.action_delete:
                    msg += "Delete";
                    Intent it2 = new Intent(ListActivity.this, ListDeleteActivity.class);
                    startActivityForResult(it2, 2);
                    break;
                case R.id.action_reportscene:
                    //通过网络上报现场数据
                    CsiApplication csiApplication=(CsiApplication)getApplication();
                    if(!csiApplication.getIsReporting()) {
                        //设置已经开始上传
                        msg += "Report";
                        Intent it3 = new Intent(ListActivity.this, ListReportSceneActivity.class);
                        startActivityForResult(it3, 3);
                    }
                    else {
                        Toast.makeText(ListActivity.this, "后台数据上传未结束", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

            if(!msg.equals("")) {
                //Toast.makeText(ListActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

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
        mAdapter = new ListAdapter(ListActivity.this,items_list);
        mListV.setAdapter(mAdapter);
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                CrimeItem item = (CrimeItem) mAdapter.getItem(position);
                if(!item.getComplete().equalsIgnoreCase("2")) {
                    // 讀取選擇的記事物件
                    Intent intent = new Intent("com.android.csiapp.EDIT_SCENE");
                    // 設定記事編號與記事物件
                    intent.putExtra("CrimeItem", item);
                    startActivityForResult(intent, 0);
                }else{
                    Toast.makeText(ListActivity.this, "已上报数据无法修改", Toast.LENGTH_SHORT).show();
                }
            }
        };
        mListV.setOnItemClickListener(itemListener);
        registerForContextMenu(mListV);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        String delete = context.getResources().getString(R.string.list_delete);
        if (v.getId()==R.id.listView) {
            menu.add(0, LIST_DELETE, 0, delete);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case LIST_DELETE:
                if(!items_list.get(info.position).getComplete().equalsIgnoreCase("2")) {
                    mCrimeProvider.delete(items_list.get(info.position).getId());
                    items_list.remove(info.position);
                    mAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(ListActivity.this, "已上报数据无法删除", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        //判断是网络版本还是插线数据上传
        Menu mMenu=menu;
        if(getResources().getString(R.string.config_app_network).equals("1")) {
            mMenu.findItem(R.id.action_reportscene).setVisible(true);
        }
        else{
            mMenu.findItem(R.id.action_reportscene).setVisible(false);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                // 新增記事資料到資料庫
                CrimeItem item = (CrimeItem) data.getExtras().getSerializable("com.android.csiapp.CrimeItem");
                boolean result = mCrimeProvider.update(item);
            }else if(requestCode == 1){
                //Search
            }else if(requestCode == 2){
                //Delete
            }
            finish();
            /*items_list = mCrimeProvider.getAll();
            mAdapter = new ListAdapter(ListActivity.this,items_list);
            mListV.setAdapter(mAdapter);

            Collections.sort(items_list,
                    new Comparator<CrimeItem>() {
                        public int compare(CrimeItem o1, CrimeItem o2) {
                            return String.valueOf(o1.getOccurredStartTime()).compareTo(String.valueOf(o2.getOccurredStartTime()));
                        }
                    });
            Collections.reverse(items_list);*/
        }
    }
}
