package com.android.csiapp.Crime.createscene;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.csiapp.Crime.utils.adapter.WitnessListAdapter;
import com.android.csiapp.Databases.CrimeItem;
import com.android.csiapp.Databases.WitnessItem;
import com.android.csiapp.Databases.WitnessProvider;
import com.android.csiapp.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateScene_FP8 extends Fragment {

    private Context context = null;
    private CrimeItem mItem;
    private WitnessItem mWitnessItem;
    private int mEvent;

    private List<WitnessItem> mWitnessList;
    private ListView mWitness_list;
    private WitnessListAdapter mWitness_adapter;
    private LinearLayout mAddWitness;

    private WitnessProvider mWitnessProvider;

    public CreateScene_FP8() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_scene_fp8, container, false);
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

        mWitnessProvider = new WitnessProvider(context);

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
        mWitnessItem = new WitnessItem();
        mAddWitness = (LinearLayout) view.findViewById(R.id.add_witness_button);
        mAddWitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), CreateScene_FP8_AddWitnessActivity.class);
                it.putExtra("com.android.csiapp.Databases.WitnessItem", mWitnessItem);
                it.putExtra("Event",1);
                startActivityForResult(it, CreateSceneUtils.EVENT_NEW_WITNESS);
            }
        });

        mWitnessList = mItem.getWitness();
        mWitness_list=(ListView) view.findViewById(R.id.listView);
        mWitness_adapter = new WitnessListAdapter(context, mWitnessList);
        mWitness_list.setAdapter(mWitness_adapter);
        CreateSceneUtils.setListViewHeightBasedOnChildren(mWitness_list);
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                WitnessItem witenssItem = (WitnessItem) mWitness_adapter.getItem(position);
                Intent it = new Intent(getActivity(), CreateScene_FP8_AddWitnessActivity.class);
                it.putExtra("com.android.csiapp.Databases.WitnessItem", witenssItem);
                it.putExtra("Event",2);
                startActivityForResult(it, CreateSceneUtils.EVENT_NEW_WITNESS);
            }
        };
        mWitness_list.setOnItemClickListener(itemListener);

        registerForContextMenu(mWitness_list);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        String delete = context.getResources().getString(R.string.list_delete);
        if (v.getId()==R.id.listView) {
            menu.add(0, CreateSceneUtils.EVENT_WITNESS_DELETE, 0, delete);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case CreateSceneUtils.EVENT_WITNESS_DELETE:
                mWitnessProvider.delete(mWitnessList.get(info.position).getId());
                mWitnessList.remove(info.position);
                CreateSceneUtils.setListViewHeightBasedOnChildren(mWitness_list);
                mWitness_adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void initData(){
        mWitnessList = mItem.getWitness();
    }

    public void saveData(){
        mItem.setWitness(mWitnessList);
    }

    @Override
    public void onResume(){
        super.onResume();
        initData();
        mWitness_adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause(){
        super.onPause();
        saveData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == CreateSceneUtils.EVENT_NEW_WITNESS) {
                WitnessItem witenssItem = (WitnessItem) data.getSerializableExtra("com.android.csiapp.Databases.WitnessItem");
                int event = (int) data.getIntExtra("Event", 1);
                // 新增記事資料到資料庫
                if (event == 1) {
                    witenssItem.setId(mWitnessProvider.insert(witenssItem));
                    mWitnessList.add(witenssItem);
                } else {
                    int position = (int) data.getIntExtra("Position", 0);
                    mWitnessList.set(position, witenssItem);
                }
                CreateSceneUtils.setListViewHeightBasedOnChildren(mWitness_list);
                mWitness_adapter.notifyDataSetChanged();
            }
        }
    }
}
