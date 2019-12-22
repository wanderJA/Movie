package com.wander.baseframe.view.recycler.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.wander.baseframe.R;
import com.wander.baseframe.utils.Tools;
import com.wander.baseframe.view.loading.CircleLoadingView;
import com.wander.baseframe.view.recycler.adapter.RVBaseViewHolder;
import com.wander.baseframe.view.recycler.adapter.RVSimpleAdapter;


/**
 * Created by zhouwei on 17/1/23.
 */

public class LoadMoreCell extends RVAbsStateCell {
    public static final int mDefaultHeight = 40;//dp
    CircleLoadingView loadingView;

    public LoadMoreCell(Object o) {
        super(o);
    }

    @Override
    public int getItemType() {
        return RVSimpleAdapter.LOAD_MORE_TYPE;
    }


    @Override
    public void onBindViewHolder(@NonNull RVBaseViewHolder holder, int position) {
        loadingView = holder.getView(R.id.loading_icon);
        loadingView.setAutoAnimation(true);
        loadingView.setStaticPlay(true);
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    protected View getDefaultView(Context context) {
        // 设置LoadMore View显示的默认高度
        setHeight(Tools.dip2px(context, mDefaultHeight));
        return LayoutInflater.from(context).inflate(R.layout.rv_load_more_layout, null);
    }

    @Override
    public void releaseResource() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }
}
