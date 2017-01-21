package com.zx.ui.lan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zx.R;
import com.zx.ui.base.BaseRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 八神火焰 on 2017/1/20.
 */

public class LanAdapter extends BaseRecyclerViewAdapter
{
    LanAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_lan;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void getView(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.btnJoin.setOnClickListener(view -> mOnItemClickListener.onItemClick(viewHolder.btnJoin, data, viewHolder.getAdapterPosition()));
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_room_id)
        TextView tvRoomId;
        @BindView(R.id.tv_room_status)
        TextView tvRoomStatus;
        @BindView(R.id.tv_room_name)
        TextView tvRoomName;
        @BindView(R.id.tv_player_0)
        TextView tvPlayer0;
        @BindView(R.id.tv_player_1)
        TextView tvPlayer1;
        @BindView(R.id.btn_join)
        Button   btnJoin;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
