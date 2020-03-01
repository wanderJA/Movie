package com.wander.movie.ui.gank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wander.movie.R;
import com.wander.movie.utils.JumpUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wander on 2018/1/23.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<GankModel> mList;

    @NotNull
    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, int position) {
        final GankModel model = mList.get(position);


        holder.title.setText(model.desc);
        holder.desc.setText(model.desc);
        holder.pubDate.setText(model.publishedAt.toString());
        holder.source.setText(model.source);

        if (model.images != null && !model.images.isEmpty()) {
            String imageUrl = model.images.get(0);
            holder.nineGrid.setImageURI(imageUrl);
            holder.nineGrid.setVisibility(View.VISIBLE);
        } else {
            holder.nineGrid.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            JumpUtils.INSTANCE.jumpToWeb(context, model.url, model.desc);
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void addData(List<GankModel> gankModelList) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.addAll(gankModelList);
        notifyDataSetChanged();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        private TextView title, desc, pubDate, source;
        private SimpleDraweeView nineGrid;

        public NewsViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            pubDate = itemView.findViewById(R.id.pubDate);
            source = itemView.findViewById(R.id.source);
            nineGrid = itemView.findViewById(R.id.nineGrid);
        }
    }
}
