package com.android.csiapp.Crime.utils.adapter;

import android.content.Context;
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

import com.android.csiapp.Crime.utils.ClearableEditText;
import com.android.csiapp.Crime.utils.CreateSceneUtils;
import com.android.csiapp.Databases.EvidenceItem;
import com.android.csiapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * Created by user on 2016/10/11.
 */
public class EvidenceAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<EvidenceItem> items;

    private PhotoUpdataInfo updataInfo;
    private OnPhotoTouchListen photoTouchListen;
    private boolean showPhotoInfo = false;
    private Context mContext;

    public EvidenceAdapter(Context context, List<EvidenceItem> items){
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
        ViewHolder holder = null;
        if(convertView==null){
            convertView = myInflater.inflate(R.layout.photoadapterview, null);
            holder = new ViewHolder(
                    (ImageView) convertView.findViewById(R.id.photo), (ClearableEditText)convertView.findViewById(R.id.photo_edit));
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final EvidenceItem item = (EvidenceItem)getItem(position);
        String path = item.getPhotoPath();
        final int pos = position;
        if(!path.isEmpty()){
            Bitmap bitmap = CreateSceneUtils.loadBitmapFromFile(new File(path));
            if(bitmap!=null) holder.txtItemPhoto.setImageBitmap(bitmap);
            /*
            holder.txtItemPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (photoTouchListen != null) {
                        photoTouchListen.onPhotoTouch(pos);
                    }
                }
            });
            */
        }

        String info = item.getPhotoInfo();
        if (info != null && !info.isEmpty()) {
            holder.editText.setText(info);
        }

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

    public void setShowPhotoInfo(boolean show) {
        showPhotoInfo = show;
    }

    public void setOnPhotoUpdataInfoListen(PhotoUpdataInfo updataInfoListen) {
        updataInfo = updataInfoListen;
    }

    public interface PhotoUpdataInfo {
        void onPhotoUpdataInfo(int pos, EvidenceItem item);
    }

    public interface OnPhotoTouchListen {
        void onPhotoTouch(int pos);
    }

    public void setPhotoTouchListen(OnPhotoTouchListen listen) {
        photoTouchListen = listen;
    }
}
