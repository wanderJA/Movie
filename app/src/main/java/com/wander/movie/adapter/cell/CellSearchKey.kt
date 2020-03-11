package com.wander.movie.adapter.cell

import android.view.ViewGroup
import com.qiyi.video.reader.widget.recycler.adapter.RVBaseCell
import com.qiyi.video.reader.widget.recycler.adapter.createBaseViewHolder
import com.wander.baseframe.view.recycler.adapter.RVBaseViewHolder
import com.wander.movie.R
import kotlinx.android.synthetic.main.cell_search_key.view.*

class CellSearchKey(key: String) : RVBaseCell<String>(key) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVBaseViewHolder {
        return parent.createBaseViewHolder(R.layout.cell_search_key)
    }

    override fun getItemType(): Int {
        return CellType.cellSearchKey
    }

    override fun onBindViewHolder(holder: RVBaseViewHolder, position: Int) {
        data?.let { data ->
            holder.itemView.searchKey.text = data
            holder.itemView.setOnClickListener(onClickListener)
        }

    }
}