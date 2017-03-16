package com.zx.ui.deckeditor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zx.R;
import com.zx.bean.CardBean;
import com.zx.game.utils.CardUtils;
import com.zx.ui.base.BaseRecyclerViewAdapter;
import com.zx.uitls.DisplayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 八神火焰 on 2016/12/26.
 */

public class CardAdapter extends BaseRecyclerViewAdapter
{
    private static final String TAG = DeckAdapter.class.getSimpleName();

    CardAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_deck_editor;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void getView(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        CardBean   cardBean   = (CardBean)data.get(position);
        int        widthPx    = (DisplayUtils.getScreenWidth() - DisplayUtils.dip2px(15)) / 10;
        int        heightPx   = widthPx * 7 / 5;
        viewHolder.imgThumbnail.setLayoutParams(new FrameLayout.LayoutParams(widthPx, heightPx));
        viewHolder.imgThumbnail.setOnClickListener(view -> mOnItemClickListener.onItemClick(view, data, position));
        viewHolder.imgThumbnail.setOnLongClickListener(view -> {
            mOnItemLongClickListener.onItemLongClick(view, data, position);
            return false;
        });
        viewHolder.imgRestrict.setVisibility(cardBean.getRestrict().equals("0") ? View.VISIBLE : View.GONE);
        Glide.with(context).load(CardUtils.getImagePathList(cardBean.getImage()).get(0)).error(R.drawable.img_unknown_thumbnail).into(viewHolder.imgThumbnail);
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.img_thumbnail)
        ImageView   imgThumbnail;
        @BindView(R.id.img_restrict)
        ImageView   imgRestrict;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
