package com.dab.zx.uitls.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class StatusBarHelperImpl21 extends StatusBarHelperApi {
    StatusBarHelperImpl21(Activity activity) {
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

    @Override
    protected void setStatusBarTranslucent() {
        Window window = mActivity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
    }
}
