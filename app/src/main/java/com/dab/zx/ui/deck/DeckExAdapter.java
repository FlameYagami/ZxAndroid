package com.dab.zx.ui.deck;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dab.zx.R;
import com.dab.zx.bean.DeckExBean;
import com.dab.zx.ui.base.BaseRecyclerViewAdapter;
import com.dab.zx.uitls.DisplayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 八神火焰 on 2016/12/22.
 */

public class DeckExAdapter extends BaseRecyclerViewAdapter {
    private static final String TAG = DeckExAdapter.class.getSimpleName();

    private static final float imgScale = (float)7 / 5;

    private int itemWidthPx;
    private int itemHeightPx;

    public DeckExAdapter(Context context, int spanCount, int widthMargin) {
        super(context);
        int widthPx = DisplayUtils.dip2px(widthMargin * 2 + spanCount * 2 * 2);
        itemWidthPx = (DisplayUtils.getScreenWidth() - widthPx) / spanCount;
        itemHeightPx = (int)(itemWidthPx * imgScale);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_card_image_ex;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void getView(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        DeckExBean deckEx     = (DeckExBean)data.get(position);
        viewHolder.imgThumbnail.setLayoutParams(new FrameLayout.LayoutParams(itemWidthPx, itemHeightPx));
        viewHolder.imgThumbnail.setOnClickListener(view -> mOnItemClickListener.onItemClick(view, data, holder.getAdapterPosition()));
        viewHolder.imgThumbnail.setOnLongClickListener(view -> {
            mOnItemLongClickListener.onItemLongClick(view, data, holder.getAdapterPosition());
            return true;
        });
        viewHolder.tvCount.setText(String.valueOf(deckEx.getCount()));
        Glide.with(context).load(deckEx.getDeckBean().getImagePath()).error(R.drawable.ic_unknown_thumbnail).centerCrop().into(viewHolder.imgThumbnail);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_thumbnail)
        ImageView imgThumbnail;
        @BindView(R.id.img_restrict)
        ImageView imgRestrict;
        @BindView(R.id.tv_count)
        TextView  tvCount;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
