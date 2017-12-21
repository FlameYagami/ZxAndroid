package com.dab.zx.view.banner;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dab.zx.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by 八神火焰 on 2016/12/26.
 */

public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).fitCenter().error(R.drawable.ic_unknown_picture).into(imageView);
    }
}
