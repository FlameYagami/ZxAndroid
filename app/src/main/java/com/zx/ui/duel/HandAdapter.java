package com.zx.ui.duel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zx.R;
import com.zx.bean.HandBean;
import com.zx.ui.base.BaseRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 八神火焰 on 2016/12/26.
 */

public class HandAdapter extends BaseRecyclerViewAdapter
{
    private static final String TAG = HandAdapter.class.getSimpleName();

    HandAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_duel_hand;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void getView(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        HandBean   handBean   = (HandBean)data.get(position);

        viewHolder.viewTop.setVisibility(handBean.isTopVisible() ? View.VISIBLE : View.GONE);
        viewHolder.viewBottom.setVisibility(handBean.isBottomVisible() ? View.VISIBLE : View.GONE);
        viewHolder.imgThumbnail.setOnClickListener(view -> {
            setItemVisible(position);
            mOnItemClickListener.onItemClick(view, data, position);
        });
        Glide.with(context).load(handBean.getDuelBean().getThumbnailPath()).error(R.drawable.img_unknown_picture).into(viewHolder.imgThumbnail);
    }

    private void setItemVisible(int position) {
        for (int i = 0; i != data.size(); i++) {
            HandBean handBean = (HandBean)data.get(i);
            if (i == position) {
                handBean.setTopVisible(!handBean.isTopVisible());
                handBean.setBottomVisible(!handBean.isBottomVisible());
            } else {
                handBean.setTopVisible(true);
                handBean.setBottomVisible(false);
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.img_thumbnail)
        ImageView imgThumbnail;
        @BindView(R.id.view_top)
        View      viewTop;
        @BindView(R.id.view_bottom)
        View      viewBottom;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
