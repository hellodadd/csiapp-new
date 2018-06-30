package com.android.csiapp.Crime.createscene;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.android.csiapp.Crime.utils.ImageCompress;
import com.android.csiapp.Crime.utils.adapter.EvidenceAdapter;
import com.android.csiapp.Crime.utils.adapter.PhotoAdapter;
import com.android.csiapp.Crime.utils.PriviewPhotoActivity;
import com.android.csiapp.Databases.CameraPhotoProvider;
import com.android.csiapp.Databases.CrimeItem;
import com.android.csiapp.Databases.CrimeProvider;
import com.android.csiapp.Databases.EvidenceItem;
import com.android.csiapp.Databases.EvidenceProvider;
import com.android.csiapp.Databases.MonitoringPhotoProvider;
import com.android.csiapp.Databases.PhotoItem;
import com.android.csiapp.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateScene_FP5 extends Fragment {
    private Context context = null;
    private Uri LocalFileUri = null;
    private CrimeItem mItem;
    private EvidenceItem mEvidenceItem;
    private int mEvent;

    private List<EvidenceItem> mEvidenceList;
    private List<PhotoItem> mMonitoringList, mCameraList;
    private ListView mEvidence_List, mMonitoring_List, mCamera_List;
    private EvidenceAdapter mEvidence_Adapter;
    private PhotoAdapter mMonitoring_Adapter, mCamera_Adapter;
    private LinearLayout mAdd_Scene_Evidence, mAdd_Monitoring, mAdd_Camera;

    private EvidenceProvider mEvidenceProvider;
    private MonitoringPhotoProvider mMonitoringPhotoProvider;
    private CameraPhotoProvider mCameraPhotoProvider;

    public CreateScene_FP5() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_scene_fp5, container, false);
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

        mEvidenceProvider = new EvidenceProvider(context);
        mMonitoringPhotoProvider = new MonitoringPhotoProvider(context);
        mCameraPhotoProvider = new CameraPhotoProvider(context);

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
        mEvidenceItem = new EvidenceItem();
        mAdd_Scene_Evidence = (LinearLayout) view.findViewById(R.id.add_scene_evidence);
        mAdd_Scene_Evidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), CreateScene_FP5_NewEvidenceActivity.class);
                it.putExtra("com.android.csiapp.Databases.Item", mItem);
                it.putExtra("com.android.csiapp.Databases.EvidenceItem", mEvidenceItem);
                it.putExtra("Event",1);
                startActivityForResult(it, CreateSceneUtils.EVENT_TYPE_EVIDENCE);
            }
        });

        mEvidenceList = mItem.getEvidenceItem();
        mEvidence_List=(ListView) view.findViewById(R.id.evidence_listview);
        mEvidence_Adapter = new EvidenceAdapter(context, mEvidenceList);
        mEvidence_Adapter.setShowPhotoInfo(true);
        mEvidence_Adapter.setPhotoTouchListen(new EvidenceAdapter.OnPhotoTouchListen() {
            @Override
            public void onPhotoTouch(int pos) {
                Intent it = new Intent(getActivity(), CreateScene_FP5_NewEvidenceActivity.class);
                mEvidenceItem = mEvidenceList.get(pos);
                it.putExtra("com.android.csiapp.Databases.Item", mItem);
                it.putExtra("com.android.csiapp.Databases.EvidenceItem", mEvidenceItem);
                it.putExtra("Event",2);
                it.putExtra("Position", pos);
                startActivityForResult(it, CreateSceneUtils.EVENT_TYPE_EVIDENCE);
            }
        });
        mEvidence_Adapter.setOnPhotoUpdataInfoListen(new EvidenceAdapter.PhotoUpdataInfo() {
            @Override
            public void onPhotoUpdataInfo(int pos, EvidenceItem item) {
                mEvidenceProvider.update(mEvidenceList.get(pos).getId(), item);
            }
        });
        mEvidence_List.setAdapter(mEvidence_Adapter);
        CreateSceneUtils.setListViewHeightBasedOnChildren(mEvidence_List);
        AdapterView.OnItemClickListener itemListener1 = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(getActivity(), CreateScene_FP5_NewEvidenceActivity.class);
                mEvidenceItem = mEvidenceList.get(position);
                it.putExtra("com.android.csiapp.Databases.Item", mItem);
                it.putExtra("com.android.csiapp.Databases.EvidenceItem", mEvidenceItem);
                it.putExtra("Event",2);
                it.putExtra("Position", position);
                startActivityForResult(it, CreateSceneUtils.EVENT_TYPE_EVIDENCE);
            }
        };
        //mEvidence_List.setOnItemClickListener(itemListener1);

        mAdd_Monitoring = (LinearLayout) view.findViewById(R.id.add_monitoring_screen);
        mAdd_Monitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalFileUri = Uri.fromFile(CreateSceneUtils.getOutputMediaFile(context, CreateSceneUtils.EVENT_PHOTO_TYPE_MONITORING));
                takePhoto(LocalFileUri, CreateSceneUtils.EVENT_PHOTO_TYPE_MONITORING);
            }
        });

        mMonitoringList = mItem.getMonitoringPhoto();
        mMonitoring_List=(ListView) view.findViewById(R.id.monitoring_photo_listview);
        mMonitoring_Adapter = new PhotoAdapter(context, mMonitoringList);
        mMonitoring_Adapter.setShowPhotoInfo(true);
        mMonitoring_Adapter.setOnPhotoUpdataInfoListen(new PhotoAdapter.PhotoUpdataInfo() {
            @Override
            public void onPhotoUpdataInfo(int pos, PhotoItem item) {
                mMonitoringPhotoProvider.update(mMonitoringList.get(pos).getId(), item);
                //mMonitoring_Adapter.notifyDataSetChanged();
            }
        });
        mMonitoring_List.setAdapter(mMonitoring_Adapter);
        /*/
        mMonitoring_List.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                Intent it = new Intent(getActivity(), PriviewPhotoActivity.class);
                it.putExtra("Path",mMonitoringList.get(position).getPhotoPath());
                startActivity(it);
            }
        });
        //*/
        CreateSceneUtils.setListViewHeightBasedOnChildren(mMonitoring_List);

        mAdd_Camera = (LinearLayout) view.findViewById(R.id.add_camera_position);
        mAdd_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalFileUri = Uri.fromFile(CreateSceneUtils.getOutputMediaFile(context, CreateSceneUtils.EVENT_PHOTO_TYPE_CAMERA));
                takePhoto(LocalFileUri, CreateSceneUtils.EVENT_PHOTO_TYPE_CAMERA);
            }
        });

        mCameraList = mItem.getCameraPhoto();
        mCamera_List=(ListView) view.findViewById(R.id.camera_photo_listview);
        mCamera_Adapter = new PhotoAdapter(context, mCameraList);
        mCamera_Adapter.setShowPhotoInfo(true);
        mCamera_Adapter.setOnPhotoUpdataInfoListen(new PhotoAdapter.PhotoUpdataInfo() {
            @Override
            public void onPhotoUpdataInfo(int pos, PhotoItem item) {
                mCameraPhotoProvider.update(mCameraList.get(pos).getId(), item);
                //mCamera_Adapter.notifyDataSetChanged();
            }
        });
        mCamera_List.setAdapter(mCamera_Adapter);
        /*/
        mCamera_List.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                Intent it = new Intent(getActivity(), PriviewPhotoActivity.class);
                it.putExtra("Path",mCameraList.get(position).getPhotoPath());
                startActivity(it);
            }
        });
        //*/
        CreateSceneUtils.setListViewHeightBasedOnChildren(mCamera_List);

        registerForContextMenu(mEvidence_List);
        registerForContextMenu(mMonitoring_List);
        registerForContextMenu(mCamera_List);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        String delete = context.getResources().getString(R.string.list_delete);
        if (v.getId()==R.id.evidence_listview) {
            menu.add(0, CreateSceneUtils.EVENT_EVIDENCE_DELETE, 0, delete);
        }else if(v.getId()==R.id.monitoring_photo_listview){
            menu.add(0, CreateSceneUtils.EVENT_MONITORING_DELETE, 0, delete);
        }else if(v.getId()==R.id.camera_photo_listview){
            menu.add(0, CreateSceneUtils.EVENT_CAMERA_DELETE, 0, delete);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case CreateSceneUtils.EVENT_EVIDENCE_DELETE:
                mEvidenceProvider.delete(mEvidenceList.get(info.position).getId());
                mEvidenceList.remove(info.position);
                CreateSceneUtils.setListViewHeightBasedOnChildren(mEvidence_List);
                mEvidence_Adapter.notifyDataSetChanged();
                return true;
            case CreateSceneUtils.EVENT_MONITORING_DELETE:
                mMonitoringPhotoProvider.delete(mMonitoringList.get(info.position).getId());
                mMonitoringList.remove(info.position);
                CreateSceneUtils.setListViewHeightBasedOnChildren(mMonitoring_List);
                mMonitoring_Adapter.notifyDataSetChanged();
                return true;
            case CreateSceneUtils.EVENT_CAMERA_DELETE:
                mCameraPhotoProvider.delete(mCameraList.get(info.position).getId());
                mCameraList.remove(info.position);
                CreateSceneUtils.setListViewHeightBasedOnChildren(mCamera_List);
                mCamera_Adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void initData(){
        mEvidenceList = mItem.getEvidenceItem();
        mMonitoringList = mItem.getMonitoringPhoto();
        mCameraList = mItem.getCameraPhoto();
    }

    public void saveData(){
        mItem.setEvidenceItem(mEvidenceList);
        mItem.setMonitoringPhoto(mMonitoringList);
        mItem.setCameraPhoto(mCameraList);
    }

    @Override
    public void onResume(){
        super.onResume();
        initData();
        mEvidence_Adapter.notifyDataSetChanged();
        mMonitoring_Adapter.notifyDataSetChanged();
        mCamera_Adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause(){
        super.onPause();
        saveData();
    }

    private void takePhoto(Uri LocalFileUri, int PHOTO_TYPE) {
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, LocalFileUri);
        startActivityForResult(it, PHOTO_TYPE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == CreateSceneUtils.EVENT_TYPE_EVIDENCE) {
                EvidenceItem evidenceItem = (EvidenceItem) data.getSerializableExtra("com.android.csiapp.Databases.EvidenceItem");
                int event = (int) data.getIntExtra("Event", 1);
                int position = (int) data.getIntExtra("Position", 0);
                evidenceItem.setPhotoInfo("");
                if (event == 1) {
                    evidenceItem.setId(mEvidenceProvider.insert(evidenceItem));
                    mEvidenceList.add(evidenceItem);
                } else {
                    mEvidenceList.set(position, evidenceItem);
                }
                CreateSceneUtils.setListViewHeightBasedOnChildren(mEvidence_List);
                mEvidence_Adapter.notifyDataSetChanged();
            } else if (requestCode == CreateSceneUtils.EVENT_PHOTO_TYPE_MONITORING) {
                String path = LocalFileUri.getPath();

                //进行图片压缩，压缩比例为1600*1600
                int width= Integer.parseInt(context.getResources().getString(R.string.image_reqWidth));
                int height= Integer.parseInt(context.getResources().getString(R.string.image_reqHeight));
                path= ImageCompress.getSmallBitmap(path,width,height);

                PhotoItem photoItem = new PhotoItem();
                photoItem.setPhotoPath(path);
                photoItem.setUuid(CrimeProvider.getUUID());
                photoItem.setPhotoInfo("");
                Log.d("Camera", "Set image to PHOTO_TYPE_MONITORING");
                photoItem.setId(mMonitoringPhotoProvider.insert(photoItem));
                mMonitoringList.add(photoItem);
                CreateSceneUtils.setListViewHeightBasedOnChildren(mMonitoring_List);
                mMonitoring_Adapter.notifyDataSetChanged();
            } else if (requestCode == CreateSceneUtils.EVENT_PHOTO_TYPE_CAMERA) {
                String path = LocalFileUri.getPath();
                //进行图片压缩，压缩比例为1600*1600
                int width= Integer.parseInt(context.getResources().getString(R.string.image_reqWidth));
                int height= Integer.parseInt(context.getResources().getString(R.string.image_reqHeight));
                path=ImageCompress.getSmallBitmap(path,width,height);

                PhotoItem photoItem = new PhotoItem();
                photoItem.setPhotoPath(path);
                photoItem.setUuid(CrimeProvider.getUUID());
                photoItem.setPhotoInfo("");
                Log.d("Camera", "Set image to PHOTO_TYPE_CAMERA");
                photoItem.setId(mCameraPhotoProvider.insert(photoItem));
                mCameraList.add(photoItem);
                CreateSceneUtils.setListViewHeightBasedOnChildren(mCamera_List);
                mCamera_Adapter.notifyDataSetChanged();
            }
        }
    }
}
