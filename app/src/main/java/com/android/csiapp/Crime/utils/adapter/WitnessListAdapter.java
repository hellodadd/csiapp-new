package com.android.csiapp.Crime.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.csiapp.Databases.CrimeItem;
import com.android.csiapp.Databases.WitnessItem;
import com.android.csiapp.R;

import java.util.List;

/**
 * Created by user on 2016/9/27.
 */
public class WitnessListAdapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private List<WitnessItem> items;

    public WitnessListAdapter(Context context, List<WitnessItem> items){
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
            convertView = myInflater.inflate(R.layout.adapterview, null);
            holder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.ItemName),
                    (TextView) convertView.findViewById(R.id.ItemContent)
            );
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        WitnessItem item = (WitnessItem)getItem(position);
        String sex=" 性别:";
        if(item.getWitnessSex().equals("1")){
            sex=sex+"男";
        }
        else{
            sex=sex+"女";
        }
        String tel="";
        if(!item.getWitnessNumber().equals("")) {
            tel= " 电话:"+item.getWitnessNumber();
        }
        holder.txtItemName.setText("见证人:"+((WitnessItem) getItem(position)).getWitnessName()+sex+tel);
        holder.txtItemContent.setText(((WitnessItem) getItem(position)).getWitnessName());
        return convertView;
    }

    private class ViewHolder {
        TextView txtItemName;
        TextView txtItemContent;
        public ViewHolder(TextView txtItemName, TextView txtItemContent){
            this.txtItemName = txtItemName;
            this.txtItemContent = txtItemContent;
        }
    }
}
