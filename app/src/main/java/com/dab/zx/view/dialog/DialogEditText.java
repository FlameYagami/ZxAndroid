package com.dab.zx.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;

import com.dab.zx.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by 八神火焰 on 2016/12/22.
 */

public class DialogEditText extends AlertDialog {
    @BindView(R.id.editText)
    AppCompatEditText editText;
    @BindView(R.id.btn_cancel)
    AppCompatButton   btnCancel;
    @BindView(R.id.btn_ok)
    AppCompatButton   btnOk;

    private OnButtonClick onButtonClick;

    public interface OnButtonClick {
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
        btnCancel.setOnClickListener(v -> dismiss());
        btnOk.setOnClickListener(v -> {
            String text = editText.getText().toString().trim();
            if (!TextUtils.isEmpty(text)) {
                this.onButtonClick.getText(this, text);
            }
        });
    }
}
