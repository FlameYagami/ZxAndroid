package com.zx.ui.deck;

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
 * Created by 八神火焰 on 2017/9/5.
 */

public class CardAdapter extends BaseRecyclerViewAdapter
{
    private static final String TAG = DeckAdapter.class.getSimpleName();

    private static final float imgScale = (float)7 / 5;

    private int itemWidthPx;
    private int itemHeightPx;

    public CardAdapter(Context context, int spanCount, int widthMargin) {
        super(context);
        int widthPx = DisplayUtils.dip2px(widthMargin * 2 + spanCount * 2 * 2);
        itemWidthPx = (DisplayUtils.getScreenWidth() - widthPx) / spanCount;
        itemHeightPx = (int)(itemWidthPx * imgScale);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_card_image;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void getView(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        CardBean   cardBean   = (CardBean)data.get(position);
        viewHolder.imgThumbnail.setLayoutParams(new FrameLayout.LayoutParams(itemWidthPx, itemHeightPx));
        viewHolder.imgThumbnail.setOnClickListener(view -> mOnItemClickListener.onItemClick(view, data, holder.getAdapterPosition()));
        viewHolder.imgThumbnail.setOnLongClickListener(view -> {
            mOnItemLongClickListener.onItemLongClick(view, data, holder.getAdapterPosition());
            return true;
        });
        viewHolder.imgRestrict.setVisibility(cardBean.getRestrict().equals("0") ? View.VISIBLE : View.GONE);
        Glide.with(context).load(CardUtils.getImagePathList(cardBean.getImage()).get(0)).error(R.drawable.ic_unknown_thumbnail).into(viewHolder.imgThumbnail);
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.img_thumbnail)
        ImageView imgThumbnail;
        @BindView(R.id.img_restrict)
        ImageView imgRestrict;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
