package com.dab.zx.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dab.zx.R;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 八神火焰 on 2016/12/1.
 */

public class DialogLoading extends AlertDialog {
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindString(R.string.waiting)
    String   pleaseWaiting;

    protected DialogLoading(Context context) {
        super(context);
        initView(context, null, null);
    }

    protected DialogLoading(Context context, CharSequence message) {
        super(context);
        initView(context, message, null);
    }

    protected DialogLoading(Context context, OnKeyListener onKeyListener) {
        super(context);
        initView(context, null, onKeyListener);
    }

    protected DialogLoading(Context context, CharSequence message, OnKeyListener onKeyListener) {
        super(context);
        initView(context, message, onKeyListener);
    }

    private void initView(Context context, CharSequence message, OnKeyListener onKeyListener) {
        View view = View.inflate(context, R.layout.dialog_loading, null);
        ButterKnife.bind(this, view);
        setView(view);
        tvMessage.setText(TextUtils.isEmpty(message) ? pleaseWaiting : message);
        setCanceledOnTouchOutside(false);
        if (onKeyListener != null) {
            setCancelable(true);
            setOnKeyListener(onKeyListener);
        } else {
            setCancelable(false);
            setOnKeyListener(null);
        }
    }
}
