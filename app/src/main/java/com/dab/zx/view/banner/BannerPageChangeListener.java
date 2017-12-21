package com.dab.zx.view.banner;

import android.support.v4.view.ViewPager;

/**
 * Created by 八神火焰 on 2016/12/26.
 */

public class BannerPageChangeListener implements ViewPager.OnPageChangeListener {
    private int currentIndex = 1;

    public int getCurrentIndex() {
        return currentIndex - 1;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
