package com.android.csiapp.Crime.listscene;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.csiapp.Crime.utils.CreateSceneUtils;
import com.android.csiapp.Crime.utils.DateTimePicker;
import com.android.csiapp.Crime.utils.DictionaryInfo;
import com.android.csiapp.Crime.utils.adapter.PhotoAdapter;
import com.android.csiapp.Databases.CrimeItem;
import com.android.csiapp.Databases.PhotoItem;
import com.android.csiapp.R;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/10/17.
 */
public class ListDeleteAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater myInflater;
    private List<CrimeItem> items;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;

    public ListDeleteAdapter(Context context, List<CrimeItem> items,HashMap<Integer,Boolean> isSelected){
        myInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.items = items;
        this.isSelected = isSelected;
        for (int i = 0; i < items.size(); i++) {
            getIsSelected().put(i, false);
        }
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
        return items.get(position).getId();
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        ListDeleteAdapter.isSelected = isSelected;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = myInflater.inflate(R.layout.listview_delete, null);
            holder = new ViewHolder();
            holder.LL = (LinearLayout) convertView.findViewById(R.id.linearLayout);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.imgPhoto = (ImageView) convertView.findViewById(R.id.photo);
            holder.txtCasetype = (TextView) convertView.findViewById(R.id.casetype);
            holder.txtArea = (TextView) convertView.findViewById(R.id.area);
            holder.txtTime = (TextView) convertView.findViewById(R.id.time);
            holder.txtComplete = (TextView) convertView.findViewById(R.id.complete);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        CrimeItem item = (CrimeItem)getItem(position);
        List<PhotoItem> photoItem = item.getImportantPhoto();
        // 监听checkBox并根据原来的状态来设置新的状态
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isSelected.get(position)) {
                    isSelected.put(position, false);
                    setIsSelected(isSelected);
                } else {
                    isSelected.put(position, true);
                    setIsSelected(isSelected);
                }
                notifyDataSetChanged();
            }
        });

        // 根据isSelected来设置checkbox的选中状况
        holder.checkBox.setChecked(getIsSelected().get(position));

        //setPhotoPath
        //===================
        //注释掉图片显示
        //if(photoItem.size()>0) {
        //    String path = photoItem.get(0).getPhotoPath();
        //    if(!path.isEmpty()) {
        //        Bitmap b = CreateSceneUtils.loadBitmapFromFile(new File(path));
        //        holder.imgPhoto.setImageBitmap(b);
        //        holder.imgPhoto.setBackgroundColor(Color.parseColor("#FFFFFF"));
        //    }
        //}

        //setText
        holder.txtCasetype.setText(DictionaryInfo.getDictValue(DictionaryInfo.mCaseTypeKey, ((CrimeItem) getItem(position)).getCasetype()));
        holder.txtArea.setText(DictionaryInfo.getDictValue(DictionaryInfo.mAreaKey, ((CrimeItem) getItem(position)).getArea()));
        holder.txtTime.setText(DateTimePicker.getCurrentTime(((CrimeItem) getItem(position)).getOccurredStartTime()));
        holder.txtComplete.setText(getCompleteText(((CrimeItem) getItem(position)).getComplete()));

        return convertView;
    }

    private class ViewHolder {
        LinearLayout LL;
        CheckBox checkBox;
        ImageView imgPhoto;
        TextView txtCasetype;
        TextView txtArea;
        TextView txtTime;
        TextView txtComplete;
    }

    private String getCompleteText(String complete){
        if(complete.equalsIgnoreCase("0")){
            return mContext.getResources().getString(R.string.incomplete);
        }else if(complete.equalsIgnoreCase("1")){
            return mContext.getResources().getString(R.string.complete);
        }else if(complete.equalsIgnoreCase("2")){
            return mContext.getResources().getString(R.string.commit);
        }else {
            return "";
        }
    }
}
