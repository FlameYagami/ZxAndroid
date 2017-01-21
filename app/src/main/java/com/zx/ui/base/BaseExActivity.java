package com.zx.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.MotionEvent;
import android.view.View;

import com.zx.uitls.AppManager;
import com.zx.view.dialog.DialogLoading;
import com.zx.uitls.DisplayUtils;
import com.zx.uitls.RxBus;
import com.zx.uitls.StatusBarUtils;

public abstract class BaseExActivity extends Activity
{
    protected void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    protected void showDialog(String message) {
        DialogLoading.showDialog(this, message, false);
    }

    protected void hideDialog() {
        DialogLoading.hideDialog();
    }

    public abstract int getLayoutId();

    /**
     * 初始化控件以及装填数据
     */
    public abstract void initViewAndData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.enableTranslucentStatusBar(this);
        setContentView(getLayoutId());
        initViewAndData();
        AppManager.getInstances().addActivity(this);
    }

    /**
     * 点击空白位置 隐藏软键盘
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            DisplayUtils.hideKeyboard(this);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DialogLoading.hideDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unSubscribe(this);
        AppManager.getInstances().finishActivity(this);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
