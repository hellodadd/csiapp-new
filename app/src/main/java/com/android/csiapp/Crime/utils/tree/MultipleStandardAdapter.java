package com.android.csiapp.Crime.utils.tree;

import java.util.Set;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.csiapp.R;

/**
 * This is a very simple adapter that provides very basic tree view with a
 * checkboxes and simple item description.
 *
 */
public class MultipleStandardAdapter extends AbstractTreeViewAdapter<String> {

    private final Set<String> selected;

    private final OnCheckedChangeListener onCheckedChange = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            final String id = (String) buttonView.getTag();
            changeSelected(isChecked, id);
        }
    };

    private void changeSelected(final boolean isChecked, final String id) {
        if (isChecked) {
            selected.add(id);
        } else {
            selected.remove(id);
        }
        Log.d("Anita","select size = "+selected.size());
    }

    public MultipleStandardAdapter(final Activity TreeViewListActivity,
                                 final Set<String> selected,
                                 final TreeStateManager<String> treeStateManager,
                                 final int numberOfLevels) {
        super(TreeViewListActivity, treeStateManager, numberOfLevels);
        this.selected = selected;
    }

    @Override
    public View getNewChildView(final TreeNodeInfo<String> treeNodeInfo) {
        final LinearLayout viewLayout = (LinearLayout) getActivity()
                .getLayoutInflater().inflate(R.layout.multiple_list_item, null);
        return updateView(viewLayout, treeNodeInfo);
    }

    @Override
    public LinearLayout updateView(final View view,
                                   final TreeNodeInfo<String> treeNodeInfo) {
        final LinearLayout viewLayout = (LinearLayout) view;
        final TextView descriptionView = (TextView) viewLayout.findViewById(R.id.demo_list_item_description);
        final TextView levelView = (TextView) viewLayout.findViewById(R.id.demo_list_item_level);
        descriptionView.setText(treeNodeInfo.getDescription());
        //levelView.setText(Integer.toString(treeNodeInfo.getLevel()));
        levelView.setVisibility(View.GONE);
        final CheckBox box = (CheckBox) viewLayout.findViewById(R.id.demo_list_checkbox);
        box.setTag(treeNodeInfo.getId());
        if (treeNodeInfo.isWithChildren()) {
            box.setVisibility(View.GONE);
        } else {
            box.setVisibility(View.VISIBLE);
            box.setChecked(selected.contains(treeNodeInfo.getId()));
        }
        box.setOnCheckedChangeListener(onCheckedChange);
        return viewLayout;
    }

    @Override
    public void handleItemClick(final View view, final Object id) {
        final String StringId = (String) id;
        final TreeNodeInfo<String> info = getManager().getNodeInfo(StringId);
        if (info.isWithChildren()) {
            super.handleItemClick(view, id);
        } else {
            final ViewGroup vg = (ViewGroup) view;
            final CheckBox cb = (CheckBox) vg
                    .findViewById(R.id.demo_list_checkbox);
            cb.performClick();
        }
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    public Set<String> getSelected(){
        return selected;
    }
}