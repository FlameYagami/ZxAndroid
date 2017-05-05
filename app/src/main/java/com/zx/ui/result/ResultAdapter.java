package com.zx.ui.result;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zx.R;
import com.zx.bean.CardBean;
import com.zx.game.utils.CardUtils;
import com.zx.ui.base.BaseRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

class ResultAdapter extends BaseRecyclerViewAdapter
{
    ResultAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_result;
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
        viewHolder.linearLayout.setOnClickListener(v -> mOnItemClickListener.onItemClick(viewHolder.linearLayout, data, position));
        viewHolder.imgRestrict.setVisibility(cardBean.getRestrict().equals("0") ? View.VISIBLE : View.GONE);
        Glide.with(context).load(CardUtils.getImagePathList(cardBean.getImage()).get(0)).error(R.drawable.ic_unknown_picture).into(viewHolder.imgThumbnail);
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.img_thumbnail)
        ImageView    imgThumbnail;
        @BindView(R.id.img_restrict)
        ImageView    imgRestrict;
        @BindView(R.id.tv_cname)
        TextView     tvCname;
        @BindView(R.id.tv_race_result)
        TextView     tvRace;
        @BindView(R.id.tv_camp_result)
        TextView     tvCamp;
        @BindView(R.id.tv_power_result)
        TextView     tvPower;
        @BindView(R.id.tv_cost_result)
        TextView     tvCost;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
