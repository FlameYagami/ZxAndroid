package com.dab.zx.ui.deck;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dab.zx.R;
import com.dab.zx.bean.DeckPreviewBean;
import com.dab.zx.ui.base.BaseRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

class DeckPreviewAdapter extends BaseRecyclerViewAdapter {
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
        viewHolder.tvStatusMain.setTextColor(bean.getStatusMain().equals(context.getString(R.string.deck_pre_complete)) ? Color.GREEN : Color.RED);
        viewHolder.tvStatusExtra.setTextColor(bean.getStatusExtra().equals(context.getString(R.string.deck_pre_complete)) ? Color.GREEN : Color.RED);
        viewHolder.ViewItemContent.setOnClickListener(v -> mOnItemClickListener.onItemClick(viewHolder.ViewItemContent, data, position));
        viewHolder.BtnDeckEditor.setOnClickListener(v -> mOnItemClickListener.onItemClick(viewHolder.BtnDeckEditor, data, position));
        viewHolder.ViewItemContent.setOnLongClickListener(v -> {
            mOnItemLongClickListener.onItemLongClick(viewHolder.ViewItemContent, data, position);
            return false;
        });
        Glide.with(context).load(bean.getPlayerPath()).error(R.drawable.ic_unknown_picture).into(viewHolder.imgThumbnail);
        Glide.with(context).load(bean.getStartPath()).error(R.drawable.ic_unknown_picture).into(viewHolder.imgStart);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.viewItemContent)
        View                 ViewItemContent;
        @BindView(R.id.img_thumbnail)
        ImageView            imgThumbnail;
        @BindView(R.id.tv_status_main)
        TextView             tvStatusMain;
        @BindView(R.id.tv_status_extra)
        TextView             tvStatusExtra;
        @BindView(R.id.tv_deck_name)
        TextView             tvDeckName;
        @BindView(R.id.btn_deck_editor)
        AppCompatImageButton BtnDeckEditor;
        @BindView(R.id.img_start)
        ImageView            imgStart;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
