package com.zx.ui.versus;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.zx.R;
import com.zx.bean.ResourceBean;
import com.zx.ui.base.BaseRecyclerViewAdapter;
import com.zx.uitls.RotateTransformation;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 八神火焰 on 2017/1/14.
 */

public class ResourceAdapter extends BaseRecyclerViewAdapter
{
    protected ResourceAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_duel_resource;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void getView(RecyclerView.ViewHolder holder, int position) {
        ViewHolder   viewHolder   = (ViewHolder)holder;
        ResourceBean resourceBean = (ResourceBean)data.get(position);
        if (resourceBean.isResourceVisible()){
            Glide.with(context).load(resourceBean.getDuelBean().getThumbnailPath()).error(R.drawable.ic_unknown_thumbnail).into(viewHolder.imgThumbnail);
        } else {
            Glide.with(context).load(resourceBean.getDuelBean().getThumbnailPath()).error(R.drawable.ic_unknown_thumbnail).transform(new RotateTransformation(context, 90f)).into(viewHolder.imgThumbnail);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.item_content)
        LinearLayout viewContent;
        @BindView(R.id.img_thumbnail)
        ImageView    imgThumbnail;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
