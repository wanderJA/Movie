package com.wander.baseframe.view.recycler.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.qiyi.video.reader.widget.recycler.adapter.RVBaseCell;
import com.wander.baseframe.R;
import com.wander.baseframe.view.recycler.adapter.RVBaseViewHolder;


/**
 * Created by wander on 2017/4/1.
 * 有一个坑  如果有两个不同的OneViewCell  注意type
 */

public class OneViewCell extends RVBaseCell<View> {
    private int type = 21300;
    protected ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public OneViewCell(View view, int type) {
        super(view);
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.cell_contain, parent, false);

        return new RVBaseViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RVBaseViewHolder holder, int position) {
        ViewGroup contain = holder.getView(R.id.one_cell_contain);
        if (getData() == null) {
            contain.removeAllViews();
            contain.setPadding(0, 0, 0, 1);
        } else {
            contain.removeAllViews();
            contain.setPadding(0, 0, 0, 0);
            if (getData().getParent() != null) {
                ((ViewGroup) getData().getParent()).removeView(getData());
            }
            contain.addView(getData(), layoutParams);
        }
    }
}
