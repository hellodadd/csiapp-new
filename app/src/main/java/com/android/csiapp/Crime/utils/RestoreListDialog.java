package com.android.csiapp.Crime.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.csiapp.Crime.utils.BackupRestore;
import com.android.csiapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by JOWE on 2016/11/4.
 */

public class RestoreListDialog extends AlertDialog {
    private Context mContext;
    private AlertDialog.Builder mBuilder;
    private AlertDialog mDialog;
    private ListView mResoteItems;
    private SimpleAdapter mAdapter;
    private int mSelectedItem = -1;

    private static final String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Csibackup";
    private static final String TAG = "RestoreListDialog";

    public RestoreListDialog(Context context) {
        super(context);
        mContext = context;
    }

    public void createRestoreListDialog () {

        File file = new File(filePath);
        File[] files = file.listFiles(); // Read file
        ArrayList fileListData = getFileName(files);
        showRestoreListDialog(fileListData);
    }

    public void showRestoreListDialog(ArrayList listFile){
        if(listFile.size() == 0) {
            Log.i(TAG,"File Not Found");
            return;
        }

        LayoutInflater layout = LayoutInflater.from(mContext);
        View resoreListView = layout.inflate(R.layout.restore_listview, null);

        mResoteItems = (ListView) resoreListView
                .findViewById(R.id.lvRestoreItems);
        mAdapter = new SimpleAdapter(mContext,
                listFile, R.layout.restore_filelist, new String[] { "Name" }, new int[] { R.id.tvFileItem });
        mResoteItems.setAdapter(mAdapter);
        mResoteItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mSelectedItem = position;
                String filename = ((Hashtable<String, String>) mAdapter.getItem(position)).get("Name");
                String filePath = ((Hashtable<String, String>) mAdapter.getItem(position)).get("Path");
                Log.d(TAG, "selectedItem="+mSelectedItem+", filename="+filename+", filePath="+filePath);
                dataRecover(filename);
                mDialog.cancel();
            }
        });

        mBuilder = new AlertDialog.Builder(mContext);
        mBuilder.setView(resoreListView);
        mBuilder.setCancelable(false);
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mDialog = mBuilder.create();
        mDialog.show();
    }

    private ArrayList<Hashtable<String, String>> getFileName(File[] files) {
        ArrayList<Hashtable<String, String>> listData = new ArrayList<Hashtable<String, String>>();
        if (files != null) { // determine if the file is null
            for (File file : files) {
                if (file.isDirectory()) {
                    getFileName(file.listFiles());
                } else {
                    String fileName = file.getName();
                    String filePath = file.getPath();
                    if (fileName.endsWith(".zip")) {
                        Hashtable<String, String> fileItem = new Hashtable<String, String>();
                        fileItem.put("Name", fileName);
                        fileItem.put("Path", filePath);
                        listData.add(fileItem);
                    }
                }
            }
        }
        return  listData;
    }

    private void dataRecover(String filename) {
        // TODO Auto-generated method stub
        new BackupRestore(mContext).execute("restroeDatabase", filename);
    }

}
