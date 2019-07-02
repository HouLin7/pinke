
package com.gome.work.common.widget.treeview.holder;

import com.gome.work.common.widget.treeview.model.TreeNode;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Created by lbm.
 */
public class SimpleViewHolder extends TreeNode.BaseNodeViewHolder<Object> {

    public SimpleViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, Object value) {
        final TextView tv = new TextView(context);
        tv.setText(String.valueOf(value));
        return tv;
    }

    @Override
    public void toggle(boolean active) {

    }
}
