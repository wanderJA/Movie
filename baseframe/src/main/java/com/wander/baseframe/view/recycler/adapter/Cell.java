package com.wander.baseframe.view.recycler.adapter;

import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

/**
 * Created by wander on 2017/2/23.
 */

public interface Cell {
    /**
     * 回收资源
     */
    void releaseResource();

    void onAttachedToWindow(RVBaseViewHolder holder);

    /**
     * 获取viewType
     *
     * @return
     */
    int getItemType();

    /**
     * 创建ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    RVBaseViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType);

    /**
     * 数据绑定
     *
     * @param holder
     * @param position
     */
    void onBindViewHolder(@NotNull RVBaseViewHolder holder, int position);
}