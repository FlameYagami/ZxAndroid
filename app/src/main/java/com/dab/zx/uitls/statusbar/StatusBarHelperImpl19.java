package com.dab.zx.uitls.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

@TargetApi(Build.VERSION_CODES.KITKAT)
class StatusBarHelperImpl19 extends StatusBarHelperApi {
    StatusBarHelperImpl19(Activity activity) {
        mActivity = activity;
    }

    @Override
    protected void setColor(int resId) {
        setStatusBarTranslucent();
        initStatusBarView();
        setActivityRootLayoutFitSystemWindowsInternal();
        Drawable drawable = new ColorDrawable(mActivity.getResources().getColor(resId));
        mStatusBarView.setBackground(drawable);
    }

    /**
     * 设置状态栏透明
     */
    @Override
    protected void setStatusBarTranslucent() {
        Window window = mActivity.getWindow();
        // Translucent status bar
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // Translucent navigation bar
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }
}
