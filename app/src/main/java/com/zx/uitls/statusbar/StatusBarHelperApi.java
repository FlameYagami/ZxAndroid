package com.zx.uitls.statusbar;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

abstract class StatusBarHelperApi
{
    protected View     mStatusBarView;
    protected Activity mActivity;
    protected boolean mActivityRootLayoutFitSystemWindows = true;

    protected abstract void setColor(int resId);

    protected abstract void setStatusBarTranslucent();

    protected int getStatusBarHeight() {
        int resourceId = mActivity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return mActivity.getResources().getDimensionPixelSize(resourceId);
    }

    protected void setActivityRootLayoutFitSystemWindowsInternal() {
        ViewGroup contentLayout = (ViewGroup)mActivity.findViewById(android.R.id.content);
        contentLayout.getChildAt(0).setFitsSystemWindows(isActivityRootLayoutFitSystemWindows());
    }

    public boolean isActivityRootLayoutFitSystemWindows() {
        return mActivityRootLayoutFitSystemWindows;
    }

    protected void initStatusBarView() {
        if (null == mStatusBarView) {
            mStatusBarView = new View(mActivity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight());
            ViewGroup contentLayout = (ViewGroup)mActivity.findViewById(android.R.id.content);
            contentLayout.addView(mStatusBarView, lp);
        }
    }
}
