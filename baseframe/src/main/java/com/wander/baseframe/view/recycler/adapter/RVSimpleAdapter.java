package com.wander.baseframe.view.recycler.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.qiyi.video.reader.widget.recycler.adapter.RVBaseCell;
import com.wander.baseframe.utils.DebugLog;
import com.wander.baseframe.utils.Tools;
import com.wander.baseframe.view.recycler.cell.BottomCell;
import com.wander.baseframe.view.recycler.cell.LoadMoreCell;

import java.util.List;


/**
 * Created by zhouwei on 17/1/23.
 */

public class RVSimpleAdapter extends RVBaseAdapter<RVBaseCell> {
    public static final int LOAD_MORE_TYPE = Integer.MAX_VALUE - 1;
    public static int BOTTOM_TYPE = Integer.MAX_VALUE - 2;
    public static final int LINE_TYPE = Integer.MAX_VALUE - 3;

    private LoadMoreCell mLoadMoreCell;
    private BottomCell mBottomCell;
    //LoadMore 是否已显示
    private boolean mIsShowLoadMore = false;
    private boolean mIsShowBottom = false;

    public RVSimpleAdapter() {
        mLoadMoreCell = new LoadMoreCell(null);
        mBottomCell = new BottomCell(null);
    }

    @Override
    protected void onViewHolderBound(RVBaseViewHolder holder, int position) {

    }

    @Override
    public void setData(List cells) {
        mIsShowLoadMore = false;
        mIsShowBottom = false;
        super.setData(cells);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        //处理GridView 布局
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    return (viewType == LOAD_MORE_TYPE) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }

    }

    @Override
    public void onViewAttachedToWindow(RVBaseViewHolder holder) {
        // 处理StaggeredGridLayoutManager 显示这个Span
        super.onViewAttachedToWindow(holder);
        int position = holder.getAdapterPosition();
        int viewType = getItemViewType(position);
        if (isStaggeredGridLayout(holder)) {
            if (viewType == LOAD_MORE_TYPE) {

                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                //设置显示整个span
                params.setFullSpan(true);
            }
        }

    }

    /**
     * 判断是否是瀑布流布局
     *
     * @param holder
     * @return
     */
    private boolean isStaggeredGridLayout(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            return true;
        }
        return false;
    }

    /**
     * 当滑动到底部时显示
     */
    public void showBottom() {
        if (mData.contains(mBottomCell)) {
            return;
        }
        add(mBottomCell);
        mIsShowBottom = true;
    }

    public void showBottom(String tip) {
        mBottomCell.setData(tip);
        showBottom();
    }

    public void hideBottom() {
        if (mData.contains(mBottomCell)) {
            remove(mBottomCell);
            mIsShowBottom = false;
        }
    }

    /**
     * 显示LoadMoreView
     * <p>当列表滑动到底部时，调用{@link #showLoadMore()} 提示加载更多，加载完数据，调用{@link #hideLoadMore()}
     * 隐藏LoadMoreView,显示列表数据。</p>
     */
    public void showLoadMore() {
        if (mData.contains(mLoadMoreCell)) {
            return;
        }
        checkNotContainSpecailCell();
        add(mLoadMoreCell);
        mIsShowLoadMore = true;
    }

    /**
     * 指定显示的LoadMore View
     *
     * @param loadMoreView
     */
    public void showLoadMore(View loadMoreView) {
        showLoadMore(loadMoreView, 0);
    }

    /**
     * 指定显示的LoadMoreView,并指定显示的高度
     *
     * @param loadMoreView
     * @param height
     */
    public void showLoadMore(View loadMoreView, int height) {
        if (loadMoreView == null) {
            return;
        }
        checkNotContainSpecailCell();
        //设置默认高度
        if (height < 0) {

        } else if (height == 0) {
            int defaultHeight = Tools.dip2px(loadMoreView.getContext(), LoadMoreCell.mDefaultHeight);
            mLoadMoreCell.setHeight(defaultHeight);
        } else {
            mLoadMoreCell.setHeight(height);
        }
        mLoadMoreCell.setView(loadMoreView);
        mIsShowLoadMore = true;
        add(mLoadMoreCell);
    }

    /**
     * 隐藏LoadMoreView
     * <p>调用{@link #showLoadMore()}之后，加载数据完成，调用{@link #hideLoadMore()}隐藏LoadMoreView</p>
     */
    public void hideLoadMore() {
        if (mData.contains(mLoadMoreCell)) {
            mLoadMoreCell.releaseResource();
            remove(mLoadMoreCell);
            mIsShowLoadMore = false;
        }
    }

    /**
     * LoadMore View 是否已经显示
     *
     * @return
     */
    public boolean isShowLoadMore() {
        return mIsShowLoadMore;
    }

    /**
     * 检查列表是否已经包含了这4种Cell
     */
    private void checkNotContainSpecailCell() {
        if (mData.contains(mLoadMoreCell)) {
            mLoadMoreCell.releaseResource();
            mData.remove(mLoadMoreCell);
        }
    }

    @Override
    public void clear() {
        mIsShowLoadMore = false;
        mIsShowBottom = false;
        super.clear();
    }


    public boolean isShowBottom() {
        return mIsShowBottom;
    }

    public LoadMoreCell getLoadMoreCell() {
        return mLoadMoreCell;
    }

    /**
     * 判断是否可以显示LoadMore
     *
     * @return
     */
    public boolean canLoadMore() {
        if (isShowLoadMore() || isShowBottom()) {
            DebugLog.i(TAG, "can not show loadMore");
            return false;
        }
        return true;
    }
}
