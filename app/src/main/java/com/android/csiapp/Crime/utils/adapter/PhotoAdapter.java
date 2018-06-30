package com.android.csiapp.Crime.utils.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.csiapp.Crime.utils.ClearableEditText;
import com.android.csiapp.Crime.utils.CreateSceneUtils;
import com.android.csiapp.Crime.utils.PriviewPhotoActivity;
import com.android.csiapp.Databases.PhotoItem;
import com.android.csiapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by user on 2016/10/7.
 */
public class PhotoAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<PhotoItem> items;
    private Bitmap bp = null;
    private ViewHolder holder =null;
    private Context mContext;

    private PhotoUpdataInfo updataInfo;
    private boolean showPhotoInfo = false;

    private String defaultInfo = null;

    public PhotoAdapter(Context context, List<PhotoItem> items){
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
            convertView = myInflater.inflate(R.layout.photoadapterview, null);
            ClearableEditText editText = (ClearableEditText) convertView.findViewById(R.id.photo_edit);
            holder = new ViewHolder(
                    (ImageView) convertView.findViewById(R.id.photo), editText);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final PhotoItem item = (PhotoItem)getItem(position);
        final String path = item.getPhotoPath();
        if(!path.isEmpty()){
            bp = CreateSceneUtils.loadBitmapFromFile(new File(path));
            if(bp!=null) holder.txtItemPhoto.setImageBitmap(bp);
            if (!showPhotoInfo) {
                holder.txtItemPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent it = new Intent(mContext, PriviewPhotoActivity.class);
                        it.putExtra("Path", path);
                        it.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(it);
                    }
                });
            }
        }

        String info = item.getPhotoInfo();
        if (info != null && !info.isEmpty()) {
            holder.editText.setText(info);
        } else {
          if (defaultInfo != null) {
              holder.editText.setText(defaultInfo);
          }
        }

        final int pos = position;

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    item.setPhotoInfo("");
                } else {
                    item.setPhotoInfo(s.toString());
                }
                if (updataInfo != null) {
                    updataInfo.onPhotoUpdataInfo(pos,item);
                }
            }
        };

        holder.editText.addTextChangedListener(watcher);
        holder.editText.setTag(watcher);
        holder.editText.setHint(mContext.getResources().getString(R.string.photo_edit_hint));
        holder.editText.setTextColor(0xff3e3e3e);
        holder.editText.setTextHintColor(Color.GRAY);
        holder.editText.setMaxLines(2);

        if (!showPhotoInfo) {
            holder.editText.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView txtItemPhoto;
        ClearableEditText editText;
        public ViewHolder(ImageView txtItemPhoto, ClearableEditText editText){
            this.txtItemPhoto = txtItemPhoto;
            this.editText = editText;
        }
    }


    public void setDefaultInfo(String info) {
        defaultInfo = info;
    }


    public void setShowPhotoInfo(boolean show) {
        showPhotoInfo = show;
    }

    public void setOnPhotoUpdataInfoListen(PhotoUpdataInfo updataInfoListen) {
        updataInfo = updataInfoListen;
    }

    public interface PhotoUpdataInfo {
        void onPhotoUpdataInfo(int pos, PhotoItem item);
    }

}
