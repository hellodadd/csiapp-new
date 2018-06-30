package com.android.csiapp.Crime.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.csiapp.Databases.LostItem;
import com.android.csiapp.R;

import java.util.List;

/**
 * Created by user on 2016/10/4.
 */
public class LostItemAdapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private List<LostItem> items;

    public LostItemAdapter(Context context, List<LostItem> items){
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
        LostItem item = (LostItem)getItem(position);
        holder.txtItemName.setText("品名:"+((LostItem) getItem(position)).getItemName()+ " 数量:"+((LostItem) getItem(position)).getItemAmount()+" 价值:"+((LostItem) getItem(position)).getItemValue());
        //holder.txtItemContent.setText(((LostItem) getItem(position)).getItemName());
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
