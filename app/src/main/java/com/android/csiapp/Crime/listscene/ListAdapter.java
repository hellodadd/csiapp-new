package com.android.csiapp.Crime.listscene;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.csiapp.Crime.utils.CreateSceneUtils;
import com.android.csiapp.Crime.utils.DateTimePicker;
import com.android.csiapp.Crime.utils.DictionaryInfo;
import com.android.csiapp.Crime.utils.adapter.PhotoAdapter;
import com.android.csiapp.Databases.CrimeItem;
import com.android.csiapp.Databases.PhotoItem;
import com.android.csiapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by AnitaLin on 2016/9/12.
 */
public class ListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater myInflater;
    private List<CrimeItem> items;
    private ArrayList<CrimeItem> arraylist;

    public ListAdapter(Context context, List<CrimeItem> items){
        myInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.items = items;
        this.arraylist = new ArrayList<CrimeItem>();
        this.arraylist.addAll(items);
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
            convertView = myInflater.inflate(R.layout.listview, null);
            holder = new ViewHolder(
                (ImageView) convertView.findViewById(R.id.photo),
                (TextView) convertView.findViewById(R.id.casetype),
                (TextView) convertView.findViewById(R.id.area),
                (TextView) convertView.findViewById(R.id.time),
                (TextView) convertView.findViewById(R.id.complete)
            );
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        CrimeItem item = (CrimeItem)getItem(position);
        //注释掉图片显示，列表中不在显示图片liwei 2017.2.27
        //===========================================
        //List<PhotoItem> photoItem = item.getImportantPhoto();
        //if(photoItem.size()>0) {
        //    String path = photoItem.get(0).getPhotoPath();
        //    if(!path.isEmpty()) {
        //        Bitmap b = CreateSceneUtils.loadBitmapFromFile(new File(path));
        //        holder.imgPhoto.setImageBitmap(b);
        //        holder.imgPhoto.setBackgroundColor(Color.parseColor("#FFFFFF"));
        //    }
        //}else{
        //    holder.imgPhoto.setImageDrawable(mContext.getResources().getDrawable(R.drawable.no_completed));
        //}
        //===========================================
        holder.txtCasetype.setText(DictionaryInfo.getDictValue(DictionaryInfo.mCaseTypeKey, ((CrimeItem) getItem(position)).getCasetype()));
        holder.txtArea.setText(DictionaryInfo.getDictValue(DictionaryInfo.mAreaKey, ((CrimeItem) getItem(position)).getArea()));
        holder.txtTime.setText(DateTimePicker.getCurrentTime(((CrimeItem) getItem(position)).getOccurredStartTime()));
        holder.txtComplete.setText(getCompleteText(((CrimeItem) getItem(position)).getComplete()));
        return convertView;
    }

    private class ViewHolder {
        ImageView imgPhoto;
        TextView txtCasetype;
        TextView txtArea;
        TextView txtTime;
        TextView txtComplete;
        public ViewHolder(ImageView imgPhoto, TextView txtCasetype, TextView txtArea, TextView txtTime, TextView txtComplete){
            this.imgPhoto = imgPhoto;
            this.txtCasetype = txtCasetype;
            this.txtArea = txtArea;
            this.txtTime = txtTime;
            this.txtComplete = txtComplete;
        }
    }

    public void filter(int searchType, String text, long timeStart, long timeEnd) {
        switch (searchType){
            case 1:
                for(CrimeItem item:arraylist){
                    if(item.getCasetype().equalsIgnoreCase(text)){
                        items.add(item);
                    }
                }
                break;
            case 2:
                for(CrimeItem item:arraylist){
                    if(item.getArea().equalsIgnoreCase(text)){
                        items.add(item);
                    }
                }
                break;
            case 3:
                for(CrimeItem item:arraylist){
                    if(item.getOccurredStartTime()>=timeStart && item.getOccurredEndTime()<=timeEnd){
                        items.add(item);
                    }
                }
                break;
            default:
                break;
        }
        Collections.sort(items,
                new Comparator<CrimeItem>() {
                    public int compare(CrimeItem o1, CrimeItem o2) {
                        return String.valueOf(o1.getOccurredStartTime()).compareTo(String.valueOf(o2.getOccurredStartTime()));
                    }
                });
        Collections.reverse(items);
        notifyDataSetChanged();
    }

    public void clearItem(){
        items.clear();
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
