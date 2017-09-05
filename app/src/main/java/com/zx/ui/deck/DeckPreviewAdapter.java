package com.zx.ui.deck;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zx.R;
import com.zx.bean.DeckPreviewBean;
import com.zx.ui.base.BaseRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

class DeckPreviewAdapter extends BaseRecyclerViewAdapter
{
    DeckPreviewAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_deck_preview;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void getView(RecyclerView.ViewHolder holder, int position) {
        ViewHolder      viewHolder = (ViewHolder)holder;
        DeckPreviewBean bean       = (DeckPreviewBean)data.get(position);
        viewHolder.tvDeckName.setText(bean.getDeckName());
        viewHolder.tvStatusMain.setText(bean.getStatusMain());
        viewHolder.tvStatusExtra.setText(bean.getStatusExtra());
        viewHolder.tvStatusMain.setTextColor(bean.getStatusMain().equals(context.getString(R.string.deck_complete)) ? Color.GREEN : Color.RED);
        viewHolder.tvStatusExtra.setTextColor(bean.getStatusExtra().equals(context.getString(R.string.deck_complete)) ? Color.GREEN : Color.RED);
        viewHolder.linearLayout.setOnClickListener(v -> mOnItemClickListener.onItemClick(viewHolder.linearLayout, data, position));
        viewHolder.linearLayout.setOnLongClickListener(v -> {
            mOnItemLongClickListener.onItemLongClick(viewHolder.linearLayout, data, position);
            return false;
        });
        Glide.with(context).load(bean.getPlayerPath()).error(R.drawable.ic_unknown_picture).into(viewHolder.imgThumbnail);
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;
        @BindView(R.id.img_thumbnail)
        ImageView    imgThumbnail;
        @BindView(R.id.tv_status_main)
        TextView     tvStatusMain;
        @BindView(R.id.tv_status_extra)
        TextView     tvStatusExtra;
        @BindView(R.id.tv_deck_name)
        TextView     tvDeckName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
