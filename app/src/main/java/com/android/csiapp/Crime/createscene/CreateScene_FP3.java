package com.android.csiapp.Crime.createscene;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.csiapp.Crime.utils.CreateSceneUtils;
import com.android.csiapp.Crime.utils.DateTimePicker;
import com.android.csiapp.Crime.utils.DictionaryInfo;
import com.android.csiapp.Crime.utils.adapter.PhotoAdapter;
import com.android.csiapp.Crime.utils.PriviewPhotoActivity;
import com.android.csiapp.Crime.utils.adapter.PhotoAdapterExtern;
import com.android.csiapp.Databases.CrimeItem;
import com.android.csiapp.Databases.CrimeProvider;
import com.android.csiapp.Databases.FlatProvider;
import com.android.csiapp.Databases.PhotoItem;
import com.android.csiapp.Databases.PositionProvider;
import com.android.csiapp.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateScene_FP3 extends Fragment {

    private Context context = null;
    private CrimeItem mItem;
    private int mEvent;

    private List<PhotoItem> mPositionList, mFlatList;
    private ListView mPosition_List, mFlat_List;
    private PhotoAdapter mPosition_Adapter, mFlat_Adapter;
    private LinearLayout mAdd_Position, mAdd_Flat;

    private PositionProvider mPositionProvider;
    private FlatProvider mFlatProvider;

    public CreateScene_FP3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_scene_fp3, container, false);
        CreateSceneActivity activity  = (CreateSceneActivity) getActivity();
        if(savedInstanceState == null){
            mItem = activity.getItem();
            mEvent = activity.getEvent();
        }else{
            mItem = (CrimeItem) savedInstanceState.getSerializable("CrimeItem");
            mEvent = (int) savedInstanceState.getSerializable("Event");
        }
        context = getActivity().getApplicationContext();

        initData();
        initView(view);

        mPositionProvider = new PositionProvider(context);
        mFlatProvider = new FlatProvider(context);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("CrimeItem",mItem);
        outState.putSerializable("Event",mEvent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            savedInstanceState.putSerializable("CrimeItem", mItem);
            savedInstanceState.putSerializable("Event", mEvent);
        }
    }

    private void initView(View view){
        mAdd_Position = (LinearLayout) view.findViewById(R.id.add_position);
        mAdd_Position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), CreateScene_FP3_NewPositionActivity_Amap.class);
                startActivityForResult(it, CreateSceneUtils.EVENT_NEW_POSITION);
            }
        });

        mPositionList = mItem.getPosition();
        mPosition_List=(ListView) view.findViewById(R.id.position_listview);
        mPosition_Adapter = new PhotoAdapter(context, mPositionList);
        mPosition_Adapter.setShowPhotoInfo(true);
        mPosition_Adapter.setOnPhotoUpdataInfoListen(new PhotoAdapter.PhotoUpdataInfo() {
            @Override
            public void onPhotoUpdataInfo(int pos, PhotoItem item) {
                if (mPositionProvider != null)
                mPositionProvider.update(mPositionList.get(pos).getId(), item);
            }
        });
        mPosition_List.setAdapter(mPosition_Adapter);
        /*/
        mPosition_List.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                Intent it = new Intent(getActivity(), PriviewPhotoActivity.class);
                it.putExtra("Path",mPositionList.get(position).getPhotoPath());
                startActivity(it);
            }
        });
        //*/
        CreateSceneUtils.setListViewHeightBasedOnChildren(mPosition_List);

        mAdd_Flat = (LinearLayout) view.findViewById(R.id.add_flat);
        mAdd_Flat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //调用cad程序=============================
                //使用当前时间作为文件名
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String date = sDateFormat.format(new java.util.Date());
                Intent myIntent = new Intent(getActivity(), MxCADAppActivity.class);
                String path=GetFlatDir();
                myIntent.putExtra("file",path+File.separator+"Flat_"+ date);
                myIntent.putExtra("path",path);
                startActivityForResult(myIntent, CreateSceneUtils.EVENT_NEW_FLAT);

                //Intent it = new Intent(getActivity(), CreateScene_FP3_PositionInformationActivity.class);
                //it.putExtra("com.android.csiapp.Databases.CrimeItem", mItem);
                //it.putExtra("Add","Flat");
                //startActivityForResult(it, CreateSceneUtils.EVENT_NEW_FLAT);
            }
        });

        mFlatList = mItem.getFlat();
        mFlat_List=(ListView) view.findViewById(R.id.flat_listview);
        mFlat_Adapter = new PhotoAdapter(context, mFlatList);
        mFlat_Adapter.setShowPhotoInfo(true);
        mFlat_Adapter.setOnPhotoUpdataInfoListen(new PhotoAdapter.PhotoUpdataInfo() {
            @Override
            public void onPhotoUpdataInfo(int pos, PhotoItem item) {
                if (mFlatProvider != null)
                mFlatProvider.update(mFlatList.get(pos).getId(), item);
            }
        });
        mFlat_List.setAdapter(mFlat_Adapter);
        /*/
        mFlat_List.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                Intent it = new Intent(getActivity(), PriviewPhotoActivity.class);
                it.putExtra("Path",mFlatList.get(position).getPhotoPath());
                startActivity(it);
            }
        });
        //*/
        CreateSceneUtils.setListViewHeightBasedOnChildren(mFlat_List);

        registerForContextMenu(mPosition_List);
        registerForContextMenu(mFlat_List);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        String delete = context.getResources().getString(R.string.list_delete);
        if (v.getId()==R.id.position_listview) {
            menu.add(0, CreateSceneUtils.EVENT_POSITION_DELETE, 0, delete);
        }else if(v.getId()==R.id.flat_listview){
            menu.add(0, CreateSceneUtils.EVENT_FLAT_DELETE, 0, delete);
            //=========================增加平面图修改菜单
            menu.add(1, CreateSceneUtils.EVENT_FLAT_EDIT, 0, "修改");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case CreateSceneUtils.EVENT_POSITION_DELETE:
                mPositionProvider.delete(mPositionList.get(info.position).getId());
                mPositionList.remove(info.position);
                CreateSceneUtils.setListViewHeightBasedOnChildren(mPosition_List);
                mPosition_Adapter.notifyDataSetChanged();
                return true;
            case CreateSceneUtils.EVENT_FLAT_DELETE:
                mFlatProvider.delete(mFlatList.get(info.position).getId());
                mFlatList.remove(info.position);
                CreateSceneUtils.setListViewHeightBasedOnChildren(mFlat_List);
                mFlat_Adapter.notifyDataSetChanged();
                return true;
            case CreateSceneUtils.EVENT_FLAT_EDIT:
                //修改，调用cad程序=============================
                Intent myIntent = new Intent(getActivity(), MxCADAppActivity.class);
                String file=mFlatList.get(info.position).getPhotoPath();
                String path=GetFlatDir();
                myIntent.putExtra("path",path);
                myIntent.putExtra("file",file.replaceAll(".png",""));
                startActivityForResult(myIntent, CreateSceneUtils.EVENT_EDIT_FLAT);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void initData(){
        mPositionList = mItem.getPosition();
        mFlatList = mItem.getFlat();
    }

    public void saveData(){
        mItem.setPosition(mPositionList);
        mItem.setFlat(mFlatList);
    }

    @Override
    public void onResume(){
        super.onResume();
        initData();
        mPosition_Adapter.notifyDataSetChanged();
        mFlat_Adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause(){
        super.onPause();
        saveData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == CreateSceneUtils.EVENT_NEW_POSITION) {
                // 新增記事資料到資料庫
                PhotoItem positionItem = new PhotoItem();
                positionItem.setPhotoPath(data.getStringExtra("Map_ScreenShot"));
                positionItem.setUuid(CrimeProvider.getUUID());


                String info = DateTimePicker.getCurrentDate(mItem.getAccessStartTime())
                        + DictionaryInfo.getDictValue(DictionaryInfo.mAreaKey,mItem.getArea())
                        + DictionaryInfo.getDictValue(DictionaryInfo.mCaseTypeKey, mItem.getCasetype());
                if (info != null && !info.isEmpty()) {
                    positionItem.setPhotoInfo(info + "" + context.getResources().getString(R.string.default_info));
                } else {
                    positionItem.setPhotoInfo("");
                }

                //Add lat on lon
                String gpsLat = (String) data.getStringExtra("gpsLat");
                String gpsLon = (String) data.getStringExtra("gpsLon");
                if(!gpsLat.isEmpty()) mItem.setGpsLat(gpsLat);
                if(!gpsLon.isEmpty()) mItem.setGpsLon(gpsLon);

                positionItem.setId(mPositionProvider.insert(positionItem));
                mPositionList.add(positionItem);
                CreateSceneUtils.setListViewHeightBasedOnChildren(mPosition_List);
                mPosition_Adapter.notifyDataSetChanged();
            }else if(requestCode == CreateSceneUtils.EVENT_NEW_FLAT){
                //新增平面图=============================
                int isSaved=data.getIntExtra("isSaved",2);
                if(isSaved==1) {
                    // 新增記事資料到資料庫
                    PhotoItem flatItem = (PhotoItem) data.getSerializableExtra("com.android.csiapp.Databases.PhotoItem");
                    String info = DateTimePicker.getCurrentDate(mItem.getAccessStartTime())
                            + DictionaryInfo.getDictValue(DictionaryInfo.mAreaKey,mItem.getArea())
                            + DictionaryInfo.getDictValue(DictionaryInfo.mCaseTypeKey, mItem.getCasetype());
                    if (info != null && !info.isEmpty()) {
                        flatItem.setPhotoInfo(info + "" + context.getResources().getString(R.string.default_info2));
                    } else {
                        flatItem.setPhotoInfo("");
                    }
                    flatItem.setId(mFlatProvider.insert(flatItem));
                    mFlatList.add(flatItem);
                    CreateSceneUtils.setListViewHeightBasedOnChildren(mFlat_List);
                    mFlat_Adapter.notifyDataSetChanged();
                }
            }else if(requestCode == CreateSceneUtils.EVENT_EDIT_FLAT){
                //修改,不需要做任何操作
            }
        }
    }

    private String GetFlatDir(){
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "FlatPic");
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String dirMxDraw = mediaStorageDir.getPath();
        return dirMxDraw;
    }
}
