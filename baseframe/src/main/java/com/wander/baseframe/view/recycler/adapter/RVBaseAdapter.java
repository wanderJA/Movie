package com.wander.baseframe.view.recycler.adapter;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.qiyi.video.reader.widget.recycler.adapter.RVBaseCell;
import com.wander.baseframe.utils.DebugLog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class RVBaseAdapter<C extends RVBaseCell> extends RecyclerView.Adapter<RVBaseViewHolder> {
    public static final String TAG = "RVBaseAdapter";
    protected List<C> mData = new ArrayList<>();

    public RVBaseAdapter() {
    }

    public void setData(List<C> cells) {
        if (cells == null) {
            return;
        }
        if (mData != null) {
            mData.clear();
            mData.addAll(cells);
            notifyDataSetChanged();
        }
    }

    public List<C> getData() {
        return mData;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (int i = 0; i < getItemCount(); i++) {
            if (viewType == mData.get(i).getItemType()) {
                return mData.get(i).onCreateViewHolder(parent, viewType);
            }
        }

        throw new RuntimeException("wrong viewType");
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        try {
            mData.get(position).onBindViewHolder(holder, position);
            onViewHolderBound(holder, position);
        } catch (Exception e) {

        }

    }

    @Override
    public void onViewDetachedFromWindow(RVBaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
//        DebugLog.e(TAG, "onViewDetachedFromWindow invoke...");
        //释放资源
        int position = holder.getAdapterPosition();
        //越界检查
        if (position < 0 || position >= mData.size()) {
            return;
        }
        mData.get(position).releaseResource();
    }

    @Override
    public void onViewAttachedToWindow(RVBaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getAdapterPosition();
        //越界检查
        if (position < 0 || position >= mData.size()) {
            return;
        }
        mData.get(position).onAttachedToWindow(holder);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getItemType();
    }

    /**
     * add one cell
     *
     * @param cell
     */
    public void add(C cell) {
        if (cell == null) {
            return;
        }
        mData.add(cell);
        int index = mData.indexOf(cell);
        notifyItemChanged(index);
    }

    public void add(int index, C cell) {
        if (cell == null) {
            return;
        }
        mData.add(index, cell);
        notifyItemChanged(index);
    }

    public void addInNext(C preCell, C cell) {
        int index = getCellIndex(preCell);
        if (index == -1) {
            return;
        }
        index++;
        if (cell == null) {
            return;
        }
        int indexOf = mData.indexOf(cell);
        if (indexOf >= 0 && indexOf < mData.size()) {
            //若已经存在则直接更新
            notifyItemChanged(indexOf);
            return;
        }
        mData.add(index, cell);
        notifyItemChanged(index);
    }


    /**
     * remove a cell
     *
     * @param cell
     */
    public int remove(C cell) {
        if (cell == null) {
            return -1;
        }
        int indexOfCell = mData.indexOf(cell);
        remove(indexOfCell);
        return indexOfCell;
    }

    public void remove(int index) {
        if (index < 0 || index >= mData.size()) {
            return;
        }
        mData.remove(index);
        notifyItemRemoved(index);
    }

    /**
     * @param start
     * @param count
     */
    public void remove(int start, int count) {
        if ((start + count) > mData.size()) {
            return;
        }

        mData.subList(start, start + count).clear();

        notifyItemRangeRemoved(start, count);
    }

    /**
     * 移除start以后的元素
     *
     * @param start
     */
    public void removeToEnd(int start) {
        int size = mData.size();
        if (start < 0 || start > size) {
            return;
        }
        int count = size - start;
        DebugLog.e(TAG, "start:" + start + "\tcount:" + count);
        mData.subList(start, size).clear();
        notifyItemRangeRemoved(start, count);
    }

    /**
     * 移除当前cell以后的元素
     *
     * @param cell
     */
    public void removeToEnd(C cell) {
        int cellIndex = getCellIndex(cell);
        if (cellIndex == -1) {
            return;
        }
        int start = cellIndex + 1;
        int size = mData.size();
        if (start < 0 || start > size) {
            return;
        }
        int count = size - start;
        DebugLog.e(TAG, "removeToEnd  start:" + start + "\tcount:" + count);
        mData.subList(start, size).clear();
        notifyItemRangeRemoved(start, count);
    }


    /**
     * add a cell list
     *
     * @param cells
     */
    public void addAll(@NotNull List<C> cells) {
        if (cells.size() == 0) {
            return;
        }
        DebugLog.e(TAG, "addAll cell size:" + cells.size());
        mData.addAll(cells);
        notifyItemRangeChanged(mData.size() - cells.size(), mData.size());
    }


    public void addAll(int index, @NotNull List<C> cells) {
        if (cells.size() == 0) {
            return;
        }
        mData.addAll(index, cells);
        notifyItemRangeChanged(index, index + cells.size());
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }


    /**
     * 如果子类需要在onBindViewHolder 回调的时候做的操作可以在这个方法里做
     *
     * @param holder
     * @param position
     */
    protected abstract void onViewHolderBound(RVBaseViewHolder holder, int position);

    /**
     * 更新制定的cell
     *
     * @param cell
     */
    public void notifyOneCell(C cell) {
        int indexOf = mData.indexOf(cell);
        if (indexOf >= 0 && indexOf < mData.size()) {
            notifyItemChanged(indexOf);
        }
    }

    /**
     * 不存在加入  存在则刷新
     *
     * @param i
     * @param cell
     */
    public void addOrNotify(int i, C cell) {
        if (cell == null) {
            return;
        }
        int indexOf = mData.indexOf(cell);
        if (indexOf >= 0 && indexOf < mData.size()) {
            notifyItemChanged(indexOf);
        } else {
            add(i, cell);
        }
    }

    /**
     * 不存在加入  存在则刷新
     *
     * @param cell
     */
    public void addOrNotify(C cell) {
        if (cell == null) {
            return;
        }
        int indexOf = mData.indexOf(cell);
        if (indexOf >= 0 && indexOf < mData.size()) {
            notifyItemChanged(indexOf);
        } else {
            add(cell);
        }
    }

    public int getCellIndex(C cell) {
        if (cell != null) {
            return mData.indexOf(cell);
        }
        return -1;
    }

    public Cell getCell(int index) {
        Cell cell = null;
        if (index >= 0 && index < mData.size()) {
            cell = mData.get(index);
        }
        return cell;
    }
}
