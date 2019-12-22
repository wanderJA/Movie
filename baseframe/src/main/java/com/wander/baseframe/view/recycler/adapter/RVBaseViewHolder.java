package com.wander.baseframe.view.recycler.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * Created by zhouwei on 17/1/19.
 */

public class RVBaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> views;
    private View mItemView;

    public RVBaseViewHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
        mItemView = itemView;
    }

    /**
     * 获取ItemView
     *
     * @return
     */
    public View getItemView() {
        return mItemView;
    }

    public <T extends View> T getView(int resId) {
        return retrieveView(resId);
    }

    public TextView getTextView(int resId) {
        return retrieveView(resId);
    }

    public ImageView getImageView(int resId) {
        return retrieveView(resId);
    }

    public Button getButton(int resId) {
        return retrieveView(resId);
    }

    @SuppressWarnings("unchecked")
    private <V extends View> V retrieveView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = mItemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (V) view;
    }

    public RVBaseViewHolder setText(int viewId, CharSequence value) {
        TextView view = getView(viewId);
        if (TextUtils.isEmpty(value)) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            view.setText(value);
        }
        return this;
    }

    public void setText(int resId, @StringRes int strId) {
        getTextView(resId).setText(strId);
    }

    public RVBaseViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public void startActivity(@NotNull Intent intent) {
        itemView.getContext().startActivity(intent);
    }

    public void showOrHide(int viewId, boolean show) {
        View view = getView(viewId);
        view.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    public void showOrHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }
}
