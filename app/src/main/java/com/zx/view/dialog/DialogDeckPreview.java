package com.zx.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zx.R;
import com.zx.bean.DeckPreviewBean;
import com.zx.game.utils.DeckUtils;
import com.zx.ui.base.BaseRecyclerViewAdapter;
import com.zx.ui.base.BaseRecyclerViewListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 八神火焰 on 2017/2/8.
 */

public class DialogDeckPreview extends AlertDialog implements BaseRecyclerViewListener.OnItemClickListener
{
    private OnDeckClick onDeckClick;

    public interface OnDeckClick
    {
        void getDeck(DialogDeckPreview dialog, DeckPreviewBean bean);
    }

    public DialogDeckPreview(@NonNull Context context, OnDeckClick mOnDeckClick) {
        super(context);
        View view = View.inflate(context, R.layout.dialog_deck_preview, null);
        ButterKnife.bind(this, view);

        setView(view);
        setTitle("卡组选择");
        RecyclerView mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 5));
        RecyclerViewAdapter mRecyclerViewAdapter = new RecyclerViewAdapter(context);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.setOnItemClickListener(this);
        this.onDeckClick = mOnDeckClick;
    }

    @Override
    public void onItemClick(View view, List<?> data, int position) {
        DeckPreviewBean bean = (DeckPreviewBean)data.get(position);
        if (!DeckUtils.checkDeck(bean.getNumberExList())) {
            Snackbar.make(view, "卡组不符合标准", Snackbar.LENGTH_SHORT).show();
            return;
        }
        onDeckClick.getDeck(this, bean);
        dismiss();
    }

    private class RecyclerViewAdapter extends BaseRecyclerViewAdapter
    {
        RecyclerViewAdapter(Context context) {
            super(context);
            updateData(DeckUtils.getDeckPreviewList());
        }

        @Override
        protected int getLayoutId() {
            return R.layout.item_dialog_deck_preview;
        }

        @Override
        protected RecyclerView.ViewHolder getViewHolder(View view) {
            return new ViewHolder(view);
        }

        @Override
        protected void getView(RecyclerView.ViewHolder holder, int position) {
            ViewHolder      viewHolder = (ViewHolder)holder;
            DeckPreviewBean bean       = (DeckPreviewBean)data.get(position);
            viewHolder.viewContent.setOnClickListener(view -> mOnItemClickListener.onItemClick(viewHolder.viewContent, data, position));
            viewHolder.textView.setText(bean.getDeckName());
            Glide.with(context).load(bean.getPlayerPath()).error(R.drawable.img_unknown_picture).into(viewHolder.imageView);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.view_content)
        LinearLayout viewContent;
        @BindView(R.id.imageView)
        ImageView    imageView;
        @BindView(R.id.textView)
        TextView     textView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
