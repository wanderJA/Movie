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

import org.jetbrains.annotations.NotNull;

/**
 * Created by wander on 2017/3/17.
 * <p>
 * 只包含一个view时，减少xml创建
 */

public abstract class BaseOneViewCell<T> extends RVBaseCell<T> {
    protected LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public BaseOneViewCell() {
    }

    public BaseOneViewCell(T t) {
        super(t);
    }

    @Override
    public BaseOneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.cell_contain, parent, false);

        return new BaseOneViewHolder(inflate);
    }

    public class BaseOneViewHolder extends RVBaseViewHolder {
        public View oneView;

        public BaseOneViewHolder(View itemView) {
            super(itemView);
            LinearLayout linearLayout = itemView.findViewById(R.id.one_cell_contain);
            oneView = getOneView(itemView.getContext());
            if (oneView != null) {
                linearLayout.addView(oneView, getLayoutParam());
            }
        }
    }

    protected LinearLayout.LayoutParams getLayoutParam() {
        return layoutParams;
    }

    @Override
    public void onBindViewHolder(@NonNull RVBaseViewHolder holder, int position) {
        onBindViewHolder((BaseOneViewHolder) holder, position);
    }

    protected abstract View getOneView(@NonNull Context context);

    protected abstract void onBindViewHolder(@NotNull BaseOneViewHolder holder, int position);


}
