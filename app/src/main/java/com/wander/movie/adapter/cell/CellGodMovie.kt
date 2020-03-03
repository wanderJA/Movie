package com.wander.movie.adapter.cell

import android.view.ViewGroup
import com.qiyi.video.reader.widget.recycler.adapter.RVBaseCell
import com.qiyi.video.reader.widget.recycler.adapter.createBaseViewHolder
import com.wander.baseframe.view.recycler.adapter.RVBaseViewHolder
import com.wander.movie.R
import com.wander.movie.bean.GodMovieItem
import com.wander.movie.utils.JumpUtils
import kotlinx.android.synthetic.main.cell_god_movie.view.*

class CellGodMovie : RVBaseCell<GodMovieItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVBaseViewHolder {
        return parent.createBaseViewHolder(R.layout.cell_god_movie)
    }

    override fun getItemType(): Int {
        return CellType.cellGodMovie
    }

    override fun onBindViewHolder(holder: RVBaseViewHolder, position: Int) {
        data?.let { data ->
            holder.itemView.cellMovieTitle.text = data.name
            holder.itemView.cellMovieTime.text = data.time
            holder.itemView.setOnClickListener {
                JumpUtils.jumpToGodMovieDetail(holder.itemView.context, data.id)
            }

        }

    }
}