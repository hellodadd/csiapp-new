package com.android.csiapp.Crime.utils.tree;

import java.util.Set;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.csiapp.R;

final class FancyColouredVariousSizesAdapter extends SimpleStandardAdapter {
    public FancyColouredVariousSizesAdapter(final TreeViewListActivity activity,
            final Set<String> selected,
            final TreeStateManager<String> treeStateManager,
            final int numberOfLevels) {
        super(activity, selected, treeStateManager, numberOfLevels);
    }

    @Override
    public LinearLayout updateView(final View view,
            final TreeNodeInfo<String> treeNodeInfo) {
        final LinearLayout viewLayout = super.updateView(view, treeNodeInfo);
        final TextView descriptionView = (TextView) viewLayout
                .findViewById(R.id.demo_list_item_description);
        final TextView levelView = (TextView) viewLayout
                .findViewById(R.id.demo_list_item_level);
        descriptionView.setTextSize(20 - 2 * treeNodeInfo.getLevel());
        levelView.setTextSize(20 - 2 * treeNodeInfo.getLevel());
        return viewLayout;
    }

    @Override
    public Drawable getBackgroundDrawable(final TreeNodeInfo<String> treeNodeInfo) {
        switch (treeNodeInfo.getLevel()) {
        case 0:
            return new ColorDrawable(Color.WHITE);
        case 1:
            return new ColorDrawable(Color.GRAY);
        case 2:
            return new ColorDrawable(Color.YELLOW);
        default:
            return null;
        }
    }
}