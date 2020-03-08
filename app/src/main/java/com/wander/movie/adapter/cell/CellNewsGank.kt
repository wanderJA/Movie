package com.wander.movie.adapter.cell

import android.view.View
import android.view.ViewGroup
import com.qiyi.video.reader.widget.recycler.adapter.RVBaseCell
import com.qiyi.video.reader.widget.recycler.adapter.createBaseViewHolder
import com.wander.baseframe.view.recycler.adapter.RVBaseViewHolder
import com.wander.movie.R
import com.wander.movie.ui.gank.GankModel
import com.wander.movie.utils.JumpUtils
import kotlinx.android.synthetic.main.item_news.view.*

class CellNewsGank : RVBaseCell<GankModel>() {
    override fun getItemType(): Int {
        return CellType.cellNewGank
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVBaseViewHolder {
        return parent.createBaseViewHolder(R.layout.item_news)
    }

    override fun onBindViewHolder(holder: RVBaseViewHolder, position: Int) {
        data?.let { model ->
            holder.itemView.title.text = model.desc
            holder.itemView.desc.text = model.desc
            holder.itemView.pubDate.text = model.publishedAt.toString()
            holder.itemView.source.text = model.source
            if (model.images != null && model.images.isNotEmpty()) {
                val imageUrl = model.images[0]
                holder.itemView.nineGrid.setImageURI(imageUrl)
                holder.itemView.nineGrid.visibility = View.VISIBLE
            } else {
                holder.itemView.nineGrid.visibility = View.GONE
            }
            holder.itemView.setOnClickListener { v: View ->
                val context = v.context
                JumpUtils.jumpToWeb(context, model.url, model.desc)
            }
        }

    }
}