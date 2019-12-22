package com.wander.baseframe.component;

import android.widget.ImageView;

/**
 * author wangdou
 * date 2018/6/20
 * 仅构建通用方法
 */
public interface ICommonTitle {
    void setTitle(CharSequence title);

    ImageView getBackView();

    void setVisibleGone();

    void setVisibleVisible();

}
