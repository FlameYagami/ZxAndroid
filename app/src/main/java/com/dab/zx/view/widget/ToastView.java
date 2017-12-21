package com.dab.zx.view.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dab.zx.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 八神火焰 on 2017/3/1.
 */

public class ToastView {
    @BindView(R.id.textView)
    TextView textView;

    private Toast mToast;

    private ToastView(Context context, CharSequence text, int duration) {
        View view = View.inflate(context, R.layout.weight_toast, null);
        ButterKnife.bind(this, view);
        textView.setText(text);
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(view);
    }

    public static ToastView make(Context context, CharSequence text, int duration) {
        return new ToastView(context, text, duration);
    }

    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }
}
