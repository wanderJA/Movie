package com.wander.baseframe.view.recycler.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.qiyi.video.reader.widget.recycler.adapter.RVBaseCell;
import com.wander.baseframe.R;
import com.wander.baseframe.view.recycler.adapter.RVBaseViewHolder;


/**
 * Created by zhouwei on 17/2/15.
 */

public abstract class RVAbsStateCell<T> extends RVBaseCell<T> {
    protected View mView;
    protected int mHeight = 0;

    public RVAbsStateCell(T o) {
        super(o);
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public void setView(View view) {
        mView = view;
    }

    @Override
    public void releaseResource() {
        if (mView != null) {
            mView = null;
        }
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_state_layout, null);
        //如果调用者没有设置显示的View就用默认的View
        if (mView == null) {
            mView = getDefaultView(parent.getContext());
        }
        if (mView != null) {
            LinearLayout container = (LinearLayout) view.findViewById(R.id.rv_cell_state_root_layout);
            container.removeAllViews();
            if (null != mView.getParent()) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
            container.addView(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        ViewGroup.LayoutParams params = null;
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mHeight > 0) {
            params.height = mHeight;
        }
        view.setLayoutParams(params);
        return new RVBaseViewHolder(view);
    }

    /**
     * 子类提供的默认布局，当没有通过{@link #setView(View)}设置显示的View的时候，就显示默认的View
     *
     * @return 返回默认布局
     */
    protected abstract View getDefaultView(Context context);
}
