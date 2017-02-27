package com.zx.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zx.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 八神火焰 on 2016/12/22.
 */

public class DialogEditText extends AlertDialog
{
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_ok)
    TextView tvOk;

    private OnButtonClick onButtonClick;

    public interface OnButtonClick
    {
        void getText(DialogEditText dialog, String content);
    }

    public DialogEditText(@NonNull Context context, String title, String content, String hint, OnButtonClick mOnButtonClick) {
        super(context);
        View view = View.inflate(context, R.layout.dialog_edittext, null);
        ButterKnife.bind(this, view);

        setView(view);
        setTitle(title);
        editText.setHint(hint);
        editText.setText(content);
        editText.setSelection(editText.getText().length());
        this.onButtonClick = mOnButtonClick;
        tvCancel.setOnClickListener(v -> dismiss());
        tvOk.setOnClickListener(v -> {
            String text = editText.getText().toString().trim();
            if (!TextUtils.isEmpty(text)) {
                this.onButtonClick.getText(this, text);
            }
        });
    }
}
