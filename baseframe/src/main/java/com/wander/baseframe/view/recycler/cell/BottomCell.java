package com.wander.baseframe.view.recycler.cell;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.wander.baseframe.R;
import com.wander.baseframe.view.recycler.adapter.RVBaseViewHolder;
import com.wander.baseframe.view.recycler.adapter.RVSimpleAdapter;

import org.jetbrains.annotations.NotNull;


/**
 * Created by wander on 2017/6/5.
 */

public class BottomCell extends RVAbsStateCell<String> {

    public BottomCell(String o) {
        super(o);
    }

    @Override
    public int getItemType() {
        return RVSimpleAdapter.BOTTOM_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RVBaseViewHolder holder, int position) {
        if (!TextUtils.isEmpty(getData())) {
            holder.setText(R.id.rv_bottom_text, getData());
        }
    }

    @Override
    protected View getDefaultView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.rv_bottom_layout, null);
    }
}
