package com.zx.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.zx.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;

/**
 * Created by 八神火焰 on 2016/12/22.
 */

public class DialogEditText extends AlertDialog
{
    private EditText editText;
    private GetText  mGetText;

    public interface GetText
    {
        void getText(DialogEditText dialog, String content);
    }

    public DialogEditText(@NonNull Context context, String title, String content, String hint, GetText mGetText) {
        super(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View           view     = inflater.inflate(R.layout.dialog_edittext, null);
        ButterKnife.bind(this, view);

        setView(view);
        setTitle(title);
        this.mGetText = mGetText;
        editText = (EditText)view.findViewById(R.id.txt_content);
        editText.setText(TextUtils.isEmpty(content) ? new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(new Date()) : content);
        editText.setHint(hint);
        editText.setSelection(editText.getText().length());
        view.findViewById(R.id.tv_cancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.tv_ok).setOnClickListener(v -> {
            String text = editText.getText().toString().trim();
            if (!TextUtils.isEmpty(text)) {
                this.mGetText.getText(this, text);
            }
        });
    }
}
