package com.android.csiapp.Crime.utils.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.csiapp.Crime.utils.ClearableEditText;
import com.android.csiapp.Crime.utils.CreateSceneUtils;
import com.android.csiapp.Crime.utils.PriviewPhotoActivity;
import com.android.csiapp.Databases.PhotoItem;
import com.android.csiapp.R;

import java.io.File;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by zwb on 28/06/18.
 */

public class PhotoAdapterExtern extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<PhotoItem> items;
    private Bitmap bp = null;
    private ViewHolder holder =null;
    private Context mContext;

    private PhotoAdapter.PhotoUpdataInfo updataInfo;
    private boolean showPhotoInfo = false;

    public PhotoAdapterExtern(Context context, List<PhotoItem> items){
        mContext = context;
        myInflater = LayoutInflater.from(context);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = myInflater.inflate(R.layout.photoadapterextern, null);
            holder = new ViewHolder(
                    (ImageView) convertView.findViewById(R.id.photo));
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final PhotoItem item = (PhotoItem)getItem(position);
        final String path = item.getPhotoPath();
        if(!path.isEmpty()){
            bp = CreateSceneUtils.loadBitmapFromFile(new File(path));
            if(bp!=null) holder.txtItemPhoto.setImageBitmap(bp);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView txtItemPhoto;
        public ViewHolder(ImageView txtItemPhoto){
            this.txtItemPhoto = txtItemPhoto;
        }
    }
}
