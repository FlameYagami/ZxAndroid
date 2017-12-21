package com.dab.zx.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.dab.zx.R;
import com.dab.zx.config.MyApp;
import com.dab.zx.uitls.AppManager;
import com.dab.zx.uitls.DisplayUtils;
import com.dab.zx.uitls.statusbar.StatusBarHelper;
import com.dab.zx.view.dialog.DialogLoadingUtils;
import com.dab.zx.view.widget.ToastView;
import com.michaelflisar.rxbus2.rx.RxDisposableManager;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

public abstract class BaseActivity extends SwipeBackActivity {
    protected SwipeBackLayout mSwipeBackLayout;

    protected StatusBarHelper mStatusBarHelper;

    protected void showToast(String message) {
        runOnUiThread(() -> ToastView.make(MyApp.context, message, Toast.LENGTH_SHORT).show());
    }

    protected void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    protected void showDialog(String message) {
        DialogLoadingUtils.show(this, message);
    }

    protected void hideDialog() {
        DialogLoadingUtils.hide();
    }

    public abstract int getLayoutId();

    public abstract void initViewAndData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        setContentView(getLayoutId());
        initViewAndData();
        onTintStatusBar();
        AppManager.addActivity(this);
    }

    protected void onTintStatusBar() {
        if (null == mStatusBarHelper) {
            mStatusBarHelper = new StatusBarHelper(this);
        }
        mStatusBarHelper.setColor(R.color.colorPrimaryDark);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            DisplayUtils.hideKeyboard(this);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        mSwipeBackLayout.scrollToFinishActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxDisposableManager.unsubscribe(this);
        AppManager.finishActivity(this);
    }
}
