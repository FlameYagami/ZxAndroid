package com.dab.zx.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.dab.zx.config.MyApp;
import com.dab.zx.uitls.AppManager;
import com.dab.zx.uitls.DisplayUtils;
import com.dab.zx.view.dialog.DialogLoadingUtils;
import com.dab.zx.view.widget.ToastView;
import com.michaelflisar.rxbus2.rx.RxDisposableManager;

public abstract class BaseExActivity extends Activity {
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
        setContentView(getLayoutId());
        initViewAndData();
        AppManager.addActivity(this);
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
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxDisposableManager.unsubscribe(this);
        AppManager.finishActivity(this);
    }
}
