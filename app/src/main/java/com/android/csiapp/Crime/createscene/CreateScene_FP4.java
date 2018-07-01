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
import com.android.csiapp.Crime.utils.adapter.PhotoAdapter;
import com.android.csiapp.Crime.utils.PriviewPhotoActivity;
import com.android.csiapp.Crime.utils.ImageCompress;
import com.android.csiapp.Databases.CrimeItem;
import com.android.csiapp.Databases.CrimeProvider;
import com.android.csiapp.Databases.ImportantPhotoProvider;
import com.android.csiapp.Databases.OverviewPhotoProvider;
import com.android.csiapp.Databases.PhotoItem;
import com.android.csiapp.Databases.PositionPhotoProvider;
import com.android.csiapp.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateScene_FP4 extends Fragment {

    private Context context = null;
    private Uri LocalFileUri = null;
    private CrimeItem mItem;
    private int mEvent;

    private List<PhotoItem> mPositionList, mLikeList, mImportantList;
    private ListView mPosition_List, mLike_List, mImportant_List;
    private PhotoAdapter mPosition_Adapter, mLike_Adapter, mImportant_Adapter;
    private LinearLayout mAdd_Position, mAdd_Like, mAdd_Important;

    private PositionPhotoProvider mPositionPhotoProvider;
    private OverviewPhotoProvider mOverviewPhotoProvider;
    private ImportantPhotoProvider mImportantPhotoProvider;

    public CreateScene_FP4() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_scene_fp4, container, false);
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

        mPositionPhotoProvider = new PositionPhotoProvider(context);
        mOverviewPhotoProvider = new OverviewPhotoProvider(context);
        mImportantPhotoProvider = new ImportantPhotoProvider(context);

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
        mAdd_Position = (LinearLayout) view.findViewById(R.id.add_position_photo_imageButton);
        mAdd_Position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalFileUri = Uri.fromFile(CreateSceneUtils.getOutputMediaFile(context, CreateSceneUtils.EVENT_PHOTO_TYPE_POSITION));
                takePhoto(LocalFileUri, CreateSceneUtils.EVENT_PHOTO_TYPE_POSITION);
            }
        });

        mPositionList = mItem.getPositionPhoto();
        mPosition_List=(ListView) view.findViewById(R.id.Position_photo_listview);
        mPosition_Adapter = new PhotoAdapter(context, mPositionList);
        mPosition_Adapter.setShowPhotoInfo(true);
        mPosition_Adapter.setOnPhotoUpdataInfoListen(new PhotoAdapter.PhotoUpdataInfo() {
            @Override
            public void onPhotoUpdataInfo(int pos, PhotoItem item) {
                if (mPositionPhotoProvider != null)
                mPositionPhotoProvider.update(mPositionList.get(pos).getId(), item);
                //mPosition_Adapter.notifyDataSetChanged();
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

        mAdd_Like = (LinearLayout) view.findViewById(R.id.add_like_photo_imageButton);
        mAdd_Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalFileUri = Uri.fromFile(CreateSceneUtils.getOutputMediaFile(context, CreateSceneUtils.EVENT_PHOTO_TYPE_LIKE));
                takePhoto(LocalFileUri, CreateSceneUtils.EVENT_PHOTO_TYPE_LIKE);
            }
        });

        mLikeList = mItem.getOverviewPhoto();
        mLike_List=(ListView) view.findViewById(R.id.Like_photo_listview);
        mLike_Adapter = new PhotoAdapter(context, mLikeList);
        mLike_Adapter.setShowPhotoInfo(true);
        mLike_Adapter.setOnPhotoUpdataInfoListen(new PhotoAdapter.PhotoUpdataInfo() {
            @Override
            public void onPhotoUpdataInfo(int pos, PhotoItem item) {
                if (mOverviewPhotoProvider != null)
                mOverviewPhotoProvider.update(mLikeList.get(pos).getId(), item);
                //mLike_Adapter.notifyDataSetChanged();
            }
        });
        mLike_List.setAdapter(mLike_Adapter);
        /*/
        mLike_List.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                Intent it = new Intent(getActivity(), PriviewPhotoActivity.class);
                it.putExtra("Path",mLikeList.get(position).getPhotoPath());
                startActivity(it);
            }
        });
        //*/
        CreateSceneUtils.setListViewHeightBasedOnChildren(mLike_List);

        mAdd_Important = (LinearLayout) view.findViewById(R.id.add_important_photo_imageButton);
        mAdd_Important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalFileUri = Uri.fromFile(CreateSceneUtils.getOutputMediaFile(context, CreateSceneUtils.EVENT_PHOTO_TYPE_IMPORTANT));
                takePhoto(LocalFileUri, CreateSceneUtils.EVENT_PHOTO_TYPE_IMPORTANT);
            }
        });

        mImportantList = mItem.getImportantPhoto();
        mImportant_List=(ListView) view.findViewById(R.id.Important_photo_listview);
        mImportant_Adapter = new PhotoAdapter(context, mImportantList);
        mImportant_Adapter.setShowPhotoInfo(true);
        mImportant_Adapter.setOnPhotoUpdataInfoListen(new PhotoAdapter.PhotoUpdataInfo() {
            @Override
            public void onPhotoUpdataInfo(int pos, PhotoItem item) {
                if (mImportantPhotoProvider != null)
                mImportantPhotoProvider.update(mImportantList.get(pos).getId(), item);
                //mImportant_Adapter.notifyDataSetChanged();
            }
        });
        mImportant_List.setAdapter(mImportant_Adapter);
        /*/
        mImportant_List.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                Intent it = new Intent(getActivity(), PriviewPhotoActivity.class);
                it.putExtra("Path",mImportantList.get(position).getPhotoPath());
                startActivity(it);
            }
        });
        //*/
        CreateSceneUtils.setListViewHeightBasedOnChildren(mImportant_List);

        registerForContextMenu(mPosition_List);
        registerForContextMenu(mLike_List);
        registerForContextMenu(mImportant_List);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        String delete = context.getResources().getString(R.string.list_delete);
        if (v.getId()==R.id.Position_photo_listview) {
            menu.add(0, CreateSceneUtils.EVENT_POSITION_PHOTO_DELETE, 0, delete);
        }else if(v.getId()==R.id.Like_photo_listview){
            menu.add(0, CreateSceneUtils.EVENT_LIKE_PHOTO_DELETE, 0, delete);
        }else if(v.getId()==R.id.Important_photo_listview){
            menu.add(0, CreateSceneUtils.EVENT_IMPORTANT_PHOTO_DELETE, 0, delete);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case CreateSceneUtils.EVENT_POSITION_PHOTO_DELETE:
                mPositionPhotoProvider.delete(mPositionList.get(info.position).getId());
                mPositionList.remove(info.position);
                CreateSceneUtils.setListViewHeightBasedOnChildren(mPosition_List);
                mPosition_Adapter.notifyDataSetChanged();
                return true;
            case CreateSceneUtils.EVENT_LIKE_PHOTO_DELETE:
                mOverviewPhotoProvider.delete(mLikeList.get(info.position).getId());
                mLikeList.remove(info.position);
                CreateSceneUtils.setListViewHeightBasedOnChildren(mLike_List);
                mLike_Adapter.notifyDataSetChanged();
                return true;
            case CreateSceneUtils.EVENT_IMPORTANT_PHOTO_DELETE:
                mImportantPhotoProvider.delete(mImportantList.get(info.position).getId());
                mImportantList.remove(info.position);
                CreateSceneUtils.setListViewHeightBasedOnChildren(mImportant_List);
                mImportant_Adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void initData(){
        mPositionList = mItem.getPositionPhoto();
        mLikeList = mItem.getOverviewPhoto();
        mImportantList = mItem.getImportantPhoto();
    }

    public void saveData(){
        mItem.setPositionPhoto(mPositionList);
        mItem.setOverviewPhoto(mLikeList);
        mItem.setImportantPhoto(mImportantList);
    }

    @Override
    public void onResume(){
        super.onResume();
        initData();
        mPosition_Adapter.notifyDataSetChanged();
        mLike_Adapter.notifyDataSetChanged();
        mImportant_Adapter.notifyDataSetChanged();
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
        super.onActivityResult(requestCode, resultCode, data);

        String path = LocalFileUri.getPath();
        PhotoItem photoItem = new PhotoItem();
        //进行图片压缩，压缩比例为1600*1600
        int width= Integer.parseInt(context.getResources().getString(R.string.image_reqWidth));
        int height= Integer.parseInt(context.getResources().getString(R.string.image_reqHeight));
        path=ImageCompress.getSmallBitmap(path,width,height);

        photoItem.setPhotoPath(path);
        photoItem.setUuid(CrimeProvider.getUUID());
        photoItem.setPhotoInfo("");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CreateSceneUtils.EVENT_PHOTO_TYPE_POSITION) {
                Log.d("Camera", "Set image to PHOTO_TYPE_POSITION");
                photoItem.setId(mPositionPhotoProvider.insert(photoItem));
                mPositionList.add(photoItem);
                CreateSceneUtils.setListViewHeightBasedOnChildren(mPosition_List);
                mPosition_Adapter.notifyDataSetChanged();
            } else if (requestCode == CreateSceneUtils.EVENT_PHOTO_TYPE_LIKE) {
                Log.d("Camera", "Set image to PHOTO_TYPE_LIKE");
                photoItem.setId(mOverviewPhotoProvider.insert(photoItem));
                mLikeList.add(photoItem);
                CreateSceneUtils.setListViewHeightBasedOnChildren(mLike_List);
                mLike_Adapter.notifyDataSetChanged();
            } else if (requestCode == CreateSceneUtils.EVENT_PHOTO_TYPE_IMPORTANT) {
                Log.d("Camera", "Set image to PHOTO_TYPE_IMPORTANT");
                photoItem.setId(mImportantPhotoProvider.insert(photoItem));
                mImportantList.add(photoItem);
                CreateSceneUtils.setListViewHeightBasedOnChildren(mImportant_List);
                mImportant_Adapter.notifyDataSetChanged();
            }
        }
    }

}
