package com.dab.zx.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dab.zx.R;
import com.dab.zx.uitls.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 八神火焰 on 2017/1/12.
 */
public class MessageView extends LinearLayout {
    @BindView(R.id.tv_key)
    TextView tvKey;
    @BindView(R.id.tv_value)
    TextView tvValue;

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = View.inflate(context, R.layout.widget_message, null);
        addView(view);
        ButterKnife.bind(this, view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MessageView);
        String     keyText    = typedArray.getString(R.styleable.MessageView_title_text);
        String     valueText  = typedArray.getString(R.styleable.MessageView_message_text);
        typedArray.recycle();
        tvKey.setText(keyText);
        if (TextUtils.isEmpty(valueText)) {
            tvValue.setVisibility(GONE);
        } else {
            tvValue.setText(valueText);
        }
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setStringSp(String key, String value) {
        SpUtils.putString(key, value);
        setValue(value);
    }

    public void setIntSp(String key, int value) {
        SpUtils.putInt(key, value);
        setValue(String.valueOf(value));
    }

    public void setValue(String valueText) {
        tvValue.setVisibility(TextUtils.isEmpty(valueText) ? GONE : VISIBLE);
        tvValue.setText(valueText);
    }
}
