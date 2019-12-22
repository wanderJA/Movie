package com.wander.baseframe.view.recycler.divider;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author wander
 */
public class RecyclerViewGapDecoration extends RecyclerView.ItemDecoration {
    private Rect mGapRect;
    private boolean includeStartEdge = true;
    private int edgeMargin;

    public RecyclerViewGapDecoration() {
    }

    public RecyclerViewGapDecoration(Rect gapRect) {
        mGapRect = gapRect;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }
        int itemCount = adapter.getItemCount();
        if (mGapRect != null) {
            if (mGapRect.left != 0) {
                outRect.left = mGapRect.left;
            }
            if (mGapRect.right != 0) {
                outRect.right = mGapRect.right;
            }
            if (mGapRect.top != 0) {
                outRect.top = mGapRect.top;
            }
            if (mGapRect.bottom != 0) {
                outRect.bottom = mGapRect.bottom;
            }
        }
        if (layoutManager instanceof LinearLayoutManager && edgeMargin != 0) {
            if (position == 0 && includeStartEdge) {
                if (((LinearLayoutManager) layoutManager).getOrientation() == RecyclerView.HORIZONTAL) {
                    outRect.left = edgeMargin;
                } else {
                    outRect.top = edgeMargin;
                }
            }
            if (position == itemCount - 1) {
                if (((LinearLayoutManager) layoutManager).getOrientation() == RecyclerView.HORIZONTAL) {
                    outRect.right = edgeMargin;
                } else {
                    outRect.bottom = edgeMargin;
                }
            }
        }
    }

    public RecyclerViewGapDecoration setmGapRect(Rect mGapRect) {
        this.mGapRect = mGapRect;
        return this;
    }

    public RecyclerViewGapDecoration setIncludeStartEdge(boolean includeStartEdge) {
        this.includeStartEdge = includeStartEdge;
        return this;
    }

    public RecyclerViewGapDecoration setEdgeMargin(int edgeMargin) {
        this.edgeMargin = edgeMargin;
        return this;
    }

    public RecyclerViewGapDecoration setMargin(int margin) {
        mGapRect = new Rect(margin, margin, margin, margin);
        return this;
    }
}
