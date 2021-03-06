package com.dab.zx.ui.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dab.zx.R;
import com.dab.zx.bean.CardBean;
import com.dab.zx.game.utils.CardUtils;
import com.dab.zx.ui.base.BaseRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

class CardPreviewAdapter extends BaseRecyclerViewAdapter {
    CardPreviewAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_card_preview;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void getView(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        CardBean   cardBean   = (CardBean)data.get(position);
        viewHolder.tvCname.setText(cardBean.getCName());
        viewHolder.tvRace.setText(cardBean.getRace());
        viewHolder.tvCamp.setText(cardBean.getCamp());
        viewHolder.tvPower.setText(cardBean.getPower());
        viewHolder.tvCost.setText(cardBean.getCost());
        viewHolder.viewItemContent.setOnClickListener(v -> mOnItemClickListener.onItemClick(viewHolder.viewItemContent, data, position));
        viewHolder.imgRestrict.setVisibility(cardBean.getRestrict().equals("0") ? View.VISIBLE : View.GONE);
        Glide.with(context).load(CardUtils.getImagePathList(cardBean.getImage()).get(0)).error(R.drawable.ic_unknown_picture).into(viewHolder.imgThumbnail);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_thumbnail)
        ImageView imgThumbnail;
        @BindView(R.id.img_restrict)
        ImageView imgRestrict;
        @BindView(R.id.tv_cname)
        TextView  tvCname;
        @BindView(R.id.tv_race_result)
        TextView  tvRace;
        @BindView(R.id.tv_camp_result)
        TextView  tvCamp;
        @BindView(R.id.tv_power_result)
        TextView  tvPower;
        @BindView(R.id.tv_cost_result)
        TextView  tvCost;
        @BindView(R.id.viewItemContent)
        View      viewItemContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
